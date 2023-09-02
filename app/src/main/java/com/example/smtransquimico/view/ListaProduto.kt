package com.example.smtransquimico.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.smtransquimico.R

class ListaProduto : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager:LayoutManager
    lateinit var listaProdutoAdapter: ListaProdutoAdapter
    var listaModelo: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_produto)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Listar Produto"
        window.statusBarColor = Color.parseColor("#083087")
        initFindViewById()
        setarRecyclerView()
    }

    fun initFindViewById(){
        recyclerView = findViewById(R.id.recyclerview_lista_produto)
    }

    fun setarRecyclerView(){
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        for (i in 0 until 10) {
           listaModelo.add("Modelo$i")
        }

        listaProdutoAdapter = ListaProdutoAdapter(listaModelo)
        recyclerView.adapter = listaProdutoAdapter
    }
}