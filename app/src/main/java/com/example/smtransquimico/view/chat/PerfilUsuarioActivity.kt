package com.example.smtransquimico.view.chat

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.smtransquimico.R
import com.example.smtransquimico.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID


class PerfilUsuarioActivity : AppCompatActivity() {

    private lateinit var btnVoltar: ImageView
    private lateinit var imgPerfil: ImageView
    private lateinit var botaoSalvarImagem: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var edtNomePerfil: EditText
    private var firebaseUser: FirebaseUser? = null
    private lateinit var databaseReference: DatabaseReference
    private var filePath: Uri? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        setarInicializacaoComponentes()

        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        setarReferenciaBanco()
        setarBotoes()

        imgPerfil.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(Intent.createChooser(intent, "escolha sua imagem"), 1)

        }

        botaoSalvarImagem.setOnClickListener {
            descarregarImagem()
            progressBar.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            filePath = data!!.data
            imgPerfil.setImageURI(data.data)
        }
    }

    private fun setarBotoes() {
        btnVoltar.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setarInicializacaoComponentes() {
        btnVoltar = findViewById(R.id.imgVoltarPerfil)
        imgPerfil = findViewById(R.id.imgPerfil)
        botaoSalvarImagem = findViewById(R.id.btnSalvarImagem)
        progressBar = findViewById(R.id.progressBar)
        edtNomePerfil = findViewById(R.id.edtNomePerfil)
    }

    private fun setarReferenciaBanco() {
        firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser != null) {
            databaseReference =
                FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)
        } else {
            Toast.makeText(
                this@PerfilUsuarioActivity,
                "Nenhum Usuário encontrado",
                Toast.LENGTH_SHORT
            ).show()
        }

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usuario = snapshot.getValue(Usuario::class.java)

                if (usuario != null) {
                    edtNomePerfil.setText(usuario.userName)

                    if (usuario.profileImage == "") {
                        imgPerfil.setImageResource(R.drawable.imagem_perfil)
                    } else {
                        if (!isDestroyed && !isFinishing) {
                            Glide.with(this@PerfilUsuarioActivity)
                                .load(usuario.profileImage)
                                .placeholder(R.drawable.imagem_perfil)
                                .into(imgPerfil)
                        }
                    }
                } else {
                    edtNomePerfil.setText("Usuário não logado")
                    imgPerfil.setImageResource(R.drawable.imagem_perfil)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun descarregarImagem() {
        if (filePath != null) {
            val ref: StorageReference =
                storageReference.child("image/" + UUID.randomUUID().toString())
            ref.putFile(filePath!!)
                .addOnSuccessListener {

                    val hashMap: HashMap<String, String> = HashMap()

                    hashMap["userName"] = edtNomePerfil.text.toString()
                    hashMap["profileImage"] = filePath.toString()
                    databaseReference.updateChildren(hashMap as Map<String, Any>)

                    progressBar.visibility = View.GONE
                    Toast.makeText(applicationContext, "Descarregado", Toast.LENGTH_SHORT)
                        .show()

                }.addOnFailureListener {
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        applicationContext,
                        "Falhou" + it.message,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
        }
    }
}