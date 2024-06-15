package com.example.kostify.ui.loginRegisterPage

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.kostify.databinding.ActivityRegisterBinding
import com.example.kostify.ui.HomeScreenActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }

        binding.tvMasukRegis.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }

        binding.btnDaftarRegis.setOnClickListener {
            val name = binding.edtNamaRegis.text.toString()
            val email = binding.edtEmailRegis.text.toString()
            val pass = binding.edtKatasandiRegis.text.toString()
            val confirmPass = binding.edtKonfirmsandiRegis.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty() && name.isNotEmpty()) {
                if (pass == confirmPass) {
                    auth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(this, "Daftar berhasil", Toast.LENGTH_SHORT).show()
                                startActivity(
                                    Intent(
                                        this@RegisterActivity,
                                        LoginActivity::class.java
                                    )
                                )
                                finish()
                                /*val user: FirebaseUser? = auth.currentUser
                                updateUI(user)*/
                            } else {
                                Toast.makeText(
                                    this,
                                    "Kata sandi atau email salah",
                                    Toast.LENGTH_SHORT
                                ).show()
                                /*updateUI(null)*/
                            }
                        }
                } else {
                    Toast.makeText(this, "Konfirmasi sandi tidak sama", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Semua harus di isi", Toast.LENGTH_SHORT).show()
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email tidak valid", Toast.LENGTH_SHORT).show()
            }

            if (pass.length < 6) {
                Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkIfUserLogged() {
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            startActivity(
                Intent(
                    this,
                    HomeScreenActivity::class.java
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()
        checkIfUserLogged()
    }

/*    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this@RegisterActivity, HomeScreenActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }*/
}