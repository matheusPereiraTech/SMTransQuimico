package com.example.smtransquimico.view.produto

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smtransquimico.R
import com.example.smtransquimico.model.Policia
import com.example.smtransquimico.view.adapter.ListaProdutoPoliciaAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import org.apache.commons.codec.language.DoubleMetaphone
import java.util.Locale

class ListaProdutoPoliciactivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListaProdutoPoliciaAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var barraPesquisa: SearchView

    var policias = mutableListOf<Policia>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_produto_policia)


        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Lista Produto Polícia"
        }

        window.statusBarColor = Color.parseColor("#083087")
        iniciandoComponentes()
        setarRecyclerView()
        databaseReference = FirebaseDatabase.getInstance().getReference("produto_policia")
        setarBarraPesquisa()
    }

    private fun iniciandoComponentes() {
        recyclerView = findViewById(R.id.recyclerlListaPolicia)
        barraPesquisa = findViewById(R.id.barraPesquisaProdutoPolicia)
    }

    private fun setarBarraPesquisa() {
        barraPesquisa.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filtroProduto(newText)
                filtrarProdutoQuimico(newText)
                return true
            }

        })
    }

    private fun filtroProduto(query: String?) {
        val listaFiltrada = ArrayList<Policia>()
        if (query != null) {
            for (i in policias) {
                if (i.codigo.toLowerCase(Locale.ROOT)
                        .contains(query)
                ) {
                    listaFiltrada.add(i)
                }
            }
            if (listaFiltrada.isNotEmpty()) {
                adapter.setListaFiltrada(listaFiltrada)
            }
        }
    }

    private fun filtrarProdutoQuimico(query: String?) {
        val doubleMetaphone = DoubleMetaphone()
        val queryEncoded = query?.let { doubleMetaphone.encode(it) }
        val lFiltered = ArrayList<Policia>()

        if (query.isNullOrEmpty()) {
            adapter.setListaFiltrada(policias)
            return
        }

        if (!queryEncoded.isNullOrEmpty()) {
            for (item in policias) {
                val codigoEncoded = doubleMetaphone.encode(item.codigo)

                if (codigoEncoded.contains(queryEncoded)) {
                    lFiltered.add(item)
                }
            }

            if (lFiltered.isNotEmpty()) {
                adapter.setListaFiltrada(lFiltered)
            }
        }
    }

    private fun setTouchSwipe() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                showDeleteConfirmationDialog(position)
            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar exclusão")
        builder.setMessage("Tem certeza que deseja excluir este item?")
        builder.setPositiveButton("Sim") { dialog, _ ->
            val deletedItem = adapter.getItem(position)
            adapter.removeItem(position)

            lifecycleScope.launch {
                databaseReference.child(deletedItem.codigo).removeValue()
            }

            dialog.dismiss()
        }
        builder.setNegativeButton("Não") { dialog, _ ->
            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }


    private fun setarRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ListaProdutoPoliciaAdapter()
        recyclerView.adapter = adapter

        setTouchSwipe()

        adapter.setarAtualizaLista { produto ->
            val intent = Intent(this, CadastraProdutoPoliciaActivity::class.java)
            intent.putExtra("Dados", produto)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                policias = mutableListOf<Policia>()
                for (childSnapshot in dataSnapshot.children) {
                    val policia = childSnapshot.getValue(Policia::class.java)
                    policia?.let { policias.add(it) }
                }
                policias.sortBy { it.codigo }


                adapter.setarDados(policias)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Trate a falha aqui
            }
        }

        databaseReference.addValueEventListener(valueEventListener)

    }
}