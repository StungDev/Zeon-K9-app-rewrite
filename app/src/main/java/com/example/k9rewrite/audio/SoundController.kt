package com.example.k9rewrite.audio

import com.example.k9rewrite.config.SoundConfig

class SoundController(
    private val bank: SoundBank,
) {
    private var dpadStreamId: Int = 0
    private var labelStreamId: Int = 0

    fun playLabel(fileName: String) {
        bank.stop(labelStreamId)
        labelStreamId = bank.playOnce(fileName, SoundConfig.LEFT_ON, SoundConfig.RIGHT_OFF)
    }

    fun playDpadDirection(fileName: String) {
        bank.stop(dpadStreamId)
        dpadStreamId = bank.playOnce(fileName, SoundConfig.LEFT_OFF, SoundConfig.RIGHT_ON)
    }

    fun playStopPriority() {
        bank.stop(dpadStreamId)
        bank.stop(labelStreamId)
        dpadStreamId = 0
        labelStreamId = 0
        bank.playOnce(SoundConfig.DPAD_STOP, SoundConfig.LEFT_OFF, SoundConfig.RIGHT_ON)
    }
}
