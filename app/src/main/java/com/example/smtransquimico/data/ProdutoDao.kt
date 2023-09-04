package com.example.smtransquimico.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.smtransquimico.model.Produto

@Dao
interface ProdutoDao {
    @Insert
    suspend fun adicionaProduto(produto: Produto)

    @Query("SELECT * FROM produto ORDER BY id DESC")
    suspend fun pegarTodosProdutodos(): List<Produto>

    @Update
    suspend fun atualizarProduto(produto: Produto)

    @Delete
    suspend fun deletarProduto(produto: Produto)
}