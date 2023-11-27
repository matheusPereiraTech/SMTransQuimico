package com.example.smtransquimico.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Execito")
class Exercito(
    val numeroOrdem: String = "",
    val nomenclatura: String = "",
    val tipoPCE: String = "",
) : java.io.Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}