package me.metropants.musicbot

import me.metropants.musicbot.config.DiscordConfig
import me.metropants.musicbot.config.YoutubeConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(DiscordConfig::class, YoutubeConfig::class)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
