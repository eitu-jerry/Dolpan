package com.eitu.dolpan.dataClass

import com.eitu.dolpan.dataClass.viewpager.YoutubeChannel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class YoutubeMember(
    val name: String?,
    val bannerImage: String?,
    val customUrl: LinkedHashMap<String?, String?>
    ){

    companion object {
        fun newInstance(owner: String) {
            val fdb = Firebase.firestore
            fdb.collection("youtubeChannel")
                .whereEqualTo("owner", owner)
                .get()
                .addOnSuccessListener {
                    if (!it.isEmpty) {
                        var channelMain: YoutubeChannel? = null
                        var channelSub: YoutubeChannel? = null
                        var channelReplay: YoutubeChannel? = null

                        for (item in it.documents) {
                            when(item.get("type")) {
                                "main" -> channelMain = YoutubeChannel(item)
                                "sub" -> channelSub = YoutubeChannel(item)
                                "replay" -> channelReplay = YoutubeChannel(item)
                            }
                        }

                        val urlMap = linkedMapOf(
                            Pair(channelMain?.customUrl, channelMain?.id),
                            Pair(channelSub?.customUrl, channelSub?.id),
                            Pair(channelReplay?.customUrl, channelReplay?.id)
                        )

                        val member = YoutubeMember(
                            name = channelMain?.title,
                            bannerImage = channelMain?.bannerImage,
                            urlMap
                        )
                    }
                }
        }
    }

}
