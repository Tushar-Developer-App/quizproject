package com.data.quizproject.question

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.data.quizproject.activities.MainActivity
import com.data.quizproject.databinding.ActivityResulBinding

class resulActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResulBinding
    var POINTS = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResulBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val correctAnswers = intent.getIntExtra("correct", 0)
        val totalQuestions = intent.getIntExtra("total", 0)

        val points = (correctAnswers * POINTS).toLong()

        binding.score.setText(String.format("%d/%d", correctAnswers, totalQuestions))

        binding.restartBtn.setOnClickListener {

            startActivity(Intent(this , MainActivity::class.java))
            finish()
        }
    }
}