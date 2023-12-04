package com.example.smtransquimico.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smtransquimico.R
import com.example.smtransquimico.constants.CorrelacaoProdutos
import com.example.smtransquimico.model.ANTT
import com.example.smtransquimico.model.Exercito
import com.example.smtransquimico.model.Policia

class ListaProdutoPrincipalAdapter :
    RecyclerView.Adapter<ListaProdutoPrincipalAdapter.ListaProdutoViewHolder>() {

    private var listaANTT = mutableListOf<ANTT>()
    private var listaPolicia = mutableListOf<Policia>()
    private var listaExercito = mutableListOf<Exercito>()
    private var atualizarProduto: ((ANTT) -> Unit)? = null
    private var deletarProduto: ((ANTT) -> Unit)? = null

    class ListaProdutoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val numeroONU: TextView = itemView.findViewById(R.id.txtlistaNumeroOnu)
        val nomeDescricao: TextView = itemView.findViewById(R.id.txtlistaNomeDescricao)
        val classeSubclasseRisco: TextView =
            itemView.findViewById(R.id.txtlistaClasseSubclasseRisco)
        val riscoSubsidiario: TextView = itemView.findViewById(R.id.txtlistaRiscoSubdiario)
        val numeroRisco: TextView = itemView.findViewById(R.id.txtlistatNumeroRisco)
        val grupoEmb: TextView = itemView.findViewById(R.id.txtlistaGrupoEmb)
        val correlacao: TextView = itemView.findViewById(R.id.textoCorrelacaoProduto)
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
        return listaANTT.size
    }

    fun setListaFiltrada(lANTT: List<ANTT>) {
        listaANTT = lANTT as MutableList<ANTT>
        notifyDataSetChanged()
    }

    fun setarDados(
        dadoANTT: List<ANTT>,
    ) {
        listaANTT.apply {
            clear()
            addAll(dadoANTT)
        }

        notifyDataSetChanged()
    }

    fun setarAtualizaLista(callback: (ANTT) -> Unit) {
        this.atualizarProduto = callback
    }

    fun setarDeletaLista(callback: (ANTT) -> Unit) {
        this.deletarProduto = callback
    }

    fun recebeListaPolicia(dadoPolicia: MutableList<Policia>) {
        listaPolicia.apply {
            clear()
            addAll(dadoPolicia)
            notifyDataSetChanged()
        }
    }

    fun recebeListaExercito(dadoExercito: MutableList<Exercito>) {
        listaExercito.apply {
            clear()
            addAll(dadoExercito)
            notifyDataSetChanged()
        }
    }


    override fun onBindViewHolder(holder: ListaProdutoViewHolder, position: Int) {

        val produto = listaANTT[position]

        val correlacaoPolicia =
            CorrelacaoProdutos.verificarCorrelacaoPolicia(produto.nomeDescricao, listaPolicia)
        val correlacaoExercito =
            CorrelacaoProdutos.verificarCorrelacaoExercito(produto.nomeDescricao, listaExercito)

        holder.correlacao.text = ""

        if (correlacaoPolicia) {
            holder.correlacao.text = "PF"
        }

        if (correlacaoExercito) {
            holder.correlacao.text = "EX"
        }

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