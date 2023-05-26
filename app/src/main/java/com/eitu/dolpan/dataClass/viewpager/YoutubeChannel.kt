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
        val type: String?,
        val owner: String?
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
        type = "",
        owner = ""
    )

    constructor(obj: DocumentSnapshot) : this(
        id = obj.getField("id"),
        title = obj.getField("title"),
        description = obj.getField("description"),
        customUrl = obj.getField("customUrl"),
        thumbnail = obj.getField("thumbnail"),
        bannerImage = obj.getField("bannerImage"),
        type = obj.getField("type"),
        owner = obj.getField("owner")
    )

    fun toHash(type: String, owner: String): HashMap<String, Any?> {
        return hashMapOf(
            "id" to this.id,
            "title" to this.title,
            "description" to this.description,
            "customUrl" to this.customUrl,
            "thumbnail" to this.thumbnail,
            "bannerImage" to this.bannerImage,
            "type" to type,
            "owner" to owner
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

            val wakArray = activity.resources.getStringArray(R.array.wak)
            val ineArray = activity.resources.getStringArray(R.array.ine)
            val jingArray = activity.resources.getStringArray(R.array.jing)
            val lilpaArray = activity.resources.getStringArray(R.array.lilpa)
            val jururuArray = activity.resources.getStringArray(R.array.jururu)
            val goseguArray = activity.resources.getStringArray(R.array.gosegu)
            val vichanArray = activity.resources.getStringArray(R.array.vichan)

            val hashList = ArrayList<HashMap<String, Any?>>()
            for (item in list) {
                val id = item.id

                val type: String
                if (channelMain.contains(id)) type = "main"
                else if (channelSub.contains(id)) type = "sub"
                else type = "replay"

                val owner: String
                if (wakArray.contains(id)) owner = "wak"
                else if (ineArray.contains(id)) owner = "ine"
                else if (jingArray.contains(id)) owner = "jing"
                else if (lilpaArray.contains(id)) owner = "lilpa"
                else if (jururuArray.contains(id)) owner = "jururu"
                else if (goseguArray.contains(id)) owner = "gosegu"
                else if (vichanArray.contains(id)) owner = "vichan"
                else owner = "wakta"

                hashList.add(item.toHash(type, owner))
            }

            return hashList
        }
    }

}