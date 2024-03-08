package com.data.quizproject.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.data.quizproject.activities.LoginActivity
import com.data.quizproject.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        val db = Firebase.firestore
        auth = Firebase.auth

        val userID = auth.currentUser?.uid

        auth.currentUser

        // make a collection of "users" in firebase and set the data in this profile  , username , e_mail , password , phone number
        val documentReference: DocumentReference = db.collection("users").document(userID!!)
        documentReference.addSnapshotListener() { documentSnapshot, e ->

            if (documentSnapshot!!.exists()) {

                binding.userPro.setText(documentSnapshot!!.getString("username"))
                binding.emailpro.setText(documentSnapshot!!.getString("e_mail"))
                binding.passPro.setText(documentSnapshot!!.getString("password"))
                binding.Phonepro.setText(documentSnapshot!!.getString("phone number"))

            } else {

                Log.d("tag", "onEvent: Document do not exists")
            }
        }


        // User sign-out in this button
        binding.btnLogout.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            auth.signOut()
            context?.startActivity(intent)
        }

        return view
    }


    companion object {
    }
}