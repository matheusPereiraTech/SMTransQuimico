package com.example.smtransquimico.view.produto

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
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
                return true
            }

        })
    }

    private fun filtroProduto(query: String?) {
        if (query != null) {
            val listaFiltrada = ArrayList<Policia>()
            for (i in policias) {
                if (i.codigo.toLowerCase(Locale.ROOT)
                        .contains(query) || i.produtoQuimico.toLowerCase(Locale.ROOT)
                        .contains(query)
                ) {
                    listaFiltrada.add(i)
                }
            }
            if (listaFiltrada.isEmpty()) {
                Toast.makeText(this, "Produto não encontrado", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setListaFiltrada(listaFiltrada)
            }
        }
    }


    private fun setarRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ListaProdutoPoliciaAdapter()
        recyclerView.adapter = adapter

        adapter.setarAtualizaLista { produto ->
            val intent = Intent(this, CadastraProdutoPoliciaActivity::class.java)
            intent.putExtra("Dados", produto)
            startActivity(intent)
        }

        adapter.setarDeletaLista { policia ->
            lifecycleScope.launch {
                val produtoRef = databaseReference.child(policia.codigo)
                produtoRef.removeValue()
            }
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