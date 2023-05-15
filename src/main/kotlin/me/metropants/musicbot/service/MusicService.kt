package me.metropants.musicbot.service

import me.metropants.musicbot.audio.GuildAudioPlayer
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion

interface MusicService {

    companion object {

        fun connectToVoiceChannel(guild: Guild, channel: AudioChannelUnion) {
            val manager = guild.audioManager
            if (manager.isConnected) {
                return
            }

            manager.openAudioConnection(channel)
        }

    }

    fun put(player: GuildAudioPlayer)

    fun remove(id: Long)

    fun guildAudioPlayer(id: Long): GuildAudioPlayer

}