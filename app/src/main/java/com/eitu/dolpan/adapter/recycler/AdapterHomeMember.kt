package com.eitu.dolpan.adapter.recycler

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.eitu.dolpan.R
import com.eitu.dolpan.adapter.BaseAdapter
import com.eitu.dolpan.dataClass.YoutubeMember
import com.eitu.dolpan.databinding.ItemRecyclerMemberAllBinding
import com.eitu.dolpan.databinding.ItemViewpagerRewindTopBinding
import com.eitu.dolpan.etc.ImageDownloader
import com.eitu.dolpan.livedata.MemberSelected
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdapterHomeMember(activity: Activity): BaseAdapter<YoutubeMember, AdapterHomeMember.Holder>() {

    private val activity: Activity
    private val member: MemberSelected

    init {
        this.activity = activity
        this.member = ViewModelProvider(activity as ViewModelStoreOwner)[MemberSelected::class.java]
        addSnapshot(id = "wak")
        addSnapshot(id = "ine")
        addSnapshot(id = "jing")
        addSnapshot(id = "lilpa")
        addSnapshot(id = "jururu")
        addSnapshot(id = "gosegu")
        addSnapshot(id = "vichan")
    }

    fun addSnapshot(id: String) {
        Firebase.firestore.collection("youtubeMember")
            .document(id)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.d("Snapshot", "catch Exception $error")
                    return@addSnapshotListener
                }

                if (value != null && value.exists()) {
                    Log.d("Snapshot", "owner=$id isLive=${value.get("isLive")}")
                    var position = 0
                    for (i in 0 until list.size) {
                        if (list[i].owner == id) {
                            position = i
                            list[position] = value.toObject(YoutubeMember::class.java)!!
                        }
                    }
                    notifyItemChanged(position)
                }
            }
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
            if (binding.isLive.visibility == View.INVISIBLE) {
                param.rightMargin = margin.toInt()
                binding.isLive.visibility = View.VISIBLE
                binding.isLive.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_show_is_live))
            }
        }
        else {
            if (binding.isLive.visibility == View.VISIBLE) {
                param.rightMargin = 0
                binding.isLive.visibility = View.INVISIBLE
                binding.isLive.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_hide_is_live))
            }
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