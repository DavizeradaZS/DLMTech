package com.example.dlmtech

import android.os.Bundle
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
    }
}