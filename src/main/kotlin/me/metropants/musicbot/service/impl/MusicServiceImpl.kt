package me.metropants.musicbot.service.impl

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import me.metropants.musicbot.audio.GuildAudioPlayer
import me.metropants.musicbot.audio.GuildAudioSendHandler
import me.metropants.musicbot.config.YoutubeConfig
import me.metropants.musicbot.service.MusicService
import net.dv8tion.jda.api.JDA
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class MusicServiceImpl(private val jda: JDA, config: YoutubeConfig) : MusicService {

    private val players = ConcurrentHashMap<Long, GuildAudioPlayer>()
    private val manager: AudioPlayerManager = DefaultAudioPlayerManager()

    init {
        manager.registerSourceManager(YoutubeAudioSourceManager(true, config.email, config.password)).let {
            AudioSourceManagers.registerLocalSource(manager)
        }
    }

    override fun put(player: GuildAudioPlayer) {
        players.computeIfAbsent(player.id) {
            GuildAudioPlayer(it, jda, manager, this)
        }
    }

    override fun remove(id: Long) {
        if (players.containsKey(id)) {
            return
        }

        players.remove(id)
    }

    override fun guildAudioPlayer(id: Long): GuildAudioPlayer {
        val cached = players[id]
        if (cached != null) {
            return cached
        }

        return GuildAudioPlayer(id, jda, manager, this).also {
            players[id] = it
            jda.getGuildById(id)?.let {
                guild -> guild.audioManager.sendingHandler = GuildAudioSendHandler(it.player)
            }
        }
    }

}