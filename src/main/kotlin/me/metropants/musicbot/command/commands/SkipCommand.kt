package me.metropants.musicbot.command.commands

import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.messages.Embed
import me.metropants.musicbot.command.Command
import me.metropants.musicbot.service.MusicService
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.springframework.stereotype.Component

@Component
class SkipCommand(private val music: MusicService) : Command("skip", "Skips the current song.") {

    override suspend fun execute(event: SlashCommandInteractionEvent) {
        val guild = event.guild ?: return
        val member = event.member ?: return

        val manager = music.guildAudioPlayer(guild.idLong)
        val current = manager.current()
        if (current == null) {
            event.replyEmbeds(Embed {
                color = 0x1C7ED6
                description = "No songs playing to skip."
            }).await()
            return
        }

        manager.skip().also {
            event.replyEmbeds(Embed {
                color = 0x1C7ED6
                description = "Skipped: `${current.info.title}`"
                footer("Skipped by: ${member.user.asTag}", member.effectiveAvatarUrl)
            }).await()
        }
    }

}