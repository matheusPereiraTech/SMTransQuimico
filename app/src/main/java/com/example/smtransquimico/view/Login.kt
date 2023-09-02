package com.example.smtransquimico.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.smtransquimico.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    lateinit var botaoCadastrar: Button
    lateinit var botaoEntrar: Button
    lateinit var campoEmail: EditText
    lateinit var campoSenha: EditText
    private var auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        window.statusBarColor = Color.parseColor("#083087")

        findViewByid()
        cliqueBotaoCadastrar()
        cliqueBotaoEntrar()
    }

    fun findViewByid() {
        botaoCadastrar = findViewById(R.id.botao_cadastrar_login)
        botaoEntrar = findViewById(R.id.botao_entrar_login)
        campoEmail = findViewById(R.id.campo_email_login)
        campoSenha = findViewById(R.id.campo_senha_login)
    }

    fun cliqueBotaoCadastrar() {
        botaoCadastrar.setOnClickListener {

            val intent = Intent(this, CadastroUsuario::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun cliqueBotaoEntrar() {
        botaoEntrar.setOnClickListener { view ->

            if (campoEmail.text.toString().isEmpty() || campoSenha.text.toString().isEmpty()) {
                val snackbar =
                    Snackbar.make(view, "Preencha todos os campos!", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            } else {
                auth.signInWithEmailAndPassword(
                    campoEmail.text.toString(),
                    campoSenha.text.toString()
                ).addOnCompleteListener { autenticacao ->
                    if (autenticacao.isSuccessful) {
                        val intent = Intent(this, MenuPrincipal::class.java)
                        startActivity(intent)
                        finish()
                    }
                }.addOnFailureListener {
                    val snackbar =
                        Snackbar.make(
                            view,
                            "Erro ao fazer o login do usuário!",
                            Snackbar.LENGTH_SHORT
                        )
                    snackbar.setBackgroundTint(Color.RED)
                    snackbar.show()
                }
            }
        }
    }
}