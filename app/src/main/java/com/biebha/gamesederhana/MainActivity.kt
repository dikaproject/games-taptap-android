package com.biebha.gamesederhana

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import android.media.MediaPlayer


class MainActivity : AppCompatActivity() {

    private lateinit var startsound: MediaPlayer
    private lateinit var playbtn: ImageButton
    private lateinit var playerNameEditText: EditText
    private lateinit var timeSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playbtn = findViewById(R.id.play_btn)
        startsound = MediaPlayer.create(this, R.raw.startgame)
        playerNameEditText = findViewById(R.id.player_name_edit_text)
        timeSpinner = findViewById(R.id.time_spinner)

        ArrayAdapter.createFromResource(
            this,
            R.array.time_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            timeSpinner.adapter = adapter
        }


        playbtn.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> applyZoomEffect(playbtn, 1.2f)
                MotionEvent.ACTION_UP -> {
                    applyZoomEffect(playbtn, 1.0f)
                    playSound();
                    startGame();
                }
            }
            true
        }
    }

    private fun startGame() {
        val playerName = playerNameEditText.text.toString()
        val selectedTime = timeSpinner.selectedItem.toString().toInt()

        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra("PLAYER_NAME", playerName)
            putExtra("SELECTED_TIME", selectedTime)
        }
        startActivity(intent)
    }

    private fun applyZoomEffect(view: View, scaleFactor: Float) {
        val scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, scaleFactor)
        val scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, scaleFactor)

        val scaleAnimatorSet = AnimatorSet()
        scaleAnimatorSet.playTogether(scaleX, scaleY)
        scaleAnimatorSet.duration = 300
        scaleAnimatorSet.interpolator = DecelerateInterpolator()

        scaleAnimatorSet.start()
    }
    private fun playSound() {
        startsound.start()
    }


    override fun onDestroy() {
        startsound.release()
        super.onDestroy()
    }
}
