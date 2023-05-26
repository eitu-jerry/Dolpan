package com.eitu.dolpan.view.base

import android.app.Activity
import android.content.SharedPreferences
import com.eitu.dolpan.network.youtube.YoutubeRetrofit
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

interface BaseViewInterface {

    var activity: Activity
    var TAG : String
    var sp : SharedPreferences
    var editor : SharedPreferences.Editor
    val fdb: FirebaseFirestore
        get() = Firebase.firestore
    var youtube: YoutubeRetrofit

    fun init()

}