package com.example.smtransquimico.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smtransquimico.R
import com.example.smtransquimico.model.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView

class ChatMensagemAdapter(
    private val context: Context,
    private val chatLista: ArrayList<Chat>
) : RecyclerView.Adapter<ChatMensagemAdapter.ViewHolder>() {

    private val MESSAGEM_TIPO_ESQUERDO = 0
    private val MESSAGEM_TIPO_DIREITO = 1
    private var firebaseUser: FirebaseUser? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textoUsuario: TextView = view.findViewById(R.id.txtMensagemItem)
        val imagemUsuario : CircleImageView = view.findViewById(R.id.imgChatItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if (viewType == MESSAGEM_TIPO_DIREITO) {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_lado_direito, parent, false)
            return ViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_lado_esquerdo, parent, false)
            return ViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return chatLista.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chatLista[position]
        holder.textoUsuario.text = chat.message
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        return if (chatLista[position].senderId == firebaseUser!!.uid) {
            MESSAGEM_TIPO_DIREITO
        } else {
            MESSAGEM_TIPO_ESQUERDO
        }
    }
}