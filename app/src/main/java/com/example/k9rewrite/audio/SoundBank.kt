package com.example.k9rewrite.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

class SoundBank(
    context: Context,
    fileNames: Set<String>,
    maxStreams: Int = 8,
) {
    private val soundPool: SoundPool
    private val soundIds = HashMap<String, Int>(fileNames.size)
    private val loaded = HashSet<Int>(fileNames.size)
    private val pendingPlays = ArrayList<PendingPlay>()
    private val idToName = HashMap<Int, String>(fileNames.size)

    init {
        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(maxStreams)
            .setAudioAttributes(attrs)
            .build()

        soundPool.setOnLoadCompleteListener { _, sampleId, status ->
            if (status != 0) return@setOnLoadCompleteListener
            loaded.add(sampleId)
            val name = idToName[sampleId] ?: return@setOnLoadCompleteListener

            val iterator = pendingPlays.iterator()
            while (iterator.hasNext()) {
                val p = iterator.next()
                if (p.fileName == name) {
                    iterator.remove()
                    soundPool.play(sampleId, p.left, p.right, 1, p.loop, 1f)
                }
            }
        }

        for (name in fileNames) {
            try {
                context.assets.openFd(name).use { afd ->
                    val id = soundPool.load(afd, 1)
                    soundIds[name] = id
                    idToName[id] = name
                }
            } catch (_: Exception) {
            }
        }
    }

    fun release() {
        soundPool.release()
    }

    fun stop(streamId: Int) {
        if (streamId != 0) soundPool.stop(streamId)
    }

    fun playOnce(fileName: String, left: Float, right: Float): Int =
        play(fileName, left, right, loop = 0)

    fun playLoop(fileName: String, left: Float, right: Float): Int =
        play(fileName, left, right, loop = -1)

    private fun play(fileName: String, left: Float, right: Float, loop: Int): Int {
        val id = soundIds[fileName] ?: return 0
        return if (loaded.contains(id)) {
            soundPool.play(id, left, right, 1, loop, 1f)
        } else {
            pendingPlays.add(PendingPlay(fileName, left, right, loop))
            0
        }
    }

    private data class PendingPlay(
        val fileName: String,
        val left: Float,
        val right: Float,
        val loop: Int,
    )
}
