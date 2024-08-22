package com.example.smtransquimico.view.chat

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smtransquimico.R
import com.example.smtransquimico.constants.BotResposta
import com.example.smtransquimico.constants.Constants
import com.example.smtransquimico.constants.Constants.Companion.RECEBER_ID
import com.example.smtransquimico.model.ANTT
import com.example.smtransquimico.model.MensagemChatBot
import com.example.smtransquimico.view.adapter.MensagemChatBotAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatBotActivity : AppCompatActivity() {

    private lateinit var adapter: MensagemChatBotAdapter
    private lateinit var databaseReferenceANTT: DatabaseReference
    private var mensagemLista = mutableListOf<MensagemChatBot>()

    private lateinit var botaoEnviar: Button
    private lateinit var campoMensagemChatBot: EditText
    private lateinit var recyclerViewChat: RecyclerView
    private lateinit var resposta: String

    var antts = mutableListOf<ANTT>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot)

        iniciandoComponentes()
        databaseReferenceANTT = FirebaseDatabase.getInstance().getReference("produtos")

        recyclerView()
        clicandoBotoes()
    }

    private fun clicandoBotoes() {
        botaoEnviar.setOnClickListener {
            enviarMensagem()
        }

        campoMensagemChatBot.setOnClickListener {
            GlobalScope.launch {
                delay(100)

                withContext(Dispatchers.Main) {
                    recyclerViewChat.scrollToPosition(adapter.itemCount - 1)

                }
            }
        }
    }

    private fun enviarMensagem() {
        val message = campoMensagemChatBot.text.toString()
        if (message.isNotEmpty()) {
            mensagemLista.add(MensagemChatBot(message, Constants.ENVIAR_ID))
            campoMensagemChatBot.setText("")

            adapter.insertMessage(MensagemChatBot(message, Constants.ENVIAR_ID))
            recyclerViewChat.scrollToPosition(adapter.itemCount - 1)

            botResposta(message)
        }
    }

    private fun botResposta(mensagem: String) {
        GlobalScope.launch {
            delay(1000)

            withContext(Dispatchers.Main) {
                resposta = BotResposta.basicResponses(mensagem, antts)
                mensagemLista.add(MensagemChatBot(resposta, Constants.RECEBER_ID))
                adapter.insertMessage(MensagemChatBot(resposta, RECEBER_ID))
                recyclerViewChat.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun recyclerView() {
        adapter = MensagemChatBotAdapter()
        recyclerViewChat.adapter = adapter
        recyclerViewChat.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun onStart() {
        super.onStart()
        GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main) {
                recyclerViewChat.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun iniciandoComponentes() {
        botaoEnviar = findViewById(R.id.botaoEnviarMensagemChatBot)
        campoMensagemChatBot = findViewById(R.id.campoMensagemChatBot)
        recyclerViewChat = findViewById(R.id.recyclerViewChatBot)
    }

    private fun setDadosFirebase() {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    childSnapshot.getValue(ANTT::class.java)?.let { antts.add(it) }
                }
                mensagemCustomizada("Olá, como posso te ajudar? Sou a inteligência artificial da TransQuimico!")
                mandaMensagemNumeroProduto()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Trate a falha aqui
            }
        }
        databaseReferenceANTT.addValueEventListener(valueEventListener)
    }

    override fun onResume() {
        super.onResume()
        setDadosFirebase()
    }

    private fun mensagemCustomizada(mensagem: String) {
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                mensagemLista.add(MensagemChatBot(mensagem, RECEBER_ID))
                adapter.insertMessage(MensagemChatBot(mensagem, RECEBER_ID))
                recyclerViewChat.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun mandaMensagemNumeroProduto() {
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                mensagemLista.add(MensagemChatBot("Número de ONU ou Nome do produto específico!", RECEBER_ID))
                adapter.insertMessage(MensagemChatBot("Número de ONU ou Nome do produto específico!", RECEBER_ID))

                recyclerViewChat.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }
}
