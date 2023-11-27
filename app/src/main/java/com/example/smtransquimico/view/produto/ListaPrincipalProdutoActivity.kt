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
import com.example.smtransquimico.model.Ibama
import com.example.smtransquimico.view.adapter.ListaProdutoPrincipalAdapter
import com.google.firebase.database.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class ListaPrincipalProdutoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var barraPesquisa: SearchView
    private lateinit var adapter: ListaProdutoPrincipalAdapter
    private lateinit var databaseReferenceIbama: DatabaseReference
    var ibamas = mutableListOf<Ibama>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_produto)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Listar Produto"
        }

        window.statusBarColor = Color.parseColor("#083087")
        initFindViewById()
        setarRecyclerView()
        databaseReferenceIbama = FirebaseDatabase.getInstance().getReference("produtos")
        setarBarraPesquisa()
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
            val listaFiltrada = ArrayList<Ibama>()
            for (i in ibamas) {
                if (i.numeroONU.toLowerCase(Locale.ROOT)
                        .contains(query) || i.nomeDescricao.toLowerCase(Locale.ROOT).contains(query)
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

    private fun initFindViewById() {
        recyclerView = findViewById(R.id.recyclerlListaProduto)
        barraPesquisa = findViewById(R.id.barraPesquisaProduto)
    }

    private fun setarRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ListaProdutoPrincipalAdapter()
        recyclerView.adapter = adapter

        adapter.setarAtualizaLista { produto ->
            val intent = Intent(this, CadastraProdutoPrincipalActivity::class.java)
            intent.putExtra("Dados", produto)
            startActivity(intent)
        }

        adapter.setarDeletaLista { produto ->
            lifecycleScope.launch {
                val produtoRef = databaseReferenceIbama.child(produto.numeroONU)
                produtoRef.removeValue()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                ibamas = mutableListOf<Ibama>()
                for (childSnapshot in dataSnapshot.children) {
                    val ibama = childSnapshot.getValue(Ibama::class.java)
                    ibama?.let { ibamas.add(it) }
                }
                ibamas.sortBy { it.numeroONU }

                adapter.setarDados(ibamas)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Trate a falha aqui
            }
        }

        databaseReferenceIbama.addValueEventListener(valueEventListener)
    }
}