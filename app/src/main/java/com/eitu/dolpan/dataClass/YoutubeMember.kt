package com.eitu.dolpan.dataClass

import com.eitu.dolpan.dataClass.viewpager.YoutubeChannel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await

data class YoutubeMember(
    val name: String?,
    val bannerImage: String?,
    val customUrl: LinkedHashMap<String?, String?>,
    val owner: String
    ){

    companion object {
        suspend fun newInstance(owner: String): YoutubeMember? {
            val result = Firebase.firestore
                .collection("youtubeChannel")
                .whereEqualTo("owner", owner)
                .get()
                .await()
            if (!result.isEmpty) {
                var channelMain: YoutubeChannel? = null
                var channelSub: YoutubeChannel? = null
                var channelReplay: YoutubeChannel? = null

                for (item in result.documents) {
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
                    urlMap,
                    owner
                )

                return member
            }
            else {
                return null
            }

//                .addOnSuccessListener {
//                    if (!it.isEmpty) {
//                        var channelMain: YoutubeChannel? = null
//                        var channelSub: YoutubeChannel? = null
//                        var channelReplay: YoutubeChannel? = null
//
//                        for (item in it.documents) {
//                            when(item.get("type")) {
//                                "main" -> channelMain = YoutubeChannel(item)
//                                "sub" -> channelSub = YoutubeChannel(item)
//                                "replay" -> channelReplay = YoutubeChannel(item)
//                            }
//                        }
//
//                        val urlMap = linkedMapOf(
//                            Pair(channelMain?.customUrl, channelMain?.id),
//                            Pair(channelSub?.customUrl, channelSub?.id),
//                            Pair(channelReplay?.customUrl, channelReplay?.id)
//                        )
//
//                        val member = YoutubeMember(
//                            name = channelMain?.title,
//                            bannerImage = channelMain?.bannerImage,
//                            urlMap
//                        )
//                    }
//                }
        }

        suspend fun toList(owners: List<String>): List<YoutubeMember?> {
            val list = ArrayList<YoutubeMember?>()
            for (owner in owners) {
                list.add(newInstance(owner))
            }
            return list
        }
    }

}
