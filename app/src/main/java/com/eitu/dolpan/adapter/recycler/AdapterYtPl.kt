package com.eitu.dolpan.adapter.recycler

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eitu.dolpan.adapter.BaseAdapter
import com.eitu.dolpan.dataClass.YoutubePlaylist
import com.eitu.dolpan.databinding.ItemRecyclerDialogYtPlBinding
import com.eitu.dolpan.etc.ImageDownloader

class AdapterYtPl: BaseAdapter<YoutubePlaylist, AdapterYtPl.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerDialogYtPlBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val binding = holder.binding
        val item = list.get(position)
        val context = holder.itemView.context

        binding.title.text = item.title
        binding.publishedAt.text = item.publishedAt

        ImageDownloader.setImage(context, binding.thumbnail, item.defaultThumb)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Holder(binding: ItemRecyclerDialogYtPlBinding) : RecyclerView.ViewHolder(binding.root) {

        val binding: ItemRecyclerDialogYtPlBinding

        init {
            this.binding = binding
            this.binding.thumbnail.clipToOutline = true
            this.binding.title.maxLines = 2
            this.binding.title.ellipsize = TextUtils.TruncateAt.END

            this.binding.root.layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT)
        }
    }

}