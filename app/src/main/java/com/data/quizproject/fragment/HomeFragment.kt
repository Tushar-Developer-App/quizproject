package com.data.quizproject.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.data.quizapp.model.categoryModel
import com.data.quizproject.adapter.myAdapter
import com.data.quizproject.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase



class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root


        val db = Firebase.firestore
        auth = Firebase.auth

        // get current user ID from firebase
        val userID = auth.currentUser?.uid

        auth.currentUser

        // Username of MainActivity code......
        val documentReference: DocumentReference = db.collection("users").document(userID!!)
        documentReference.addSnapshotListener() { documentSnapshot, e ->

            if (documentSnapshot!!.exists()) {

                // set the name in home fragment
                binding.useername.setText(documentSnapshot!!.getString("username"))

            } else {

                Log.d("tag", "onEvent: Document do not exists")
            }
        }


        val categories: ArrayList<categoryModel> = ArrayList<categoryModel>()

        val adapter = myAdapter(context, categories)


        // Make a collection of "categories" in firebase and set the data from firestore in recycler view
        db.collection("categories")
            .addSnapshotListener(EventListener<QuerySnapshot?> { value, error ->
                categories.clear()
                for (snapshot in value!!.documents) {
                    var model: categoryModel = snapshot.toObject(categoryModel::class.java)!!
                    model.categoryId = snapshot.id
                    categories.add(model)
                }
                adapter.notifyDataSetChanged()
            })


        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.adapter = adapter



        return view
    }

    companion object {
    }
}