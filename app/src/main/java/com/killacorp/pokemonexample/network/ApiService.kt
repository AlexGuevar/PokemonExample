package com.killacorp.pokemonexample.network

import com.killacorp.pokemonexample.model.PokemonDetails
import com.killacorp.pokemonexample.model.all.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit : String
    ): PokemonList

    @GET("pokemon/{id}")
    suspend fun getPokemonById(@Path("id") id: Int): PokemonDetails
}