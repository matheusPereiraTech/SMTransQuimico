package com.example.smtransquimico.project

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smtransquimico.R
import com.example.smtransquimico.adapter.ListaProdutoAdapter
import com.example.smtransquimico.model.Produto
import kotlinx.coroutines.launch

class ListaProduto : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    private var mAdaptador: ListaProdutoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_produto)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Listar Produto"
        window.statusBarColor = Color.parseColor("#083087")
        initFindViewById()
    }

    fun initFindViewById() {
        recyclerView = findViewById(R.id.recyclerview_lista_produto)
    }

    private fun setarAdapatador(listaProduto: List<Produto>) {
        mAdaptador?.setarDados(listaProduto)
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            val todosProdutos =
                AppDatabase(this@ListaProduto).pegarProdutoDao().pegarTodosProdutodos()

            mAdaptador = ListaProdutoAdapter()

            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@ListaProduto)
                mAdaptador = ListaProdutoAdapter()
                adapter = mAdaptador
                setarAdapatador(todosProdutos)

                mAdaptador?.setarAtualizaLista {
                    val intent = Intent(this@ListaProduto, CadastraProduto::class.java)
                    intent.putExtra("Dados", it)
                    startActivity(intent)
                }
                mAdaptador?.setarDeletaLista { produto ->
                    lifecycleScope.launch {

                        AppDatabase(this@ListaProduto).pegarProdutoDao()
                            .deletarProduto(produto)
                        val lista = AppDatabase(this@ListaProduto).pegarProdutoDao()
                            .pegarTodosProdutodos()
                        setarAdapatador(lista)
                    }
                }
            }
        }
    }
}