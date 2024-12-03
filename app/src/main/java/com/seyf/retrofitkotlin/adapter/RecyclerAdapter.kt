package com.seyf.retrofitkotlin.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seyf.retrofitkotlin.databinding.RowLayoutBinding
import com.seyf.retrofitkotlin.model.CryptoModel


class RecyclerAdapter (private val cryptoList: ArrayList<CryptoModel>, private val listener: Listener) : RecyclerView.Adapter<RecyclerAdapter.RowHolder>() {

    interface Listener {
        fun onItemClick(cryptoModel: CryptoModel)
    }

    private val colors: Array<String> = arrayOf("#005c4b","#5c9cb8","#008080","#ffd700","#666666","#4c7f35","#7b7464","#992d48")

    class RowHolder(val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cryptoModel: CryptoModel, colors:Array<String>,position: Int, listener: Listener) {

            itemView.setOnClickListener {
                listener.onItemClick(cryptoModel)
            }
            itemView.setBackgroundColor(Color.parseColor(colors[position%8]))
            binding.textName.text = cryptoModel.currency
            binding.textPrice.text = cryptoModel.price
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val binding = RowLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
       // val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout,parent,false)
        return RowHolder(binding)
    }

    override fun getItemCount(): Int {
       return cryptoList.count()
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
            holder.bind(cryptoList[position],colors,position,listener)
    }

}