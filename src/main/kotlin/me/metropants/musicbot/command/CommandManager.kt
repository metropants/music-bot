package me.metropants.musicbot.command

import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.messages.Embed
import dev.minn.jda.ktx.util.SLF4J
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class CommandManager(context: ApplicationContext, jda: JDA) {

    private final val logger by SLF4J
    private final val commands = ConcurrentHashMap<String, Command>()

    init {
        context.getBeansOfType(Command::class.java).forEach { (_, command) ->
            commands[command.name] = command
            logger.info("Registered ${command.name} command.")
        }

        jda.updateCommands().addCommands(commands.values.map { it.data }).queue {
            logger.info("Successfully updated slash commands.")
        }

        jda.listener<SlashCommandInteractionEvent> {
            val name = it.name
            val command = commands[name] ?: return@listener
            val guild = it.guild ?: return@listener
            val member = it.member ?: return@listener

            if (!member.hasPermission(command.permission)) {
                it.replyEmbeds(Embed {
                    color = 0x1C7ED6
                    description = "You are missing required permission to use this."
                }).setEphemeral(true).queue()
                return@listener
            }

            if (CommandFlag.MUST_BE_IN_VOICE in command.flags) {
                val state = member.voiceState ?: return@listener
                if (!state.inAudioChannel()) {
                    it.replyEmbeds(Embed {
                        color = 0x1C7ED6
                        description = "You must be connected to a voice channel to play music."
                    }).setEphemeral(true).queue()
                    return@listener
                }
            }

            if (CommandFlag.IN_SAME_VOICE in command.flags) {
                val memberState = member.voiceState ?: return@listener
                val botState = guild.selfMember.voiceState ?: return@listener
                if (memberState.channel != null && botState.channel != null) {
                    if (memberState.channel != botState.channel) {
                        it.replyEmbeds(Embed {
                            color = 0x1C7ED6
                            description = "You must be connected to the same voice channel to play music."
                        }).setEphemeral(true).queue()
                        return@listener
                    }
                }
            }

            command.execute(it)
        }
    }

}