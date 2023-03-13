package com.killacorp.pokemonexample.model.all

data class PokemonList(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: ArrayList<Result>
)