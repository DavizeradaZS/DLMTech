package com.example.dlmtech

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.dlmtech.api.RetrofitClient
import com.example.dlmtech.api.Usuario
import com.example.dlmtech.api.Produto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    // URL base para as imagens enviadas ao servidor
    private val imageBaseUrl = "http://192.168.15.5:80/dlmtech_api/uploads/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        configurarNavegacao()
        verificarAcesso()
        //carregarDadosDashboard()
    }

    private fun configurarNavegacao() {
        findViewById<ImageButton>(R.id.btnNavAnalise).setOnClickListener {
            startActivity(Intent(this, Activity_Sobre::class.java))
        }
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
        findViewById<ImageButton>(R.id.ExibiProd_ImgBtnCarinho).setOnClickListener {
            startActivity(Intent(this, Activity_Carrinho::class.java))
        }
    }

    private fun verificarAcesso() {
        val preferences = getSharedPreferences("DLMTechPrefs", MODE_PRIVATE)
        val tipoUsuario = preferences.getString("TIPO_USUARIO", "cliente")
        val nivelAcesso = preferences.getString("NIVEL_ACESSO", "")

        val btnNavFuncionarios = findViewById<ImageButton>(R.id.btnNavFuncionarios)
        if (tipoUsuario.equals("cliente", ignoreCase = true) || nivelAcesso.equals("User", ignoreCase = true)) {
            btnNavFuncionarios.visibility = View.GONE
        }
    }


    private fun carregarImagem(caminho: String?, imageView: ImageView) {
        if (!caminho.isNullOrEmpty()) {
            val url = if (caminho.startsWith("http")) caminho else imageBaseUrl + caminho
            Glide.with(this).load(url).placeholder(R.drawable.image_10).into(imageView)
        }
    }
}
