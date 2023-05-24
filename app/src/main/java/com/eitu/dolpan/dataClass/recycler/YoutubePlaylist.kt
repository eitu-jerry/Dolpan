package com.eitu.dolpan.dataClass.recycler

import org.json.JSONObject

//https://developers.google.com/youtube/v3/docs/playlists?hl=ko#resource
data class YoutubePlaylist(val obj: JSONObject) {

    val id: String
    val title: String
    val publishedAt: String
    val defaultThumb: String
    val standardThumb: String
    val playlistId: String

    init {
        val snippet = obj.getJSONObject("snippet")
        title = snippet.getString("title")
        playlistId = snippet.getString("playlistId")

        val resourceId = snippet.getJSONObject("resourceId")
        id = resourceId.getString("videoId")

        val thumbnails = snippet.getJSONObject("thumbnails")
        defaultThumb = thumbnails.getJSONObject("default").getString("url")
        standardThumb = thumbnails.getJSONObject("standard").getString("url")

        val contentDetails = obj.getJSONObject("contentDetails")
        publishedAt = contentDetails.getString("videoPublishedAt")
    }

    fun toHash(): HashMap<String, Any> {
        return hashMapOf(
            "id" to this.id,
            "title" to this.title,
            "publishedAt" to this.publishedAt,
            "defaultThumb" to this.defaultThumb,
            "standardThumb" to this.standardThumb,
        )
    }

    companion object {
        fun toList(body: String?): List<YoutubePlaylist> {
            val list = ArrayList<YoutubePlaylist>()

            if (body != null) {
                try {
                    val items = JSONObject(body).getJSONArray("items")
                    for (i in 0 until items.length()) {
                        list.add(YoutubePlaylist(items.getJSONObject(i)))
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            return list
        }

        fun toHashList(list: List<YoutubePlaylist>): List<HashMap<String, Any>> {
            val hashList = ArrayList<HashMap<String, Any>>()
            for (item in list) {
                hashList.add(item.toHash())
            }

            return hashList
        }
    }

}
