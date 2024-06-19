package com.example.kostify.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kostify.R
import com.example.kostify.adapter.BookmarkAdapter
import com.example.kostify.databinding.ActivityFavoriteBinding
import com.example.kostify.ui.loginRegisterPage.LoginActivity
import com.example.kostify.ui.profil.ProfileActivity
import com.example.kostify.viewmodel.FavoriteViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var auth: FirebaseAuth
    private val viewModel by viewModels<FavoriteViewModel>()
    private lateinit var bookmarkAdapter: BookmarkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        /*bookmarkAdapter = BookmarkAdapter()*/

        bookmarkAdapter = BookmarkAdapter { bookmark ->
            val intent = Intent(this, DetailKosActivity::class.java)
            intent.putExtra(DetailKosActivity.EXTRA_KOS_ID, bookmark.kostDetails.id)
            startActivity(intent)
        }
        binding.rvFavkos.adapter = bookmarkAdapter
        binding.rvFavkos.layoutManager = LinearLayoutManager(this)

        //  menggunakan listadapter    Amati perubahan pada LiveData bookmarks di ViewModel
           viewModel.bookmarks.observe(this) { bookmarks ->
               bookmarkAdapter.submitList(bookmarks) // Gunakan submitList untuk memperbarui Adapter
           }

//        menggunakan recycle view adapter
       /* viewModel.bookmarks.observe(this) { bookmarks ->
            bookmarkAdapter.setData(bookmarks) // Gunakan setData() untuk memperbarui adapter
        }*/

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading) // Fungsi untuk menampilkan/menyembunyikan ProgressBar
        }

        viewModel.error.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        binding.bottomNavigationView.selectedItemId = R.id.nav_favorite
        binding.bottomNavigationView.setOnItemSelectedListener {
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
        if (firebaseUser == null) {
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

    override fun onResume() {
        super.onResume()
        viewModel.refreshBookmarks() // Refresh daftar bookmark saat Activity kembali terlihat
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar3.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}