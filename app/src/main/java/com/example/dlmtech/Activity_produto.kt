package com.example.dlmtech

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Activity_produto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produto)

        // Mapeando os campos do layout
        val txtNome = findViewById<TextView>(R.id.ExibiProduto_TxtProduto)
        val txtValor = findViewById<TextView>(R.id.ExibiProduto_TxtValor)
        val txtDesc = findViewById<TextView>(R.id.ExibiProd_TxtDesc)

        // Recebendo dados passados pela lista do estoque
        val nome = intent.getStringExtra("NOME_PRODUTO") ?: "Produto"
        val valor = intent.getStringExtra("VALOR_PRODUTO") ?: "0.00"
        val desc = intent.getStringExtra("DESC_PRODUTO") ?: "Sem descrição."

        txtNome.text = nome
        txtValor.text = "R$ $valor"
        txtDesc.text = desc
    }
}