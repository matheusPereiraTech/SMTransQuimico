package com.example.smtransquimico.view

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.smtransquimico.R
import com.example.smtransquimico.view.usuario.CadastroUsuario
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class Login : AppCompatActivity() {

    lateinit var botaoCadastrar: Button
    lateinit var botaoEntrar: Button
    lateinit var campoEmail: EditText
    lateinit var campoSenha: EditText
    private lateinit var googleEntrarCliente: GoogleSignInClient
    private lateinit var botaoGoogle: SignInButton
    private var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        window.statusBarColor = Color.parseColor("#083087")

        findViewByid()
        cliqueBotaoCadastrar()
        cliqueBotaoEntrar()
        cliqueBotaoGoogle()
    }

    private fun cliqueBotaoGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleEntrarCliente = GoogleSignIn.getClient(this, gso)

        botaoGoogle.setOnClickListener {
            entrarContaGoogle()
        }
    }

    private fun entrarContaGoogle() {
        val signIntent = googleEntrarCliente.signInIntent
        launcher.launch(signIntent)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
        }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val conta: GoogleSignInAccount? = task.result
            if (conta != null) {
                atualizaUi(conta)
            }
        } else {
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun atualizaUi(conta: GoogleSignInAccount) {
        val credenciais = GoogleAuthProvider.getCredential(conta.idToken, null)
        auth.signInWithCredential(credenciais).addOnCompleteListener {
            if (it.isSuccessful) {
                val intent = Intent(this, MenuPrincipal::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun findViewByid() {
        botaoCadastrar = findViewById(R.id.botao_cadastrar_login)
        botaoEntrar = findViewById(R.id.botao_entrar_login)
        campoEmail = findViewById(R.id.campo_email_login)
        campoSenha = findViewById(R.id.campo_senha_login)
        botaoGoogle = findViewById(R.id.botao_google)
    }

    private fun cliqueBotaoCadastrar() {
        botaoCadastrar.setOnClickListener {
            val intent = Intent(this, CadastroUsuario::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun cliqueBotaoEntrar() {
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