package com.example.smtransquimico.view.usuario

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.smtransquimico.EmptyTextWatcher
import com.example.smtransquimico.TelefoneMaskWatcher
import com.example.smtransquimico.controller.CadastroUsuarioController
import com.example.smtransquimico.databinding.ActivityCadastraUsuarioBinding
import com.example.smtransquimico.view.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class CadastroUsuarioActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var controller: CadastroUsuarioController
    private lateinit var binding: ActivityCadastraUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastraUsuarioBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        controller = CadastroUsuarioController(binding)

        val telefoneMaskWatcher = TelefoneMaskWatcher(binding.edtTelefoneUsuario)
        binding.edtTelefoneUsuario.addTextChangedListener(telefoneMaskWatcher)

        supportActionBar?.hide()
        window.statusBarColor = Color.parseColor("#083087")
        auth = FirebaseAuth.getInstance()
        cliqueSalvarUsuario()

    }

    private fun mensagemDeErro() {

        val snackbar =
            Snackbar.make(
                binding.btnSalvarUsuario,
                "Preencha todos os campos!",
                Snackbar.LENGTH_SHORT
            )
        snackbar.setBackgroundTint(Color.RED)
        snackbar.show()
    }


    private fun setErroCampoNome(): Boolean {
        if (binding.edtNomeUsuario.text.toString().isEmpty()) {
            binding.edtNomeUsuario.addTextChangedListener(
                EmptyTextWatcher(
                    binding.edtNomeUsuario,
                    binding.inputNomeUsuario
                )
            )
            return true
        }
        return false
    }

    private fun setErroCampoTelefone(): Boolean {
        if (binding.edtTelefoneUsuario.text.toString().isEmpty()) {
            binding.edtTelefoneUsuario.addTextChangedListener(
                EmptyTextWatcher(
                    binding.edtTelefoneUsuario,
                    binding.inputTelefoneUsuario
                )
            )
            return true
        }
        return false
    }

    private fun setErroCampoEmail(): Boolean {
        if (binding.edtEmailUsuario.text.toString().isEmpty()) {
            binding.edtEmailUsuario.addTextChangedListener(
                EmptyTextWatcher(
                    binding.edtEmailUsuario,
                    binding.inputEmailUsuario
                )
            )
            return true
        }
        return false
    }

    private fun setErroCampoSenha(): Boolean {
        if (binding.edtSenhaUsuario.text.toString().isEmpty()) {
            binding.edtSenhaUsuario.addTextChangedListener(
                EmptyTextWatcher(
                    binding.edtSenhaUsuario,
                    binding.inputSenhaUsuario
                )
            )
            return true
        }
        return false
    }

    private fun cliqueSalvarUsuario() {

        binding.btnSalvarUsuario.setOnClickListener { view ->

            if (setErroCampoNome() || setErroCampoTelefone() || setErroCampoEmail() || setErroCampoSenha()) {

                if (setErroCampoNome()) {
                    binding.inputNomeUsuario.error = "Campo Obrigatório"
                }
                if (setErroCampoTelefone()) {
                    binding.inputTelefoneUsuario.error = "Campo Obrigatório"
                }
                if (setErroCampoEmail()) {
                    binding.inputEmailUsuario.error = "Campo Obrigatório"
                }
                if (setErroCampoSenha()) {
                    binding.inputSenhaUsuario.error = "Campo Obrigatório"
                }

                mensagemDeErro()

            } else {
                auth.createUserWithEmailAndPassword(
                    binding.edtEmailUsuario.text.toString(),
                    binding.edtSenhaUsuario.text.toString()
                )
                    .addOnCompleteListener { cadastro ->
                        if (cadastro.isSuccessful) {

                            val user: FirebaseUser? = auth.currentUser
                            val userId: String = user!!.uid

                            databaseReference =
                                FirebaseDatabase.getInstance().getReference("Users").child(userId)

                            val hashMap = controller.passandoValorHashMap(userId)

                            databaseReference.setValue(hashMap).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    controller.setandoCamposValoresVazio()
                                    val intent = Intent(
                                        this@CadastroUsuarioActivity,
                                        LoginActivity::class.java
                                    )
                                    startActivity(intent)
                                }
                            }
                        }
                    }.addOnFailureListener { exception ->
                        Log.e(TAG, "Erro durante a criação do usuário: ${exception.message}")
                    }
            }
        }
    }
}

