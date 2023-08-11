package com.eitu.dolpan.dataClass

import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

data class YoutubeMember(
    @PropertyName("name")
    val name: String? = null,
    @PropertyName("profileImage")
    val profileImage: String? = null,
    @PropertyName("bannerImage")
    val bannerImage: String? = null,
    @PropertyName("description")
    val description: String? = null,
    @PropertyName("channel")
    val channel: HashMap<String, String> = HashMap(),
    @PropertyName("owner")
    val owner: String = "",
    @get:PropertyName("isLive")
    @set:PropertyName("isLive")
    var isLive: Boolean = false,
    @PropertyName("twitch")
    val twitch: String = ""
    ){

    override fun toString(): String {
        return "name:$name\n" +
                "profileImage:$profileImage\n" +
                "bannerImage:$bannerImage\n" +
                "description:$description\n" +
                "channel:$channel\n" +
                "owner:$owner\n" +
                "isLive:$isLive\n" +
                "twitch:$twitch\n"
    }

}
