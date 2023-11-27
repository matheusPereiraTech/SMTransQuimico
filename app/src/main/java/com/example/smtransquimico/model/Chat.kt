package com.example.smtransquimico.model

data class Chat(
    var senderId: String = "",
    val receiverId: String = "",
    val message: String = ""
)