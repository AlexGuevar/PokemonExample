package com.killacorp.pokemonexample.ui


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.killacorp.pokemonexample.R
import com.killacorp.pokemonexample.adapter.OfflinePokemonAdapter
import com.killacorp.pokemonexample.adapter.PokemonAdapter
import com.killacorp.pokemonexample.databinding.ActivityPokemonBinding
import com.killacorp.pokemonexample.model.offline.CustomizedModel
import com.killacorp.pokemonexample.viewmodels.AppViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL

@AndroidEntryPoint
class PokemonActivity : AppCompatActivity() {
    private var counter = 0
    private lateinit var list : ArrayList<com.killacorp.pokemonexample.model.all.Result>
    private val appViewModel : AppViewModel by viewModels()
    private lateinit var pokemonAdapter: PokemonAdapter
    private lateinit var offlinePokemonAdapter: OfflinePokemonAdapter
    private lateinit var binding : ActivityPokemonBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list = arrayListOf()
        binding.logout.setOnClickListener {
            appViewModel.signOut()
            appViewModel.checkCurrentUser()
            if (appViewModel.user.value == null){
                Intent(this,AuthenticationActivity::class.java).apply {
                    startActivity(this)
                    finish()
                }
            }
        }
        binding.recyclerView.layoutManager = GridLayoutManager(this,2)
        binding.recyclerView.setHasFixedSize(true)
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().isNotEmpty()){
                    filterList(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        if(isConnected()){
            fetchOnline()
        } else {
            fetchOffline()
        }

    }

    private fun fetchOnline(){
        appViewModel.getPokemonList("1100")
        lifecycleScope.launch {
            appViewModel.state.collectLatest {
                when (it) {
                    is AppViewModel.UiStates.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is AppViewModel.UiStates.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        pokemonAdapter = PokemonAdapter(it.value.results)
                        list = it.value.results
                        binding.recyclerView.adapter = pokemonAdapter
                        pokemonAdapter.onItem(object : PokemonAdapter.OnPokemonClickListener {
                            override fun onClick(position: Int) {
                                Intent(
                                    this@PokemonActivity,
                                    PokemonTypeActivity::class.java
                                ).apply {
                                    putExtra("pos", position)
                                    startActivity(this)
                                }
                            }
                        })

                        for(i in 0 until 20){
                            val position = i + 1
                            val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$position.png"
                            insertIntoDB(imageUrl,it.value.results[i].name)
                        }

                    }
                    is AppViewModel.UiStates.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                    }
                    else -> {}
                }
            }
        }

    }
    private fun fetchOffline(){
        Log.d("TAG","CALLED ONE")
        appViewModel.getAllPokemons()
        lifecycleScope.launch {
            appViewModel.offlineState.collectLatest {
                when(it){
                    is AppViewModel.OfflineStates.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        Log.d("TAG","CALLED TWO")
                    }
                    is AppViewModel.OfflineStates.SUCCESS -> {
                        Log.d("TAG","CALLED THREE")
                        binding.progressBar.visibility = View.GONE
                        offlinePokemonAdapter  = OfflinePokemonAdapter(it.value)
                        binding.recyclerView.adapter = offlinePokemonAdapter
                    }
                    is AppViewModel.OfflineStates.ERROR -> {
                        Log.d("TAG","CALLED FOUR")
                        binding.progressBar.visibility = View.GONE
                    }
                    else -> {}
                }
            }
        }
    }
    private fun filterList(query : String) {
        val emptyList = ArrayList<com.killacorp.pokemonexample.model.all.Result>()
        for (result in list){
            if(result.name.contains(query)){
                emptyList.add(result)
            }

            pokemonAdapter = PokemonAdapter(emptyList)
            binding.recyclerView.adapter = pokemonAdapter
            pokemonAdapter.onItem(object : PokemonAdapter.OnPokemonClickListener{
                override fun onClick(position: Int) {
                    Intent(this@PokemonActivity,PokemonTypeActivity::class.java).apply {
                        putExtra("pos",position)
                        startActivity(this)
                    }
                }
            })
        }
    }
    private fun isConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
    private fun insertIntoDB(imageUrl : String , name : String){
        val url = URL(imageUrl)
        val connection = url.openConnection() as HttpURLConnection
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                connection.connect()
                val inputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val  byteArray = stream.toByteArray()

                withContext(Dispatchers.Main){
                    appViewModel.insertPokemon(CustomizedModel(name,byteArray))
                }
            }
        }


    }
}