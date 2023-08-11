package com.eitu.dolpan.view.dialog

import android.content.Intent
import android.graphics.Color
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.eitu.dolpan.databinding.DialogMemberSelectedBinding
import com.eitu.dolpan.etc.IntentHelper
import com.eitu.dolpan.livedata.MemberSelected
import com.eitu.dolpan.view.activity.ChatActivity
import com.eitu.dolpan.view.base.BaseActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class DialogMemberSelected(
    activity: BaseActivity,
    memberSelected: MemberSelected
    ) {

    private val bottomSheetDialog : BottomSheetDialog by lazy {
        BottomSheetDialog(activity)
    }

    private val binding : DialogMemberSelectedBinding by lazy {
        DialogMemberSelectedBinding.inflate(activity.layoutInflater)
    }

    init {
        binding.root.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT).apply {
            height = activity.resources.displayMetrics.heightPixels
        }
        binding.profile.apply {
            clipToOutline = true
            setOnClickListener { v ->
                val member = memberSelected.member.value
                val intent = Intent(activity, ChatActivity::class.java).apply {
                    putExtra("owner", member?.owner)
                    putExtra("name", member?.name)
                    putExtra("profileImage", member?.profileImage)
                }
                IntentHelper.intentDetail(activity, intent = intent)
                bottomSheetDialog.dismiss()
            }
        }
        binding.background.setColorFilter(Color.parseColor("#44000000"))

        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.behavior.peekHeight = activity.resources.displayMetrics.heightPixels

        memberSelected.member.observe(activity) {
            binding.member = it

            it.profileImage?.let { profile -> Glide.with(binding.root).load(profile).into(binding.profile) }
            it.bannerImage?.let { banner -> Glide.with(binding.root).load("$banner=w2560").into(binding.background) }

            bottomSheetDialog.show()
        }
    }

}