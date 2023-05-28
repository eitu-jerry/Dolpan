package com.eitu.dolpan.etc

import android.app.Activity
import android.content.Intent
import com.eitu.dolpan.R
import com.eitu.dolpan.view.activity.MainActivity

class IntentHelper {



    companion object {
        fun intentDetail(activity: Activity, intent: Intent) {
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.anim_show_detail, R.anim.anim_littlemove_left)
        }
    }

}