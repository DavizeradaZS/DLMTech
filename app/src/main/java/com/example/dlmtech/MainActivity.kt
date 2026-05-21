package com.example.dlmtech

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Configuração das margens (Edge to Edge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ==========================================
        // 1. MAPEAMENTO DOS BOTÕES DA BARRA INFERIOR
        // ==========================================
        val btnNavFuncionarios = findViewById<ImageButton>(R.id.btnNavFuncionarios)
        val btnNavClientes = findViewById<ImageButton>(R.id.btnNavClientes)
        val btnNavHome = findViewById<ImageButton>(R.id.btnNavHome)
        val btnNavEstoque = findViewById<ImageButton>(R.id.btnNavEstoque)
        val btnNavAnalise = findViewById<ImageButton>(R.id.btnNavAnalise)

        // ==========================================
        // 2. CONFIGURAÇÃO DOS CLIQUES (INTENTS)
        // ==========================================

        // Botão Funcionários -> Vai para a Activity de Cadastro de Funcionários
        btnNavFuncionarios.setOnClickListener {
            val intent = Intent(this, Activity_CadastroFunc::class.java)
            startActivity(intent)
        }

        // Botão Estoque -> Vai para a Activity de Estoque
        btnNavEstoque.setOnClickListener {
            val intent = Intent(this, Activity_Estoque::class.java)
            startActivity(intent)
        }

        // Botão Home -> Já estamos nela, então apenas exibe uma mensagem
        btnNavHome.setOnClickListener {
            Toast.makeText(this, getString(R.string.msg_already_on_home), Toast.LENGTH_SHORT).show()
        }

        // Botão Clientes (Exemplo com Toast - Substitua o Intent quando a tela de Clientes estiver finalizada)
        btnNavClientes.setOnClickListener {
            Toast.makeText(this, getString(R.string.msg_in_development, getString(R.string.label_clients)), Toast.LENGTH_SHORT).show()
            // Exemplo de como ficará:
            // startActivity(Intent(this, Activity_Edit_Cliente::class.java))
        }

        // Botão Análise (Exemplo com Toast)
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