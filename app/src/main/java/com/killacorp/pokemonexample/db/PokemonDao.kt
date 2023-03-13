package com.killacorp.pokemonexample.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.killacorp.pokemonexample.model.offline.CustomizedModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Query("SELECT * FROM data ORDER BY id")
    fun getAllPokemons() : Flow<MutableList<CustomizedModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(customizedModel: CustomizedModel)

    @Query("DELETE FROM data")
    suspend fun deleteAllPokemons()

}