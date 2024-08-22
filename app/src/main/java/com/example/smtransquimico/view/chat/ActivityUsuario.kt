package com.example.smtransquimico.view.chat

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smtransquimico.R
import com.example.smtransquimico.data.FirebaseService
import com.example.smtransquimico.databinding.ActivityUsuarioChatBinding
import com.example.smtransquimico.model.Users
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

class ActivityUsuario : AppCompatActivity() {
    private var usersList = ArrayList<Users>()
    private lateinit var binding: ActivityUsuarioChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsuarioChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setandoBarraDeAcao()
        setandoPreferenciasFirebase()

        binding.listaUsuarioChat.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        abrirChatBot()
        abrirFotoPerfil()
        validandoPermissoes()
    }

    private fun setandoPreferenciasFirebase() {
        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            FirebaseService.token = it.token
        }
    }

    private fun setandoBarraDeAcao() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            title = "Chat"
        }
    }

    private fun validandoPermissoes() {
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
                123
            )
        }
    }

    private fun abrirFotoPerfil() = binding.imgUsuarioChat.setOnClickListener {
        startActivity(Intent(this@ActivityUsuario, PerfilUsuarioActivity::class.java))
    }

    private fun abrirChatBot() = binding.btnChatBot.setOnClickListener {
        startActivity(Intent(this, ChatBotActivity::class.java))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
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

                    val data = dataSnapshot.getValue(Users::class.java)

                    if (data?.userId != firebase.uid) {
                        usersList.add(data!!)
                    } else {
                        binding.nomePerfil.text = data.userName

                        if (!data.profileImage.isNullOrEmpty()) {
                            Glide.with(this@ActivityUsuario)
                                .load(data.profileImage)
                                .placeholder(R.drawable.imagem_perfil)
                                .into(binding.imgUsuarioChat)
                        }
                    }
                }

                binding.listaUsuarioChat.adapter =
                    ListaUsuarioChatAdapter(this@ActivityUsuario, usersList)
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
            123 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pegarUsuarioLista()
                } else {
                    Toast.makeText(this, "Permissão de armazenamento negada", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}
