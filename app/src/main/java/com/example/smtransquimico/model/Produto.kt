package com.example.smtransquimico.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Produto(
    val codigoProduto: String,
    val nomeProduto: String,
    val tipoProduto: String
):java.io.Serializable{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}