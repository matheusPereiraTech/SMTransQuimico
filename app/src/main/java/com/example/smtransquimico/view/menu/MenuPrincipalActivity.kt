package com.example.smtransquimico.view.menu

import android.R
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.smtransquimico.controller.MenuPrincipalController
import com.example.smtransquimico.databinding.ActivityMenuPrincipalBinding
import com.example.smtransquimico.view.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


class MenuPrincipalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        window.statusBarColor = Color.parseColor("#083087")

        val controler = MenuPrincipalController(binding)
        controler.listarProdutoPrincipal()
        controler.abrirCadastraProdutoPrincipal()
        controler.abrirCadastroExercito()
        controler.abrirCadastroPolicia()
        controler.abrirConsultaUsuario()
        controler.abrirChat()
        controler.listarProdutoExercito()
        controler.listarProdutoPolicia()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                FirebaseAuth.getInstance().signOut()

                val googleSignInClient =
                    GoogleSignIn.getClient(binding.root.context, GoogleSignInOptions.DEFAULT_SIGN_IN)
                googleSignInClient.signOut().addOnCompleteListener {
                    val intent = Intent(binding.root.context, LoginActivity::class.java)
                    binding.root.context.startActivity(intent)
                }
            }

            else -> {}
        }
        return true
    }
}