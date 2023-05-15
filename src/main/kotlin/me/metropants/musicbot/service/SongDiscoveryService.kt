package me.metropants.musicbot.service

interface SongDiscoveryService {

    companion object {

        const val YOUTUBE_TYPE = "video"
        const val YOUTUBE_PART = "snippet"
        const val YOUTUBE_CATEGORY_ID = "10"

    }

    suspend fun discoveredURL(query: String): String

}