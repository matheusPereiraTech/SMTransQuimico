package com.example.smtransquimico.view.chat

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smtransquimico.R
import com.example.smtransquimico.RetrofitInstance
import com.example.smtransquimico.controller.ChatController
import com.example.smtransquimico.databinding.ActivityChatBinding
import com.example.smtransquimico.model.Chat
import com.example.smtransquimico.model.NotificationData
import com.example.smtransquimico.model.PushNotification
import com.example.smtransquimico.model.Usuario
import com.example.smtransquimico.view.adapter.ChatMensagemAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    private var firebaseUser: FirebaseUser? = null
    private var reference: DatabaseReference? = null
    private var listaChat = ArrayList<Chat>()
    private lateinit var binding: ActivityChatBinding
    private lateinit var controller: ChatController
    var topic = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        controller = ChatController(binding)

        binding.listaChat.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val intent = intent
        val userId = intent.getStringExtra("userId")
        val userName = intent.getStringExtra("userName")


        binding.imgVoltarChat.setOnClickListener {
            onBackPressed()
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)

        setarImagemBancoFirebase()
        lerMensagem(firebaseUser!!.uid, userId)

        clicarBotaoEnviarMensagem(userId, userName)
    }


    private fun setarImagemBancoFirebase() {
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usuario = snapshot.getValue(Usuario::class.java)
                binding.txtNomeChat.text = usuario!!.userName
                if (usuario.profileImage == "") {
                    controller.setImagemPerfil(R.drawable.imagem_perfil)
                } else {
                    Glide.with(this@ChatActivity).load(usuario.profileImage).into(binding.imgChat)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Erro no Firebase: ${error.message}")
            }
        })
    }

    private fun clicarBotaoEnviarMensagem(id: String, nome: String?) {
        binding.btnEnviarMensagem.setOnClickListener {
            val mensagem: String = binding.edtMensagem.text.toString()

            if (mensagem.isEmpty()) {
                Toast.makeText(applicationContext, "Mensagem Vazia", Toast.LENGTH_SHORT).show()
                binding.edtMensagem.text = Editable.Factory.getInstance().newEditable("")
            } else {
                enviarMensagem(firebaseUser!!.uid, id, mensagem)
                binding.edtMensagem.text = Editable.Factory.getInstance().newEditable("")
                topic = "/topics/$id"
                PushNotification(
                    NotificationData(nome!!, mensagem),
                    topic
                ).also {
                    sendNotification(it)
                }
            }
        }
    }

    private fun enviarMensagem(enviarId: String, receberId: String, mensagem: String) {
        controller.enviarMensagem(enviarId, receberId, mensagem)
    }

    private fun lerMensagem(enviarId: String, receberId: String) {

        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaChat.clear()

                for (dataSnapShot: DataSnapshot in snapshot.children) {

                    val chat = dataSnapShot.getValue(Chat::class.java)
                    if (chat!!.senderId == enviarId && chat.receiverId == receberId || chat.senderId == receberId && chat.receiverId == enviarId
                    ) {
                        listaChat.add(chat)
                    }
                }
                setarAdaptador()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setarAdaptador() {
        binding.listaChat.layoutManager =
            LinearLayoutManager(this@ChatActivity, RecyclerView.VERTICAL, false)
        val chatMensagemAdapter = ChatMensagemAdapter(listaChat)
        binding.listaChat.adapter = chatMensagemAdapter
    }

    private fun sendNotification(notificacao: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.postNotification(notificacao)

                if (response.isSuccessful) {
                    Log.d("TAG", "Response: ${Gson().toJson(response)}")
                } else {
                    Log.e("TAG", response.errorBody()!!.string())
                }
            } catch (e: Exception) {
                Log.e("TAG", e.toString())
            }
        }
}