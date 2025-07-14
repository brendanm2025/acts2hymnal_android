package com.example.acts2hymnal

import android.content.Context
import android.content.res.AssetManager
import java.io.File
import java.io.IOException

class SongData(
    // metadata
    var name: String = "",
    var author: String = "",
    var composer: String = "",
    var translator: String = "",
    var christmas: Boolean = false,

    // raw verse/chorus chunks
    val chunks: MutableList<String> = mutableListOf(),
    val isChorus: MutableList<Boolean> = mutableListOf(), // used to change chorus italicization

    // associated music file
    var audio_file: String = "",
) : Comparable<SongData> {

    // comparison override (for sorting)
    override fun compareTo(other: SongData): Int {
        return this.name.compareTo(other.name, ignoreCase = true)
    }

    fun buildMetadata(): String {
        val sb = StringBuilder()

        listOf(
            "Author" to author,
            "Composer" to composer,
            "Translator" to translator,
        ).forEach { (key, value) ->
            if (value.isNotEmpty()) {
                sb.append("$key: $value\n")
            }
        }

        return sb.toString()
    }
}

fun assetExists(fileName: String, assetManager: AssetManager): Boolean {
    return try {
        assetManager.open(fileName).close()
        true
    } catch (e: IOException){
        false
    }
}

fun findAudioFile(fileName: String, newExtension: String, context: Context): String {
    val folderName = "audio_data"
    val rootName = File(fileName).nameWithoutExtension
    val targetFile = "$rootName.$newExtension"
    // println("targetfile: $targetFile\n")
    return try {
        context.assets.open("$folderName/$targetFile").close()
        "$folderName/$targetFile"
    } catch (e: IOException) {
        ""
    }
}

fun readAllSongs(context: Context): List<SongData> {

    val allSongs = mutableListOf<SongData>()

    val folderName = "lyric_data"
    val assetManager = context.assets

    val fileNames = assetManager.list(folderName) ?: emptyArray()
    for (fileName in fileNames) {

        val audio_file = findAudioFile(fileName, "mp3", context)
        // println("Audio File: $audio_file\n")

        // open each file
        val inputStream = assetManager.open("$folderName/$fileName")

        // storage
        val song = SongData()
        song.audio_file = audio_file

        val handlers = mapOf<String, (String) -> Unit>(
            "name:: " to { value -> song.name = value },
            "author:: " to { value -> song.author = value },
            "composer:: " to { value -> song.composer = value },
            "translator:: " to { value -> song.translator = value },
            "collection:: " to { value -> song.translator = value }
        )
        var reachedText = false
        val sb = StringBuilder()
        var inChorus = false

        inputStream.bufferedReader().useLines { lines ->
            lines.forEach { line ->

                if (line.trim().equals("text::", ignoreCase = true)) {
                    reachedText = true
                    return@forEach
                }

                if (!reachedText) {
                    for ((prefix, handler) in handlers) {
                        if (line.startsWith(prefix, ignoreCase = true)) {
                            val value = line.substringAfter(prefix).trim()
                            handler(value)
                            break
                        }
                    }
                    // Handle collection:: separately
                    if (line.startsWith("collection:: ", ignoreCase = true)) {
                        val value = line.substringAfter("collection:: ").trim()
                        if (value.equals("Christmas", ignoreCase = true)) {
                            song.christmas = true
                        }
                    }
                } else {
                    if (line.isNotEmpty()) {
                        if (line == "[Refrain]") {
                            inChorus = true
                        } else {
                            sb.append("$line\n")
                        }
                    } else {
                        song.chunks.add(sb.toString())
                        song.isChorus.add(inChorus)
                        inChorus = false
                        sb.clear()
                    }
                }
            }
        }
        if (sb.isNotEmpty()) {
            song.chunks.add(sb.toString().trim())
            song.isChorus.add(inChorus)
        }
        allSongs.add(song)
    }

    // sort the SongData list alphabetically
    val allSongsSorted = allSongs.sorted()
    allSongsSorted.forEach {item -> println("${item.name}\n${item.chunks.last()}\n")
    }
    return allSongsSorted
}
