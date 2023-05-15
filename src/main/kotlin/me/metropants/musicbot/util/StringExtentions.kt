package me.metropants.musicbot.util

import java.net.MalformedURLException
import java.net.URL

fun String.isURL(): Boolean {
    return try {
        URL(this)
        true
    } catch (ignore: MalformedURLException) {
        false
    }
}
