package com.data.quizproject.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.data.quizproject.R
import com.data.quizproject.databinding.ActivityQuizBinding
import com.data.quizproject.question.Question
import com.data.quizproject.question.resulActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.Random


class QuizActivity : AppCompatActivity() {

    // Declare all Classes
    private lateinit var binding: ActivityQuizBinding
    private var questions = ArrayList<Question>()
    private var index = 0
    private lateinit var question: Question
    private lateinit var dialog: ProgressDialog
    var database: FirebaseFirestore? = null
    private var correctAnswers = 0
    lateinit var timer: CountDownTimer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set viewBinding in this Activity
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // initialize here
        questions = ArrayList()
        database = FirebaseFirestore.getInstance()


        // Progress dialog show
        dialog = ProgressDialog(this)
        dialog.setMessage("Wait for Questions...")

        dialog.setCancelable(true)

        // Random Class
        val random = Random()
        val rand = random.nextInt(10)

        // get the ID from Adapter class with using getStringExtra
        val catId = intent.getStringExtra("catID")


        if (catId != null) {

            // Proceed only if catId is not null

            database!!.collection("categories")
                .document(catId)
                .collection("questions")
                .whereGreaterThanOrEqualTo("index", rand)
                .orderBy("index")
                .limit(5).get()
                .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                    dialog.dismiss()
                    if (queryDocumentSnapshots.documents.size < 5) {
                        dialog.show()
                        database!!.collection("categories")
                            .document(catId)
                            .collection("questions")
                            .whereLessThanOrEqualTo("index", rand)
                            .orderBy("index")
                            .limit(5).get()
                            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                                for (snapshot in queryDocumentSnapshots) {
                                    val question = snapshot.toObject(Question::class.java)
                                    questions.add(question)
                                }
                                setNextQuestion()
                            })
                    } else {
                        for (snapshot in queryDocumentSnapshots) {
                            val question = snapshot.toObject(Question::class.java)
                            questions.add(question)
                        }
                        setNextQuestion()
                    }
                })
            resetTimer()
        }


        // Similar blocks for other categories...
        // get the ID from Adapter class with using getStringExtra
        val setId = intent.getStringExtra("setID")


        if (setId != null) {
            // Proceed only if setId is not null


            database!!.collection("allquizCategories")
                .document(setId)
                .collection("questions")
                .whereGreaterThanOrEqualTo("index", rand)
                .orderBy("index")
                .limit(5).get()
                .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                    dialog.dismiss()
                    if (queryDocumentSnapshots.documents.size < 5) {
                        dialog.show()
                        database!!.collection("allquizCategories")
                            .document(setId)
                            .collection("questions")
                            .whereLessThanOrEqualTo("index", rand)
                            .orderBy("index")
                            .limit(5).get()
                            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                                for (snapshot in queryDocumentSnapshots) {
                                    val question = snapshot.toObject(Question::class.java)
                                    questions.add(question)
                                }
                                setNextQuestion()
                            })
                    } else {
                        for (snapshot in queryDocumentSnapshots) {
                            val question = snapshot.toObject(Question::class.java)
                            questions.add(question)
                        }
                        setNextQuestion()
                    }
                })

            resetTimer()
        }
    }



    // CountDownTimer code
    private fun resetTimer() {
        timer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.timer.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                showAnswer()
                timer.cancel()
            }


        }
    }

    // Show Answer Background set in editTet
    private fun showAnswer() {
        when {
            question.answer == binding.option1.text.toString() -> binding.option1.setBackgroundResource(
                R.drawable.option_right
            )
            question.answer == binding.option2.text.toString() -> binding.option2.setBackgroundResource(
                R.drawable.option_right
            )
            question.answer == binding.option3.text.toString() -> binding.option3.setBackgroundResource(
                R.drawable.option_right
            )
            question.answer == binding.option4.text.toString() -> binding.option4.setBackgroundResource(
                R.drawable.option_right
            )
        }
    }

    // setNextQuestion code
    private fun setNextQuestion() {
        if (timer != null) timer.cancel()

        timer.start()
        // get the question from firestore and set in
        if (index < questions.size) {
            binding.questionCounter.text = String.format("%d/%d", (index + 1), questions.size)
            question = questions[index]
            binding.question.text = question.question
            binding.option1.text = question.option1
            binding.option2.text = question.option2
            binding.option3.text = question.option3
            binding.option4.text = question.option4
        } else {
          timer.cancel()
        }
    }


    private fun checkAnswer(textView: TextView) {
        val selectedAnswer = textView.text.toString()
        if (selectedAnswer == question.answer) {
            correctAnswers++
            textView.setBackgroundResource(R.drawable.option_right)
        } else {
            showAnswer()
            textView.setBackgroundResource(R.drawable.option_wrong)
        }
    }

    private fun reset() {
        binding.option1.setBackgroundResource(R.drawable.option_unselected)
        binding.option2.setBackgroundResource(R.drawable.option_unselected)
        binding.option3.setBackgroundResource(R.drawable.option_unselected)
        binding.option4.setBackgroundResource(R.drawable.option_unselected)
    }


    fun onClick(view: View) {
        when (view.id) {
            R.id.option_1, R.id.option_2, R.id.option_3, R.id.option_4 -> {
                if (timer != null) timer.cancel()
                val selected = view as TextView
                checkAnswer(selected)
            }
            R.id.nextBtn -> {
                reset()
                if (index < questions.size) {
                    index++
                    setNextQuestion()
                } else {
                    val intent = Intent(this@QuizActivity, resulActivity::class.java)
                    intent.putExtra("correct", correctAnswers)
                    intent.putExtra("total", questions.size)
                    startActivity(intent)
                    finish()
                }
            }
        }

        }





    }











