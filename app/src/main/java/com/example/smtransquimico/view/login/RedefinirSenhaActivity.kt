package com.example.smtransquimico.view.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.smtransquimico.R
import com.google.firebase.auth.FirebaseAuth

class RedefinirSenhaActivity : AppCompatActivity() {

    private lateinit var campoIdentificador: EditText
    private lateinit var botaoSolicitarRedefinicaoSenha: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_senha)

        inicializarComponentes()
        inicializarRedifinicaoSenha()
    }

    private fun inicializarComponentes() {
        campoIdentificador = findViewById(R.id.edtIdentificador)
        botaoSolicitarRedefinicaoSenha = findViewById(R.id.btnSolicitarRedefinicaoSenha)
        auth = FirebaseAuth.getInstance()
    }

    private fun inicializarRedifinicaoSenha() {
        botaoSolicitarRedefinicaoSenha.setOnClickListener {
            val identificador = campoIdentificador.text.toString()

            if (identificador.isEmpty()) {
                Toast.makeText(this, "Por favor, insira seu identificador (ex: endereço de e-mail)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(identificador)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Solicitação de redefinição de senha enviada para $identificador", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Falha na solicitação de redefinição de senha: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}