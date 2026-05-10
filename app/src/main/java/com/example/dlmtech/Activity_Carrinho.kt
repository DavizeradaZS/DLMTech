package com.example.dlmtech

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dlmtech.adapter.CarrinhoAdapter
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
                    txtTotal.text = "Total: R$ %.2f".format(total)
                }
            }
            override fun onFailure(call: Call<List<Carrinho>>, t: Throwable) { /* Erro */ }
        })
    }

    private fun removerItem(id: Int) {
        RetrofitClient.instance.removerDoCarrinho(id).enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                Toast.makeText(this@Activity_Carrinho, "Removido!", Toast.LENGTH_SHORT).show()
                carregarCarrinho() // Recarrega a lista
            }
            override fun onFailure(call: Call<Usuario>, t: Throwable) { /* Erro */ }
        })
    }
}