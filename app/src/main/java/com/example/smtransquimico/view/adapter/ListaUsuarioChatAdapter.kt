package com.example.smtransquimico.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smtransquimico.R
import com.example.smtransquimico.model.Usuario
import com.example.smtransquimico.view.chat.ChatActivity
import de.hdodenhof.circleimageview.CircleImageView

class ListaUsuarioChatAdapter(
    private val context: Context,
    private val listaUsuario: ArrayList<Usuario>,
) : RecyclerView.Adapter<ListaUsuarioChatAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textoUsuario: TextView = view.findViewById(R.id.txtNomeUsuarioChat)
        val imagemUsuario: CircleImageView = view.findViewById(R.id.imgUsuarioChat)
        val layoutUsuario: LinearLayout = view.findViewById(R.id.layoutUsuario)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_usuario_chat, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaUsuario.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usuario = listaUsuario[position]
        holder.textoUsuario.text = usuario.userName

        Glide.with(context).load(usuario.profileImage).placeholder(R.drawable.imagem_perfil)
            .into(holder.imagemUsuario)

        holder.layoutUsuario.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("userId", usuario.userId)
            intent.putExtra("userName", usuario.userName)
            context.startActivity(intent)
        }
    }
}