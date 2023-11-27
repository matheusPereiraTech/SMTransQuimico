package com.example.smtransquimico.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smtransquimico.R
import com.example.smtransquimico.model.Exercito
import com.example.smtransquimico.model.Ibama

class ListaProdutoExercitoAdapter : RecyclerView.Adapter<ListaProdutoExercitoAdapter.ListaProdutoExecitoViewHolder>() {

    private var listaExercito = mutableListOf<Exercito>()
    private var atualizarProduto: ((Exercito) -> Unit)? = null
    private var deletarProduto: ((Exercito) -> Unit)? = null

    class ListaProdutoExecitoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val numeroOrdem: TextView = itemView.findViewById(R.id.txtlistaNumeroOrdem)
        val nomenclatura: TextView = itemView.findViewById(R.id.txtlistaNomenclatura)
        val tipoPCE: TextView = itemView.findViewById(R.id.txtlistaPCE)
        val campoAtualizar: ImageView = itemView.findViewById(R.id.imgAtualizarListaExercito)
        val campoDeletar: ImageView = itemView.findViewById(R.id.imgExcluirListaExercito)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListaProdutoExecitoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_lista_exercito, parent, false)
        return ListaProdutoExercitoAdapter.ListaProdutoExecitoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaExercito.size
    }

    fun setarDados(dados: List<Exercito>) {
        listaExercito.apply {
            clear()
            addAll(dados)
        }
        notifyDataSetChanged()
    }

    fun setarAtualizaLista(callback: (Exercito) -> Unit) {
        this.atualizarProduto = callback
    }

    fun setarDeletaLista(callback: (Exercito) -> Unit) {
        this.deletarProduto = callback
    }

    override fun onBindViewHolder(holder: ListaProdutoExecitoViewHolder, position: Int) {
        val exercito = listaExercito[position]
        holder.numeroOrdem.text = exercito.numeroOrdem
        holder.nomenclatura.text = exercito.nomenclatura
        holder.tipoPCE.text = exercito.tipoPCE
        holder.campoAtualizar.setOnClickListener { atualizarProduto?.invoke(exercito) }
        holder.campoDeletar.setOnClickListener {
            deletarProduto?.invoke(exercito)
        }
    }
}