package com.example.smtransquimico.controller

import android.widget.EditText
import com.example.smtransquimico.databinding.ActivityCadastraUsuarioBinding


class CadastroUsuarioController(var binding: ActivityCadastraUsuarioBinding) {

    fun passandoValorHashMap(userId: String): HashMap<String, String> {
        val hashMap: HashMap<String, String> = HashMap()
        hashMap["userId"] = userId
        hashMap["userName"] = binding.edtNomeUsuario.text.toString()
        hashMap["profileImage"] = ""
        hashMap["phone"] = binding.edtTelefoneUsuario.text.toString()
        hashMap["email"] = binding.edtEmailUsuario.text.toString()
        return hashMap
    }

    fun setandoCamposValoresVazio() {
        binding.edtNomeUsuario.setText("")
        binding.edtEmailUsuario.setText("")
        binding.edtSenhaUsuario.setText("")
        binding.edtTelefoneUsuario.setText("")
    }
}