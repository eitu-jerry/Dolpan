package com.eitu.dolpan

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.LocalCacheSettings
import com.google.firebase.firestore.PersistentCacheSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.persistentCacheSettings
import com.google.firebase.ktx.Firebase
import com.navercorp.nid.NaverIdLoginSDK

class Dolpan: Application() {

    override fun onCreate() {
        super.onCreate()
        NaverIdLoginSDK.initialize(this,
            resources.getString(R.string.n_c_id),
            resources.getString(R.string.n_c_secret),
            resources.getString(R.string.app_name))

        val localCacheSettings = PersistentCacheSettings
            .newBuilder()
            .setSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()

        val fdbSetting = FirebaseFirestoreSettings
            .Builder()
            .setLocalCacheSettings(localCacheSettings)
            .build()

        val fdb = Firebase.firestore
        fdb.firestoreSettings = fdbSetting

        FirebaseFirestore.getInstance().enableNetwork()
    }
}