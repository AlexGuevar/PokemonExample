package com.killacorp.pokemonexample.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.killacorp.pokemonexample.R
import com.killacorp.pokemonexample.databinding.OfflinePokemonRowsLayoutBinding
import com.killacorp.pokemonexample.model.offline.CustomizedModel

class OfflinePokemonAdapter (var list : MutableList<CustomizedModel>) : RecyclerView.Adapter<OfflinePokemonAdapter.ViewHolder>(){


    inner class ViewHolder(var binding : OfflinePokemonRowsLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
        return  ViewHolder(DataBindingUtil.inflate(view,
            R.layout.offline_pokemon_rows_layout,parent,false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.binding.model = data
        holder.binding.name.text = data.name
        val currentPos = position + 1
        if(currentPos <= 9){
            holder.binding.number.text = "#00$currentPos"
        } else if (currentPos in 10..20){ holder.binding.number.text = "#0$currentPos" }
        holder.binding.img.setImageBitmap(byteArrayToBitmap(data.data))

    }


    override fun getItemCount(): Int {
        return list.size
    }

    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}