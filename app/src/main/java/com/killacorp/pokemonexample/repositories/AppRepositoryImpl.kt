package com.killacorp.pokemonexample.repositories

import com.google.firebase.auth.FirebaseUser
import com.killacorp.pokemonexample.model.PokemonDetails
import com.killacorp.pokemonexample.model.all.PokemonList
import com.killacorp.pokemonexample.model.offline.CustomizedModel
import kotlinx.coroutines.flow.Flow

interface AppRepositoryImpl {

    suspend fun signIn(email: String, password: String): com.killacorp.pokemonexample.network.Result<Unit>
    suspend fun signUp(email: String, password: String): com.killacorp.pokemonexample.network.Result<Unit>
    suspend fun signOut(): com.killacorp.pokemonexample.network.Result<Unit>
    suspend fun uploadUserInfo(email : String , name : String , onDone : (Boolean) -> Unit)
    fun checkCurrentUser(): FirebaseUser?

    // Api
    suspend fun getPokemonList(limit : String) : Flow<PokemonList>
    suspend fun getOfflinePokemonList(limit : String) : Flow<PokemonList>
    suspend fun getPokemonById(id : Int) : Flow<PokemonDetails>

    // db
    suspend fun getAllPokemons() : Flow<MutableList<CustomizedModel>>
    suspend fun insertPokemon(customizedModel: CustomizedModel)
    suspend fun deleteAllPokemons()
}