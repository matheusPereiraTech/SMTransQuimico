package com.example.smtransquimico.view.produto

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smtransquimico.R
import com.example.smtransquimico.model.Exercito
import com.example.smtransquimico.model.Ibama
import com.example.smtransquimico.view.adapter.ListaProdutoExercitoAdapter
import com.google.firebase.database.*
import kotlinx.coroutines.launch

class ListaProdutoExercitoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListaProdutoExercitoAdapter
    private lateinit var databaseReference: DatabaseReference
    var exercitos = mutableListOf<Exercito>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_produto_exercito)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Listar Produto Exército"

            window.statusBarColor = Color.parseColor("#083087")
            initFindViewById()
            setarRecyclerView()
            databaseReference = FirebaseDatabase.getInstance().getReference("produto_exercito")
        }
    }

    private fun initFindViewById() {
        recyclerView = findViewById(R.id.recyclerlListaExercito)
    }

    private fun setarRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ListaProdutoExercitoAdapter()
        recyclerView.adapter = adapter

        adapter.setarAtualizaLista { produto ->
            val intent = Intent(this, CadastraProdutoExercitoActivity::class.java)
            intent.putExtra("Dados", produto)
            startActivity(intent)
        }

        adapter.setarDeletaLista { exercito ->
            lifecycleScope.launch {
                val produtoExercitoRef = databaseReference.child(exercito.numeroOrdem)
                produtoExercitoRef.removeValue()
            }
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