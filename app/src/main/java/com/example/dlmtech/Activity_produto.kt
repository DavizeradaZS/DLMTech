package com.example.dlmtech

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dlmtech.api.RetrofitClient
import com.example.dlmtech.api.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Activity_produto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produto)

        // Mapeando os campos do layout
        val txtNome = findViewById<TextView>(R.id.ExibiProduto_TxtProduto)
        val txtValor = findViewById<TextView>(R.id.ExibiProduto_TxtValor)
        val txtDesc = findViewById<TextView>(R.id.ExibiProd_TxtDesc)

        // Recebendo dados passados pela lista do estoque
        val idProduto = intent.getIntExtra("ID_PRODUTO", -1)
        val nome = intent.getStringExtra("NOME_PRODUTO") ?: getString(R.string.title_produto)
        val valor = intent.getStringExtra("VALOR_PRODUTO") ?: "0.00"
        val desc = intent.getStringExtra("DESC_PRODUTO") ?: getString(R.string.label_no_description)

        txtNome.text = nome
        txtValor.text = getString(R.string.currency_symbol) + " " + valor
        txtDesc.text = desc

        // ==========================================
        // AÇÕES INTERNAS DO PRODUTO
        // ==========================================
        val btnAdicionar = findViewById<android.widget.Button>(R.id.TxtAdicionarAoProduto)
        val btnEditar = findViewById<android.widget.Button>(R.id.ExibiProd_BtnEditar)
        val btnRemover = findViewById<android.widget.Button>(R.id.Remover)

        btnAdicionar.setOnClickListener {
            if (idProduto != -1) {
                // Requisição Real para a API usando ApiResponse
                RetrofitClient.instance.addCarrinho(idProduto).enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                        if (response.isSuccessful) {
                            val resposta = response.body()
                            if (resposta != null && resposta.sucesso) {
                                Toast.makeText(this@Activity_produto, resposta.mensagem, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@Activity_produto, resposta?.mensagem ?: "Erro ao adicionar", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        Toast.makeText(this@Activity_produto, "Erro de conexão", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Produto inválido!", Toast.LENGTH_SHORT).show()
            }
        }

        btnEditar.setOnClickListener {
            Toast.makeText(this, getString(R.string.msg_in_development, getString(R.string.label_edit_screen)), Toast.LENGTH_SHORT).show()
        }

        btnRemover.setOnClickListener {
            if (idProduto != -1) {
                // Aqui entrará a requisição DELETE para a API usando a variável 'idProduto'
                Toast.makeText(this, getString(R.string.msg_deleting_product, idProduto), Toast.LENGTH_SHORT).show()
                finish() // Volta para o estoque após deletar
            } else {
                Toast.makeText(this, getString(R.string.msg_invalid_product_id), Toast.LENGTH_SHORT).show()
            }
        }

        // ==========================================
        // NAVEGAÇÃO DA BARRA INFERIOR
        // ==========================================
        val btnNavFuncionarios = findViewById<ImageButton>(R.id.btnNavFuncionarios)
        val btnNavClientes = findViewById<ImageButton>(R.id.btnNavClientes)
        val btnNavHome = findViewById<ImageButton>(R.id.btnNavHome)
        val btnNavEstoque = findViewById<ImageButton>(R.id.btnNavEstoque)
        val btnNavAnalise = findViewById<ImageButton>(R.id.btnNavAnalise)

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
            Toast.makeText(this, getString(R.string.msg_in_development, getString(R.string.label_analysis)), Toast.LENGTH_SHORT).show()
        }

        // ==========================================
        // NAVEGAÇÃO DO CABEÇALHO (HEADER)
        // ==========================================
        val btnCarrinho = findViewById<ImageButton>(R.id.ExibiProd_ImgBtnCarinho)
        btnCarrinho?.setOnClickListener {
            startActivity(Intent(this, Activity_Carrinho::class.java))
        }
    }
}