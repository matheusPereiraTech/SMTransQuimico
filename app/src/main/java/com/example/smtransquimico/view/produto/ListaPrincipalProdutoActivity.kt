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
import com.example.smtransquimico.model.Exercito
import com.example.smtransquimico.model.ANTT
import com.example.smtransquimico.model.Policia
import com.example.smtransquimico.view.adapter.ListaProdutoPrincipalAdapter
import com.google.firebase.database.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class ListaPrincipalProdutoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var barraPesquisa: SearchView
    private lateinit var adapter: ListaProdutoPrincipalAdapter
    private lateinit var databaseReferenceANTT: DatabaseReference
    private lateinit var databaseReferencePolicia: DatabaseReference
    private lateinit var databaseReferenceExercito: DatabaseReference
    var antts = mutableListOf<ANTT>()
    var policias = mutableListOf<Policia>()
    var execercitos = mutableListOf<Exercito>()

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
        databaseReferenceANTT = FirebaseDatabase.getInstance().getReference("produtos")
        databaseReferencePolicia = FirebaseDatabase.getInstance().getReference("produto_policia")
        databaseReferenceExercito = FirebaseDatabase.getInstance().getReference("produto_exercito")
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
            val listaFiltrada = ArrayList<ANTT>()
            for (i in antts) {
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
                val produtoRef = databaseReferenceANTT.child(produto.numeroONU)
                produtoRef.removeValue()

            }
        }
    }

    fun setDadosFirebaseExercito() {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                execercitos = mutableListOf<Exercito>()
                for (childSnapshot in dataSnapshot.children) {
                    val exercito = childSnapshot.getValue(Exercito::class.java)
                    exercito?.let { execercitos.add(it) }
                }

                adapter.recebeListaExercito(execercitos)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Trate a falha aqui
            }
        }

        databaseReferenceExercito.addValueEventListener(valueEventListener)
    }

    fun setDadosFirebasePolicia() {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                policias = mutableListOf<Policia>()
                for (childSnapshot in dataSnapshot.children) {
                    val policia = childSnapshot.getValue(Policia::class.java)
                    policia?.let { policias.add(it) }
                }
                adapter.recebeListaPolicia(policias)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Trate a falha aqui
            }
        }

        databaseReferencePolicia.addValueEventListener(valueEventListener)
    }

    override fun onResume() {
        super.onResume()
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                antts = mutableListOf<ANTT>()
                for (childSnapshot in dataSnapshot.children) {
                    val antt = childSnapshot.getValue(ANTT::class.java)
                    antt?.let { antts.add(it) }
                }
                antts.sortBy { it.numeroONU }

                adapter.setarDados(antts)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Trate a falha aqui
            }
        }

        databaseReferenceANTT.addValueEventListener(valueEventListener)

        setDadosFirebasePolicia()
        setDadosFirebaseExercito()
    }
}