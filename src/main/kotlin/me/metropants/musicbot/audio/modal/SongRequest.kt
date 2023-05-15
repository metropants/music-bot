package me.metropants.musicbot.audio.modal

import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.interactions.InteractionHook

data class SongRequest(val requester: Member, val url: String, val hook: InteractionHook)
