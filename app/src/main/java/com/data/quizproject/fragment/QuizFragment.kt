package com.data.quizproject.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.data.quizapp.model.categoryModel
import com.data.quizproject.R
import com.data.quizproject.adapter.allQuizAdapter
import com.data.quizproject.adapter.myAdapter
import com.data.quizproject.databinding.FragmentHomeBinding
import com.data.quizproject.databinding.FragmentQuizBinding
import com.data.quizproject.model.allquizModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class QuizFragment : Fragment() {

    private lateinit var binding:FragmentQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuizBinding.inflate(inflater,container , false)
        val view = binding.root

        val db = Firebase.firestore

        val allcategories: ArrayList<allquizModel> = ArrayList<allquizModel>()

        val adapter = allQuizAdapter(context ,allcategories)



        db.collection("allquizCategories")
            .addSnapshotListener(EventListener<QuerySnapshot?> { value, error ->
                allcategories.clear()
                for (snapshot in value!!.documents) {
                    var model: allquizModel = snapshot.toObject(allquizModel::class.java)!!
                    model.allquizID = snapshot.id
                    allcategories.add(model)
                }
                adapter.notifyDataSetChanged()
            })

        binding.recyclerView.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerView.adapter = adapter



        return view
    }

    companion object {

    }
}