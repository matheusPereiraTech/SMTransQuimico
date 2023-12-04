package com.example.smtransquimico.controller

import com.example.smtransquimico.EmptyTextWatcher
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

    fun setErroCampoNumeroOrdem(): Boolean {
        if (binding.txtNumeroOrdem.text.toString().isEmpty()) {
            binding.txtNumeroOrdem.addTextChangedListener(
                EmptyTextWatcher(
                    binding.txtNumeroOrdem,
                    binding.inputTxtNumeroOrdem
                )
            )
            return true
        }
        return false
    }

    fun setErroCampoNomenclatura(): Boolean {
        if (binding.txtNomenclaturaProduto.text.toString().isEmpty()) {
            binding.txtNomenclaturaProduto.addTextChangedListener(
                EmptyTextWatcher(
                    binding.txtNomenclaturaProduto,
                    binding.inputTxtNomenclaturaProduto
                )
            )
            return true
        }
        return false
    }

    fun setErroCampoTipoPCE(): Boolean {
        if (binding.txtTipoPCE.text.toString().isEmpty()) {
            binding.txtTipoPCE.addTextChangedListener(
                EmptyTextWatcher(
                    binding.txtTipoPCE,
                    binding.inputTxtTipoPCE
                )
            )
            return true
        }
        return false
    }
}