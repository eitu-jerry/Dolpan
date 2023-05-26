package com.eitu.dolpan.dialog

import android.R
import android.app.Activity
import android.app.Dialog
import android.content.res.Configuration
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup.LayoutParams
import androidx.viewbinding.ViewBinding


class DolpanDialog(activity: Activity): Dialog(activity) {

    private val activity: Activity
    private val dm:DisplayMetrics

    init {
        this.activity = activity
        this.dm = activity.resources.displayMetrics
        this.window?.setBackgroundDrawableResource(R.color.transparent)
    }

    fun viewBinding(viewBinding: ViewBinding): DolpanDialog {
        this.setContentView(viewBinding.root)
        window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        return this
    }

    fun buttons(positive: View, negative: View?, listener: OnClickListener?): DolpanDialog {
        positive.setOnClickListener {
            listener?.onPositive()
            dismiss()
        }
        negative?.setOnClickListener {
            listener?.onNegative()
            dismiss()
        }

        return this
    }

    interface OnClickListener {
        fun onPositive()
        fun onNegative()
    }

}