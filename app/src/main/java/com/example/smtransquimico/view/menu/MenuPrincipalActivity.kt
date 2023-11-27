package com.example.smtransquimico.view.menu

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smtransquimico.controller.MenuPrincipalController
import com.example.smtransquimico.databinding.ActivityMenuPrincipalBinding

class MenuPrincipalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()
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
        controler.deslogarAplicativo()
    }
}