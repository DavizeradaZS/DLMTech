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
            // Aqui entrará a requisição para a API do Carrinho
            Toast.makeText(this, getString(R.string.msg_adding_to_cart, nome), Toast.LENGTH_SHORT).show()
        }

        btnEditar.setOnClickListener {
            Toast.makeText(this, getString(R.string.msg_in_development, getString(R.string.label_edit_screen)), Toast.LENGTH_SHORT).show()
            // Exemplo de como abrir a tela de edição passando o ID do produto:
            // val intent = Intent(this, Activity_Edit_Produto::class.java)
            // intent.putExtra("PRODUTO_ID", idDoProduto)
            // startActivity(intent)
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
            Toast.makeText(this, getString(R.string.msg_in_development, getString(R.string.label_clients)), Toast.LENGTH_SHORT).show()
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