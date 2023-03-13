package com.killacorp.pokemonexample.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.killacorp.pokemonexample.model.offline.CustomizedModel

@Database(entities = [CustomizedModel::class], version = 1, exportSchema = false)
abstract  class PokemonDatabase : RoomDatabase(){

    abstract fun pokemonDao() : PokemonDao
}