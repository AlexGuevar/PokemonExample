package com.killacorp.pokemonexample.di

import android.content.Context
import androidx.room.Room
import com.killacorp.pokemonexample.db.PokemonDatabase
import com.killacorp.pokemonexample.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofitInstance() : ApiService {
        return  Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabaseInstance(@ApplicationContext context : Context) =
        Room.databaseBuilder(context.applicationContext, PokemonDatabase::class.java,"pokemon.db")
            .fallbackToDestructiveMigration().build()


    @Singleton
    @Provides
    fun provideDaoInstance(pokemonDatabase: PokemonDatabase) = pokemonDatabase.pokemonDao()
}