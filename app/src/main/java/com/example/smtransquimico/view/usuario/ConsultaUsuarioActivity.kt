package com.example.smtransquimico.view.usuario

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smtransquimico.R
import com.example.smtransquimico.model.Usuario
import com.example.smtransquimico.view.adapter.ConsultaUsuarioAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ConsultaUsuarioActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var listaConsultaUsuario = ArrayList<Usuario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulta_usuario)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Consultar Usuário"
        window.statusBarColor = Color.parseColor("#083087")

        inicializarRecyclerView()
        pegarListaUsuario()
    }

    private fun inicializarRecyclerView (){
        recyclerView = findViewById(R.id.recyclerListaUsuario)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

    }

    private fun pegarListaUsuario() {
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val usuario = dataSnapShot.getValue(Usuario::class.java)

                    if (!usuario!!.userId.equals(firebase.uid)) {
                        listaConsultaUsuario.add(usuario)
                    }
                }

                val usuarioAdapter = ConsultaUsuarioAdapter(this@ConsultaUsuarioActivity, listaConsultaUsuario)

                recyclerView.adapter = usuarioAdapter

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}