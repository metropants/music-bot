package me.metropants.musicbot.listener

import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.util.SLF4J
import me.metropants.musicbot.service.MusicService
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent
import org.springframework.stereotype.Component

@Component
class VoiceChannelListener(jda: JDA, music: MusicService) {

    private val logger by SLF4J

    init {
        jda.listener<GuildVoiceUpdateEvent> {
            val guild = it.guild

            it.channelLeft?.let { channel ->
                if (channel.members.size == 1 && channel.members[0].user.isBot) {
                    val manager = music.guildAudioPlayer(guild.idLong)

                    manager.disconnect()
                    logger.info("Disconnected audio connection for ${guild.name} guild.")
                }
            }
        }
    }

}