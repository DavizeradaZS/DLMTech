package com.example.dlmtech

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dlmtech.adapter.ProdutoAdapter
import com.example.dlmtech.api.Produto
import com.example.dlmtech.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Activity_Estoque : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estoque)

        // ==========================================
        // CONFIGURAÇÃO DO RECYCLERVIEW (PRODUTOS)
        // ==========================================
        val recyclerView = findViewById<RecyclerView>(R.id.Estoque_produtos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Chamada para buscar produtos no banco
        RetrofitClient.instance.listarProdutos().enqueue(object : Callback<List<Produto>> {
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                if (response.isSuccessful) {
                    val produtos = response.body() ?: emptyList()
                    recyclerView.adapter = ProdutoAdapter(produtos)
                }
            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                Toast.makeText(this@Activity_Estoque, "Erro ao carregar estoque", Toast.LENGTH_SHORT).show()
            }
        })

        // ==========================================
        // BOTÃO ADICIONAR PRODUTO (AÇÃO INTERNA)
        // ==========================================
        val btnAddProduto = findViewById<ImageButton>(R.id.Estoque_ImgBtnAddProduto)

        btnAddProduto.setOnClickListener {
            // Navega para a tela de produto (Cadastro/Detalhes)
            val intent = Intent(this, Activity_produto::class.java)
            startActivity(intent)
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
            startActivity(Intent(this, Activity_CadastroFunc::class.java))
            finish()
        }
        btnNavEstoque.setOnClickListener {
            Toast.makeText(this, "Você já está na tela de Estoque", Toast.LENGTH_SHORT).show()
        }
        btnNavHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        btnNavClientes.setOnClickListener {
            Toast.makeText(this, "Tela de Clientes em desenvolvimento", Toast.LENGTH_SHORT).show()
        }
        btnNavAnalise.setOnClickListener {
            Toast.makeText(this, "Tela de Análise em desenvolvimento", Toast.LENGTH_SHORT).show()
        }
    }
}