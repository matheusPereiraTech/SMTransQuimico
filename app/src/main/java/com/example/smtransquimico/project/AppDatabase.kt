package com.example.smtransquimico.project

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smtransquimico.model.Produto

@Database(entities = [Produto::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pegarProdutoDao(): ProdutoDao

    companion object {
        @Volatile
        private var instancia: AppDatabase? = null
        private val trancado = Any()

        operator fun invoke(context: Context) = instancia ?: synchronized(trancado) {
            instancia ?: buildDatabase(context).also {
                instancia = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app-database"
        ).build()
    }
}