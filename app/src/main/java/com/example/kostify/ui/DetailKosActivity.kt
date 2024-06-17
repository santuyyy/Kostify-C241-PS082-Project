package com.example.kostify.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.kostify.R
import com.example.kostify.data.api.response.DetailKostResponse
import com.example.kostify.databinding.ActivityDetailKos2Binding
import com.example.kostify.ui.loginRegisterPage.LoginActivity
import com.example.kostify.viewmodel.DetailKosViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DetailKosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailKos2Binding
    private val viewModel by viewModels<DetailKosViewModel>()
 /*   private val viewModel by viewModels<DetailKosViewModel> {
        DetailKosViewModelFactory.getInstance(this)
    }*/
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailKos2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth


        val kostId = intent.getIntExtra(EXTRA_KOS_ID, -1)
        if (kostId != -1) {
            viewModel.getDetailKos(kostId)
        } else {
            Toast.makeText(this, "Invalid ID", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.ivBackBtn.setOnClickListener { finish() }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.detailKos.observe(this) {
          setKost(it)

            /*setKost(kost)
            kost?.id?.let {
                viewModel.checkBookmarkStatus(it.toString())
            }*/
        }

   /*     viewModel.bookmarkState.observe(this) { isBookmarked ->
            setFavoriteButtonIcon(isBookmarked ?: false)
        }

        viewModel.errorState.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }*/

      /*  binding.fabFav.setOnClickListener {
            val kost = viewModel.detailKos.value
            if (kost != null) {
                if (viewModel.bookmarkState.value == true) {
                    val bookmarkId = kost.bookmarks?.firstOrNull { it.kostId == kost.id.toString() }?.id // Ubah kost.id menjadi String
                    bookmarkId?.let { viewModel.removeBookmark(it) }
                } else {
                    viewModel.addBookmark(kost.id ?: 0) // Pastikan fungsi addBookmark menerima tipe data yang sesuai dengan kost.id
                }
            }
        }*/
    }

    private fun setFavoriteButtonIcon(isFavorite: Boolean) {
        if (isFavorite) {
            binding.fabFav.setImageResource(R.drawable.ic_heart_redfill)
        } else {
            binding.fabFav.setImageResource(R.drawable.ic_heart_redoutlined)
        }
    }

    private fun setKost(kost: DetailKostResponse) {
        if (kost != null) {
            binding.apply {
                tvTipeDetail.text = kost.gender ?: ""
                tvNamaDetail.text = kost.namaKost ?: ""
                tvAlamatDetail.text = kost.alamat ?: ""
                tvLuasKamarDetail.text = kost.luasKamar ?: ""
                tvDeskDetail.text = kost.deskripsi ?: ""
                tvHargaDetail.text = getString(R.string.harga_kosItem, kost.harga)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_KOS_ID = "extra_kos_id"
        private const val TAG = "DetailKosActivity"
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
}