package com.example.smtransquimico.view.chat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smtransquimico.R
import com.example.smtransquimico.constants.BotResposta
import com.example.smtransquimico.constants.Constants
import com.example.smtransquimico.constants.Constants.Companion.ABRIR_GOOGLE
import com.example.smtransquimico.constants.Constants.Companion.ABRIR_PESQUISA
import com.example.smtransquimico.constants.Constants.Companion.RECEBER_ID
import com.example.smtransquimico.constants.HoraChatBotMensagem
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
    var mensagemLista = mutableListOf<MensagemChatBot>()

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
        val timeStamp = HoraChatBotMensagem.mostrarHora()

        if (message.isNotEmpty()) {

            mensagemLista.add(MensagemChatBot(message, Constants.ENVIAR_ID, timeStamp))
            campoMensagemChatBot.setText("")

            adapter.inserirMensagem(MensagemChatBot(message, Constants.ENVIAR_ID, timeStamp))
            recyclerViewChat.scrollToPosition(adapter.itemCount - 1)

            botResposta(message)
        }
    }

    private fun botResposta(mensagem: String) {
        val timeStamp = HoraChatBotMensagem.mostrarHora()

        GlobalScope.launch {
            delay(1000)

            withContext(Dispatchers.Main) {
                resposta = BotResposta.basicResponses(mensagem, antts)


                mensagemLista.add(MensagemChatBot(resposta, Constants.RECEBER_ID, timeStamp))

                adapter.inserirMensagem(MensagemChatBot(resposta, RECEBER_ID, timeStamp))

                recyclerViewChat.scrollToPosition(adapter.itemCount - 1)

                when (resposta) {
                    ABRIR_GOOGLE -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.google.com/")
                        startActivity(site)
                    }

                    ABRIR_PESQUISA -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        val searchTerm: String? = mensagem.substringAfterLast("search")
                        site.data = Uri.parse("https://www.google.com/search?&q=$searchTerm")
                        startActivity(site)
                    }
                }
            }
        }
    }

    fun recyclerView() {
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

                    val antt = childSnapshot.getValue(ANTT::class.java)

                    antt?.let { antts.add(it) }
                }
                mensagemCustomizada("Olá, como posso te ajudar.Sou a inteligência artificial da TransQuimico !")

                mandaMensagemProduto()

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
                val timeStamp = HoraChatBotMensagem.mostrarHora()
                mensagemLista.add(MensagemChatBot(mensagem, RECEBER_ID, timeStamp))
                adapter.inserirMensagem(MensagemChatBot(mensagem, RECEBER_ID, timeStamp))

                recyclerViewChat.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun mandaMensagemProduto() {

        val mensagem = "Por Favor, forneça o número de ONU do produto específico !"

        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val timeStamp = HoraChatBotMensagem.mostrarHora()
                mensagemLista.add(MensagemChatBot(mensagem, RECEBER_ID, timeStamp))
                adapter.inserirMensagem(MensagemChatBot(mensagem, RECEBER_ID, timeStamp))

                recyclerViewChat.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }
}