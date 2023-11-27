package com.example.smtransquimico.view.produto

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smtransquimico.R
import com.example.smtransquimico.model.Ibama
import com.example.smtransquimico.model.Policia
import com.example.smtransquimico.view.adapter.ListaProdutoPoliciaAdapter
import com.google.firebase.database.*
import kotlinx.coroutines.launch

class ListaProdutoPoliciactivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListaProdutoPoliciaAdapter
    private lateinit var databaseReference: DatabaseReference
    var policias = mutableListOf<Policia>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_produto_policia)


        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Lista Produto Polícia"
        }

        window.statusBarColor = Color.parseColor("#083087")
        initFindViewById()
        setarRecyclerView()
        databaseReference = FirebaseDatabase.getInstance().getReference("produto_policia")
    }

    private fun initFindViewById() {
        recyclerView = findViewById(R.id.recyclerlListaPolicia)
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

    fun enviarListaPoliciaDados(){
        
    }

}