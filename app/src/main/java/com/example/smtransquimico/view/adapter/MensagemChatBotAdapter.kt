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

    var mtListMessage = mutableListOf<MensagemChatBot>()

    inner class MensagemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userSended: TextView = itemView.findViewById(R.id.usuarioEnvia)
        val userReceived: TextView = itemView.findViewById(R.id.usuarioRecebe)

        init {
            itemView.setOnClickListener {
                mtListMessage.removeAt(adapterPosition)
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

    override fun getItemCount(): Int = mtListMessage.size

    override fun onBindViewHolder(holder: MensagemViewHolder, position: Int) {
        when (mtListMessage[position].id) {
            ENVIAR_ID -> {
                holder.userSended.apply {
                    text = mtListMessage[position].mensagem.toString()
                    visibility = View.VISIBLE
                }

                holder.userReceived.visibility = View.GONE
            }

            RECEBER_ID -> {
                holder.userReceived.apply {
                    text = mtListMessage[position].mensagem
                    visibility = View.VISIBLE
                }
                holder.userSended.visibility = View.GONE
            }
        }
    }

    fun insertMessage(message: MensagemChatBot) {
        this.mtListMessage.add(message)
        notifyItemInserted(mtListMessage.size)
        notifyDataSetChanged()
    }
}