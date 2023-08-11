package com.eitu.dolpan.network.repo

import android.content.Context
import com.eitu.dolpan.R
import com.eitu.dolpan.dataClass.YoutubeMember
import com.eitu.dolpan.dataClass.youtube.YoutubeChannel
import com.eitu.dolpan.network.DolpanResult
import com.eitu.dolpan.network.api.YoutubeAPI_
import com.eitu.dolpan.network.returnResult
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class YoutubeRepo @Inject constructor(
    @ApplicationContext
    val context : Context,
    val api : YoutubeAPI_,
    val fdb : FirebaseFirestore
    ) {

    val wak = context.resources.getStringArray(R.array.wak)
    val wakta = context.resources.getStringArray(R.array.wakta)
    val ine = context.resources.getStringArray(R.array.ine)
    val jing = context.resources.getStringArray(R.array.jing)
    val lilpa = context.resources.getStringArray(R.array.lilpa)
    val jururu = context.resources.getStringArray(R.array.jururu)
    val gosegu = context.resources.getStringArray(R.array.gosegu)
    val vichan = context.resources.getStringArray(R.array.vichan)

    val mainChannel = context.resources.getStringArray(R.array.channel_main)
    val subChannel = context.resources.getStringArray(R.array.channel_sub)
    val replayChannel = context.resources.getStringArray(R.array.channel_replay)

    suspend fun updateChannels() {
        val ids = context.resources.getStringArray(R.array.channelAll)
        try {
            val result = returnResult(api.getChannels(ids.joinToString(",")))
            when(result) {
                is DolpanResult.Success -> {
                    val channels = result.data.channels
                    withContext(Dispatchers.Default) {
                        setYoutubeChannels(channels)
                    }
                    withContext(Dispatchers.Default) {
                        setYoutubeMembers(channels)
                    }
                }
                is DolpanResult.Error -> {
                    result.exception.printStackTrace()
                }
                else -> {

                }
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }

    }

    private fun setYoutubeChannels(channels: List<YoutubeChannel>) {
        channels.forEach {
            fdb.collection("youtubeChannel")
                .document(it.id)
                .set(it)
        }
    }

    private fun setYoutubeMembers(channels: List<YoutubeChannel>) {
        setYoutubeMember("wak", channels.filter { wak.contains(it.id) })
        setYoutubeMember("wakta", channels.filter { wakta.contains(it.id) })
        setYoutubeMember("ine", channels.filter { ine.contains(it.id) })
        setYoutubeMember("jing", channels.filter { jing.contains(it.id) })
        setYoutubeMember("lilpa", channels.filter { lilpa.contains(it.id) })
        setYoutubeMember("jururu", channels.filter { jururu.contains(it.id) })
        setYoutubeMember("gosegu", channels.filter { gosegu.contains(it.id) })
        setYoutubeMember("vichan", channels.filter { vichan.contains(it.id) })
    }

    private fun setYoutubeMember(owner : String, channels: List<YoutubeChannel>) {
        val main = channels.find { mainChannel.contains(it.id) }
        val sub = channels.find { subChannel.contains(it.id) }
        val replay = channels.find { replayChannel.contains(it.id) }

        val name : String
        val twitch : String

        getNameAndTwitch(owner).apply {
            name = this[0]
            twitch = this[1]
        }

        fdb.collection("youtubeMember")
            .document(owner)
            .set(YoutubeMember(
                name = name,
                profileImage = main?.snippet?.thumbnails?.medium?.url,
                bannerImage = main?.brandSettings?.bannerImage?.bannerUrl,
                description = main?.snippet?.description,
                channel = getHashMap(main, sub, replay),
                owner = owner,
                isLive = false,
                twitch = twitch
            ))
    }

    private fun getNameAndTwitch(owner: String) : Array<String> {
        return when(owner) {
            "wak" -> {
               arrayOf("우왁굳 WOOWAKGOOD", "woowakgood")
            }
            "wakta" -> {
                arrayOf("왁타버스 WAKTAVERSE", "woowakgood")
            }
            "ine" -> {
                arrayOf("아이네 INE", "vo_ine")
            }
            "jing" -> {
                arrayOf("징버거 JINGBURGER", "jingburger")
            }
            "lilpa" -> {
                arrayOf("릴파 LILPA", "lipaaaaaa")
            }
            "jururu" -> {
                arrayOf("주르르 JURURU", "cotton__123")
            }
            "gosegu" -> {
                arrayOf("고세구 GOSEGU", "gosegugosegu")
            }
            "vichan" -> {
                arrayOf("비챤 VICHAN", "viichan6")
            }
            else -> {
                arrayOf("", "")
            }
        }
    }

    private fun getHashMap(main : YoutubeChannel?, sub : YoutubeChannel?, replay : YoutubeChannel?) : HashMap<String, String> {
        val channel = hashMapOf<String, String>()
        main?.let { channel.put("main", "${it.id},${it.snippet.title},${it.snippet.customUrl}") }
        sub?.let { channel.put("sub", "${it.id},${it.snippet.title},${it.snippet.customUrl}") }
        replay?.let { channel.put("replay", "${it.id},${it.snippet.title},${it.snippet.customUrl}") }

        return channel
    }

}