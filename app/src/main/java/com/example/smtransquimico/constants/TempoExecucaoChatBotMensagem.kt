package com.example.smtransquimico.constants

import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

object TempoExecucaoChatBotMensagem {

    fun hora(): String {
        val hora = Timestamp(System.currentTimeMillis())
        val sdf = SimpleDateFormat("HH:mm")
        val tm = sdf.format(Date(hora.time))

        return tm.toString()
    }
}