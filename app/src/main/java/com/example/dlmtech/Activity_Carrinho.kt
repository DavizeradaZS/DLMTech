package com.example.dlmtech

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dlmtech.adapter.CarrinhoAdapter
import com.example.dlmtech.api.ApiResponse
import com.example.dlmtech.api.Carrinho
import com.example.dlmtech.api.RetrofitClient
import com.example.dlmtech.api.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Activity_Carrinho : AppCompatActivity() {
    private lateinit var rv: RecyclerView
    private lateinit var txtTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrinho)

        rv = findViewById(R.id.rvCarrinho)
        txtTotal = findViewById(R.id.txtCarrinho_Total)
        rv.layoutManager = LinearLayoutManager(this)

        carregarCarrinho()

        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        // Ação do botão Cancelar
        btnCancelar.setOnClickListener {
            Toast.makeText(this, getString(R.string.msg_registration_cancelled), Toast.LENGTH_SHORT).show()
            finish() // Fecha a tela e volta para a anterior
        }

        // ==========================================
        // NAVEGAÇÃO DA BARRA INFERIOR
        // ==========================================
        val btnNavFuncionarios = findViewById<ImageButton>(R.id.btnNavFuncionarios)
        val btnNavClientes = findViewById<ImageButton>(R.id.btnNavClientes)
        val btnNavHome = findViewById<ImageButton>(R.id.btnNavHome)
        val btnNavEstoque = findViewById<ImageButton>(R.id.btnNavEstoque)


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
        findViewById<ImageButton>(R.id.btnNavAnalise).setOnClickListener {
            startActivity(Intent(this, Activity_Sobre::class.java))
        }

        // ==========================================
        // NAVEGAÇÃO DO CABEÇALHO (HEADER)
        // ==========================================
        val btnCarrinho = findViewById<ImageButton>(R.id.ExibiProd_ImgBtnCarinho)
        btnCarrinho?.setOnClickListener {
            Toast.makeText(this, getString(R.string.msg_already_on_cart), Toast.LENGTH_SHORT).show()
        }
    }

    private fun carregarCarrinho() {
        RetrofitClient.instance.listarCarrinho().enqueue(object : Callback<List<Carrinho>> {
            override fun onResponse(call: Call<List<Carrinho>>, response: Response<List<Carrinho>>) {
                if (response.isSuccessful) {
                    val itens = response.body()?.toMutableList() ?: mutableListOf()

                    // Configura o Adapter com a função de remover
                    rv.adapter = CarrinhoAdapter(itens) { item ->
                        removerItem(item.id)
                    }

                    // Calcula o total
                    val total = itens.sumOf { it.valor.toDouble() }
                    txtTotal.text = getString(R.string.label_total).format(total)
                }
            }
            override fun onFailure(call: Call<List<Carrinho>>, t: Throwable) { /* Erro */ }
        })
    }

    private fun removerItem(id: Int) {
        // Alterado de Callback<Usuario> para Callback<ApiResponse>
        RetrofitClient.instance.removerDoCarrinho(id).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                Toast.makeText(this@Activity_Carrinho, getString(R.string.msg_removed), Toast.LENGTH_SHORT).show()
                carregarCarrinho() // Recarrega a lista
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) { /* Erro */ }
        })
    }
}