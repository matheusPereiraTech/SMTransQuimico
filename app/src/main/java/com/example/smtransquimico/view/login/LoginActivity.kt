package com.example.smtransquimico.view.login

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.smtransquimico.R
import com.example.smtransquimico.controller.LoginController
import com.example.smtransquimico.databinding.ActivityLoginBinding
import com.example.smtransquimico.view.menu.MenuPrincipalActivity
import com.example.smtransquimico.view.usuario.CadastroUsuarioActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var googleEntrarCliente: GoogleSignInClient
    private var firebaseUser: FirebaseUser? = null
    private var auth = FirebaseAuth.getInstance()
    private lateinit var controller: LoginController
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()
        window.statusBarColor = Color.parseColor("#083087")

        firebaseUser = auth.currentUser

        cliqueBotaoCadastrar()
        cliqueBotaoEntrar()
        cliqueBotaoGoogle()
        cliqueBotaoEsqueciSenha()

        controller = LoginController(this, binding)

    }

    private fun cliqueBotaoGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleEntrarCliente = GoogleSignIn.getClient(this, gso)

        binding.btnGoogle.setOnClickListener {
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
                lidarResultados(task)
            }
        }

    private fun lidarResultados(task: Task<GoogleSignInAccount>) {
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
                exibirTelaMenuPrincipal()
            } else {
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cliqueBotaoEsqueciSenha() {
        binding.btnEsqueciSenha.setOnClickListener {
            val intent = Intent(this, RedefinirSenhaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun cliqueBotaoCadastrar() {
        binding.btnCadastrarLogin.setOnClickListener {
            val intent = Intent(this, CadastroUsuarioActivity::class.java)
            startActivity(intent)
        }
    }

    fun mostrarLoginIncorreto(mensagem: String) {
        val snackbar =
            Snackbar.make(binding.btnEntrarLogin, mensagem, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(Color.RED)
        snackbar.show()
    }

    private fun cliqueBotaoEntrar() {
        binding.btnEntrarLogin.setOnClickListener { view ->

            if (controller.setErroEmailLogin() || controller.setErroSenhaLogin()) {

                if (controller.setErroEmailLogin()) {
                    binding.inputEdtEmailLogin.error = "Campo Obrigatório"
                }

                if (controller.setErroSenhaLogin()) {
                    binding.inputEdtSenhaLogin.error = "Campo Obrigatório"
                }

                return@setOnClickListener
            }
            controller.clicarBotaoCadastro()
        }
    }

    fun verificarLoginEmailSenha(email: String, senha: String) {
        auth.signInWithEmailAndPassword(
            email,
            senha
        ).addOnCompleteListener { autenticacao ->
            if (autenticacao.isSuccessful) {
                exibirTelaMenuPrincipal()
            }
        }.addOnFailureListener {
            mostrarLoginIncorreto("Erro ao fazer o login do usuário!")
        }
    }

    private fun exibirTelaMenuPrincipal() {
        val intent = Intent(this, MenuPrincipalActivity::class.java)
        startActivity(intent)
        finish()
    }
}