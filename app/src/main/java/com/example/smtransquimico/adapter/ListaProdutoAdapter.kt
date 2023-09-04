package com.example.smtransquimico.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smtransquimico.R
import com.example.smtransquimico.model.Produto

class ListaProdutoAdapter : RecyclerView.Adapter<ListaProdutoAdapter.ListaProdutoViewHolder>() {

    private var listaProduto = mutableListOf<Produto>()
    private var atualizar: ((Produto) -> Unit)? = null
    private var deletar: ((Produto) -> Unit)? = null

    class ListaProdutoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val codigoProduto: TextView = itemView.findViewById(R.id.linha_codigo_produto)
        val nomeProduto: TextView = itemView.findViewById(R.id.linha_nome_produto)
        val tipoProduto: TextView = itemView.findViewById(R.id.linha_tipo_produto)
        val campoAtualizar: ImageView = itemView.findViewById(R.id.img_atualizar_lista)
        val campoDeletar: ImageView = itemView.findViewById(R.id.img_excluir_lista)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaProdutoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.linha_lista_produto, parent, false)
        return ListaProdutoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaProduto.size
    }

    fun setarDados(dados: List<Produto>) {
        listaProduto.apply {
            clear()
            addAll(dados)
        }
    }

    fun setarAtualizaLista(callback: (Produto) -> Unit) {
        this.atualizar = callback
    }

    fun setarDeletaLista(callback: (Produto) -> Unit) {
        this.deletar = callback
    }


    override fun onBindViewHolder(holder: ListaProdutoViewHolder, position: Int) {
        val produto = listaProduto[position]
        holder.codigoProduto.text = produto.codigoProduto
        holder.nomeProduto.text = produto.nomeProduto
        holder.tipoProduto.text = produto.tipoProduto
        holder.campoAtualizar.setOnClickListener { atualizar?.invoke(produto) }
        holder.campoDeletar.setOnClickListener {
            deletar?.invoke(produto)
            listaProduto.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}