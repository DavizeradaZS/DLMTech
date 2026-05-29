package com.example.dlmtech

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.dlmtech.api.ApiResponse
import com.example.dlmtech.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class activity_cadastro_produto : AppCompatActivity() {

    private var imageUri: Uri? = null
    private lateinit var edtImg: ImageButton

    // =======================================================
    // LAUNCHER: ABRE A GALERIA DO TELEMÓVEL DE FORMA SEGURA
    // =======================================================
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            edtImg.setImageURI(uri) // Substitui o ícone padrão pela foto escolhida
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_produto)

        val edtNome = findViewById<EditText>(R.id.edtNome)
        val edtDesc = findViewById<EditText>(R.id.edtDesc)
        val edtValor = findViewById<EditText>(R.id.edtValor)
        edtImg = findViewById(R.id.edtImg)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        // Ao clicar no botão da imagem, abre a galeria para ficheiros do tipo "image/*"
        edtImg.setOnClickListener {
            pickImage.launch("image/*")
        }

        btnCancelar.setOnClickListener {
            finish()
        }

        btnSalvar.setOnClickListener {
            val nome = edtNome.text.toString().trim()
            val desc = edtDesc.text.toString().trim()
            val valor = edtValor.text.toString().trim()

            if (nome.isEmpty() || valor.isEmpty()) {
                Toast.makeText(this, "Preencha pelo menos o Nome e o Valor!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            enviarProdutoParaApi(nome, desc, valor)
        }

        configurarNavegacao()
    }

    // =======================================================
    // PREPARAÇÃO E ENVIO MULTIPART PARA O SERVIDOR
    // =======================================================
    private fun enviarProdutoParaApi(nome: String, desc: String, valor: String) {
        // Converte os textos simples num formato RequestBody usando a sintaxe atualizada
        val nomePart = nome.toRequestBody("text/plain".toMediaTypeOrNull())
        val descPart = desc.toRequestBody("text/plain".toMediaTypeOrNull())
        val valorPart = valor.toRequestBody("text/plain".toMediaTypeOrNull())

        var imagemPart: MultipartBody.Part? = null

        // Se o utilizador tiver escolhido uma foto, converte-a para um ficheiro físico e empacota-a
        if (imageUri != null) {
            val file = uriToFile(imageUri!!)
            if (file != null) {
                // Nova sintaxe para ficheiros: file.asRequestBody()
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                imagemPart = MultipartBody.Part.createFormData("imagem", file.name, requestFile)
            }
        }

        RetrofitClient.instance.cadastrarProduto(nomePart, descPart, valorPart, imagemPart)
            .enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful && response.body()?.sucesso == true) {
                        Toast.makeText(this@activity_cadastro_produto, "Produto cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                        finish() // Volta ao ecrã anterior após o sucesso
                    } else {
                        Toast.makeText(this@activity_cadastro_produto, response.body()?.mensagem ?: "Erro ao cadastrar", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Toast.makeText(this@activity_cadastro_produto, "Erro de conexão com o servidor", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // =======================================================
    // FUNÇÃO AUXILIAR: CONVERTE A FOTO DA GALERIA NUM FICHEIRO
    // =======================================================
    private fun uriToFile(uri: Uri): File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            // Cria um ficheiro temporário na memória cache da aplicação para fazer o upload
            val tempFile = File(cacheDir, "upload_produto_temp.jpg")
            val outputStream = FileOutputStream(tempFile)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // =======================================================
    // NAVEGAÇÃO E CONTROLE DE ACESSO (RBAC)
    // =======================================================
    private fun configurarNavegacao() {
        val btnNavFuncionarios = findViewById<ImageButton>(R.id.btnNavFuncionarios)
        val btnNavClientes = findViewById<ImageButton>(R.id.btnNavClientes)
        val btnNavHome = findViewById<ImageButton>(R.id.btnNavHome)
        val btnNavEstoque = findViewById<ImageButton>(R.id.btnNavEstoque)
        val btnNavAnalise = findViewById<ImageButton>(R.id.btnNavAnalise)

        val preferences = getSharedPreferences("DLMTechPrefs", MODE_PRIVATE)
        val tipoUsuario = preferences.getString("TIPO_USUARIO", "cliente") ?: "cliente"
        val nivelAcesso = preferences.getString("NIVEL_ACESSO", "") ?: ""

        if (tipoUsuario.equals("cliente", ignoreCase = true) || nivelAcesso.equals("User", ignoreCase = true)) {
            btnNavFuncionarios.visibility = View.GONE
        }

        btnNavFuncionarios.setOnClickListener {
            startActivity(Intent(this, Activity_VisualizarFuncionarios::class.java))
            finish()
        }
        btnNavEstoque.setOnClickListener {
            startActivity(Intent(this, Activity_Estoque::class.java))
            finish()
        }
        btnNavHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        btnNavClientes.setOnClickListener {
            startActivity(Intent(this, Activity_VisualizarClientes::class.java))
            finish()
        }
        btnNavAnalise.setOnClickListener {
            Toast.makeText(this, "Em desenvolvimento", Toast.LENGTH_SHORT).show()
        }

        // Cabeçalho
        findViewById<ImageView>(R.id.ExibiProd_ImgBtnHome).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        findViewById<ImageButton>(R.id.ExibiProd_ImgBtnCarinho).setOnClickListener {
            startActivity(Intent(this, Activity_Carrinho::class.java))
        }
    }
}