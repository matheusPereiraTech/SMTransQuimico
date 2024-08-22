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
import com.example.smtransquimico.model.ANTT
import com.example.smtransquimico.model.Exercito
import com.example.smtransquimico.model.Policia
import com.example.smtransquimico.view.adapter.ListProduceMainAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import org.apache.commons.codec.language.DoubleMetaphone
import java.util.Locale


class ListMainProduceActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchTab: SearchView
    private lateinit var adapter: ListProduceMainAdapter
    private lateinit var dbRefANTT: DatabaseReference
    private lateinit var dbRefPolicia: DatabaseReference
    private lateinit var dbRefExercito: DatabaseReference
    private var mtListAntt = mutableListOf<ANTT>()
    private var mtListPolice = mutableListOf<Policia>()
    private var mtListArmy = mutableListOf<Exercito>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_produce)

        settingsActionBar()
        initFindViewById()
        setRecyclerView()
        getNewInstanceTablesFirebase()
        setSearchTab()
    }

    private fun settingsActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Listar Produto"
        }
        window.statusBarColor = Color.parseColor("#083087")
    }

    private fun getNewInstanceTablesFirebase() {
        dbRefANTT = FirebaseDatabase.getInstance().getReference("produtos")
        dbRefPolicia = FirebaseDatabase.getInstance().getReference("produto_policia")
        dbRefExercito = FirebaseDatabase.getInstance().getReference("produto_exercito")
    }

    private fun setSearchTab() {
        searchTab.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterProduceName(newText)
                filterProduceNumberOnu(newText)
                return true
            }

        })
    }

    private fun filterProduceNumberOnu(query: String?) {
        if (query != null) {
            val listaFiltrada = ArrayList<ANTT>()
            for (i in mtListAntt) {
                if (i.numeroONU.toLowerCase(Locale.ROOT)
                        .contains(query)
                ) {
                    listaFiltrada.add(i)
                }
            }
            if (listaFiltrada.isNotEmpty()) {
                adapter.setListFiltered(listaFiltrada)
            }
        }
    }

    private fun initFindViewById() {
        recyclerView = findViewById(R.id.recyclerlListaProduto)
        searchTab = findViewById(R.id.barraPesquisaProduto)
    }

    private fun setRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ListProduceMainAdapter()
        recyclerView.adapter = adapter

        setTouchSwipe()

        adapter.setUpdateList {
            val intent = Intent(this, CadastraProdutoPrincipalActivity::class.java)
            intent.putExtra("Dados", it)
            startActivity(intent)
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
                dbRefANTT.child(deletedItem.numeroONU).removeValue()
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

    private fun setTouchSwipe() {
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

    private fun setDataArmyFirebase() {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mtListArmy = mutableListOf<Exercito>()
                for (childSnapshot in dataSnapshot.children) {
                    childSnapshot.getValue(Exercito::class.java)?.let {
                        mtListArmy.add(it)
                    }
                }
                adapter.getListArmy(mtListArmy)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Trate a falha aqui
            }
        }

        dbRefExercito.addValueEventListener(valueEventListener)
    }

    private fun setDataPoliceFirebase() {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mtListPolice = mutableListOf<Policia>()
                for (childSnapshot in dataSnapshot.children) {
                    childSnapshot.getValue(Policia::class.java)?.let {
                        mtListPolice.add(it)
                    }
                }
                adapter.getListPolice(mtListPolice)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Trate a falha aqui
            }
        }
        dbRefPolicia.addValueEventListener(valueEventListener)
    }

    override fun onResume() {
        super.onResume()
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mtListAntt = mutableListOf<ANTT>()
                for (childSnapshot in dataSnapshot.children) {
                    childSnapshot.getValue(ANTT::class.java)?.let {
                        mtListAntt.add(it)
                    }
                }
                mtListAntt.sortBy { it.numeroONU }
                adapter.setData(mtListAntt)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Trate a falha aqui
            }
        }
        dbRefANTT.addValueEventListener(valueEventListener)

        setDataPoliceFirebase()
        setDataArmyFirebase()
    }

    private fun filterProduceName(query: String?) {
        val lFiltered = ArrayList<ANTT>()
        val doubleMetaphone = DoubleMetaphone()

        if (query.isNullOrEmpty()) {
            adapter.setListFiltered(mtListAntt)
            return
        }

        val normalizedQuery = normalizeText(query)
        val queryWords = normalizedQuery.split(" ")

        val queryEncoded = doubleMetaphone.encode(normalizedQuery)

        for (item in mtListAntt) {
            val normalizedNomeDescricao = normalizeText(item.nomeDescricao)

            val nomeDescricaoEncoded = doubleMetaphone.encode(normalizedNomeDescricao)

            val containsAnyWord = queryWords.any { normalizedNomeDescricao.contains(it) }
            val isFoneticallySimilar = nomeDescricaoEncoded == queryEncoded || nomeDescricaoEncoded.startsWith(queryEncoded)

            val containsQueryParts = queryWords.any { word -> normalizedNomeDescricao.contains(word) }

            if (containsAnyWord || isFoneticallySimilar || containsQueryParts) {
                lFiltered.add(item)
            }
        }

        adapter.setListFiltered(lFiltered)
    }

    private fun normalizeText(text: String): String {
        return text
            .lowercase(Locale.getDefault())
            .replace(Regex("[áàãâä]"), "a")
            .replace(Regex("[éèẽêë]"), "e")
            .replace(Regex("[íìîï]"), "i")
            .replace(Regex("[óòõôö]"), "o")
            .replace(Regex("[úùûü]"), "u")
            .replace(Regex("[ç]"), "c")
            .replace(Regex("[\\p{M}]"), "")
    }

}