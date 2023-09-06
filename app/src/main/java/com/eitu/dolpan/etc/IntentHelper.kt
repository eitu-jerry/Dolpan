package com.eitu.dolpan.etc

import android.app.Activity
import android.content.Intent
import com.eitu.dolpan.R
import com.eitu.dolpan.view.activity.MainActivity
import com.eitu.dolpan.view.activity.WebViewActivity

class IntentHelper {

    companion object {
        fun intentDetail(activity: Activity, intent: Intent) {
            activity.startActivity(intent)
            inDetailAnim(activity)
        }

        fun intentDetail(activity: Activity, goto: Class<*>) {
            activity.startActivity(Intent(activity, goto))
            inDetailAnim(activity)
        }

        fun goToWebView(activity: Activity, url : String) {
            activity.startActivity(Intent(activity, WebViewActivity::class.java).apply {
                putExtra("url", url)
            })
            inDetailAnim(activity)
        }

        fun inDetailAnim(activity: Activity) {
            activity.overridePendingTransition(R.anim.anim_show_detail, R.anim.anim_littlemove_left)
        }

        fun outDetailAnim(activity: Activity) {
            activity.overridePendingTransition(R.anim.anim_littlemove_right, R.anim.anim_hide_detail)
        }
    }

}