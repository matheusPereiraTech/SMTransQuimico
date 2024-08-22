package com.example.smtransquimico.view.chat

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.smtransquimico.R
import com.example.smtransquimico.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.FileOutputStream
import java.util.UUID


class PerfilUsuarioActivity : AppCompatActivity() {

    private lateinit var btnVoltar: ImageView
    private lateinit var imgPerfil: ImageView
    private lateinit var botaoSalvarImagem: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var edtNomePerfil: EditText
    private lateinit var nomePerfil: TextView
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
        escolherImagemPerfilUsuario()
        clicarBotaoSalvarImagem()
    }

    private fun clicarBotaoSalvarImagem() =
        botaoSalvarImagem.setOnClickListener {
            descarregarImagem()
            progressBar.visibility = View.VISIBLE
        }

    private fun escolherImagemPerfilUsuario() = imgPerfil.setOnClickListener {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(Intent.createChooser(intent, "escolha sua imagem"), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        data?.let {
            if (resultCode == Activity.RESULT_OK) {
                filePath = it.data
                imgPerfil.setImageURI(it.data)
            }
        }
    }

    private fun setarBotoes() = btnVoltar.setOnClickListener {
        onBackPressed()
    }

    private fun setarInicializacaoComponentes() {
        btnVoltar = findViewById(R.id.imgVoltarPerfil)
        imgPerfil = findViewById(R.id.imgPerfil)
        botaoSalvarImagem = findViewById(R.id.btnSalvarImagem)
        progressBar = findViewById(R.id.progressBar)
        edtNomePerfil = findViewById(R.id.edtNomePerfil)
        nomePerfil = findViewById(R.id.perfilNome)
    }

    private fun setarReferenciaBanco() {
        firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)
        } else {
            Toast.makeText(
                this@PerfilUsuarioActivity,
                "Nenhum Usuário encontrado",
                Toast.LENGTH_SHORT
            ).show()
        }

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(Users::class.java)

                if (data != null) {
                    edtNomePerfil.setText(data.userName)

                    if (data.profileImage == "") {
                        imgPerfil.setImageResource(R.drawable.imagem_perfil)
                    } else {
                        if (!isDestroyed && !isFinishing) {

                            nomePerfil.text = data.userName

                            Glide.with(this@PerfilUsuarioActivity)
                                .load(data.profileImage)
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

    private fun mostrarAvisoErroImagem(exception: Exception){
        Toast.makeText(
            applicationContext,
            "Falhou: ${exception.message}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun descarregarImagem() {
        if (filePath != null) {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
            val file = File(cacheDir, "profile_image.jpg")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            filePath = Uri.fromFile(file)

            val ref: StorageReference =
                storageReference.child("image/" + UUID.randomUUID().toString())
            ref.putFile(filePath!!)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                        val hashMap: HashMap<String, Any> = HashMap()
                        hashMap["userName"] = edtNomePerfil.text.toString()
                        hashMap["profileImage"] = uri.toString()
                        databaseReference.updateChildren(hashMap)
                            .addOnSuccessListener {
                                progressBar.visibility = View.GONE
                                Toast.makeText(
                                    applicationContext,
                                    "Descarregado",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener {
                                progressBar.visibility = View.GONE
                                mostrarAvisoErroImagem(it)
                            }
                    }
                }
                .addOnFailureListener {
                    progressBar.visibility = View.GONE
                    mostrarAvisoErroImagem(it)
                }
        }
    }
}