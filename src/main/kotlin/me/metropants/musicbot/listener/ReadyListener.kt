package me.metropants.musicbot.listener

import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.util.SLF4J
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.session.ReadyEvent
import org.springframework.stereotype.Component

@Component
class ReadyListener(jda: JDA) {

    private val logger by SLF4J

    init {
        jda.listener<ReadyEvent> {
            val name = it.jda.selfUser.name
            logger.info("$name is now online.")
        }
    }

}