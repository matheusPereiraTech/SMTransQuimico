package com.example.smtransquimico.controller

import com.example.smtransquimico.EmptyTextWatcher
import com.example.smtransquimico.databinding.ActivityCadastraProdutoBinding
import com.example.smtransquimico.model.ANTT
import java.io.Serializable

class CadastroProdutoPrincipalController(
    var antt: ANTT? = null,
    var binding: ActivityCadastraProdutoBinding
) {
    fun setarDadosCampoProduto(produtoSerializable: Serializable?) {
        if (produtoSerializable != null && produtoSerializable is ANTT) {
            antt = produtoSerializable as ANTT
            binding.btnSalvarProduto.text = "Atualizar Produto"
            binding.txtNumeroOnu.setText(antt?.numeroONU ?: "")
            binding.txtNomeDescricao.setText(antt?.nomeDescricao ?: "")
            binding.txtClasseSubclasse.setText(antt?.classeSubclasse ?: "")
            binding.txtRiscoSubsidiario.setText(antt?.riscoSubsidiario ?: "")
            binding.txtNumeroRisco.setText(antt?.numeroRisco ?: "")
            binding.txtGrupoEmb.setText(antt?.grupoEmb ?: "")
            binding.txtProvisoesEspeciais.setText(antt?.provisoesEspeciais ?: "")
        } else {
            antt = null
            binding.btnSalvarProduto.text = "Salvar Produto"
        }
    }

    fun setErroCampoNumeroONU(): Boolean {
        if (binding.txtNumeroOnu.text.toString().isEmpty()) {
            binding.txtNumeroOnu.addTextChangedListener(
                EmptyTextWatcher(
                    binding.txtNumeroOnu,
                    binding.inputTxtNumeroOnu
                )
            )
            return true
        }

        return false
    }

    fun setErroNomeDescricao(): Boolean {
        if (binding.txtNomeDescricao.text.toString().isEmpty()) {
            binding.txtNomeDescricao.addTextChangedListener(
                EmptyTextWatcher(
                    binding.txtNomeDescricao,
                    binding.inputTxtNomeDescricao
                )
            )
            return true
        }

        return false
    }

    fun salvarProduto(): ANTT {
        return ANTT(
            binding.txtNumeroOnu.text.toString(),
            binding.txtNomeDescricao.text.toString(),
            binding.txtClasseSubclasse.text.toString(),
            binding.txtRiscoSubsidiario.text.toString(),
            binding.txtNumeroRisco.text.toString(),
            binding.txtGrupoEmb.text.toString(),
            binding.txtProvisoesEspeciais.text.toString()
        )
    }
}