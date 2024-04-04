package com.example.pjiii

import Validações.Geolocalização
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.pjiii.databinding.ActivityMainABinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import data.Dados1

class MainActivityA : AppCompatActivity(), View.OnClickListener{

    private lateinit var binding: ActivityMainABinding
    private lateinit var auth: FirebaseAuth
    private lateinit var referece: DatabaseReference
    var controler = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding =  ActivityMainABinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        referece = Firebase.database.reference


        binding.criarConta.setOnClickListener(this)
        binding.button.setOnClickListener(this)

       // var dadostrans = mutableListOf<Dados1>()
        //
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onClick(view: View) {
        if (view.id == R.id.criar_conta){
           CriarConta()
        }
        else if(view.id ==R.id.button){
           validadados()
        }
    }

    private fun validadados(){
        val user = binding.User.text.toString().trim()
        val senha = binding.Senha.text.toString().trim()

        if (user.isNotEmpty()) {

            if (senha.isNotEmpty()) {
                inserder(user,senha)
            } else {
                Toast.makeText(this, "Senha Invalida", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this, "Usuario invalido", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inserder(email: String,password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                  //  verificação da agenda se ja montada
                    verificaAgenda()
                    //startActivity(Intent(this, MainActivityP::class.java))
                } else {
                    Toast.makeText(this, task.exception?.message , Toast.LENGTH_SHORT).show()
                }
            }
    }
   private fun CriarConta(){
       startActivity(Intent(this, MainActivityC::class.java))
   }

    private fun verificaAgenda() {
        var c = 0
        referece
            .child("Dias")
            .child(auth.currentUser?.uid?:"")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var dadosDI = mutableListOf<Dados1>()
                    for (ds in snapshot.children) {
                        val dia = ds.getValue(Dados1::class.java) as Dados1
                        dadosDI.add(dia)
                    }
                    if (!(dadosDI.isEmpty())&& c==0) {
                        c=1
                        proximaAct()
                        onDestroy()
                    } else {
                        c=1
                        proximaActAge()
                        onDestroy()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
                }
            })


    }

    private fun proximaAct() {
        startActivity(Intent(this, MainActivityBP::class.java))


    }

    private fun proximaActAge() {
        startActivity(Intent(this, MainActivityP::class.java))


    }
    }