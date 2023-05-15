package me.metropants.musicbot.command

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import java.util.EnumSet

abstract class Command(val name: String, description: String) {

    val permission: EnumSet<Permission> = EnumSet.noneOf(Permission::class.java)
    val flags: EnumSet<CommandFlag> = EnumSet.noneOf(CommandFlag::class.java)
    val data: SlashCommandData = Commands.slash(name, description)

    fun options(vararg options: OptionData) {
        data.addOptions(*options)
    }

    fun permissions(vararg permissions: Permission) {
        this.permission.addAll(permissions)
    }

    fun flags(flags: EnumSet<CommandFlag>) {
        this.flags.addAll(flags)
    }

    abstract suspend fun execute(event: SlashCommandInteractionEvent)

}