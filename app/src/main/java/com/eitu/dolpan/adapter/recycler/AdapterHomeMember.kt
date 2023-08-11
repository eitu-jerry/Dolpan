package com.eitu.dolpan.adapter.recycler

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eitu.dolpan.R
import com.eitu.dolpan.adapter.BaseAdapter
import com.eitu.dolpan.dataClass.YoutubeMember
import com.eitu.dolpan.databinding.ItemRecyclerMemberAllBinding
import com.eitu.dolpan.databinding.ItemViewpagerRewindTopBinding
import com.eitu.dolpan.etc.ImageDownloader
import com.eitu.dolpan.livedata.MemberSelected
import com.eitu.dolpan.livedata.YoutubeMemberModel
import com.eitu.dolpan.view.base.BaseActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdapterHomeMember(
    val activity: BaseActivity,
    val memberSelected: MemberSelected,
    ytMembers : YoutubeMemberModel
) : BaseAdapter<YoutubeMember, AdapterHomeMember.Holder>() {

    init {
        ytMembers.members.observe(activity ) {
            setList(it)
        }
        addSnapshot(id = "wak", 0)
        addSnapshot(id = "ine", 1)
        addSnapshot(id = "jing", 2)
        addSnapshot(id = "lilpa", 3)
        addSnapshot(id = "jururu", 4)
        addSnapshot(id = "gosegu", 5)
        addSnapshot(id = "vichan", 6)
    }

    fun addSnapshot(id: String, position: Int) {
        Firebase.firestore.collection("youtubeMember")
            .document(id)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.d("Snapshot", "catch Exception $error")
                    return@addSnapshotListener
                }

                if (value != null && value.exists() && list.size > 0) {
                    Log.d("Snapshot", "owner=$id isLive=${value.get("isLive")}")
                    list[position] = value.toObject(YoutubeMember::class.java)!!
                    notifyItemChanged(position)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerMemberAllBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setView()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class Holder(val binding: ItemRecyclerMemberAllBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.profile.clipToOutline = true
            binding.isLive.visibility = View.GONE

            binding.root.apply {
                layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT)

                setOnClickListener {
                    memberSelected.updateValue(getMember())
                }
            }

            binding.isLive.setOnClickListener {
                getMember()?.twitch?.let {
                    val intent = Intent(Intent.ACTION_VIEW)
                    try {
                        intent.data = Uri.parse("twitch://open?stream=$it")
                        activity.startActivity(intent)
                    } catch (e : Exception) {
                        intent.data = Uri.parse("https://twitch.tv/$it")
                        activity.startActivity(intent)
                    }
                }
            }
        }

        fun setView() {
            val item = getMember()
            binding.member = item

            item?.profileImage?.let {
                Glide.with(binding.root).load(it).into(binding.profile)
            }

            item?.isLive?.let { binding.isLive.startAnimation(getAnimation(it)) }
        }

        private fun getAnimation(isLive : Boolean) : Animation {
            return if (isLive && binding.isLive.visibility == View.GONE) {
                AnimationUtils.loadAnimation(activity, R.anim.anim_show_is_live)
            }
            else if (!isLive && binding.isLive.visibility == View.VISIBLE){
                AnimationUtils.loadAnimation(activity, R.anim.anim_hide_is_live)
            }
            else {
                AnimationUtils.loadAnimation(activity, R.anim.anim_null)
            }
        }

        private fun getMember() : YoutubeMember? {
            return when(adapterPosition) {
                0 -> list.find { it.owner == "wak" }
                1 -> list.find { it.owner == "ine" }
                2 -> list.find { it.owner == "jing" }
                3 -> list.find { it.owner == "lilpa" }
                4 -> list.find { it.owner == "jururu" }
                5 -> list.find { it.owner == "gosegu" }
                6 -> list.find { it.owner == "vichan" }
                7 -> list.find { it.owner == "wakta" }
                else -> list[0]
            }
        }

    }

}