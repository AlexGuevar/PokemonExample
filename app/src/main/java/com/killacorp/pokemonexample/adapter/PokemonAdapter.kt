package com.killacorp.pokemonexample.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.killacorp.pokemonexample.R
import com.killacorp.pokemonexample.databinding.PokemonRowItemLayoutBinding
import com.squareup.picasso.Picasso

class PokemonAdapter ( var response : ArrayList<com.killacorp.pokemonexample.model.all.Result>) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>(){

    private  var listener : OnPokemonClickListener? = null
    public interface OnPokemonClickListener {
        fun onClick(position : Int)
    }

    public fun onItem(listener : OnPokemonClickListener){
        this.listener = listener
    }

    inner class ViewHolder(var binding : PokemonRowItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
        return  ViewHolder(DataBindingUtil.inflate(view, R.layout.pokemon_row_item_layout,parent,false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = response[position]
        holder.binding.model = data
        val currentPos = position + 1
        val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$currentPos.png"
        Picasso.get().load(imageUrl).into(holder.binding.img)
        holder.binding.name.text = data.name


        if(currentPos <= 9){
            holder.binding.number.text = "#00$currentPos"
        } else if (currentPos in 10..99){
            holder.binding.number.text = "#0$currentPos"
        } else {
            holder.binding.number.text = "#$currentPos"
        }


        holder.itemView.setOnClickListener {
            if(listener != null){
                val pos = holder.adapterPosition
                if(pos != RecyclerView.NO_POSITION){
                    listener!!.onClick(pos + 1)
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return response.size
    }

}