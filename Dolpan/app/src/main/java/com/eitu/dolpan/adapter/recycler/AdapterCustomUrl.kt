package com.eitu.dolpan.adapter.recycler

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.eitu.dolpan.adapter.BaseAdapter
import com.eitu.dolpan.databinding.ItemRecyclerCustomUrlBinding

class AdapterCustomUrl(activity: Activity): BaseAdapter<Any, AdapterCustomUrl.Holder>() {

    private var customUrls: LinkedHashMap<String?, String?> = linkedMapOf()
    private val activity: Activity

    init {
        this.activity = activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerCustomUrlBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val binding = holder.binding
        binding.customUrl.text = customUrls.keys.elementAt(position)
    }

    override fun getItemCount(): Int {
        return customUrls.keys.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCustomUrls(customUrls: LinkedHashMap<String?, String?>) {
        this.customUrls = customUrls;
        notifyDataSetChanged()
    }

    inner class Holder(binding: ItemRecyclerCustomUrlBinding): RecyclerView.ViewHolder(binding.root) {

        val binding: ItemRecyclerCustomUrlBinding

        init {
            this.binding = binding
            this.binding.customUrl.setOnClickListener {
                val position = adapterPosition
                //val id = customUrls.get(customUrls.keys.elementAt(position))
                val id = customUrls.keys.elementAt(position)
                Toast.makeText(
                    binding.root.context,
                    id,
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("vnd.youtube://$id")
                activity.startActivity(intent)
            }
        }

    }

}