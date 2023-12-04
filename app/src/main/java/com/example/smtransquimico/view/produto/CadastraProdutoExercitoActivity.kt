package com.example.smtransquimico.view.produto

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smtransquimico.controller.CadastroProdutoExercitoController
import com.example.smtransquimico.databinding.ActivityCadastraProdutoExercitoBinding
import com.example.smtransquimico.model.Exercito
import com.example.smtransquimico.view.menu.MenuPrincipalActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CadastraProdutoExercitoActivity : AppCompatActivity() {

    lateinit var controller: CadastroProdutoExercitoController
    private var exercito: Exercito? = null
    private lateinit var binding: ActivityCadastraProdutoExercitoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastraProdutoExercitoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setandoBarraInicial()
        val database = FirebaseDatabase.getInstance()
        val produtosRef = database.getReference("produto_exercito")
        controller = CadastroProdutoExercitoController(exercito, binding)
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
        binding.btnSalvarProdutoExercito.setOnClickListener {

            if (exercito == null) {
                val chave = binding.txtNumeroOrdem.text.toString()
                val produto = controller.salvarProduto()

                if (controller.setErroCampoNumeroOrdem() || controller.setErroCampoNomenclatura() || controller.setErroCampoTipoPCE()) {

                    if (controller.setErroCampoNumeroOrdem()) {
                        binding.inputTxtNumeroOrdem.error = "Campo Obrigatório"
                    }

                    if (controller.setErroCampoNomenclatura()) {
                        binding.inputTxtNomenclaturaProduto.error = "Campo Obrigatório"
                    }

                    if (controller.setErroCampoTipoPCE()) {
                        binding.inputTxtTipoPCE.error = "Campo Obrigatório"
                    }

                    return@setOnClickListener
                }

                produtosRef.child(chave).setValue(produto)
                voltarMenuPrincipal()
            } else {
                val chaveExistente = exercito!!.numeroOrdem
                val produtoAtualizado = controller.salvarProduto()

                if (controller.setErroCampoNumeroOrdem() || controller.setErroCampoNomenclatura() || controller.setErroCampoTipoPCE()) {

                    if (controller.setErroCampoNumeroOrdem()) {
                        binding.inputTxtNumeroOrdem.error = "Campo Obrigatório"
                    }

                    if (controller.setErroCampoNomenclatura()) {
                        binding.inputTxtNomenclaturaProduto.error = "Campo Obrigatório"
                    }

                    if (controller.setErroCampoTipoPCE()) {
                        binding.inputTxtTipoPCE.error = "Campo Obrigatório"
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