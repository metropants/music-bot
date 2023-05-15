package me.metropants.musicbot.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("discord")
data class DiscordConfig(val token: String)
