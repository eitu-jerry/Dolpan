package com.eitu.dolpan

import android.app.Application
import com.navercorp.nid.NaverIdLoginSDK

class Dolpan: Application() {

    override fun onCreate() {
        super.onCreate()
        NaverIdLoginSDK.initialize(this,
            resources.getString(R.string.n_c_id),
            resources.getString(R.string.n_c_secret),
            resources.getString(R.string.app_name))
    }
}