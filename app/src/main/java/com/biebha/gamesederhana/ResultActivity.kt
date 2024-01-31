package com.biebha.gamesederhana

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var playerName: String
    private var finalScore: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)


        playerName = intent.getStringExtra("PLAYER_NAME") ?: "Player"
        finalScore = intent.getIntExtra("FINAL_SCORE", 0)

        val resultMessage = findViewById<TextView>(R.id.result_message)
        resultMessage.text = getString(R.string.result_message, playerName, finalScore)

        val kembali = findViewById<ImageButton>(R.id.kembali_btn)
        kembali.setOnClickListener {
            val intent = Intent(this@ResultActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
