package com.example.smtransquimico.controller

import android.content.Intent
import com.example.smtransquimico.databinding.ActivityMenuPrincipalBinding
import com.example.smtransquimico.view.chat.UsuarioActivity
import com.example.smtransquimico.view.login.LoginActivity
import com.example.smtransquimico.view.produto.*
import com.example.smtransquimico.view.usuario.ConsultaUsuarioActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MenuPrincipalController(var binding: ActivityMenuPrincipalBinding) {

    fun listarProdutoPrincipal() {
        binding.cardListaproduto.setOnClickListener {
            val intent = Intent(binding.root.context, ListaPrincipalProdutoActivity::class.java)
            binding.root.context.startActivity(intent)
        }
    }

    fun abrirCadastraProdutoPrincipal() {
        binding.cardCadastraProduto.setOnClickListener {
            val intent = Intent(binding.root.context, CadastraProdutoPrincipalActivity::class.java)
            binding.root.context.startActivity(intent)
        }
    }

    fun abrirConsultaUsuario() {
        binding.cardConsultaUsuario.setOnClickListener {
            val intent = Intent(binding.root.context, ConsultaUsuarioActivity::class.java)
            binding.root.context.startActivity(intent)
        }
    }

    fun abrirChat() {
        binding.cardChat.setOnClickListener {
            val intent = Intent(binding.root.context, UsuarioActivity::class.java)
            binding.root.context.startActivity(intent)
        }
    }

    fun deslogarAplicativo() {
        binding.btnDeslogar.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val googleSignInClient =
                GoogleSignIn.getClient(binding.root.context, GoogleSignInOptions.DEFAULT_SIGN_IN)
            googleSignInClient.signOut().addOnCompleteListener {
                val intent = Intent(binding.root.context, LoginActivity::class.java)
                binding.root.context.startActivity(intent)
            }
        }
    }

    fun abrirCadastroExercito() {
        binding.cardCadastrarProdutoExercito.setOnClickListener {
            val intent = Intent(binding.root.context, CadastraProdutoExercitoActivity::class.java)
            binding.root.context.startActivity(intent)
        }
    }

    fun abrirCadastroPolicia() {
        binding.cardCadastrarProdutoPolicia.setOnClickListener {
            val intent = Intent(binding.root.context, CadastraProdutoPoliciaActivity::class.java)
            binding.root.context.startActivity(intent)
        }
    }

    fun listarProdutoExercito() {
        binding.cardListaProdutoExercito.setOnClickListener {
            val intent = Intent(binding.root.context, ListaProdutoExercitoActivity::class.java)
            binding.root.context.startActivity(intent)
        }
    }

    fun listarProdutoPolicia() {
        binding.cardListaProdutoPolicia.setOnClickListener {
            val intent = Intent(binding.root.context, ListaProdutoPoliciactivity::class.java)
            binding.root.context.startActivity(intent)
        }
    }
}