package com.example.smtransquimico.constants

import android.util.Log
import com.example.smtransquimico.model.Exercito
import com.example.smtransquimico.model.Policia
import java.util.Locale

object CorrelacaoProdutos {

    fun verificarCorrelacaoPolicia(nomeDescricao: String, policias: MutableList<Policia>): Boolean {

        var encontrou = false

        val padrao = Regex("\\b[A-Z]+\\b")
        val maiusculas = padrao.findAll(nomeDescricao).joinToString(" ") { it.value }

        policias.forEach {
            if (maiusculas == it.produtoQuimico) {
                encontrou = true
            }
        }

        return encontrou
    }

    fun verificarCorrelacaoExercito(nomeDescricao: String, exercitos: MutableList<Exercito>): Boolean {

        var encontrou = false

        val padrao = Regex("\\b[A-Z]+\\b")
        val maiusculas = padrao.findAll(nomeDescricao).joinToString(" ") { it.value }

        exercitos.forEach {
            if (maiusculas == it.nomenclatura) {
                encontrou = true
            }
        }

        return encontrou
    }
}