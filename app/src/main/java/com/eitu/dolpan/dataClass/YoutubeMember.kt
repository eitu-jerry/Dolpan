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

    companion object {
        suspend fun toHash(owner: String): HashMap<String, Any?>? {
            val result = Firebase.firestore
                .collection("youtubeChannel")
                .whereEqualTo("owner", owner)
                .get()
                .await()
            if (!result.isEmpty) {
                val name: String
                val twitch: String
                when(owner) {
                    "wak" -> {
                        name = "우왁굳 WOOWAKGOOD"
                        twitch = "woowakgood"
                    }
                    "wakta" -> {
                        name = "왁타버스 WAKTAVERSE"
                        twitch = ""
                    }
                    "ine" -> {
                        name = "아이네 INE"
                        twitch = "vo_ine"
                    }
                    "jing" -> {
                        name = "징버거 JINGBURGER"
                        twitch = "jingburger"
                    }
                    "lilpa" -> {
                        name = "릴파 LILPA"
                        twitch = "lilpaaaaaa"
                    }
                    "jururu" -> {
                        name = "주르르 JURURU"
                        twitch = "cotton__123"
                    }
                    "gosegu" -> {
                        name = "고세구 GOSEGU"
                        twitch = "gosegugosegu"
                    }
                    "vichan" -> {
                        name = "비챤 VICHAN"
                        twitch = "viichan6"
                    }
                    else -> {
                        name = ""
                        twitch = ""
                    }
                }

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

                val profileImage: String?
                val description: String?
                if (channelMain != null) {
                    profileImage = channelMain.thumbnail
                    description = channelMain.description
                }
                else {
                    profileImage = ""
                    description = ""
                }

                val channelMap = LinkedHashMap<String, String>()
                if (channelMain != null) channelMap.put("main", getChannelString(channelMain))
                if (channelSub != null) channelMap.put("sub", getChannelString(channelSub))
                if (channelReplay != null) channelMap.put("replay", getChannelString(channelReplay))

                return hashMapOf(
                    Pair("name", name),
                    Pair("profileImage", profileImage),
                    Pair("bannerImage", channelMain?.bannerImage),
                    Pair("description", description),
                    Pair("channel", channelMap),
                    Pair("owner", owner),
                    Pair("isLive", false),
                    Pair("twitch", twitch)
                )
            }
            else {
                return null
            }
        }

        private fun getChannelString(channel: YoutubeChannel): String {
            return "${channel.id},${channel.title},${channel.customUrl}"
        }

    }

}
