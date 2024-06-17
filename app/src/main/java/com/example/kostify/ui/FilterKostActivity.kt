package com.example.kostify.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.kostify.R
import com.example.kostify.databinding.ActivityFilterKostBinding
import com.example.kostify.ui.loginRegisterPage.LoginActivity
import com.example.kostify.viewmodel.SearchKosViewModel
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FilterKostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilterKostBinding
    private val viewModel by viewModels<SearchKosViewModel>()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterKostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }
        val lokasiItems = resources.getStringArray(R.array.lokasi_array)

        val jenisKosItems = resources.getStringArray(R.array.jenis_kos_array)

        // Menggunakan setSimpleItems untuk Lokasi
        (binding.dropdownLokasiKost.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(
            lokasiItems
        )

        // Menggunakan setSimpleItems untuk Jenis Kos
        (binding.dropdownJenisKos.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(
            jenisKosItems
        )

        showLoading(false)
        viewModel.listRekomendasiKos.observe(this) { listKos ->
            // Buat Intent untuk pindah ke SearchActivity
            val intent = Intent(this, SearchKosActivity::class.java)
            intent.putExtra("listKos", ArrayList(listKos))
            startActivity(intent)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.btnGetRekomendasi.setOnClickListener {
            val harga = binding.edtHargaKost.text.toString()
            val gender =
                (binding.dropdownJenisKos.editText as? AutoCompleteTextView)?.text.toString()
            val lokasi =
                (binding.dropdownLokasiKost.editText as? AutoCompleteTextView)?.text.toString()
            val fasilitas = mutableMapOf<String, Int>()
            fasilitas["AC"] = if (binding.fasilitasAc.isChecked) 1 else 0
            fasilitas["Kasur"] = if (binding.fasilitasKasur.isChecked) 1 else 0
            fasilitas["Lemari"] = if (binding.fasilitasLemari.isChecked) 1 else 0
            fasilitas["Wifi"] = if (binding.fasilitasWifi.isChecked) 1 else 0
            fasilitas["Wc_duduk"] = if (binding.fasilitasWcDuduk.isChecked) 1 else 0
            fasilitas["Kamar_mandi_dalam"] =
                if (binding.fasilitasKamarmandiDalam.isChecked) 1 else 0
            fasilitas["Listrik"] = 1 // Assuming listrik is always 1 based on the URL

            showLoading(false)
            viewModel.getRecomendasiKost(harga, gender, lokasi, fasilitas)

        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar5.visibility = if (isLoading) View.VISIBLE else View.GONE
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