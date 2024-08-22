package com.example.smtransquimico.view.produto

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smtransquimico.controller.CadastroProdutoPoliciaController
import com.example.smtransquimico.databinding.ActivityCadastraProdutoPoliciaBinding
import com.example.smtransquimico.model.Policia
import com.example.smtransquimico.view.menu.MenuPrincipalActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CadastraProdutoPoliciaActivity : AppCompatActivity() {

    lateinit var controller: CadastroProdutoPoliciaController
    private var policia: Policia? = null
    private lateinit var binding: ActivityCadastraProdutoPoliciaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastraProdutoPoliciaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setandoBarraInicial()
        initExtras()
        adicionarProduto(FirebaseDatabase.getInstance().getReference("produto_policia"))
    }

    private fun setandoBarraInicial() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Cadastrar Produto"
        window.statusBarColor = Color.parseColor("#083087")
    }

    private fun initExtras() {
        controller = CadastroProdutoPoliciaController(policia, binding)
        controller.setarDadosCampoProduto(intent.getSerializableExtra("Dados"))
    }

    private fun adicionarProduto(produtosRef: DatabaseReference) {
        binding.btnSalvarProdutoPolicia.setOnClickListener {
            if (policia == null) {
                if (controller.setErroCodigoQuimico() || controller.setErroProdutoQuimico()) {
                    if (controller.setErroCodigoQuimico()) {
                        binding.inputEdtCodigoQuimico.error = "Campo Obrigatório"
                    }
                    if (controller.setErroProdutoQuimico()) {
                        binding.inputEdtProdutoQuimic.error = "Campo Obrigatório"
                    }
                    return@setOnClickListener
                }

                produtosRef.child(binding.edtCodigoQuimico.text.toString()).setValue(controller.salvarProduto())
                voltarMenuPrincipal()
            } else {
                if (controller.setErroCodigoQuimico() || controller.setErroProdutoQuimico()) {
                    if (controller.setErroCodigoQuimico()) {
                        binding.inputEdtCodigoQuimico.error = "Campo Obrigatório"
                    }
                    if (controller.setErroProdutoQuimico()) {
                        binding.inputEdtProdutoQuimic.error = "Campo Obrigatório"
                    }
                    return@setOnClickListener
                }
                produtosRef.child(policia!!.codigo).setValue(controller.salvarProduto())
                voltarMenuPrincipal()
            }
        }
    }

    private fun voltarMenuPrincipal() {
        val intent = Intent(this, MenuPrincipalActivity::class.java)
        startActivity(intent)
    }
}