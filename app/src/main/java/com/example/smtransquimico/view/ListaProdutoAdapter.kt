package com.example.smtransquimico.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smtransquimico.R

class ListaProdutoAdapter(val listaModelo: ArrayList<String>) : RecyclerView.Adapter<ListaProdutoAdapter.ListaProdutoViewHolder>() {

    class ListaProdutoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textoModelo: TextView = itemView.findViewById(R.id.texto_modelo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaProdutoViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.linha_lista_produto,parent,false)
        return ListaProdutoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaModelo.size
    }

    override fun onBindViewHolder(holder: ListaProdutoViewHolder, position: Int) {
        val modelo = listaModelo[position]
        holder.textoModelo.text = modelo
    }
}