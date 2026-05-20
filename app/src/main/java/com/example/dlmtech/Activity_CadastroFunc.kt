package com.example.dlmtech

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Activity_CadastroFunc : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastro_func)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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
            Toast.makeText(this, "Você já está na tela de Cadastro de Funcionários", Toast.LENGTH_SHORT).show()
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


    }
}