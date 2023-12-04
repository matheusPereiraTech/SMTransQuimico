package com.example.smtransquimico.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smtransquimico.R
import com.example.smtransquimico.model.Policia

class ListaProdutoPoliciaAdapter :
    RecyclerView.Adapter<ListaProdutoPoliciaAdapter.ListaProdutoPoliciaViewHolder>() {

    private var listaPolicia = mutableListOf<Policia>()
    private var atualizarProduto: ((Policia) -> Unit)? = null
    private var deletarProduto: ((Policia) -> Unit)? = null

    class ListaProdutoPoliciaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val codigo: TextView = itemView.findViewById(R.id.txtlistaCodigo)
        val codigoProduto: TextView = itemView.findViewById(R.id.txtlistaProdutoQumico)
        val campoAtualizar: ImageView = itemView.findViewById(R.id.imgAtualizarListaPolicia)
        val campoDeletar: ImageView = itemView.findViewById(R.id.imgExcluirListaPolicia)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListaProdutoPoliciaViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_lista_policia, parent, false)
        return ListaProdutoPoliciaAdapter.ListaProdutoPoliciaViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaPolicia.size
    }

    fun setarDados(dados: List<Policia>) {
        listaPolicia.apply {
            clear()
            addAll(dados)
        }
        notifyDataSetChanged()
    }

    fun setarAtualizaLista(callback: (Policia) -> Unit) {
        this.atualizarProduto = callback
    }

    fun setarDeletaLista(callback: (Policia) -> Unit) {
        this.deletarProduto = callback
    }

    fun setListaFiltrada(lPolicia: List<Policia>) {
        listaPolicia = lPolicia as MutableList<Policia>
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ListaProdutoPoliciaViewHolder, position: Int) {
        val policia = listaPolicia[position]
        holder.codigo.text = policia.codigo
        holder.codigoProduto.text = policia.produtoQuimico
        holder.campoAtualizar.setOnClickListener { atualizarProduto?.invoke(policia) }
        holder.campoDeletar.setOnClickListener {
            deletarProduto?.invoke(policia)
        }
    }
}