package com.example.pjiii

import Validações.Geolocalização
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.pjiii.databinding.ActivityMainBpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.jakewharton.threetenabp.AndroidThreeTen
import data.Dados1
import data.Hrario
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import java.time.Clock
import java.time.ZoneId
import java.util.Calendar


class MainActivityBP : AppCompatActivity(), View.OnClickListener {

    private lateinit var bindingbp:  ActivityMainBpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var referece: DatabaseReference
    var controleTrueFalse = false
   //lateinit var diaAtual = calendar.get(Calendar.DAY_OF_WEEK)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindingbp = ActivityMainBpBinding.inflate(layoutInflater)
        setContentView(bindingbp.root)

        auth = Firebase.auth
        referece = Firebase.database.reference

        EntradaOuSaida2()

        bindingbp.btbPonto.setOnClickListener(this)
        bindingbp.BtnRelatorio.setOnClickListener(this)

    }


    fun GetUser(){
        referece
            .child("Dias")
            .child(auth.currentUser?.uid?:"")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var dadosDI = mutableListOf<Dados1>()
                    var contador = -1
                    var controle = false
                    val diaHoje = diaDeHoje()
                    for (ds in snapshot.children) {
                        val dia = ds.getValue(Dados1::class.java) as Dados1
                        dadosDI.add(dia)
                        contador+=1

                    }
                    while(contador!=-1){
                        if(diaHoje == dadosDI[contador].dia.toString()){
                            controle = true
                        }
                        contador = contador - 1
                    }
                    if(controle) getHora()
                    else{
                        Toast.makeText(applicationContext, "Não pode bater ponto", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
                }
            })
    }

    override fun onClick(v: View) {
        if (v.id == R.id.btbPonto){
            val geolocalização = Geolocalização(this)
            geolocalização.fecthLocation(this){isInPucCampinas ->
                if(isInPucCampinas){
                    BaterPonto()
                }
                else Toast.makeText(this, "Não está em PUC-CAMP", Toast.LENGTH_SHORT).show()
            }
        }
        if(v.id == R.id.BtnRelatorio){
            startActivity(Intent(this, MainActivityR::class.java))
        }
    }

    fun teste(D:String){
        bindingbp.textView.text = D
    }
    fun diaDeHoje(): String {
        val calendario = Calendar.getInstance()
        val diaAtual = calendario.get(Calendar.DAY_OF_WEEK)
        if (diaAtual == 1) return "Domingo"
        if (diaAtual == 2) return "Segunda-Feira"
        if (diaAtual == 3) return "Terça-Feira"
        if (diaAtual == 4) return "Quarta-Feira"
        if (diaAtual == 5) return "Quinta-Feira"
        if (diaAtual == 6) return "Sexta-Feira"
        if (diaAtual == 7) return "Sábado"
        return TODO("Provide the return value")
    }
    private fun BaterPonto(){
        GetUser()
    }



    private fun getHora(){
        referece
            .child("Dias")
            .child(auth.currentUser?.uid?:"")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var dadosDI = mutableListOf<Dados1>()
                    var contador = -1
                    var calendario = Calendar.getInstance()
                    var hrAtual = calendario.get(Calendar.HOUR_OF_DAY)
                    var controle = false
                    for (ds in snapshot.children) {
                        val dia = ds.getValue(Dados1::class.java) as Dados1
                        dadosDI.add(dia)
                        contador+=1

                    }
                    while(contador!=-1){
                        if(hrAtual >= dadosDI[contador].hre && hrAtual <= dadosDI[contador].hrs){
                            controle = true
                        }
                        contador = contador - 1
                    }
                    if(controle) EntradaOuSaida()
                    else{
                        Toast.makeText(applicationContext, "Não pode bater ponto", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
                }
            })

    }

    fun SalveDate(){
        val idDad = referece.database.reference.push().key ?: ""
        var calendario = Calendar.getInstance()
        var Ponto = Hrario()
        val diaHoje = diaDeHoje()
        Ponto.OutOrIn = "Entrada"
        Ponto.hr = calendario.get(Calendar.HOUR_OF_DAY)
        Ponto.Mins = calendario.get(Calendar.MINUTE)
        Ponto.dia = diaHoje
        referece
            .child("Ponto")
            .child(auth.currentUser?.uid?:"")
            .child(idDad)
            .setValue(Ponto)
        Toast.makeText(this, "Sucesso ao registrar", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this,MainActivityBP::class.java))
    }
    fun EntradaOuSaida() {
        var controle =0
        referece
            .child("Ponto")
            .child(auth.currentUser?.uid?:"")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var horario = mutableListOf<Hrario>()
                    for (ds in snapshot.children) {
                        val hora = ds.getValue(Hrario::class.java) as Hrario
                        horario.add(hora)
                    }
                    if (horario.isEmpty() && controle==0) {
                        SalveDate()
                        controle+=1
                    }
                    if(!(horario.isEmpty())&& controle ==0) {
                        Verificação()
                        controle += 1
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
                }
            })

    }

    fun Verificação(){
        var contador = -1
        var verificador = 0
        referece
            .child("Ponto")
            .child(auth.currentUser?.uid?:"")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var horario = mutableListOf<Hrario>()
                    for (ds in snapshot.children) {
                        val hora = ds.getValue(Hrario::class.java) as Hrario
                        horario.add(hora)
                        contador+=1
                    }
                    if(horario[contador].OutOrIn=="Saída" && verificador==0) {
                        SalveDate()
                        verificador+=1
                    }
                    if(horario[contador].OutOrIn=="Entrada" && verificador==0){
                        SalveDateSaida()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                }

            })
    }
    fun SalveDateSaida(){
        val idDad = referece.database.reference.push().key ?: ""
        var calendario = Calendar.getInstance()
        var Ponto = Hrario()
        val diaHoje = diaDeHoje()
        Ponto.OutOrIn = "Saída"
        Ponto.hr = calendario.get(Calendar.HOUR_OF_DAY)
        Ponto.Mins = calendario.get(Calendar.MINUTE)
        Ponto.dia = diaHoje
        referece
            .child("Ponto")
            .child(auth.currentUser?.uid?:"")
            .child(idDad)
            .setValue(Ponto)
        Toast.makeText(this, "Sucesso ao registrar", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this,MainActivityBP::class.java))
    }











    fun EntradaOuSaida2() {
        var controle =0
        referece
            .child("Ponto")
            .child(auth.currentUser?.uid?:"")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var horario = mutableListOf<Hrario>()
                    for (ds in snapshot.children) {
                        val hora = ds.getValue(Hrario::class.java) as Hrario
                        horario.add(hora)
                    }
                    if (horario.isEmpty() && controle==0) {
                        bindingbp.textView.text = "Seu próximo ponto é uma entrada!"
                        controle+=1
                    }
                    if(!(horario.isEmpty())&& controle ==0) {
                        Verificação2()
                        controle += 1
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
                }
            })

    }

    fun Verificação2(){
        var contador = -1
        var verificador = 0
        referece
            .child("Ponto")
            .child(auth.currentUser?.uid?:"")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var horario = mutableListOf<Hrario>()
                    for (ds in snapshot.children) {
                        val hora = ds.getValue(Hrario::class.java) as Hrario
                        horario.add(hora)
                        contador+=1
                    }
                    if(horario[contador].OutOrIn=="Saída" && verificador==0) {
                        bindingbp.textView.text = "Seu próximo ponto é uma entrada!"
                        verificador+=1
                    }
                    if(horario[contador].OutOrIn=="Entrada" && verificador==0){
                        bindingbp.textView.text = "Seu próximo ponto é uma saída!"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                }

            })
    }
}