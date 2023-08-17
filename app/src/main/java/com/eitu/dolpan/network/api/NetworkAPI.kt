package com.eitu.dolpan.network.api

import com.eitu.dolpan.dataClass.naver.member.NaverCafeMemberResult
import com.eitu.dolpan.dataClass.naver.menu.NaverCafeMenuResult
import com.eitu.dolpan.dataClass.twitch.chat.request.TwitchChatPayload
import com.eitu.dolpan.dataClass.twitch.TwitchLive
import com.eitu.dolpan.dataClass.twitch.TwitchToken
import com.eitu.dolpan.dataClass.twitch.chat.response.TwitchChatItem
import com.eitu.dolpan.dataClass.youtube.YoutubeChannels
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*



interface TwitchUpdateTokenAPI {

    @POST("oauth2/token?" +
            "client_id=${CLIENT_ID}&" +
            "client_secret=${CLIENT_SECRET}&" +
            "grant_type=${GRANT_TYPE}")
    suspend fun getAccessToken(): Response<TwitchToken>

}

interface TwitchCheckLiveAPI {

    @Headers("Client-ID: ${CLIENT_ID}")
    @GET("helix/search/channels")
    suspend fun isLive(@Header("Authorization") accessToken: String, @Query("query") id:String): Response<TwitchLive>

}

interface TwitchGetChatAPI {

    @POST("gql")
    suspend fun getChat(
        @Header("Client-ID") clientId : String = CHAT_CLIENT_ID,
        @Header("Authorization") authorization : String = CHAT_AUTHORIZATION,
        @Body payload: TwitchChatPayload
    ): Response<TwitchChatItem>

}

interface YoutubeAPI {

    @GET("playlistItems?" +
            "part=id,snippet,contentDetails&" +
            "key=${YOUTUBE_API_KEY}")
    suspend fun getPlaylist(@Query("playlistId") playlistId: String): Call<String>

    @GET("channels?" +
            "part=id,snippet,brandingSettings&" +
            "maxResults=10&" +
            "key=${YOUTUBE_API_KEY}")
    suspend fun getChannels(@Query("id") id: String): Response<YoutubeChannels>

}

interface NaverCafeAPI {

    @GET("cafe-web/cafe2/ArticleListV2dot1.json")
    suspend fun getMenuArticles(
        @Header("Cookie")           cookie    : String = COOKIE,
        @Query("search.clubid")     cafeId    : String = "27842958",
        @Query("search.queryType")  queryType : String = "lastArticle",
        @Query("search.page")       page      : Int = 1,
        @Query("search.perPage")    offset    : Int = 50,
        @Query("search.menuid")     menuId    : String
    ) : Response<NaverCafeMenuResult>

    @GET("cafe-web/cafe-mobile/CafeMemberNetworkArticleList")
    suspend fun getMemberArticles(
        @Header("Cookie")           cookie      : String = COOKIE,
        @Query("search.cafeId")     cafeId      : String = "27842958",
        @Query("search.page")       page        : Int = 1,
        @Query("search.perPage")    offset      : Int = 50,
        @Query("requestFrom")       requestFrom : String = "A",
        @Query("search.memberKey")  memberKey   : String
    ) : Response<NaverCafeMemberResult>

}
