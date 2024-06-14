package com.example.kostify.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kostify.R
import com.example.kostify.databinding.ActivityFavoriteBinding
import com.example.kostify.databinding.ActivityHomeScreenBinding
import com.example.kostify.ui.loginRegisterPage.LoginActivity
import com.example.kostify.ui.profil.ProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.nav_favorite
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeScreenActivity::class.java))
                    true
                }
                R.id.nav_favorite -> {
                    true
                }
                R.id.nav_search -> {
                    startActivity(Intent(this, SearchKosActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun checkIfUserLogged() {
        val firebaseUser = auth.currentUser
        if(firebaseUser == null){
            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()
        checkIfUserLogged()
    }
}