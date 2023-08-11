package com.eitu.dolpan.adapter.viewpager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eitu.dolpan.adapter.BaseAdapter
import com.eitu.dolpan.dataClass.firestore.YoutubeMember
import com.eitu.dolpan.databinding.ItemViewpagerRewindTopBinding
import com.eitu.dolpan.etc.ImageDownloader

class AdapterRewindTop: BaseAdapter<YoutubeMember?, AdapterRewindTop.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemViewpagerRewindTopBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (list.isNotEmpty()) {
            val binding = holder.binding
            val item = list.get(position % list.size)
            val context = holder.itemView.context

            if (item?.bannerImage != null) ImageDownloader.setBanner(context, binding.banner, item.bannerImage)

        }
    }

    override fun getItemCount(): Int {
        if (list.size > 2) return Int.MAX_VALUE
        else return list.size
    }

    class Holder(binding: ItemViewpagerRewindTopBinding): RecyclerView.ViewHolder(binding.root) {

        val binding: ItemViewpagerRewindTopBinding

        init {
            this.binding = binding
            binding.banner.clipToOutline = true
            binding.root.layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT)
        }

    }

}