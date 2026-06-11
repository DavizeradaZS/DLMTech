package com.example.dlmtech

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.dlmtech.api.RetrofitClient
import com.example.dlmtech.api.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Activity_produto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produto)

        val txtNome = findViewById<TextView>(R.id.ExibiProduto_TxtProduto)
        val txtValor = findViewById<TextView>(R.id.ExibiProduto_TxtValor)
        val txtQuantidade = findViewById<TextView>(R.id.txt_quantidade)
        val txtDesc = findViewById<TextView>(R.id.ExibiProd_TxtDesc)
        val imgProdutoDetalhe = findViewById<ImageView>(R.id.imgExibiProduto_Produto)

        val idProduto = intent.getIntExtra("ID_PRODUTO", -1)
        val nome = intent.getStringExtra("NOME_PRODUTO") ?: getString(R.string.title_produto)
        val valor = intent.getStringExtra("VALOR_PRODUTO") ?: "0.00"
        val desc = intent.getStringExtra("DESC_PRODUTO") ?: getString(R.string.label_no_description)
        val quantidade = intent.getIntExtra("QUANTIDADE_PRODUTO", 0)
        val imageUrl = intent.getStringExtra("IMG_PRODUTO") ?: ""

        txtNome.text = nome
        txtValor.text = getString(R.string.currency_symbol) + " " + valor
        txtQuantidade.text = "Estoque: $quantidade"
        txtDesc.text = desc

        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .centerCrop()
            .into(imgProdutoDetalhe)

        val btnAdicionar = findViewById<android.widget.Button>(R.id.TxtAdicionarAoProduto)
        val btnEditar = findViewById<android.widget.Button>(R.id.ExibiProd_BtnEditar)
        val btnRemover = findViewById<android.widget.Button>(R.id.Remover)

        btnAdicionar.setOnClickListener {
            if (idProduto != -1) {
                val preferences = getSharedPreferences("DLMTechPrefs", MODE_PRIVATE)
                val userId = preferences.getInt("USER_ID", -1)
                val tipo = preferences.getString("TIPO_USUARIO", "cliente")

                val funcionarioId = if (tipo == "funcionario") userId else 0
                val clienteId = if (tipo == "cliente") userId else 0

                // Correção: Passando 'id = null' como primeiro parâmetro para INSERT
                RetrofitClient.instance.addCarrinho(
                    id = null, 
                    produtoId = idProduto, 
                    clienteId = clienteId, 
                    funcionarioId = funcionarioId, 
                    quantidade = 1, 
                    valor = valor
                ).enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                        if (response.isSuccessful && response.body()?.sucesso == true) {
                            Toast.makeText(this@Activity_produto, "Adicionado ao carrinho!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@Activity_produto, response.body()?.mensagem ?: "Erro ao adicionar", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        Toast.makeText(this@Activity_produto, "Erro de conexão", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        btnEditar.setOnClickListener {
            val intentEdit = Intent(this, activity_edit_produto::class.java)
            intentEdit.putExtra("ID_PRODUTO", idProduto)
            intentEdit.putExtra("NOME_PRODUTO", nome)
            intentEdit.putExtra("VALOR_PRODUTO", valor)
            intentEdit.putExtra("DESC_PRODUTO", desc)
            intentEdit.putExtra("QUANTIDADE_PRODUTO", quantidade)
            startActivity(intentEdit)
        }

        btnRemover.setOnClickListener {
            if (idProduto != -1) {
                AlertDialog.Builder(this)
                    .setTitle("Excluir Produto")
                    .setMessage("Tem certeza que deseja excluir o produto $nome?")
                    .setPositiveButton("Sim") { _, _ -> deletarProdutoNaApi(idProduto) }
                    .setNegativeButton("Não", null)
                    .show()
            }
        }

        configurarNavegacao()
    }

    private fun deletarProdutoNaApi(id: Int) {
        RetrofitClient.instance.deletarProduto(id).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body()?.sucesso == true) {
                    Toast.makeText(this@Activity_produto, "Produto excluído!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {}
        })
    }

    private fun configurarNavegacao() {
        val btnNavFuncionarios = findViewById<ImageButton>(R.id.btnNavFuncionarios)
        val preferences = getSharedPreferences("DLMTechPrefs", MODE_PRIVATE)
        val tipoUsuario = preferences.getString("TIPO_USUARIO", "cliente") ?: "cliente"
        val nivelAcesso = preferences.getString("NIVEL_ACESSO", "") ?: ""

        if (tipoUsuario.equals("cliente", ignoreCase = true) || nivelAcesso.equals("User", ignoreCase = true)) {
            btnNavFuncionarios.visibility = View.GONE
        }

        btnNavFuncionarios.setOnClickListener { startActivity(Intent(this, Activity_VisualizarFuncionarios::class.java)); finish() }
        findViewById<ImageButton>(R.id.btnNavEstoque).setOnClickListener { startActivity(Intent(this, Activity_Estoque::class.java)) }
        findViewById<ImageButton>(R.id.btnNavHome).setOnClickListener { startActivity(Intent(this, MainActivity::class.java)); finish() }
        findViewById<ImageButton>(R.id.btnNavClientes).setOnClickListener { startActivity(Intent(this, Activity_VisualizarClientes::class.java)) }
        findViewById<ImageButton>(R.id.btnNavAnalise).setOnClickListener { startActivity(Intent(this, Activity_Sobre::class.java)) }
        findViewById<ImageButton>(R.id.ExibiProd_ImgBtnCarinho)?.setOnClickListener { startActivity(Intent(this, Activity_Carrinho::class.java)) }
        findViewById<ImageView>(R.id.ExibiProd_ImgBtnHome)?.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)); finish() }
    }
}
