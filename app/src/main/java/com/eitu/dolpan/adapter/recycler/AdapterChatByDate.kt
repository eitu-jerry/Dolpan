package com.eitu.dolpan.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eitu.dolpan.adapter.BaseAdapter
import com.eitu.dolpan.dataClass.firestore.Chat
import com.eitu.dolpan.databinding.ItemRecyclerChatByDateBinding

class AdapterChatByDate: BaseAdapter<Chat, AdapterChatByDate.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemRecyclerChatByDateBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val binding = holder.binding
        val item = list[position]
        val context = holder.binding.root.context

        binding.text.text = "${item.owner} ${item.type} ${item.id} ${item.title} ${item.date} "
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addItem(item: Chat) {
        list.add(item)
        notifyItemInserted(list.size - 1)
    }

    inner class Holder(binding: ItemRecyclerChatByDateBinding): RecyclerView.ViewHolder(binding.root) {

        val binding: ItemRecyclerChatByDateBinding

        init {
            this.binding = binding
            binding.root.layoutParams =
                RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.WRAP_CONTENT )
        }

    }

}