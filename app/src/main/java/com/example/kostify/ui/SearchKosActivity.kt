package com.example.kostify.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kostify.R
import com.example.kostify.adapter.KosAdapter
import com.example.kostify.data.api.response.ItemsItem
import com.example.kostify.databinding.ActivitySearchKosBinding
import com.example.kostify.ui.loginRegisterPage.LoginActivity
import com.example.kostify.ui.profil.ProfileActivity
import com.example.kostify.viewmodel.SearchKosViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SearchKosActivity : AppCompatActivity()/*, KosAdapter.ListViewHolder.UserItemClickListener*/{

    private lateinit var binding: ActivitySearchKosBinding
    private lateinit var adapter: KosAdapter
    private val viewModel by viewModels<SearchKosViewModel>()
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchKosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        auth = Firebase.auth

        adapter = KosAdapter { kos ->
            val intent = Intent(this, DetailKosActivity::class.java)
            intent.putExtra(DetailKosActivity.EXTRA_KOS_ID, kos.id)
            startActivity(intent)
        }

        binding.rvListKos.layoutManager = LinearLayoutManager(this)
        binding.rvListKos.adapter = adapter

        viewModel.listKos.observe(this) {listKos ->
            getDataKos(listKos)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    viewModel.findKostSearch(searchView.text.toString())
                    searchView.hide()
                        Toast.makeText(this@SearchKosActivity,searchView.text, Toast.LENGTH_SHORT).show()
                    false
                }
        }

        binding.searchBar.apply {
            menuInflater.inflate(R.menu.searchbar_menu, menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId){
                    R.id.btn_filter -> {
                        val intent = Intent(this@SearchKosActivity, FilterKostActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
        }

        binding.bottomNavigationView.selectedItemId = R.id.nav_search
        binding.bottomNavigationView.setOnItemSelectedListener {
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

    private fun getDataKos(item: List<ItemsItem?>?) {
        adapter.submitList(item)

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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onStart() {
        super.onStart()
        checkIfUserLogged()
    }
}