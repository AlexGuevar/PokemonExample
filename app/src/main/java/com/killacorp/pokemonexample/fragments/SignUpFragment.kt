package com.killacorp.pokemonexample.fragments

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
import androidx.navigation.Navigation
import com.killacorp.pokemonexample.R
import com.killacorp.pokemonexample.databinding.FragmentSignUpBinding
import com.killacorp.pokemonexample.ui.PokemonActivity
import com.killacorp.pokemonexample.utils.Utils.isEmailValid
import com.killacorp.pokemonexample.viewmodels.AppViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private val appViewModel : AppViewModel by viewModels()
    private lateinit var registerLayoutBinding: FragmentSignUpBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        registerLayoutBinding = FragmentSignUpBinding.inflate(inflater,container,false)
        return registerLayoutBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerLayoutBinding.haveAccount.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        registerLayoutBinding.signUpBtn.setOnClickListener {
            val email = registerLayoutBinding.emailEdit.text.toString()
            val password = registerLayoutBinding.passwordEdit.text.toString()
            val fullName = registerLayoutBinding.nameEdit.text.toString()

            if(!email.isEmailValid()){
                Toast.makeText(requireContext(),"Correo electronico no valido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(fullName)){
                Toast.makeText(requireContext(),"Full Name cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(email)){
                Toast.makeText(requireContext(),"Email cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(password)){
                Toast.makeText(requireContext(),"Password cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            appViewModel.signUp(email, password)
            appViewModel.checkCurrentUser()
            appViewModel.user.observe(viewLifecycleOwner, Observer {
                if(it != null){
                    appViewModel.uploadUserInfo(email,fullName){
                        Toast.makeText(requireContext(),"User Account Successfully Created", Toast.LENGTH_SHORT).show()
                        Intent(requireContext(), PokemonActivity::class.java).apply {
                            startActivity(this)
                            requireActivity().finish()
                        }
                    }
                }
            })
        }
    }
}