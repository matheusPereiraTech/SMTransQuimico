package com.example.smtransquimico.constants


import CalculoChatBotMensagem
import com.example.smtransquimico.constants.Constants.Companion.ABRIR_GOOGLE
import com.example.smtransquimico.constants.Constants.Companion.ABRIR_PESQUISA
import com.example.smtransquimico.model.Ibama
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

object BotResposta {

    fun basicResponses(mensagem: String, ibamas: MutableList<Ibama>): String {

        val random = (0..2).random()
        val message = mensagem.toLowerCase()

        var encontrou = false
        var produto: String = ""

        ibamas.forEach {
            if (mensagem.lowercase().contains(it.numeroONU.trim())) {
                encontrou = true
                produto =
                    "Numero ONU: ${it.numeroONU}\n " +
                            "Nome e Descrição: ${it.nomeDescricao}\n" +
                            "Classe ou Subclasse de Risco: ${it.classeSubclasse}\n" +
                            "Risco Subsidiario ${it.riscoSubsidiario}\n" +
                            "Número de Risco: ${it.numeroRisco}\n" +
                            "Grupo de Emb: ${it.grupoEmb}\n" +
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

            //Hello
            message.contains("oi") -> {
                when (random) {
                    0 -> "Olá"
                    1 -> "E aí"
                    2 -> "Como vai!"
                    else -> "erro"
                }
            }

            //How are you?
            message.contains("Como vai") -> {
                when (random) {
                    0 -> "Estou bem, obrigado!"
                    1 -> "Estou com fome..."
                    2 -> "Muito bom! E você?"
                    else -> "erro"
                }
            }

            //What time is it?
            message.contains("horas") && message.contains("?") -> {
                val timeStamp = Timestamp(System.currentTimeMillis())
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val date = sdf.format(Date(timeStamp.time))

                date.toString()
            }

            //Abrir google
            message.contains("abrir") && message.contains("google") -> {
                ABRIR_GOOGLE
            }

            //Buscar na internet
            message.contains("pesquisar") -> {
                ABRIR_PESQUISA
            }

            //When the programme doesn't understand...
            else -> {
                when (random) {
                    0 -> "Eu não entendo..."
                    1 -> "Tente me perguntar algo diferente"
                    2 -> "Refaça sua pergunta de outra forma"
                    else -> "erro"
                }
            }
        }
    }
}


