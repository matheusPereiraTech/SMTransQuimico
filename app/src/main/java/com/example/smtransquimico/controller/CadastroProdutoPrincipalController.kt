package com.example.smtransquimico.controller

import com.example.smtransquimico.databinding.ActivityCadastraProdutoBinding
import com.example.smtransquimico.model.Ibama
import java.io.Serializable

class CadastroProdutoPrincipalController(
    var ibama: Ibama? = null,
    var binding: ActivityCadastraProdutoBinding
) {
    fun setarDadosCampoProduto(produtoSerializable: Serializable?) {
        if (produtoSerializable != null && produtoSerializable is Ibama) {
            ibama = produtoSerializable as Ibama
            binding.btnSalvarProduto.text = "Atualizar Produto"
            binding.txtNumeroOnu.setText(ibama?.numeroONU ?: "")
            binding.txtNomeDescricao.setText(ibama?.nomeDescricao ?: "")
            binding.txtClasseSubclasse.setText(ibama?.classeSubclasse ?: "")
            binding.txtRiscoSubsidiario.setText(ibama?.riscoSubsidiario ?: "")
            binding.txtNumeroRisco.setText(ibama?.numeroRisco ?: "")
            binding.txtGrupoEmb.setText(ibama?.grupoEmb ?: "")
            binding.txtProvisoesEspeciais.setText(ibama?.provisoesEspeciais ?: "")
        } else {
            ibama = null
            binding.btnSalvarProduto.text = "Salvar Produto"
        }
    }

    fun salvarProduto(): Ibama {
        return Ibama(
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