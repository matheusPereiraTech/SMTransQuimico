package com.example.smtransquimico.constants

import CalculoChatBotMensagem
import com.example.smtransquimico.model.ANTT

object BotResposta {
    private val productInfos = mutableListOf<String>()
    private val random = (0..2).random()
    fun basicResponses(value: String, antts: MutableList<ANTT>): String {
        val sanitizedValue = value.toLowerCase().trim()
        antts.forEach { it ->
            val unNumberWithoutZero = it.numeroONU.dropWhile { it == '0' }.padStart(4, '0')

            if (sanitizedValue == unNumberWithoutZero || sanitizedValue in it.nomeDescricao.toLowerCase() || sanitizedValue in unNumberWithoutZero
            ) {
                val productInfo = """
                    Numero ONU: $unNumberWithoutZero

                    Nome e Descrição: ${it.nomeDescricao}

                    Classe ou Subclasse de Risco: ${it.classeSubclasse}

                    Risco Subsidiário: ${it.riscoSubsidiario}

                    Número de Risco: ${it.numeroRisco}

                    Grupo de Embalagem: ${it.grupoEmb}

                    Provisões Especiais: ${it.provisoesEspeciais}
                """.trimIndent()
                productInfos.add(productInfo)
            }
        }

        return when {
            productInfos.isNotEmpty() -> productInfos.joinToString("\n\n---\n\n")

            sanitizedValue.contains("resolver") -> {
                val equation = sanitizedValue.substringAfterLast("resolver").trim()
                return try {
                    val answer = CalculoChatBotMensagem.calcularMatematica(equation)
                    "$answer"
                } catch (e: Exception) {
                    "Desculpe, não consigo resolver isso."
                }
            }

            sanitizedValue.contains("oi") -> {
                when (random) {
                    0 -> "Olá"
                    1 -> "E aí"
                    2 -> "Como vai!"
                    else -> "erro"
                }
            }

            sanitizedValue.contains("como você está?") -> {
                when (random) {
                    0 -> "Estou bem, obrigado!"
                    1 -> "Estou com fome..."
                    2 -> "Muito bom! E você?"
                    else -> "erro"
                }
            }

            else -> {
                when (random) {
                    0 -> "Eu não entendo..."
                    1 -> "Tente de novo..."
                    2 -> "Diga-me de novo..."
                    else -> "erro"
                }
            }
        }
    }
}
