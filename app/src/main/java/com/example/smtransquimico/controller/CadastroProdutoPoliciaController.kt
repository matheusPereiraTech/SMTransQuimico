package com.example.smtransquimico.controller

import com.example.smtransquimico.databinding.ActivityCadastraProdutoPoliciaBinding
import com.example.smtransquimico.model.Ibama
import com.example.smtransquimico.model.Policia
import java.io.Serializable

class CadastroProdutoPoliciaController(
    var policia: Policia? = null,
    var binding: ActivityCadastraProdutoPoliciaBinding
) {
    fun setarDadosCampoProduto(produtoSerializable: Serializable?) {
        if (produtoSerializable != null && produtoSerializable is Policia) {
            policia = produtoSerializable as Policia
            binding.btnSalvarProdutoPolicia.text = "Atualizar Produto"
            binding.edtCodigoQuimico.setText(policia?.codigo ?: "")
            binding.edtProdutoQuimico.setText(policia?.produtoQuimico ?: "")

        } else {
            policia = null
            binding.btnSalvarProdutoPolicia.text = "Salvar Produto"
        }
    }

    fun salvarProduto(): Policia {
        return Policia(
            binding.edtCodigoQuimico.text.toString(),
            binding.edtProdutoQuimico.text.toString()
        )
    }
}