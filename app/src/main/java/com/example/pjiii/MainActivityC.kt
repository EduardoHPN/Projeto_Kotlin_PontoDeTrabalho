package com.example.pjiii

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pjiii.databinding.ActivityMainCBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class MainActivityC : AppCompatActivity(),View.OnClickListener {

    private lateinit var binding: ActivityMainCBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var referece: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainCBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        referece = Firebase.database.reference

        binding.jaTem.setOnClickListener(this)
        binding.button2.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.ja_tem) {
            Back()
        } else if (view.id == R.id.button2) {
            ConferirCadastro()
        }
    }

    private fun Back() {
        startActivity(Intent(this, MainActivityA::class.java))
    }

    private fun ConferirCadastro() {
        val user = binding.NovoUser.text.toString().trim()
        val senha = binding.NovaSenha.text.toString().trim()

        if (user.isNotEmpty()) {

            if (senha.isNotEmpty()) {
                RegisterUser(user,senha)
            } else {
                Toast.makeText(this, "Senha Invalida", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this, "Usuario invalido", Toast.LENGTH_SHORT).show()
        }

    }
    fun RegisterUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Back()
                    onDestroy()
                } else {
                    Toast.makeText(this, task.exception?.message , Toast.LENGTH_SHORT).show()
                }
            }
    }


        fun onCancelled(error: DatabaseError) {
            Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
        }
    }

