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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Navegação Inferior
        findViewById<ImageButton>(R.id.btnNavFuncionarios).setOnClickListener {
            startActivity(Intent(this, Activity_VisualizarFuncionarios::class.java))
        }

        findViewById<ImageButton>(R.id.btnNavClientes).setOnClickListener {
            startActivity(Intent(this, Activity_VisualizarClientes::class.java))
        }

        findViewById<ImageButton>(R.id.btnNavHome).setOnClickListener {
            Toast.makeText(this, getString(R.string.msg_already_on_home), Toast.LENGTH_SHORT).show()
        }

        findViewById<ImageButton>(R.id.btnNavEstoque).setOnClickListener {
            startActivity(Intent(this, Activity_Estoque::class.java))
        }

        // Cabeçalho
        findViewById<ImageButton>(R.id.ExibiProd_ImgBtnCarinho).setOnClickListener {
            startActivity(Intent(this, Activity_Carrinho::class.java))
        }
    }
}
