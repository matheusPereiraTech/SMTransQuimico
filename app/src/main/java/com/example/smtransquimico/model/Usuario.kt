package com.example.smtransquimico.model

import java.net.URI

data class Usuario(
    var userId: String = "",
    val userName: String = "",
    val profileImage: String = "",
    val phone: String = "",
    val email: String = ""
)