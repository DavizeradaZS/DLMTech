package com.example.dlmtech

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class  Activity_produto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produto)

        // Evita erro se a view principal não tiver o id "main"
        val mainView = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.main)
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

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
            startActivity(Intent(this, Activity_Estoque::class.java))
            finish()
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

        // ==========================================
        // NAVEGAÇÃO DO CABEÇALHO (HEADER)
        // ==========================================
        val btnCarrinho = findViewById<ImageButton>(R.id.ExibiProd_ImgBtnCarinho)
        btnCarrinho?.setOnClickListener {
            startActivity(Intent(this, Activity_Carrinho::class.java))
        }
    }
}