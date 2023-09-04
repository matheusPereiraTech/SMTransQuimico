package com.example.smtransquimico.project

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.cardview.widget.CardView
import com.example.smtransquimico.R
import com.google.firebase.auth.FirebaseAuth

class MenuPrincipal : AppCompatActivity() {

    lateinit var cardListaProduto: CardView
    lateinit var cardCadastraProduto: CardView
    lateinit var cardConsultaUsuario: CardView
    lateinit var cardChat: CardView
    lateinit var botaoDeslogar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)
        supportActionBar?.hide()
        window.statusBarColor = Color.parseColor("#083087")

        initFindViewById()
        cliqueListarProduto()
        cliqueCadastrarProduto()
        cliqueConsultarUsuario()
        cliqueChat()
        cliqueDeslogar()
    }

    fun initFindViewById() {
        cardListaProduto = findViewById(R.id.card_lista_produto)
        cardCadastraProduto = findViewById(R.id.card_cadastra_produto)
        cardConsultaUsuario = findViewById(R.id.card_consulta_usuario)
        cardChat = findViewById(R.id.card_chat)
        botaoDeslogar = findViewById(R.id.botao_deslogar)
    }

    fun cliqueListarProduto() {
        cardListaProduto.setOnClickListener {
            val intent = Intent(this, ListaProduto::class.java)
            startActivity(intent)
        }
    }

    fun cliqueCadastrarProduto() {
        cardCadastraProduto.setOnClickListener {
            val intent = Intent(this, CadastraProduto::class.java)
            startActivity(intent)
        }
    }

    fun cliqueConsultarUsuario() {
        cardConsultaUsuario.setOnClickListener {
            val intent = Intent(this, ConsultaUsuario::class.java)
            startActivity(intent)
        }
    }

    fun cliqueChat() {
        cardChat.setOnClickListener {
            val intent = Intent(this, Chat::class.java)
            startActivity(intent)
        }
    }

    fun cliqueDeslogar(){
        botaoDeslogar.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}