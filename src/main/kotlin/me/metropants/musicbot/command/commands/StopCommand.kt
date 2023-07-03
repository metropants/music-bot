package me.metropants.musicbot.command.commands

import dev.minn.jda.ktx.messages.Embed
import me.metropants.musicbot.command.Command
import me.metropants.musicbot.service.MusicService
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.springframework.stereotype.Component

@Component
class StopCommand(val music: MusicService) : Command("stop", "Stops the music from playing.") {

    override suspend fun execute(event: SlashCommandInteractionEvent) {
        val guild = event.guild ?: return
        val member = event.member ?: return

        val state = member.voiceState
        if (state == null || state.channel == null) {
            event.replyEmbeds(Embed {
                color = 0x1C7ED6
                description = "You cannot stop music without being in the channel."
            }).queue()
            return
        }

        val manager = music.guildAudioPlayer(guild.idLong)
        manager.disconnect().also {
            event.replyEmbeds(Embed {
                color = 0x1C7ED6
                description = "${member.effectiveName} stopped the music."
            }).queue()
        }
    }

}