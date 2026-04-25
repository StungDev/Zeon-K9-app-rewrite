package com.stungdev.k9rewrite.audio

import android.os.Handler
import android.os.Looper
import com.stungdev.k9rewrite.config.SoundConfig

class ConstantClickScheduler(
    private val bank: SoundBank,
) {
    private val handler = Handler(Looper.getMainLooper())
    private var enabled = false

    private val runnable = object : Runnable {
        override fun run() {
            if (!enabled) return
            bank.playOnce(SoundConfig.CONSTANT_CLICK, SoundConfig.LEFT_OFF, SoundConfig.RIGHT_ON)
            handler.postDelayed(this, SoundConfig.CONSTANT_CLICK_INTERVAL_MS)
        }
    }

    fun start() {
        enabled = true
        handler.removeCallbacks(runnable)
        handler.post(runnable)
    }

    fun stop() {
        enabled = false
        handler.removeCallbacks(runnable)
    }

    fun release() {
        handler.removeCallbacks(runnable)
    }
}

