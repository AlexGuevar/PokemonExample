package com.killacorp.pokemonexample.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.killacorp.pokemonexample.databinding.ActivityPokemonTypeBinding
import com.killacorp.pokemonexample.viewmodels.AppViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PokemonTypeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPokemonTypeBinding
    private val appViewModel : AppViewModel by viewModels()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.let {
            val pos = it.getIntExtra("pos",1)
            appViewModel.getPokemonById(pos)
        }


        lifecycleScope.launch {
            appViewModel.typeState.collectLatest {
                when(it){
                    is AppViewModel.TypeStates.LOADING -> {
                    }
                    is AppViewModel.TypeStates.SUCCESS -> {
                        val data = it.value
                        Picasso.get().load(data.sprites.other.home.front_default).into(binding.image)
                        binding.height.text = "Height : " +  data.height.toString()
                        binding.weight.text =  "Weight : " + data.weight.toString()
                        binding.move.text =  "Move : " + data.moves[0].move.name
                        binding.type.text =  "Type : " + data.types[0].type.name
                    }
                    is AppViewModel.TypeStates.ERROR -> {
                    }
                    else -> {}
                }
            }
        }



    }
}