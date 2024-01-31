package com.biebha.gamesederhana

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.media.MediaPlayer

class GameActivity : AppCompatActivity() {

    private lateinit var playerName: String
    private lateinit var gameScoreText: TextView
    private lateinit var tapMeImage: ImageView
    private lateinit var timeLeftText: TextView
    private lateinit var barnilai: ProgressBar
    private var selectedTime: Int = 0
    private val maxProgressBarValue = 90
    private var maxScore = 0
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var bintang1: ImageView
    private lateinit var bintang2: ImageView
    private lateinit var bintang3: ImageView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var getbintang: MediaPlayer

    private var isAchievementSoundPlayed = false
    private var score = 0
    private var gameStarted = false
    private var isSoundPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        barnilai = findViewById(R.id.barnilai)
        bintang1 = findViewById(R.id.bintang1)
        bintang2 = findViewById(R.id.bintang2)
        bintang3 = findViewById(R.id.bintang3)

        playerName = intent.getStringExtra("PLAYER_NAME") ?: "Player"
        selectedTime = intent.getIntExtra("SELECTED_TIME", 0)
        setMaxScore(selectedTime)

        gameScoreText = findViewById(R.id.game_score_text)
        timeLeftText = findViewById(R.id.time_left_text)  // Inisialisasi TextView

        tapMeImage = findViewById(R.id.tap_me_image)

        gameScoreText.text = getString(R.string.your_score, score)

        mediaPlayer = MediaPlayer.create(this, R.raw.pop_sound)
        getbintang = MediaPlayer.create(this, R.raw.achievement)

        tapMeImage.setOnClickListener {
            animatePopUp()
            incrementScore()
        }

        startGame()
    }

    private fun setMaxScore(selectedTime: Int) {
        when (selectedTime) {
            15 -> maxScore = 90
            30 -> maxScore = 200
            60 -> maxScore = 400
        }
    }

    private fun startGame() {
        countDownTimer = object : CountDownTimer((selectedTime * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = (millisUntilFinished / 1000).toInt()
                val timeLeftString = getString(R.string.time_left, secondsLeft)
                timeLeftText.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }
        }
        gameStarted = true
        isAchievementSoundPlayed = false
        barnilai.progress = 0
        countDownTimer.start()
    }

    private fun endGame() {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("PLAYER_NAME", playerName)
            putExtra("FINAL_SCORE", score)
        }
        startActivity(intent)
        finish()
    }

    private fun incrementScore() {
        score++
        gameScoreText.text = getString(R.string.your_score, score)

        if (score <= maxScore) {
            val progress = (score * maxProgressBarValue) / maxScore
            barnilai.progress = progress
        }

        updateBintang()

        if (score == maxScore) {
            endGame()
        }

        if (!gameStarted) {
            startGame()
        }
    }

    private fun updateBintang() {
        when (selectedTime) {
            15 -> {
                if (score >= 39 && score <= 64) {
                    bintang1.setImageResource(R.drawable.bintang)
                    playBintangSound()
                } else if (score >= 65 && score <= 89) {
                    bintang2.setImageResource(R.drawable.bintang)
                    playBintangSound()
                } else if (score >= 90) {
                    bintang3.setImageResource(R.drawable.bintang)
                    playBintangSound()
                }
            }
            30 -> {
                if (score >= 90 && score <= 130) {
                    bintang1.setImageResource(R.drawable.bintang)
                    playBintangSound()
                } else if (score >= 131 && score <= 199) {
                    bintang2.setImageResource(R.drawable.bintang)
                    playBintangSound()
                } else if (score >= 200) {
                    bintang3.setImageResource(R.drawable.bintang)
                    playBintangSound()
                }
            }
            60 -> {
                if (score >= 180 && score <= 280) {
                    bintang1.setImageResource(R.drawable.bintang)
                    playBintangSound()
                } else if (score >= 281 && score <= 399) {
                    bintang2.setImageResource(R.drawable.bintang)
                    playBintangSound()
                } else if (score >= 400) {
                    bintang3.setImageResource(R.drawable.bintang)
                    playBintangSound()
                }
            }
        }
    }

    private fun playBintangSound() {
        if (!isAchievementSoundPlayed) {
            getbintang.start()
            isAchievementSoundPlayed = true
        }
    }

    private fun animatePopUp() {
        val scaleXUp = ObjectAnimator.ofFloat(tapMeImage, "scaleX", 1.2f, 1.0f)
        val scaleYUp = ObjectAnimator.ofFloat(tapMeImage, "scaleY", 1.2f, 1.0f)

        val bounceAnimation = AnimatorSet()
        bounceAnimation.play(scaleXUp).with(scaleYUp)
        bounceAnimation.duration = 130
        bounceAnimation.interpolator = BounceInterpolator()

        bounceAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                tapMeImage.setImageResource(R.drawable.popcat2)
                if (!isSoundPlaying) {
                    mediaPlayer.seekTo(0)
                    mediaPlayer.start()
                    isSoundPlaying = true
                }
            }

            override fun onAnimationEnd(animation: Animator) {
                tapMeImage.setImageResource(R.drawable.popcat1)
                isSoundPlaying = false
            }
        })

        bounceAnimation.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        getbintang.release()
    }
}
