package com.stungdev.k9rewrite

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.stungdev.k9rewrite.audio.SoundBank
import com.stungdev.k9rewrite.audio.SoundController
import com.stungdev.k9rewrite.config.SoundConfig

class StartActivity : ComponentActivity() {
    private lateinit var bank: SoundBank
    private lateinit var sounds: SoundController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_start)

        bank = SoundBank(
            context = this,
            fileNames = SoundConfig.ALL_ASSET_SOUNDS,
        )
        sounds = SoundController(bank)

        sounds.playLabel("LabelSounds/i am k9.wav")

        findViewById<Button>(R.id.startButton).apply {
            isSoundEffectsEnabled = false
            setOnClickListener {
                startActivity(Intent(this@StartActivity, ControlsActivity::class.java))
            }
        }
    }

    override fun onDestroy() {
        bank.release()
        super.onDestroy()
    }
}
