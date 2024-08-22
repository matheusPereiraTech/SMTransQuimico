package com.example.smtransquimico.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smtransquimico.R
import com.example.smtransquimico.model.ANTT
import com.example.smtransquimico.model.Exercito
import com.example.smtransquimico.model.Policia

class ListProduceMainAdapter :
    RecyclerView.Adapter<ListProduceMainAdapter.ListProduceViewHolder>() {

    private var mtListAntt = mutableListOf<ANTT>()
    private var mtListPolice = mutableListOf<Policia>()
    private var mtListArmy = mutableListOf<Exercito>()
    private var updateProduce: ((ANTT) -> Unit)? = null

    class ListProduceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val OnuNumber: TextView = itemView.findViewById(R.id.txtlistaNumeroOnu)
        val Descriptionname: TextView = itemView.findViewById(R.id.txtlistaNomeDescricao)
        val RiskclassAndSubclass: TextView = itemView.findViewById(R.id.txtlistaClasseSubclasseRisco)
        val subsidiaryRisk: TextView = itemView.findViewById(R.id.txtlistaRiscoSubdiario)
        val RiskNumber: TextView = itemView.findViewById(R.id.txtlistatNumeroRisco)
        val groupEmb: TextView = itemView.findViewById(R.id.txtlistaGrupoEmb)
        val correlation: TextView = itemView.findViewById(R.id.textoCorrelacaoProduto)
        val specialProvisions: TextView = itemView.findViewById(R.id.txtlistaProvisoesEspeciais)
        val fieldUpdate: ImageView = itemView.findViewById(R.id.imgAtualizarLista)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListProduceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lista_produto, parent, false)
        return ListProduceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mtListAntt.size
    }

    fun setListFiltered(lANTT: List<ANTT>) {
        mtListAntt = lANTT as MutableList<ANTT>
        notifyDataSetChanged()
    }

    fun setData(dadoANTT: List<ANTT>) {
        mtListAntt.apply {
            clear()
            addAll(dadoANTT)
        }
        notifyDataSetChanged()
    }

    fun setUpdateList(callback: (ANTT) -> Unit) {
        this.updateProduce = callback
    }

    fun getListPolice(dadoPolicia: MutableList<Policia>) {
        mtListPolice.apply {
            clear()
            addAll(dadoPolicia)
            notifyDataSetChanged()
        }
    }

    fun getListArmy(dadoExercito: MutableList<Exercito>) {
        mtListArmy.apply {
            clear()
            addAll(dadoExercito)
            notifyDataSetChanged()
        }
    }

    fun getItem(position: Int): ANTT {
        return mtListAntt[position]
    }

    fun removeItem(position: Int) {
        mtListAntt.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onBindViewHolder(holder: ListProduceViewHolder, position: Int) {
        val item = mtListAntt[position]

        val correlacaoPolicia = mtListPolice.any { it.produtoQuimico == item.nomeDescricao }
        val correlacaoExercito = mtListArmy.any { it.nomenclatura == item.nomeDescricao }

        holder.correlation.text = when {
            correlacaoPolicia && correlacaoExercito -> "PF e EX"
            correlacaoPolicia -> "PF"
            correlacaoExercito -> "EX"
            else -> ""
        }

        holder.OnuNumber.text = item.numeroONU
        holder.Descriptionname.text = item.nomeDescricao
        holder.RiskclassAndSubclass.text = item.classeSubclasse
        holder.subsidiaryRisk.text = item.riscoSubsidiario
        holder.RiskNumber.text = item.numeroRisco
        holder.groupEmb.text = item.grupoEmb
        holder.specialProvisions.text = item.provisoesEspeciais
        holder.fieldUpdate.setOnClickListener { updateProduce?.invoke(item) }
    }
}
