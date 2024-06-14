package com.example.kostify.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.kostify.R
import com.example.kostify.data.api.response.DetailKostResponse
import com.example.kostify.databinding.ActivityDetailKos2Binding
import com.example.kostify.viewmodel.DetailKosViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DetailKosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailKos2Binding
    private val viewModel by viewModels<DetailKosViewModel>()
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
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.detailKos.observe(this) {
            setKost(it)
        }

        binding.ivBackBtn.setOnClickListener { finish() }
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
    }
}