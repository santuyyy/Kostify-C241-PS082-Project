package com.example.kostify.ui.profil

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kostify.R
import com.example.kostify.databinding.ActivityDetailKos2Binding
import com.example.kostify.databinding.ActivityProfileBinding
import com.example.kostify.databinding.ActivitySearchKosBinding
import com.example.kostify.ui.FavoriteActivity
import com.example.kostify.ui.HomeScreenActivity
import com.example.kostify.ui.SearchKosActivity
import com.example.kostify.ui.loginRegisterPage.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.nav_profile
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeScreenActivity::class.java))
                    true
                }
                R.id.nav_favorite -> {
                    startActivity(Intent(this, FavoriteActivity::class.java))
                    true
                }
                R.id.nav_search -> {
                    startActivity(Intent(this, SearchKosActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    true
                }
                else -> false
            }
        }

        binding.btnKeluar.setOnClickListener{
            auth.signOut()
            startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
        }
    }

    private fun checkIfUserLogged() {
        val firebaseUser = auth.currentUser
        if(firebaseUser != null){
            binding.tvNamaProfile.text = firebaseUser.email
        }else{
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