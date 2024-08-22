package com.example.smtransquimico.view.chat

import android.os.Bundle
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
import com.example.smtransquimico.model.Users
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
    private var topic = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = ChatController(binding)

        val intent = intent
        val userId = intent.getStringExtra("userId")
        val userName = intent.getStringExtra("userName")

        setarBotaoVoltarChat()

        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)

        popularChatComDadosFirebase(firebaseUser!!.uid, userId)

        clicarBotaoEnviarMensagem(userId, userName)
    }

    private fun setarBotaoVoltarChat() {
        binding.imgVoltarChat.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setarImagemBancoFirebase() {
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(Users::class.java)

                data?.let {
                    binding.txtNomeChat.text = it.userName
                    if (it.profileImage == "") {
                        controller.setImagemPerfil(R.drawable.imagem_perfil)
                    } else {
                        Glide.with(this@ChatActivity).load(it.profileImage).into(binding.imgChat)
                        setarAdaptador(data.profileImage)
                    }
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
                Toast.makeText(this@ChatActivity, "Mensagem Vazia", Toast.LENGTH_SHORT).show()
            }  else {
                enviarMensagem(firebaseUser!!.uid, id, mensagem)
                binding.edtMensagem.text.clear()

                topic = "/topics/$id"
                enviarNotificacao(nome, mensagem)
            }
        }
    }

    private fun enviarNotificacao(nome: String?, mensagem: String) {
        PushNotification(
            NotificationData(nome!!, mensagem),
            topic
        ).also {
            sendNotification(it)
        }
    }

    private fun enviarMensagem(enviarId: String, receberId: String, mensagem: String) {
        controller.enviarMensagem(enviarId, receberId, mensagem)
    }

    private fun popularChatComDadosFirebase(enviarId: String, receberId: String) {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaChat.clear()

                for (dataSnapShot: DataSnapshot in snapshot.children) {

                    val data = dataSnapShot.getValue(Chat::class.java)

                    data?.let {
                        if (it.senderId == enviarId && it.receiverId == receberId
                            || it.senderId == receberId && it.receiverId == enviarId
                        ) {
                            listaChat.add(it)
                        }
                    }
                }
                setarImagemBancoFirebase()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun setarAdaptador(senderImageUrl: String?) {
        binding.listaChat.layoutManager = LinearLayoutManager(this@ChatActivity, RecyclerView.VERTICAL, false)
        binding.listaChat.adapter = ChatMensagemAdapter(listaChat, senderImageUrl!!)
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