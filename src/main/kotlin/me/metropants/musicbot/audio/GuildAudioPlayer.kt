package me.metropants.musicbot.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import me.metropants.musicbot.audio.modal.SongRequest
import me.metropants.musicbot.service.MusicService
import me.metropants.musicbot.service.MusicService.Companion.connectToVoiceChannel
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion

class GuildAudioPlayer(
    val id: Long,
    private val jda: JDA,
    private val manager: AudioPlayerManager,
    private val music: MusicService,
    val player: AudioPlayer = manager.createPlayer(),
    private val scheduler: GuildAudioScheduler = GuildAudioScheduler(player)
) {

    fun queue(guild: Guild, channel: AudioChannelUnion, request: SongRequest) {
        connectToVoiceChannel(guild, channel).also {
            manager.loadItemOrdered(this, request.url, AudioResultHandler(scheduler, request))
        }
    }

    fun skip() = scheduler.next()

    fun disconnect() {
        val guild = jda.getGuildById(id) ?: return

        guild.audioManager.closeAudioConnection()
        music.remove(id).also {
            guild.audioManager.closeAudioConnection()
            scheduler.clear()
            player.destroy()
        }
    }

    fun current(): AudioTrack? = player.playingTrack

}