package com.eitu.dolpan.dataClass.viewpager

import android.app.Activity
import com.eitu.dolpan.R
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ktx.getField
import org.json.JSONObject

data class YoutubeChannel(
        val id: String?,
        val title: String?,
        val description: String?,
        val customUrl: String?,
        val thumbnail: String?,
        val bannerImage: String?,
        val type: String?
    ) {

    constructor(obj: JSONObject) : this(
        id = obj.getString("id"),
        title = obj.getJSONObject("snippet").getString("title"),
        description = obj.getJSONObject("snippet").getString("description"),
        customUrl = obj.getJSONObject("snippet").getString("customUrl"),
        thumbnail = obj.getJSONObject("snippet").getJSONObject("thumbnails")
        .getJSONObject("medium")
        .getString("url"),
        bannerImage = obj.getJSONObject("brandingSettings")
        .getJSONObject("image")
        .getString("bannerExternalUrl"),
        type = ""
    )

    constructor(obj: DocumentSnapshot) : this(
        id = obj.getField<String>("id"),
        title = obj.getField<String>("title"),
        description = obj.getField<String>("description"),
        customUrl = obj.getField<String>("customUrl"),
        thumbnail = obj.getField<String>("thumbnail"),
        bannerImage = obj.getField<String>("bannerImage"),
        type = obj.getField<String>("type")
    )

    fun toHash(type: String): HashMap<String, Any?> {
        return hashMapOf(
            "id" to this.id,
            "title" to this.title,
            "description" to this.description,
            "customUrl" to this.customUrl,
            "thumbnail" to this.thumbnail,
            "bannerImage" to this.bannerImage,
            "type" to type,
        )
    }

    companion object {
        fun toList(body: String?): List<YoutubeChannel> {
            val list = ArrayList<YoutubeChannel>()

            if (body != null) {
                try {
                    val items = JSONObject(body).getJSONArray("items")
                    for (i in 0 until items.length()) {
                        list.add(YoutubeChannel(items.getJSONObject(i)))
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            return list
        }

        fun toList(fromDB: List<DocumentSnapshot>): List<YoutubeChannel> {
            val list = ArrayList<YoutubeChannel>()

            for (item in fromDB) {
                list.add(YoutubeChannel(item))
            }

            return list
        }

        fun toHashList(list: List<YoutubeChannel>, activity: Activity): List<HashMap<String, Any?>> {
            val channelMain = activity.resources.getStringArray(R.array.channel_main)
            val channelSub = activity.resources.getStringArray(R.array.channel_sub)
            val channelReplay = activity.resources.getStringArray(R.array.channel_replay)

            val hashList = ArrayList<HashMap<String, Any?>>()
            for (item in list) {
                val id = item.id
                val type: String
                if (channelMain.contains(id)) type = "main"
                else if (channelSub.contains(id)) type = "sub"
                else type = "replay"
                hashList.add(item.toHash(type))
            }

            return hashList
        }
    }

}