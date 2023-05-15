package me.metropants.musicbot.service.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.metropants.musicbot.config.YoutubeConfig
import me.metropants.musicbot.service.MusicService
import me.metropants.musicbot.service.SongDiscoveryService
import me.metropants.musicbot.service.SongDiscoveryService.Companion.YOUTUBE_CATEGORY_ID
import me.metropants.musicbot.service.SongDiscoveryService.Companion.YOUTUBE_PART
import me.metropants.musicbot.service.SongDiscoveryService.Companion.YOUTUBE_TYPE
import net.dv8tion.jda.api.utils.data.DataObject
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class SongDiscoveryServiceImpl(val config: YoutubeConfig) : SongDiscoveryService {

    private val client = OkHttpClient()

    private fun url(query: String): HttpUrl {
        val endpoint = config.endpoint.split("/")
        return HttpUrl.Builder()
            .scheme("https")
            .host(endpoint[0])
            .addPathSegment(endpoint[1])
            .addPathSegment(endpoint[2])
            .addPathSegment(endpoint[3])
            .addQueryParameter("key", config.apiKey)
            .addQueryParameter("q", query)
            .addQueryParameter("type", YOUTUBE_TYPE)
            .addQueryParameter("part", YOUTUBE_PART)
            .addQueryParameter("videoCategoryId", YOUTUBE_CATEGORY_ID)
            .build()
    }

    override suspend fun discoveredURL(query: String): String {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url(url(query))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Failed to discover song.")

                val body = response.body!!
                val data = DataObject.fromJson(body.byteStream())

                val results = data.getArray("items")
                val id = results.getObject(0).getObject("id").getString("videoId")
                "https://www.youtube.com/watch?v=${id}"
            }
        }
    }

}