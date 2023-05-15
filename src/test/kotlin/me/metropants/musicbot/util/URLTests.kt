package me.metropants.musicbot.util

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class URLTests {

    @Test
    fun isURL() {
        assertTrue("https://www.youtube.com/".isURL())
        assertFalse("www.youtube".isURL())
    }

}