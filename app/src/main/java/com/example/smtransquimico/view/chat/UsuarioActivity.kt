package com.example.smtransquimico.view.chat

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smtransquimico.R
import com.example.smtransquimico.data.FirebaseService
import com.example.smtransquimico.model.Usuario
import com.example.smtransquimico.view.adapter.ListaUsuarioChatAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging

class UsuarioActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var usuarioList = ArrayList<Usuario>()
    private lateinit var btnPerfil: ImageView
    private lateinit var botaoChatBot: Button


    companion object {
        private const val REQUEST_STORAGE_PERMISSION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario_chat)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        inicializarComponentes()

        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            FirebaseService.token = it.token
        }

        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        botaoChatBot.setOnClickListener {
            val intent = Intent(this, ChatBotActivity::class.java)
            startActivity(intent)
        }

        btnPerfil.setOnClickListener {
            val intent = Intent(this@UsuarioActivity, PerfilUsuarioActivity::class.java)
            startActivity(intent)
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            pegarUsuarioLista()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_STORAGE_PERMISSION
            )
        }
    }

    private fun inicializarComponentes() {
        recyclerView = findViewById(R.id.listaUsuarioChat)
        btnPerfil = findViewById(R.id.imgUsuarioChat)
        botaoChatBot = findViewById(R.id.chatBot)

    }

    private fun pegarUsuarioLista() {
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

        val userid = firebase.uid
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/$userid")

        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot: DataSnapshot in snapshot.children) {
                    val usuario = dataSnapshot.getValue(Usuario::class.java)
                    if (usuario?.userId != firebase.uid) {
                        usuarioList.add(usuario!!)
                    }
                }

                val usuarioAdapter = ListaUsuarioChatAdapter(this@UsuarioActivity, usuarioList)
                recyclerView.adapter = usuarioAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permissão concedida, carregue a lista de usuários
                    pegarUsuarioLista()
                } else {
                    Toast.makeText(this, "Permissão de armazenamento negada", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}
