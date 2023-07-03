package me.metropants.musicbot.command.commands

import dev.minn.jda.ktx.messages.Embed
import me.metropants.musicbot.audio.modal.SongRequest
import me.metropants.musicbot.command.Command
import me.metropants.musicbot.command.CommandFlag
import me.metropants.musicbot.service.MusicService
import me.metropants.musicbot.service.SongDiscoveryService
import me.metropants.musicbot.util.isURL
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import org.springframework.stereotype.Component
import java.util.*

@Component
class PlayCommand(
    private val discovery: SongDiscoveryService,
    private val music: MusicService
) : Command("play", "Plays the provided song.") {

    init {
        options(
            OptionData(OptionType.STRING, "query", "A link to the song or title of the song.", true)
        )
        flags(EnumSet.of(CommandFlag.MUST_BE_IN_VOICE, CommandFlag.IN_SAME_VOICE))
    }

    override suspend fun execute(event: SlashCommandInteractionEvent) {
        event.deferReply().queue()

        val guild = event.guild ?: return
        val member = event.member ?: return
        val query = event.getOption("query", OptionMapping::getAsString) ?: return

        var url: String = query
        if (!query.isURL()) {
            try {
                url = discovery.discoveredURL(query)
            } catch (ignored: Exception) {
                event.hook.sendMessageEmbeds(Embed {
                    color = 0x1C7ED6
                    description = "Something went wrong. Try again."
                }).queue()
            }
        }

        val player = music.guildAudioPlayer(guild.idLong)
        val channel = member.voiceState?.channel ?: return
        player.queue(guild, channel, SongRequest(member, url, event.hook))
    }

}
