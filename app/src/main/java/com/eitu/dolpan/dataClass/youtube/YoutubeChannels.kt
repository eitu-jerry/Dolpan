package com.eitu.dolpan.dataClass.youtube

import com.google.gson.annotations.SerializedName

data class YoutubeChannels(
    @SerializedName("items")
    val channels : List<YoutubeChannel>
)

data class YoutubeChannel(
    @SerializedName("id")
    val id : String,
    @SerializedName("snippet")
    val snippet : Snippet,
    @SerializedName("brandingSettings")
    val brandSettings : BrandSettings,
) {
    data class Snippet(
        @SerializedName("title")
        val title : String,
        @SerializedName("description")
        val description : String,
        @SerializedName("customUrl")
        val customUrl : String,
        @SerializedName("thumbnails")
        val thumbnails : Thumbnails,
    ) {
        data class Thumbnails(
            @SerializedName("medium")
            val medium : Thumbnail
        ) {
            data class Thumbnail(
                @SerializedName("url")
                val url : String
            )
        }
    }

    data class BrandSettings(
        @SerializedName("image")
        val bannerImage : BannerImage
    ) {
        data class BannerImage(
            @SerializedName("bannerExternalUrl")
            val bannerUrl : String
        )
    }
}