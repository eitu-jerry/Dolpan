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

const val CHAT_CLIENT_ID : String = "kimne78kx3ncx6brgo4mv6wki5h1ko"
const val CHAT_AUTHORIZATION : String = "OAuth 33n243nv1kk3u7jnmc3u5kahfpoltr"
const val CLIENT_ID = "ywq2id2tpi7ke78l8allk0ui8ksh5c"
const val CLIENT_SECRET = "pa22487zs2b9pozb6n88hofhzh0ruv"
const val GRANT_TYPE = "client_credentials"
const val YOUTUBE_API_KEY = "AIzaSyDAQTrSTT4rwytrm4yOIqVMtshfJhC56uo"
const val COOKIE = "NNB=66ROIEEAG7BWI; nid_inf=-1577984345; NID_AUT=rIB/cG2DWvMf0Z6ehYNYAGpM5MlxrPCiWZEaFTjEvoZSWLWo1AYztQ9BJZHBl20d; NID_JKL=A2cpTrUrWhjpezUv9zULlF/jBMeT38lJu2zgkPb/o+k=; NID_SES=AAABwepHm2bSckf+C3niIrbH2O5mUCFkwpa1O2neq7J6q72b8zAmSE0ZbdVRhRrob3CwU9i0dFOLZ5JDMlQkCaCWGeItMyMszjubzi2j9QJZV19imaeSOVIah/IoobL3xwbEVWgAzYjGdgY+PQxPtiLPYUERFnf9KdDj+ig90y6f/LtwIYEN5WdkTcl4IdWNyZOTzJx0o7up1linq1O1IQ6yIypYCiZFrWvfauOK0HBALwi6f6Gk4R8DYRZ1sFOFdOeot1K1IPz97Ke9pLxWLPNTUSgexAH+wjsT4tT1Yj43Rkg7zTgoaNimScG36uZPP16a85MYjP5i8KDNLPtltFyoQPMwgT+ePW1XiicHI61oVUCWmhFgxReMyhppjG2uGV6YHBeGVsHI7Bk2/kcB2TLWcaE0Zu+eVyPaMFAuAwLuzt0BtQ6Vv1IRq1okHboudU9kR0HyRGx01/Rdl61g9lQZUzodcW2q8ef8dZ5dymaeplXTtXFkEUdzDdPjlm8Ctzaf0A+0duS48/MGMRpknexsZCd1QQfB1Aqw6CtCkHZ0eJ6V7eKzz1eqpsS42OcLrZOUb6Kn24agPreTMryE8fXIK3AyQCt4AgW/W2jw5orG2QiH"

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