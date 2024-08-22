package com.example.smtransquimico.controller

import com.example.smtransquimico.EmptyTextWatcher
import com.example.smtransquimico.databinding.ActivityLoginBinding
import com.example.smtransquimico.view.login.LoginActivity

class LoginController(
    private val contexto: LoginActivity,
    private val binding: ActivityLoginBinding
) {

    fun clickButtonRegister() {
        val email = binding.edtEmailLogin.text.toString()
        val senha = binding.edtSenhaLogin.text.toString()

        if (email.isEmpty() || senha.isEmpty()) {
            contexto.showLoginIncorrect("Preencha todos os campos!")
        } else {
            contexto.checkLoginEmailAndPassword(email, senha)
        }
    }

    fun setErroEmailLogin(): Boolean {
        if (binding.edtEmailLogin.text.toString().isEmpty()) {
            binding.edtEmailLogin.addTextChangedListener(
                EmptyTextWatcher(
                    binding.edtEmailLogin,
                    binding.inputEdtEmailLogin
                )
            )
            return true
        }
        return false
    }

    fun setErroSenhaLogin(): Boolean {
        if (binding.edtSenhaLogin.text.toString().isEmpty()) {
            binding.edtSenhaLogin.addTextChangedListener(
                EmptyTextWatcher(
                    binding.edtSenhaLogin,
                    binding.inputEdtSenhaLogin
                )
            )
            return true
        }
        return false
    }
}