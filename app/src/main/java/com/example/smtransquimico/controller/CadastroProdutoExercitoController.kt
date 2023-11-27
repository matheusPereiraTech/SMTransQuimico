package com.example.smtransquimico.controller

import com.example.smtransquimico.databinding.ActivityCadastraProdutoExercitoBinding
import com.example.smtransquimico.model.Exercito
import java.io.Serializable

class CadastroProdutoExercitoController(
    var exercito: Exercito? = null,
    var binding: ActivityCadastraProdutoExercitoBinding
) {
    fun setarDadosCampoProduto(produtoSerializable: Serializable?) {
        if (produtoSerializable != null && produtoSerializable is Exercito) {
            exercito = produtoSerializable as Exercito
            binding.btnSalvarProdutoExercito.text = "Atualizar Produto"
            binding.txtNumeroOrdem.setText(exercito?.numeroOrdem ?: "")
            binding.txtNomenclaturaProduto.setText(exercito?.nomenclatura ?: "")
            binding.txtTipoPCE.setText(exercito?.tipoPCE ?: "")

        } else {
            exercito = null
            binding.btnSalvarProdutoExercito.text = "Salvar Produto"
        }
    }

    fun salvarProduto(): Exercito {
        return Exercito(
            binding.txtNumeroOrdem.text.toString(),
            binding.txtNomenclaturaProduto.text.toString(),
            binding.txtTipoPCE.text.toString(),
        )
    }
}