package com.example.smtransquimico.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smtransquimico.R
import com.example.smtransquimico.constants.Constants.Companion.ENVIAR_ID
import com.example.smtransquimico.constants.Constants.Companion.RECEBER_ID
import com.example.smtransquimico.model.MensagemChatBot

class MensagemChatBotAdapter : RecyclerView.Adapter<MensagemChatBotAdapter.MensagemViewHolder>() {

    var mensagemLista = mutableListOf<MensagemChatBot>()

    inner class MensagemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val usuarioEnvia = itemView.findViewById<TextView>(R.id.usuarioEnvia)
        val usuarioRecebe = itemView.findViewById<TextView>(R.id.usuarioRecebe)

        init {
            itemView.setOnClickListener {
                mensagemLista.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensagemViewHolder {
        return MensagemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.mensagem_item_chatbot, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mensagemLista.size
    }

    override fun onBindViewHolder(holder: MensagemViewHolder, position: Int) {
        val mensagemChatBot = mensagemLista[position]

        when (mensagemChatBot.id) {
            ENVIAR_ID -> {
                holder.usuarioEnvia.apply {
                    text = mensagemChatBot.mensagem
                    visibility = View.VISIBLE
                }

                holder.usuarioRecebe.visibility = View.GONE
            }

            RECEBER_ID -> {
                holder.usuarioRecebe.apply {
                    text = mensagemChatBot.mensagem
                    visibility = View.VISIBLE
                }
                holder.usuarioEnvia.visibility = View.GONE
            }
        }
    }

    fun inserirMensagem(mensagem: MensagemChatBot) {
        this.mensagemLista.add(mensagem)
        notifyItemInserted(mensagemLista.size)
        notifyDataSetChanged()
    }
}