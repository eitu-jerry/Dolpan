package com.eitu.dolpan.adapter.recycler

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.eitu.dolpan.adapter.BaseAdapter
import com.eitu.dolpan.dataClass.YoutubeMember
import com.eitu.dolpan.databinding.ItemRecyclerMemberAllBinding
import com.eitu.dolpan.databinding.ItemViewpagerRewindTopBinding
import com.eitu.dolpan.etc.ImageDownloader
import com.eitu.dolpan.livedata.MemberSelected

class AdapterHomeMember(activity: Activity, member: MemberSelected): BaseAdapter<YoutubeMember, AdapterHomeMember.Holder>() {

    private val activity: Activity
    private val member: MemberSelected

    init {
        this.activity = activity
        this.member = member
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerMemberAllBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val binding = holder.binding
        val item = list[position]
        val context = holder.itemView.context

        Log.d("item", "id=${item.owner} isLive=${item.isLive}")

        val profileImage = item.profileImage
        if (profileImage != null) ImageDownloader.setImage(context, binding.profile, profileImage)

        binding.name.text = item.name

        val description = item.description
        if (description != null && description != "") {
            binding.desc.visibility = View.VISIBLE
            binding.desc.text = description
        }
        else {
            binding.desc.visibility = View.GONE
        }

        val param = binding.layoutInfo.layoutParams as ConstraintLayout.LayoutParams
        val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, context.resources.displayMetrics)
        if (item.isLive) {
            param.rightMargin = margin.toInt()
            binding.isLive.visibility = View.VISIBLE
        }
        else {
            param.rightMargin = 0
            binding.isLive.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class Holder(binding: ItemRecyclerMemberAllBinding): RecyclerView.ViewHolder(binding.root) {

        val binding: ItemRecyclerMemberAllBinding

        init {
            this.binding = binding
            binding.profile.clipToOutline = true
            binding.root.layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT)
            binding.root.setOnClickListener {
                member.updateValue(getList()[adapterPosition])
            }
            binding.isLive.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("twitch://open?stream=${list.get(adapterPosition).twitch}")
                activity.startActivity(intent)
            }
        }

    }

}