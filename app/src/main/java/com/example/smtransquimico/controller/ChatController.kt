package com.example.smtransquimico.controller

import com.example.smtransquimico.databinding.ActivityChatBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ChatController(var binding: ActivityChatBinding) {

    fun setImagemPerfil(imageResource: Int) {
        binding.imgChat.setImageResource(imageResource)
    }

    fun enviarMensagem(enviarId: String, receberId: String, mensagem: String) {

        val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
        val hashMap: HashMap<String, String> = HashMap()
        hashMap["senderId"] = enviarId
        hashMap["receiverId"] = receberId
        hashMap["message"] = mensagem
        reference.child("Chat").push().setValue(hashMap)
    }
}