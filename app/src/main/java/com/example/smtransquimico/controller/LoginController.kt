package com.example.smtransquimico.controller

import com.example.smtransquimico.databinding.ActivityLoginBinding
import com.example.smtransquimico.view.login.LoginActivity

class LoginController(private val contexto: LoginActivity, private val binding: ActivityLoginBinding) {

    fun clicarBotaoCadastro(){
        val email = binding.edtEmailLogin.text.toString()
        val senha = binding.edtSenhaLogin.text.toString()

        if (email.isEmpty() || senha.isEmpty()){
            contexto.mostrarLoginIncorreto("Preencha todos os campos!")
        }else{
          contexto.verificarLoginEmailSenha(email, senha)
        }
    }
}