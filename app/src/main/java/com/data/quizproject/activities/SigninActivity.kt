package com.data.quizproject.activities

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.data.quizproject.R
import com.data.quizproject.databinding.ActivitySigninBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SigninActivity : AppCompatActivity() {

    // Declare viewBinding in this activity
    private lateinit var binding: ActivitySigninBinding
    // Declare Firebase auth in this Activity
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set viewBinding in this Activity
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        // Initialize FireAuth
        auth = Firebase.auth
        // Initialize Firebase Firestore
        val db = Firebase.firestore



        // Alert Dialog code
        val dialog = AlertDialog.Builder(this)

        dialog.setTitle("Attention!!")
        dialog.setMessage(R.string.dialog_message)
        dialog.setIcon(R.drawable.baseline_warning_24)

        dialog.setPositiveButton("OK") {

                dialogInterface,which ->
        }



        // AlertDialog create and show from here
        val alertDialog:AlertDialog = dialog.create()
        alertDialog.setCancelable(false)
        alertDialog.show()



        binding.btnSign.setOnClickListener {

            // All Edit Text convert in String
            val username = binding.editUser.text.toString().trim()
            val email = binding.editEmail.text.toString().trim()
            val password = binding.editPass.text.toString().trim()
            val phoneNum = binding.editPhone.text.toString().trim()

            // Validation in EditText

            if (TextUtils.isEmpty(username)) {

                binding.editUser.setError("Username is required...")
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(email)){

                binding.editEmail.setError("Email is required...")
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {

                binding.editPass.setError("Password is required...")
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(phoneNum)) {

                binding.editPhone.setError("Mobile Number is required...")
                return@setOnClickListener
            }

            if (username.length > 20) {

                binding.editUser.setError("Username must be in 20 Characters...")
                return@setOnClickListener
            }

            if (password.length < 6) {

                binding.editPass.setError("Password must be in 6 Characters...")
                return@setOnClickListener
            }

            if (phoneNum.length > 10) {

                binding.editPhone.setError("Invalid phone number")
                return@setOnClickListener

            } else if (phoneNum.length < 10) {

                binding.editPhone.setError("Invalid phone number")
                return@setOnClickListener
            }

            // User create our account with email , password , username and phone number
            auth.createUserWithEmailAndPassword(binding.editEmail.text.toString() , binding.editPass.text.toString()).addOnCompleteListener(this) { task ->

                // if user is successfully create our account
                if (task.isSuccessful){

                    val user = hashMapOf(
                        "username" to binding.editUser.text.toString(),
                        "e_mail" to binding.editEmail.text.toString(),
                        "password" to binding.editPass.text.toString(),
                        "phone number" to binding.editPhone.text.toString()
                    )

                    // user ID generate in firebase
                    val userID = auth.currentUser?.uid

                    // Create one collection of user in firestore and store those data that user entered
                    db.collection("users")
                        .document(userID!!)
                        .set(user)
                        .addOnSuccessListener {

                            Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                        }

                    startActivity(Intent(this , MainActivity::class.java))
                    finish()

                } else{

                    Toast.makeText(this, "" + task.exception?.localizedMessage, Toast.LENGTH_SHORT).show();
                }
            }
        }

        // Sign In button
        binding.SiginBtn.setOnClickListener {

            startActivity(Intent(this , LoginActivity::class.java))
            finish()
        }
    }
}