package com.killacorp.pokemonexample.utils

import android.content.Context
import android.text.TextUtils
import android.util.Patterns

object Utils {
    fun addSession(context : Context, email : String, password : String){
        val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email",email)
        editor.putString("password",password)
        editor.apply()
    }

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}