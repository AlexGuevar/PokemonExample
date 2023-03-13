package com.killacorp.pokemonexample.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.killacorp.pokemonexample.db.PokemonDao
import com.killacorp.pokemonexample.model.PokemonDetails
import com.killacorp.pokemonexample.model.all.PokemonList
import com.killacorp.pokemonexample.model.offline.CustomizedModel
import com.killacorp.pokemonexample.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AppRepository  @Inject constructor(
    private var apiService: ApiService,
    private var pokemonDao: PokemonDao
) : AppRepositoryImpl{

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference = FirebaseDatabase.getInstance().reference

    override suspend fun signIn(email: String, password: String): com.killacorp.pokemonexample.network.Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            com.killacorp.pokemonexample.network.Result.Success(Unit)
        } catch (e: Exception) {
            com.killacorp.pokemonexample.network.Result.Error(e)
        }
    }
    override suspend fun signUp(email: String, password: String): com.killacorp.pokemonexample.network.Result<Unit> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            com.killacorp.pokemonexample.network.Result.Success(Unit)
        } catch (e: Exception) {
            com.killacorp.pokemonexample.network.Result.Error(e)
        }
    }
    override suspend fun signOut(): com.killacorp.pokemonexample.network.Result<Unit> {
        return try {
            firebaseAuth.signOut()
            com.killacorp.pokemonexample.network.Result.Success(Unit)
        } catch (e: Exception) {
            com.killacorp.pokemonexample.network.Result.Error(e)
        }
    }

    override suspend fun uploadUserInfo(email: String, name: String , onDone : (Boolean) -> Unit) {
        try {
            val map = hashMapOf<String,String>()
            map["email"] = email
            map["password"] = name
            databaseReference.child("infos").child(firebaseAuth.currentUser!!.uid).setValue(map).await()
            onDone.invoke(true)
        }catch (ex : Exception){
            onDone.invoke(true)
        }
    }

    override fun checkCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
    override suspend fun getPokemonList(limit : String): Flow<PokemonList> {
        return flow {
            emit(apiService.getPokemonList(limit))
        }
    }

    override suspend fun getOfflinePokemonList(limit : String): Flow<PokemonList> {
        return flow {
            emit(apiService.getPokemonList(limit))
        }
    }

    override suspend fun getPokemonById(id: Int): Flow<PokemonDetails> {
        return flow {
            emit(apiService.getPokemonById(id))
        }
    }

    override suspend fun getAllPokemons(): Flow<MutableList<CustomizedModel>> {
        return pokemonDao.getAllPokemons()
    }

    override suspend fun insertPokemon(customizedModel: CustomizedModel) {
        pokemonDao.insertPokemon(customizedModel)
    }

    override suspend fun deleteAllPokemons() {
        pokemonDao.deleteAllPokemons()
    }


}