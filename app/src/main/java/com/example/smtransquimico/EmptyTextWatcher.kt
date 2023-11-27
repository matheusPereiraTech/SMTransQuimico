package com.example.smtransquimico

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EmptyTextWatcher(private val editText: TextInputEditText, private val textInputLayout: TextInputLayout) :
    TextWatcher {

    override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
        // Não é necessário implementar
    }

    override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
        // Não é necessário implementar
    }

    override fun afterTextChanged(editable: Editable?) {
        val text = editable.toString().trim()

        if (text.isEmpty()) {
            textInputLayout.error = "Campo obrigatório"

        } else {
            textInputLayout.error = null
        }
    }
}