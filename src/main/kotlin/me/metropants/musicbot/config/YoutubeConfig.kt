package me.metropants.musicbot.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("youtube")
data class YoutubeConfig(val email: String, val password: String, val apiKey: String, val endpoint: String)
