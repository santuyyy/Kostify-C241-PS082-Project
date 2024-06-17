package com.example.kostify.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kostify.R
import com.example.kostify.adapter.RandomKosAdapter
import com.example.kostify.data.api.response.RandomkostitemItem
import com.example.kostify.databinding.ActivityHomeScreenBinding
import com.example.kostify.ui.loginRegisterPage.LoginActivity
import com.example.kostify.ui.loginRegisterPage.RegisterActivity
import com.example.kostify.ui.profil.ProfileActivity
import com.example.kostify.viewmodel.HomeScreenViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var adapter: RandomKosAdapter
    private val viewModel by viewModels<HomeScreenViewModel>()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        adapter = RandomKosAdapter { kos ->
            val intent = Intent(this, DetailKosActivity::class.java)
            intent.putExtra(DetailKosActivity.EXTRA_KOS_ID, kos.id)
            startActivity(intent)
        }

        binding.rvListHome.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvListHome.adapter = adapter

        viewModel.listRandomKos.observe(this) { listRandomKos ->
            getDataRandomKos(listRandomKos)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.ivArrowHomeToList.setOnClickListener {
            startActivity(Intent(this, SearchKosActivity::class.java))
        }

        binding.ivArrowHomeToList.setOnClickListener {
            startActivity(Intent(this, SearchKosActivity::class.java))
            finish()
        }

        binding.bottomNavigationView.selectedItemId = R.id.nav_home
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
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
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }

                else -> false
            }
        }
    }

    private fun getDataRandomKos(item: List<RandomkostitemItem?>?) {
        adapter.submitList(item)

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar4.visibility = if (isLoading) View.VISIBLE else View.GONE
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