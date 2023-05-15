package me.metropants.musicbot.audio

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import dev.minn.jda.ktx.messages.Embed
import me.metropants.musicbot.audio.modal.SongRequest

class AudioResultHandler(private val scheduler: GuildAudioScheduler, private val request: SongRequest) : AudioLoadResultHandler {

    override fun trackLoaded(track: AudioTrack?) {
        if (track == null) {
            return
        }

        val queued = scheduler.queue(track)
        val hook = request.hook
        if (queued) {
            hook.sendMessageEmbeds(Embed {
                color = 0x1C7ED6
                description = "Queued: `${track.info.title}`"
                footer("Requested by: ${request.requester.user.asTag}", request.requester.effectiveAvatarUrl)
            }).queue()
            return
        }

        hook.sendMessageEmbeds(Embed {
            color = 0x1C7ED6
            description = "Now playing: `${track.info.title}`"
            footer("Requested by: ${request.requester.user.asTag}", request.requester.effectiveAvatarUrl)
        }).queue()
    }

    override fun playlistLoaded(playlist: AudioPlaylist?) {
        if (playlist == null) {
            return
        }

        val track = playlist.selectedTrack ?: playlist.tracks[0]
        val hook = request.hook

        val queued = scheduler.queue(track)
        if (queued) {
            hook.sendMessageEmbeds(Embed {
                color = 0x1C7ED6
                description = "Queued: `${track.info.title}`"
                footer("Requested by: ${request.requester.user.asTag}", request.requester.effectiveAvatarUrl)
            }).queue()
        }

        val tracks = playlist.tracks
        if (tracks.size <= 1) {
            return
        }

        for (i in 1 until tracks.size) {
            scheduler.queue(tracks[i])
        }
    }

    override fun noMatches() {
        request.hook.sendMessageEmbeds(Embed {
            color = 0x1C7ED6
            description = "No results found."
            footer("Requested by: ${request.requester.user.asTag}", request.requester.effectiveAvatarUrl)
        }).queue()
    }

    override fun loadFailed(exception: FriendlyException?) {
        request.hook.sendMessageEmbeds(Embed {
            color = 0x1C7ED6
            description = exception?.localizedMessage ?: "Failed to load song."
            footer("Requested by: ${request.requester.user.asTag}", request.requester.avatarUrl)
        }).queue()
    }

}