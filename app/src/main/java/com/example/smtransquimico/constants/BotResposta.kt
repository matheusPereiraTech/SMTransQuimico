package com.example.smtransquimico.constants


import CalculoChatBotMensagem
import com.example.smtransquimico.constants.Constants.Companion.ABRIR_GOOGLE
import com.example.smtransquimico.constants.Constants.Companion.ABRIR_PESQUISA
import com.example.smtransquimico.model.ANTT
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

object BotResposta {

    fun basicResponses(mensagem: String, antts: MutableList<ANTT>): String {

        val random = (0..2).random()
        val message = mensagem.toLowerCase()

        var encontrou = false
        var produto: String = ""

        antts.forEach {
            if (mensagem.lowercase().contains(it.numeroONU.trim())) {
                encontrou = true
                produto =
                    "Numero ONU: ${it.numeroONU}\n\n" +
                            "Nome e Descrição: ${it.nomeDescricao}\n\n" +
                            "Classe ou Subclasse de Risco: ${it.classeSubclasse}\n\n" +
                            "Risco Subsidiario ${it.riscoSubsidiario}\n\n" +
                            "Número de Risco: ${it.numeroRisco}\n\n" +
                            "Grupo de Emb: ${it.grupoEmb}\n\n" +
                            "Provisões Especiais: ${it.provisoesEspeciais}"
            }
        }

        return when {
            encontrou -> {
                produto
            }

            message.contains("resolver") -> {
                val equation: String? = message.substringAfterLast("resolver")
                return try {
                    val answer = CalculoChatBotMensagem.calcularMatematica(equation ?: "0")
                    "$answer"

                } catch (e: Exception) {
                    "Desculpe, não consigo resolver isso."
                }
            }

            message.contains("oi") -> {
                when (random) {
                    0 -> "Olá"
                    1 -> "E aí"
                    2 -> "Como vai!"
                    else -> "erro"
                }
            }

            message.contains("Como você está?") -> {
                when (random) {
                    0 -> "Estou bem, obrigado!"
                    1 -> "Estou com fome..."
                    2 -> "Muito bom! E você?"
                    else -> "erro"
                }
            }

            message.contains("horas") && message.contains("?") -> {
                val timeStamp = Timestamp(System.currentTimeMillis())
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val date = sdf.format(Date(timeStamp.time))

                date.toString()
            }

            message.contains("abrir") && message.contains("google") -> {
                ABRIR_GOOGLE
            }

            message.contains("pesquisar") -> {
                ABRIR_PESQUISA
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


