package com.example.smtransquimico

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class TelefoneMaskWatcher(private val editText: EditText) : TextWatcher {
    private var isFormatting = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Não é necessário implementar
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // Não é necessário implementar
    }

    override fun afterTextChanged(s: Editable?) {
        if (isFormatting) return
        isFormatting = true

        val digits = s?.toString()?.replace("\\D".toRegex(), "") ?: ""
        var formatted = ""

        var i = 0
        var j = 0

        val mask = "(##) #####-####"

        while (i < mask.length && j < digits.length) {
            if (mask[i] == '#') {
                formatted += digits[j]
                j++
            } else {
                formatted += mask[i]
            }
            i++
        }

        editText.removeTextChangedListener(this)
        s?.replace(0, s.length, formatted)
        editText.addTextChangedListener(this)

        isFormatting = false
    }
}