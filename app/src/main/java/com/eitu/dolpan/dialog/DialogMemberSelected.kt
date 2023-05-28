package com.eitu.dolpan.dialog

import android.app.Activity
import android.graphics.Color
import android.graphics.ColorFilter
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.eitu.dolpan.R
import com.eitu.dolpan.databinding.DialogMemberSelectedBinding
import com.eitu.dolpan.etc.ImageDownloader
import com.eitu.dolpan.livedata.MemberSelected
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class DialogMemberSelected(activity: Activity) {

    private val memberSelected: MemberSelected

    init {
        memberSelected = ViewModelProvider(activity as ViewModelStoreOwner)[MemberSelected::class.java]
        memberSelected.member.observe(activity as LifecycleOwner) {
            val bottomSheetDialog = BottomSheetDialog(activity)

            val binding = DialogMemberSelectedBinding.inflate(activity.layoutInflater)

            val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            params.height = activity.resources.displayMetrics.heightPixels
            binding.root.layoutParams = params

            binding.background.setColorFilter(Color.parseColor("#44000000"))

            binding.profile.clipToOutline = true

            it.bannerImage?.let { profile -> ImageDownloader.setFullBanner(activity, binding.background, profile) }
            it.profileImage?.let { profile -> ImageDownloader.setImage(activity, binding.profile, profile) }
            binding.name.text = it.name
            binding.desc.text = it.description

            bottomSheetDialog.setContentView(binding.root)
            bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetDialog.behavior.peekHeight = activity.resources.displayMetrics.heightPixels
            bottomSheetDialog.show()
        }
    }

}