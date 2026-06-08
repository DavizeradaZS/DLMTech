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
import com.example.dlmtech.api.DashboardResponse
import com.example.dlmtech.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    // URL base configurada para apontar corretamente para a pasta da sua API no XAMPP
    private val imageBaseUrl = "http://192.168.15.5/dlmtech_api/"

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

        // Ativando os cliques nas categorias da Home mapeadas do seu XML
        configurarCategorias()

        // Chamada ativada para carregar os dados reais do banco de dados!
        carregarDadosDashboard()
    }

    // ==========================================
    // SISTEMA DE FILTRO DE CATEGORIAS
    // ==========================================
    private fun configurarCategorias() {

        // 1. Cabos
        findViewById<View>(R.id.cardCat1)?.setOnClickListener {
            abrirEstoqueComFiltro("Cabos")
        }

        // 2. Carregadores
        findViewById<View>(R.id.cardCat2)?.setOnClickListener {
            abrirEstoqueComFiltro("Carregadores")
        }

        // 3. Som
        findViewById<View>(R.id.cardCat3)?.setOnClickListener {
            abrirEstoqueComFiltro("Som")
        }

        // 4. Fones
        findViewById<View>(R.id.cardCat4)?.setOnClickListener {
            abrirEstoqueComFiltro("Fones")
        }

        // 5. Relógios
        findViewById<View>(R.id.cardCat5)?.setOnClickListener {
            abrirEstoqueComFiltro("Relógios")
        }

        // 6. Capinhas
        findViewById<View>(R.id.cardCat6)?.setOnClickListener {
            abrirEstoqueComFiltro("Capinhas")
        }
    }

    private fun abrirEstoqueComFiltro(categoria: String) {
        val intent = Intent(this, Activity_Estoque::class.java)
        intent.putExtra("CATEGORIA_FILTRO", categoria)
        startActivity(intent)
    }

    // ==========================================
    // CARREGAMENTO DO DASHBOARD
    // ==========================================
    private fun carregarDadosDashboard() {
        RetrofitClient.instance.getDashboardData().enqueue(object : Callback<DashboardResponse> {
            override fun onResponse(call: Call<DashboardResponse>, response: Response<DashboardResponse>) {
                if (response.isSuccessful && response.body()?.sucesso == true) {
                    val data = response.body()!!
                    val topProdutos = data.maisVendidos

                    // PRODUTO 1 (Ouro)
                    val prod1 = topProdutos.getOrNull(0)
                    if (prod1 != null) {
                        findViewById<TextView>(R.id.tvBottomProd1Name).text = prod1.nome ?: "Produto 1"
                        findViewById<TextView>(R.id.tvBottomProd1Price).text = "R$ ${prod1.valor}"
                        carregarImagem(prod1.imagem, findViewById(R.id.ivBottomProd1))

                        findViewById<TextView>(R.id.tvProd1Name).text = prod1.nome
                        findViewById<ProgressBar>(R.id.pbProd1).progress = 100
                    }

                    // PRODUTO 2 (Prata)
                    val prod2 = topProdutos.getOrNull(1)
                    if (prod2 != null) {
                        findViewById<TextView>(R.id.tvBottomProd2Name).text = prod2.nome ?: "Produto 2"
                        findViewById<TextView>(R.id.tvBottomProd2Price).text = "R$ ${prod2.valor}"
                        carregarImagem(prod2.imagem, findViewById(R.id.ivBottomProd2))
                    } else {
                        findViewById<TextView>(R.id.tvBottomProd2Name).text = ""
                        findViewById<TextView>(R.id.tvBottomProd2Price).text = ""
                    }

                    // PRODUTO 3 (Bronze)
                    val prod3 = topProdutos.getOrNull(2)
                    if (prod3 != null) {
                        findViewById<TextView>(R.id.tvBottomProd3Name).text = prod3.nome ?: "Produto 3"
                        findViewById<TextView>(R.id.tvBottomProd3Price).text = "R$ ${prod3.valor}"
                        carregarImagem(prod3.imagem, findViewById(R.id.ivBottomProd3))
                    } else {
                        findViewById<TextView>(R.id.tvBottomProd3Name).text = ""
                        findViewById<TextView>(R.id.tvBottomProd3Price).text = ""
                    }

                    findViewById<TextView>(R.id.tvTopFuncionario).text = "Admin"
                    findViewById<TextView>(R.id.tvTopVendasCount).text = "${data.totalProdutos} produtos no sistema"

                } else {
                    Toast.makeText(this@MainActivity, "Erro ao carregar Dashboard", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                Log.e("API_ERROR", "Erro no Dashboard: ${t.message}")
            }
        })
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
            Glide.with(this)
                .load(url)
                .placeholder(R.drawable.image_10)
                .error(R.drawable.image_10)
                .centerCrop()
                .into(imageView)
        }
    }
}