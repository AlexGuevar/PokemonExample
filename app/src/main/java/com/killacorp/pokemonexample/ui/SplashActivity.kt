package com.killacorp.pokemonexample.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.killacorp.pokemonexample.databinding.ActivitySplashBinding
import com.killacorp.pokemonexample.viewmodels.AppViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val appViewModel : AppViewModel by viewModels()
    private lateinit var binding : ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            appViewModel.checkCurrentUser()
            if(appViewModel.user.value != null){
                Intent(this@SplashActivity,PokemonActivity::class.java).apply {
                    startActivity(this)
                    finish()
                }
            } else {
                Intent(this@SplashActivity,AuthenticationActivity::class.java).apply {
                    startActivity(this)
                    finish()
                }
            }
        }

    }


}