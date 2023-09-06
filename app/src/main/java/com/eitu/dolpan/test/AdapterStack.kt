package com.eitu.dolpan.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eitu.dolpan.databinding.ItemRecyclerStackBinding

class AdapterStack(private val bgView : View) : RecyclerView.Adapter<AdapterStack.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemRecyclerStackBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.set(position)
    }

    override fun getItemCount(): Int {
        return 10
    }

    inner class Holder(private val binding: ItemRecyclerStackBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
        }

        fun set(position: Int) {

        }

    }

}