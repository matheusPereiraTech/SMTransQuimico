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
import com.example.smtransquimico.model.Policia

class ListaProdutoPrincipalAdapter :
    RecyclerView.Adapter<ListaProdutoPrincipalAdapter.ListaProdutoViewHolder>() {

    private var listaIbama = mutableListOf<Ibama>()
    private var atualizarProduto: ((Ibama) -> Unit)? = null
    private var deletarProduto: ((Ibama) -> Unit)? = null

    class ListaProdutoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val numeroONU: TextView = itemView.findViewById(R.id.txtlistaNumeroOnu)
        val nomeDescricao: TextView = itemView.findViewById(R.id.txtlistaNomeDescricao)
        val classeSubclasseRisco: TextView =
            itemView.findViewById(R.id.txtlistaClasseSubclasseRisco)
        val riscoSubsidiario: TextView = itemView.findViewById(R.id.txtlistaRiscoSubdiario)
        val numeroRisco: TextView = itemView.findViewById(R.id.txtlistatNumeroRisco)
        val grupoEmb: TextView = itemView.findViewById(R.id.txtlistaGrupoEmb)
        val provisoesEspeciais: TextView = itemView.findViewById(R.id.txtlistaProvisoesEspeciais)
        val campoAtualizar: ImageView = itemView.findViewById(R.id.imgAtualizarLista)
        val campoDeletar: ImageView = itemView.findViewById(R.id.imgExcluirLista)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaProdutoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_lista_produto, parent, false)
        return ListaProdutoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaIbama.size
    }

    fun setListaFiltrada(lIbama: List<Ibama>) {
        listaIbama = lIbama as MutableList<Ibama>
        notifyDataSetChanged()
    }

    fun setarDados(
        dadoIbama: List<Ibama>,
    ) {
        listaIbama.apply {
            clear()
            addAll(dadoIbama)
        }

        notifyDataSetChanged()
    }

    fun setarAtualizaLista(callback: (Ibama) -> Unit) {
        this.atualizarProduto = callback
    }

    fun setarDeletaLista(callback: (Ibama) -> Unit) {
        this.deletarProduto = callback
    }

    override fun onBindViewHolder(holder: ListaProdutoViewHolder, position: Int) {

        val produto = listaIbama[position]

        holder.numeroONU.text = produto.numeroONU
        holder.nomeDescricao.text = produto.nomeDescricao
        holder.classeSubclasseRisco.text = produto.classeSubclasse
        holder.riscoSubsidiario.text = produto.riscoSubsidiario
        holder.numeroRisco.text = produto.numeroRisco
        holder.grupoEmb.text = produto.grupoEmb
        holder.provisoesEspeciais.text = produto.provisoesEspeciais
        holder.campoAtualizar.setOnClickListener { atualizarProduto?.invoke(produto) }
        holder.campoDeletar.setOnClickListener {
            deletarProduto?.invoke(produto)
        }
    }
}