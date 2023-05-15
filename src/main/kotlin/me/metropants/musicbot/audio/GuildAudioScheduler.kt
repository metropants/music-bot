package me.metropants.musicbot.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import java.util.concurrent.LinkedBlockingDeque

class GuildAudioScheduler(private val player: AudioPlayer) : AudioEventAdapter() {

    private val queue = LinkedBlockingDeque<AudioTrack>()

    init {
        player.addListener(this)
    }

    fun queue(track: AudioTrack): Boolean {
        if (!player.startTrack(track, true)) {
            return queue.offerLast(track)
        }
        return false;
    }

    fun clear() {
        player.destroy()
        queue.clear()
    }

    fun next() {
        val track = queue.pollFirst()
        if (track == null) {
            player.destroy()
            return
        }

        player.startTrack(track, false)
    }

    override fun onTrackEnd(player: AudioPlayer?, track: AudioTrack?, reason: AudioTrackEndReason?) {
        if (reason != null && reason.mayStartNext) {
            next()
        }
    }

}
