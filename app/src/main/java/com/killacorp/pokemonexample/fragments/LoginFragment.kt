package com.killacorp.pokemonexample.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.ActivityNavigator
import androidx.navigation.Navigation
import com.killacorp.pokemonexample.R
import com.killacorp.pokemonexample.databinding.FragmentLoginBinding
import com.killacorp.pokemonexample.ui.PokemonActivity
import com.killacorp.pokemonexample.utils.Utils
import com.killacorp.pokemonexample.utils.Utils.addSession
import com.killacorp.pokemonexample.utils.Utils.isEmailValid
import com.killacorp.pokemonexample.viewmodels.AppViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val appViewModel : AppViewModel by viewModels()
    private lateinit var loginLayoutBinding: FragmentLoginBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        loginLayoutBinding = FragmentLoginBinding.inflate(inflater,container,false)
        return loginLayoutBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateUi()
        loginLayoutBinding.noAccount.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        loginLayoutBinding.loginBtn.setOnClickListener {
            val email = loginLayoutBinding.emailEdit.text.toString()
            val password = loginLayoutBinding.passwordEdit.text.toString()

            if(!email.isEmailValid()){
                Toast.makeText(requireContext(),"Correo electronico no valido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(email)){
                Toast.makeText(requireContext(),"El correo no puede estar vacio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(password)){
                Toast.makeText(requireContext(),"La contraseña no puede estar vacia", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            appViewModel.signIn(email,password)
            appViewModel.checkCurrentUser()
            appViewModel.user.observe(viewLifecycleOwner, Observer {
                if(it != null){
                    // user logged In
                    Utils.addSession(requireContext(),email,password)
                    Toast.makeText(requireContext(),"User Successfully Logged In", Toast.LENGTH_SHORT).show()
                    Intent(requireContext(), PokemonActivity::class.java).apply {
                        startActivity(this)
                        requireActivity().finish()
                    }
                }
            })

        }
    }

    private fun populateUi(){
        val sharedPreferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email","")
        val password = sharedPreferences.getString("password","")

        loginLayoutBinding.emailEdit.setText(email)
        loginLayoutBinding.passwordEdit.setText(password)
    }
}