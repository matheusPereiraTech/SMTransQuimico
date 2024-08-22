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
import com.example.smtransquimico.model.Exercito
import com.example.smtransquimico.view.adapter.ListaProdutoExercitoAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import org.apache.commons.codec.language.DoubleMetaphone
import java.util.Locale

class ListaProdutoExercitoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListaProdutoExercitoAdapter
    private lateinit var barraPesquisa: SearchView
    private lateinit var databaseReference: DatabaseReference
    var exercitos = mutableListOf<Exercito>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_produto_exercito)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Listar Produto Exército"

            window.statusBarColor = Color.parseColor("#083087")
            iniciandoComponentes()
            setarRecyclerView()
            databaseReference = FirebaseDatabase.getInstance().getReference("produto_exercito")

            setarBarraPesquisa()
        }
    }

    private fun setarBarraPesquisa() {
        barraPesquisa.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filtroNumeroOrdem(newText)
                filtroNomenclatura(newText)
                return true
            }

        })
    }

    private fun filtroNumeroOrdem(query: String?) {
        if (query != null) {
            val listaFiltrada = ArrayList<Exercito>()
            for (i in exercitos) {
                if (i.numeroOrdem.toLowerCase(Locale.ROOT)
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

    private fun filtroNomenclatura(query: String?) {
        val doubleMetaphone = DoubleMetaphone()
        val queryEncoded = query?.let { doubleMetaphone.encode(it) }
        val lFiltered = ArrayList<Exercito>()

        if (query.isNullOrEmpty()) {
            adapter.setListaFiltrada(exercitos)
            return
        }

        if (!queryEncoded.isNullOrEmpty()) {
            for (item in exercitos) {
                if (doubleMetaphone.encode(item.nomenclatura).contains(queryEncoded)) {
                    lFiltered.add(item)
                }
            }

            if (lFiltered.isNotEmpty()) {
                adapter.setListaFiltrada(lFiltered)
            }
        }
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar exclusão")
        builder.setMessage("Tem certeza que deseja excluir este item?")
        builder.setPositiveButton("Sim") { dialog, _ ->
            val deletedItem = adapter.getItem(position)
            adapter.removeItem(position)

            lifecycleScope.launch {
                databaseReference.child(deletedItem.numeroOrdem).removeValue()
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

    private fun setTouchSwipe(adapter: ListaProdutoExercitoAdapter) {
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

    private fun iniciandoComponentes() {
        recyclerView = findViewById(R.id.recyclerlListaExercito)
        barraPesquisa = findViewById(R.id.barraPesquisaProdutoExercito)
    }

    private fun setarRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ListaProdutoExercitoAdapter()
        recyclerView.adapter = adapter

        setTouchSwipe(adapter)

        adapter.setarAtualizaLista { produto ->
            val intent = Intent(this, CadastraProdutoExercitoActivity::class.java)
            intent.putExtra("Dados", produto)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                exercitos = mutableListOf<Exercito>()
                for (childSnapshot in dataSnapshot.children) {
                    val exercito = childSnapshot.getValue(Exercito::class.java)
                    exercito?.let { exercitos.add(it) }
                }
                exercitos.sortBy { it.numeroOrdem }
                adapter.setarDados(exercitos)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Trate a falha aqui
            }
        }

        databaseReference.addValueEventListener(valueEventListener)
    }

}