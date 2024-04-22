package com.example.smtransquimico.controller

import android.content.Intent
import com.example.smtransquimico.databinding.ActivityMenuPrincipalBinding
import com.example.smtransquimico.view.chat.ActivityUsuario
import com.example.smtransquimico.view.produto.CadastraProdutoExercitoActivity
import com.example.smtransquimico.view.produto.CadastraProdutoPoliciaActivity
import com.example.smtransquimico.view.produto.CadastraProdutoPrincipalActivity
import com.example.smtransquimico.view.produto.ListaPrincipalProdutoActivity
import com.example.smtransquimico.view.produto.ListaProdutoExercitoActivity
import com.example.smtransquimico.view.produto.ListaProdutoPoliciactivity
import com.example.smtransquimico.view.usuario.ConsultaUsuarioActivity

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
            val intent = Intent(binding.root.context, ActivityUsuario::class.java)
            binding.root.context.startActivity(intent)
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