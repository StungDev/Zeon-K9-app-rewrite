package com.example.k9rewrite

import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.k9rewrite.audio.ConstantClickScheduler
import com.example.k9rewrite.audio.SoundBank
import com.example.k9rewrite.audio.SoundController
import com.example.k9rewrite.config.SoundConfig

class ControlsActivity : ComponentActivity() {
    private lateinit var bank: SoundBank
    private lateinit var sounds: SoundController
    private lateinit var constantClick: ConstantClickScheduler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_controls)

        bank = SoundBank(
            context = this,
            fileNames = SoundConfig.ALL_ASSET_SOUNDS,
        )
        sounds = SoundController(bank)
        constantClick = ConstantClickScheduler(bank)

        findViewById<Button>(R.id.backToStart).apply {
            isSoundEffectsEnabled = false
            setOnClickListener { finish() }
        }

        findViewById<android.view.View>(R.id.label1)?.apply {
            isSoundEffectsEnabled = false
            setOnClickListener {
                sounds.playLabel("LabelSounds/laser.wav")
            }
        }

        findViewById<android.view.View>(R.id.label2)?.apply {
            isSoundEffectsEnabled = false
            setOnClickListener {
                startActivity(android.content.Intent(this@ControlsActivity, VoicesActivity::class.java))
                overridePendingTransition(R.anim.bounce_in, R.anim.fade_out)
            }
        }

        val labelIds = listOf(
            R.id.label3, R.id.label4,
            R.id.label5, R.id.label6, R.id.label7, R.id.label8
        )
        for (i in labelIds.indices) {
            findViewById<android.view.View>(labelIds[i])?.apply {
                isSoundEffectsEnabled = false
                setOnClickListener {
                    if (i + 2 < SoundConfig.LABEL_SOUNDS.size) {
                        sounds.playLabel(SoundConfig.LABEL_SOUNDS[i + 2])
                    }
                }
            }
        }

        wireDpadButton(buttonId = R.id.dpadUp, downFile = SoundConfig.DPAD_FORWARD)
        wireDpadButton(buttonId = R.id.dpadDown, downFile = SoundConfig.DPAD_BACKWARD)
        wireDpadButton(buttonId = R.id.dpadLeft, downFile = SoundConfig.DPAD_LEFT)
        wireDpadButton(buttonId = R.id.dpadRight, downFile = SoundConfig.DPAD_RIGHT)

        findViewById<Button>(R.id.dpadStop).apply {
            isSoundEffectsEnabled = false
            setOnClickListener { sounds.playStopPriority() }
        }
    }

    private fun wireDpadButton(buttonId: Int, downFile: String) {
        val blackout = findViewById<android.view.View>(R.id.dpadCenterBlackout)
        val button = findViewById<Button>(buttonId).apply { isSoundEffectsEnabled = false }
        button.setOnTouchListener { v, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    sounds.playDpadDirection(downFile)
                    blackout?.visibility = android.view.View.VISIBLE
                    v.isPressed = true
                    true
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    sounds.playStopPriority()
                    blackout?.visibility = android.view.View.INVISIBLE
                    v.isPressed = false
                    true
                }

                else -> false
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
