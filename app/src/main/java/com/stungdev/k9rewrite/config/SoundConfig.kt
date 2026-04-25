package com.stungdev.k9rewrite.config

object SoundConfig {
    const val LEFT_OFF = 0f
    const val LEFT_ON = 1f
    const val RIGHT_OFF = 0f
    const val RIGHT_ON = 1f

    const val CONSTANT_CLICK = "Constant_click.wav"
    const val CONSTANT_CLICK_INTERVAL_MS = 1_000L

    const val DPAD_FORWARD = "forward.wav"
    const val DPAD_BACKWARD = "backward.wav"
    const val DPAD_LEFT = "left.wav"
    const val DPAD_RIGHT = "right.wav"
    const val DPAD_STOP = "stop.wav"

    val LABEL_SOUNDS: List<String> = listOf(
        "LabelSounds/affirmative master.wav",
        "LabelSounds/i am k9.wav",
        "LabelSounds/negetive.wav",
        "LabelSounds/offencive capability.wav",
        "LabelSounds/stimulating conversation.wav",
        "LabelSounds/too many variables.wav",
        "LabelSounds/warning you.wav",
        "LabelSounds/friend.wav",
    )

    val ALL_ASSET_SOUNDS: Set<String> = buildSet {
        add(CONSTANT_CLICK)
        add(DPAD_FORWARD)
        add(DPAD_BACKWARD)
        add(DPAD_LEFT)
        add(DPAD_RIGHT)
        add(DPAD_STOP)
        add("LabelSounds/laser.wav")
        addAll(LABEL_SOUNDS)
    }
}
