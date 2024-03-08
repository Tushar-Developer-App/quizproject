package com.data.quizproject.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.data.quizproject.databinding.ActivityMainBinding
import com.data.quizproject.fragment.HomeFragment
import com.data.quizproject.fragment.ProfileFragment
import com.data.quizproject.fragment.QuizFragment
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : AppCompatActivity() {

    // Declare viewBinding in this activity
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set viewBinding in this activity
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        replaceFragment(HomeFragment())

        // Bottom Navigation code
        binding.bottomBar.setOnTabSelectListener(object :AnimatedBottomBar.OnTabSelectListener{
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {

                // When user clicked in bottom navigation
                when(newIndex) {

                    0 -> replaceFragment(HomeFragment())

                    1 -> replaceFragment(QuizFragment())

                    2-> replaceFragment(ProfileFragment())

                    else->{
                    }
                }
            }
        })
    }

    // replaceFragment too many screen
    fun replaceFragment(fragment: Fragment){

        supportFragmentManager.beginTransaction().replace(binding.content.id, fragment).commit()
    }
}
