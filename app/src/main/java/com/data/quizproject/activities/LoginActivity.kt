package com.data.quizproject.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.data.quizproject.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {

    // Declare viewBinding in this activity
    private lateinit var binding: ActivityLoginBinding

    // Declare Firebase auth in this Activity
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set viewBinding in this Activity
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize firebase auth
        auth = Firebase.auth



        // if user Already Sign in go Main Activity
        if(auth.currentUser!=null){
            startActivity(Intent(this , MainActivity::class.java))
            finish()
        }




        binding.loginBtn.setOnClickListener {

            // EditText convert in String
            val email = binding.logEmail.text.toString().trim()
            val password = binding.logPass.text.toString().trim()

            // Validation in EditText
            if (TextUtils.isEmpty(email)){

                binding.logEmail.setError("Email is required...")
                return@setOnClickListener

            }


            if (TextUtils.isEmpty(password)) {

                binding.logPass.setError("Password is required...")
                return@setOnClickListener

            }


            if (password.length < 6) {

                binding.logPass.setError("Password must be in 6 Characters...")
                return@setOnClickListener

            }

            // User Login with Email and Password
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->

                try{

                    // if user login successful
                    if (task.isSuccessful){
                        Toast.makeText(this, "Logged in Successfully", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this , MainActivity::class.java))
                    } else {

                        Toast.makeText(this, "Error ! " + task.getException()!!.message, Toast.LENGTH_SHORT).show();
                    }

                } catch (e:Exception){

                    Toast.makeText(this , "Something went wrong!!" , Toast.LENGTH_SHORT).show()

                }
            }
        }

        binding.createaccBtn.setOnClickListener {

            startActivity(Intent(this , SigninActivity::class.java))
            finish()
        }
    }
}