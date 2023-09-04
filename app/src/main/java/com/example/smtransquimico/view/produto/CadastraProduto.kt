package com.example.smtransquimico.view.produto

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.example.smtransquimico.R
import com.example.smtransquimico.model.Produto
import com.example.smtransquimico.data.AppDatabase
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class CadastraProduto : AppCompatActivity() {

    private lateinit var campoCodigoProduto: TextInputEditText
    private lateinit var campoNomeProduto: TextInputEditText
    private lateinit var campoTipoProduto: TextInputEditText
    private lateinit var botaoSalvarProduto: Button
    private var produto: Produto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastra_produto)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Cadastrar Produto"
        window.statusBarColor = Color.parseColor("#083087")

        initFindViewBydId()
        initExtras()
        adicionarProduto()
    }

    private fun initExtras() {
        val produtoSerializable = intent.getSerializableExtra("Dados")

        if (produtoSerializable != null && produtoSerializable is Produto) {
            produto = produtoSerializable as Produto
            botaoSalvarProduto.text = "Atualizar Produto"
            campoCodigoProduto.setText(produto?.codigoProduto ?: "")
            campoNomeProduto.setText(produto?.nomeProduto ?: "")
            campoTipoProduto.setText(produto?.tipoProduto ?: "")
        } else {
            produto = null
            botaoSalvarProduto.text = "Salvar Produto"
        }
    }

    private fun initFindViewBydId() {
        campoCodigoProduto = findViewById(R.id.campo_codigo_produto)
        campoNomeProduto = findViewById(R.id.campo_nome_produto)
        campoTipoProduto = findViewById(R.id.campo_tipo_produto)
        botaoSalvarProduto = findViewById(R.id.botao_salvar_produto)
    }

    private fun adicionarProduto() {
        botaoSalvarProduto.setOnClickListener {
            val codigoProduto = campoCodigoProduto.text.toString()
            val nomeProduto = campoNomeProduto.text.toString()
            val tipoProduto = campoTipoProduto.text.toString()

            lifecycleScope.launch {
                if (produto == null) {
                    val produto = Produto(codigoProduto, nomeProduto, tipoProduto)
                    AppDatabase(context = this@CadastraProduto).pegarProdutoDao()
                        .adicionaProduto(produto)
                    finish()
                } else {
                    val produtoAtualizado = Produto(codigoProduto, nomeProduto, tipoProduto)
                    produtoAtualizado.id = produto?.id ?: 0
                    AppDatabase(this@CadastraProduto).pegarProdutoDao()
                        .atualizarProduto(produtoAtualizado)
                    finish()
                }
            }
        }
    }
}