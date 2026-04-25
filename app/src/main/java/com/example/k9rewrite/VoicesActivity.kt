package com.example.k9rewrite

import android.os.Bundle
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.activity.ComponentActivity
import com.example.k9rewrite.audio.ConstantClickScheduler
import com.example.k9rewrite.audio.SoundBank
import com.example.k9rewrite.audio.SoundController
import com.example.k9rewrite.config.SoundConfig

class VoicesActivity : ComponentActivity() {
    private lateinit var bank: SoundBank
    private lateinit var sounds: SoundController
    private lateinit var constantClick: ConstantClickScheduler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voices)

        bank = SoundBank(this, SoundConfig.ALL_ASSET_SOUNDS)
        sounds = SoundController(bank)
        constantClick = ConstantClickScheduler(bank)

        findViewById<Button>(R.id.backToStart).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
                overridePendingTransition(R.anim.fade_in, R.anim.bounce_out)
            }
        })

        val voiceButtons = listOf(
            R.id.voice1, R.id.voice2, R.id.voice3, R.id.voice4,
            R.id.voice5, R.id.voice6, R.id.voice7, R.id.voice8
        )

        for (i in voiceButtons.indices) {
            findViewById<Button>(voiceButtons[i])?.apply {
                isSoundEffectsEnabled = false
                setOnClickListener {
                    sounds.playLabel(SoundConfig.LABEL_SOUNDS[i])
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        constantClick.start()
    }

    override fun onStop() {
        constantClick.stop()
        super.onStop()
    }

    override fun onDestroy() {
        constantClick.release()
        bank.release()
        super.onDestroy()
    }
}
