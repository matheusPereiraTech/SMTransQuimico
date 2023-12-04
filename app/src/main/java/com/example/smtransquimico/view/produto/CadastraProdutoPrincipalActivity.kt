package com.example.smtransquimico.view.produto

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smtransquimico.controller.CadastroProdutoPrincipalController
import com.example.smtransquimico.databinding.ActivityCadastraProdutoBinding
import com.example.smtransquimico.model.ANTT
import com.example.smtransquimico.view.menu.MenuPrincipalActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class CadastraProdutoPrincipalActivity : AppCompatActivity() {

    lateinit var controller: CadastroProdutoPrincipalController
    private var antt: ANTT? = null
    private lateinit var binding: ActivityCadastraProdutoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastraProdutoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setandoBarraInicial()

        val database = FirebaseDatabase.getInstance()
        val produtosRef = database.getReference("produtos")
        controller = CadastroProdutoPrincipalController(antt, binding)
        initExtras()
        adicionarProduto(produtosRef)
    }

    private fun setandoBarraInicial() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Cadastrar Produto"
        window.statusBarColor = Color.parseColor("#083087")
    }

    private fun initExtras() {
        val produtoSerializable = intent.getSerializableExtra("Dados")
        controller.setarDadosCampoProduto(produtoSerializable)
    }

    private fun adicionarProduto(produtosRef: DatabaseReference) {
        binding.btnSalvarProduto.setOnClickListener {

            if (antt == null) {
                val chave = binding.txtNumeroOnu.text.toString()
                val produto = controller.salvarProduto()

                if (controller.setErroCampoNumeroONU() || controller.setErroNomeDescricao()) {

                    if (controller.setErroCampoNumeroONU()) {
                        binding.inputTxtNumeroOnu.error = "Campo Obrigatório"
                    }

                    if (controller.setErroNomeDescricao()) {
                        binding.inputTxtNomeDescricao.error = "Campo Obrigatório"
                    }

                    return@setOnClickListener
                }

                produtosRef.child(chave).setValue(produto)
                voltarMenuPrincipal()
            } else {
                val chaveExistente = antt!!.numeroONU
                val produtoAtualizado = controller.salvarProduto()

                if (controller.setErroCampoNumeroONU() || controller.setErroNomeDescricao()) {

                    if (controller.setErroCampoNumeroONU()) {
                        binding.inputTxtNumeroOnu.error = "Campo Obrigatório"
                    }

                    if (controller.setErroNomeDescricao()) {
                        binding.inputTxtNomeDescricao.error = "Campo Obrigatório"
                    }

                    return@setOnClickListener
                }


                produtosRef.child(chaveExistente).setValue(produtoAtualizado)
                voltarMenuPrincipal()
            }
        }
    }

    private fun voltarMenuPrincipal() {
        val intent = Intent(this, MenuPrincipalActivity::class.java)
        startActivity(intent)
    }
}