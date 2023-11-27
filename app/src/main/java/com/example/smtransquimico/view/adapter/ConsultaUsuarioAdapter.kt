package com.example.smtransquimico.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smtransquimico.R
import com.example.smtransquimico.model.Usuario

class ConsultaUsuarioAdapter(
    private val context: Context,
    private val listaConsultaUsuario: ArrayList<Usuario>
) : RecyclerView.Adapter<ConsultaUsuarioAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNomeConsultaUsuario: TextView = view.findViewById(R.id.txtConsultaNomeUsuario)
        val txtTelefoneConsultausuario: TextView = view.findViewById(R.id.txtConsultaTelefoneUsuario)
        val txtEmailConsultaUsuario: TextView = view.findViewById(R.id.txtConsultaEmailUsuario)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ConsultaUsuarioAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_consulta_usuario, parent, false)
        return ConsultaUsuarioAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaConsultaUsuario.size
    }

    override fun onBindViewHolder(holder: ConsultaUsuarioAdapter.ViewHolder, position: Int) {
        val usuario = listaConsultaUsuario[position]
        holder.txtNomeConsultaUsuario.text = usuario.userName
        holder.txtTelefoneConsultausuario.text = usuario.phone
        holder.txtEmailConsultaUsuario.text = usuario.email
    }

}