package com.example.smtransquimico.model

import androidx.room.PrimaryKey

class Policia(
    val codigo: String = "",
    val produtoQuimico: String = "",
) : java.io.Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}