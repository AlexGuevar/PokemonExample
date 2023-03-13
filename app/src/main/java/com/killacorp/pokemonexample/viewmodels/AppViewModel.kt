package com.killacorp.pokemonexample.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.killacorp.pokemonexample.model.PokemonDetails
import com.killacorp.pokemonexample.model.all.PokemonList
import com.killacorp.pokemonexample.model.offline.CustomizedModel
import com.killacorp.pokemonexample.repositories.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private var appRepository: AppRepository
) : ViewModel() {

    private val _offlineStateFlow : MutableStateFlow<OfflineStates> = MutableStateFlow(OfflineStates.EMPTY)
    val offlineState : StateFlow<OfflineStates> get() = _offlineStateFlow

    private val _pokemonStateFlow : MutableStateFlow<UiStates> = MutableStateFlow(UiStates.EMPTY)
    val state : StateFlow<UiStates> get() = _pokemonStateFlow

    private val _pokemonTypeStateFlow : MutableStateFlow<TypeStates> = MutableStateFlow(TypeStates.EMPTY)
    val typeState : StateFlow<TypeStates> get() = _pokemonTypeStateFlow

    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?> = _user


    // API + FIREBASE
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val result = appRepository.signIn(email, password)
            if (result is com.killacorp.pokemonexample.network.Result.Success) {
                _user.value = appRepository.checkCurrentUser()

            } else if (result is com.killacorp.pokemonexample.network.Result.Error) {
                _user.value = null
            }
        }
    }
    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            val result = appRepository.signUp(email, password)
            if (result is com.killacorp.pokemonexample.network.Result.Success) {
                _user.value = appRepository.checkCurrentUser()
            } else if (result is com.killacorp.pokemonexample.network.Result.Error) {
                _user.value = null
            }
        }
    }
    fun signOut() {
        viewModelScope.launch {
            val result = appRepository.signOut()
            if (result is com.killacorp.pokemonexample.network.Result.Success) {
                _user.value = null
            } else if (result is com.killacorp.pokemonexample.network.Result.Error) {
                _user.value = appRepository.checkCurrentUser()

            }
        }
    }
    fun checkCurrentUser() {
        _user.value = appRepository.checkCurrentUser()
    }
    fun uploadUserInfo(email: String,name: String, onDone : (Boolean) -> Unit){
        viewModelScope.launch {
            appRepository.uploadUserInfo(email,name,onDone)
        }
    }
    fun getPokemonList(limit : String){
        viewModelScope.launch {
            try {
                _pokemonStateFlow.value = UiStates.LOADING
                appRepository.getPokemonList(limit).collectLatest {
                    _pokemonStateFlow.value = UiStates.SUCCESS(it)
                }
            }catch (ex : Exception){
                _pokemonStateFlow.value = UiStates.ERROR(ex.message!!)
            }
        }
    }
    fun getPokemonById(id : Int){
        viewModelScope.launch {
            try {
                _pokemonTypeStateFlow.value = TypeStates.LOADING
                appRepository.getPokemonById(id).collectLatest {
                    _pokemonTypeStateFlow.value = TypeStates.SUCCESS(it)
                }
            }catch (ex : Exception){
                _pokemonTypeStateFlow.value = TypeStates.ERROR(ex.message!!)
            }
        }
    }

    // DB
    fun getAllPokemons(){
        viewModelScope.launch {
            try {
                _offlineStateFlow.value = OfflineStates.LOADING
                appRepository.getAllPokemons().collectLatest {
                    _offlineStateFlow.value = OfflineStates.SUCCESS(it)
                }
            }catch (ex : Exception){
                _offlineStateFlow.value = OfflineStates.ERROR(ex.message!!)
            }
        }
    }
    fun insertPokemon(customizedModel: CustomizedModel){
        viewModelScope.launch {
            appRepository.insertPokemon(customizedModel)
        }
    }
    fun deletePokemons(){
        viewModelScope.launch {
            deletePokemons()
        }
    }


    sealed class UiStates{
        object LOADING : UiStates()
        data class SUCCESS(var value : PokemonList) : UiStates()
        data class ERROR(var error : String) : UiStates()
        object EMPTY : UiStates()
    }
    sealed class TypeStates{
        object LOADING : TypeStates()
        data class SUCCESS(var value : PokemonDetails) : TypeStates()
        data class ERROR(var error : String) : TypeStates()
        object EMPTY : TypeStates()
    }
    sealed class OfflineStates{
        object LOADING : OfflineStates()
        data class SUCCESS(var value : MutableList<CustomizedModel>) : OfflineStates()
        data class ERROR(var error : String) : OfflineStates()
        object EMPTY : OfflineStates()
    }


}