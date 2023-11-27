package com.example.smtransquimico.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Produto")
data class Ibama(
    val numeroONU: String = "",
    val nomeDescricao: String = "",
    val classeSubclasse: String = "",
    val riscoSubsidiario: String = "",
    val numeroRisco: String = "",
    val grupoEmb: String = "",
    val provisoesEspeciais: String = ""
) : java.io.Serializable {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}