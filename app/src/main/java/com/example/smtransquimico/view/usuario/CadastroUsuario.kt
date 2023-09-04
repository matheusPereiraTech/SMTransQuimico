package com.example.smtransquimico.view.usuario

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.smtransquimico.R
import com.example.smtransquimico.view.Login
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class CadastroUsuario : AppCompatActivity() {

    lateinit var botaoSalvarUsuario: Button
    lateinit var campoEmail: EditText
    lateinit var campoSenha: EditText
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastra_usuario)
        supportActionBar?.hide()
        window.statusBarColor = Color.parseColor("#083087")
        initFindViewById()
        cliqueSalvarUsuario()
    }

    private fun initFindViewById() {
        botaoSalvarUsuario = findViewById(R.id.botao_salvar_usuario)
        campoEmail = findViewById(R.id.campo_email_usuario)
        campoSenha = findViewById(R.id.campo_senha_usuario)
    }

    private fun cliqueSalvarUsuario() {

        botaoSalvarUsuario.setOnClickListener { view ->

            if (campoEmail.text.toString().isEmpty() || campoSenha.text.toString().isEmpty()) {
                val snackbar =
                    Snackbar.make(view, "Preencha todos os campos!", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            } else {
                auth.createUserWithEmailAndPassword(
                    campoEmail.text.toString(),
                    campoSenha.text.toString()
                )
                    .addOnCompleteListener { cadastro ->
                        if (cadastro.isSuccessful) {
                            val snackbar = Snackbar.make(
                                view,
                                "Sucesso ao cadastrar o usuário!",
                                Snackbar.LENGTH_SHORT
                            )
                            snackbar.setBackgroundTint(Color.BLUE)
                            snackbar.show()
                            navegarTelaPrincipal()
                        }
                    }.addOnFailureListener { exception ->
                        val mensagemErro = when (exception) {
                            is FirebaseAuthWeakPasswordException -> "Digite uma senha com mínimo 6 caracteres!"
                            is FirebaseAuthInvalidCredentialsException -> "Digite um email válido!"
                            is FirebaseAuthUserCollisionException -> "Esta conta já foi cadastrada!"
                            is FirebaseNetworkException -> "Sem conexão com a internet!"
                            else -> "Erro ao cadastrar o usuário!"
                        }
                        val snackbar = Snackbar.make(view, mensagemErro, Snackbar.LENGTH_SHORT)
                        snackbar.setBackgroundTint(Color.RED)
                        snackbar.show()
                    }
            }
        }
    }

    private fun navegarTelaPrincipal() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()

        val usuarioAtual = FirebaseAuth.getInstance().currentUser
        if (usuarioAtual != null) {
            navegarTelaPrincipal()
        }
    }
}