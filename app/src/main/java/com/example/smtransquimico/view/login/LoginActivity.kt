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
        setContentView(binding.root)

        settingsActionBar()
        firebaseUser = auth.currentUser

        cliqueBotaoCadastrar()
        clickButtonOpen()
        clickButtonGoogle()
        cliqueBotaoEsqueciSenha()

        controller = LoginController(this, binding)
    }

    private fun settingsActionBar() {
        supportActionBar?.hide()
        window.statusBarColor = Color.parseColor("#083087")
    }
    private fun clickButtonGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleEntrarCliente = GoogleSignIn.getClient(this, gso)
        binding.btnGoogle.setOnClickListener {
            signInAccountGoogle()
        }
    }
    private fun signInAccountGoogle() = launcher.launch(googleEntrarCliente.signInIntent)

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                dealResults(task)
            }
        }
    private fun dealResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val conta: GoogleSignInAccount? = task.result
            if (conta != null) {
                updateUi(conta)
            }
        } else {
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateUi(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                displayScreenMenuMain()
            } else {
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun cliqueBotaoEsqueciSenha() =
        binding.btnEsqueciSenha.setOnClickListener {
            startActivity(Intent(this, RedefinirSenhaActivity::class.java))
        }
    private fun cliqueBotaoCadastrar() =
        binding.btnCadastrarLogin.setOnClickListener {
            startActivity(Intent(this, CadastroUsuarioActivity::class.java))
        }
    fun showLoginIncorrect(message: String) =
        Snackbar.make(binding.btnEntrarLogin, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(Color.RED).show()
    private fun clickButtonOpen() {
        binding.btnEntrarLogin.setOnClickListener {

            if (controller.setErroEmailLogin() || controller.setErroSenhaLogin()) {
                if (controller.setErroEmailLogin())
                    binding.inputEdtEmailLogin.error = "Campo Obrigatório"
                if (controller.setErroSenhaLogin())
                    binding.inputEdtSenhaLogin.error = "Campo Obrigatório"

                return@setOnClickListener
            }
            controller.clickButtonRegister()
        }
    }
    fun checkLoginEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(
            email,
            password
        ).addOnCompleteListener {
            if (it.isSuccessful)
                displayScreenMenuMain()
        }.addOnFailureListener {
            showLoginIncorrect("Erro ao fazer o login do usuário!")
        }
    }
    private fun displayScreenMenuMain() {
        startActivity(Intent(this, MenuPrincipalActivity::class.java))
        finish()
    }
}