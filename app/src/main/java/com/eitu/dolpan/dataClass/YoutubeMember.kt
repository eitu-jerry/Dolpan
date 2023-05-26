package com.eitu.dolpan.dataClass

import com.eitu.dolpan.dataClass.viewpager.YoutubeChannel
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

data class YoutubeMember(
    @PropertyName("name")
    val name: String? = null,
    @PropertyName("bannerImage")
    val bannerImage: String? = null,
    @PropertyName("channel")
    val channel: HashMap<String, String> = HashMap(),
    @PropertyName("owner")
    val owner: String = ""
    ){

    companion object {
        suspend fun newInstance(owner: String): HashMap<String, Any?>? {
            val result = Firebase.firestore
                .collection("youtubeChannel")
                .whereEqualTo("owner", owner)
                .get()
                .await()
            if (!result.isEmpty) {
                val name: String
                when(owner) {
                    "wak" -> name = "우왁굳 WOOWAKGOOD"
                    "wakta" -> name = "왁타버스 WAKTAVERSE"
                    "ine" -> name = "아이네 INE"
                    "jing" -> name = "징버거 JINGBURGER"
                    "lilpa" -> name = "릴파 LILPA"
                    "jururu" -> name = "주르르 JURURU"
                    "gosegu" -> name = "고세구 GOSEGU"
                    "vichan" -> name = "비챤 VICHAN"
                    else -> name = ""
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

                val channelMap = LinkedHashMap<String, String>()
                if (channelMain != null) channelMap.put("main", getChannelString(channelMain))
                if (channelSub != null) channelMap.put("sub", getChannelString(channelSub))
                if (channelReplay != null) channelMap.put("replay", getChannelString(channelReplay))

                return hashMapOf(
                    Pair("name", name),
                    Pair("bannerImage", channelMain?.bannerImage),
                    Pair("channel", channelMap),
                    Pair("owner", owner)
                )
            }
            else {
                return null
            }
        }

        fun getChannelString(channel: YoutubeChannel): String {
            return "${channel.id},${channel.title},${channel.customUrl}"
        }

        suspend fun toList(owners: List<String>): List<YoutubeMember?> {
            val list = ArrayList<YoutubeMember?>()
            for (owner in owners) {
                //list.add(newInstance(owner))
            }
            return list
        }
    }

}
