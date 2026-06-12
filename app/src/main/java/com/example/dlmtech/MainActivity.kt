package com.example.dlmtech

import android.content.Context
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
import com.example.dlmtech.api.Produto
import com.example.dlmtech.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

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
        configurarCategorias()
        carregarDadosDashboard()
    }

    private fun configurarCategorias() {
        findViewById<View>(R.id.cardCat1)?.setOnClickListener { abrirEstoqueComFiltro("Cabos") }
        findViewById<View>(R.id.cardCat2)?.setOnClickListener { abrirEstoqueComFiltro("Carregadores") }
        findViewById<View>(R.id.cardCat3)?.setOnClickListener { abrirEstoqueComFiltro("Som") }
        findViewById<View>(R.id.cardCat4)?.setOnClickListener { abrirEstoqueComFiltro("Fones") }
        findViewById<View>(R.id.cardCat5)?.setOnClickListener { abrirEstoqueComFiltro("Relógios") }
        findViewById<View>(R.id.cardCat6)?.setOnClickListener { abrirEstoqueComFiltro("Capinhas") }
    }

    private fun abrirEstoqueComFiltro(categoria: String) {
        val intent = Intent(this, Activity_Estoque::class.java)
        intent.putExtra("CATEGORIA_FILTRO", categoria)
        startActivity(intent)
    }

    private fun carregarDadosDashboard() {
        RetrofitClient.instance.getDashboardData().enqueue(object : Callback<DashboardResponse> {
            override fun onResponse(call: Call<DashboardResponse>, response: Response<DashboardResponse>) {
                if (response.isSuccessful && response.body()?.sucesso == true) {
                    val data = response.body()!!
                    exibirProdutosDashboard(data.maisVendidos)
                    findViewById<TextView>(R.id.tvTopFuncionario).text = data.topVendedorNome ?: "Nenhum"
                } else {
                    Toast.makeText(this@MainActivity, "Erro ao carregar Dashboard", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                Log.e("API_ERROR", "Erro no Dashboard: ${t.message}")
            }
        })
    }

    private fun exibirProdutosDashboard(produtos: List<Produto>) {
        // PRODUTO 1 (Ranking Topo e Card 1)
        produtos.getOrNull(0)?.let { prod ->
            findViewById<TextView>(R.id.tvProd1Name).apply {
                text = prod.nome
                setOnClickListener { abrirDetalhesProduto(prod) }
            }
            findViewById<TextView>(R.id.tvProd1Count).text = "${prod.quantidade_estoque} un."
            findViewById<ProgressBar>(R.id.pbProd1).progress = 100
            
            findViewById<TextView>(R.id.tvBottomProd1Name).text = prod.nome
            findViewById<TextView>(R.id.tvBottomProd1Price).text = "R$ ${prod.valor}"
            carregarImagem(prod.imagem, findViewById(R.id.tvBottomProd1Name))
            findViewById<View>(R.id.layoutProdBottom1)?.setOnClickListener { abrirDetalhesProduto(prod) }
        }

        // PRODUTO 2 (Ranking Topo e Card 2)
        produtos.getOrNull(1)?.let { prod ->
            findViewById<TextView>(R.id.tvProd2Name).apply {
                text = prod.nome
                setOnClickListener { abrirDetalhesProduto(prod) }
            }
            findViewById<TextView>(R.id.tvProd2Count).text = "${prod.quantidade_estoque} un."
            findViewById<ProgressBar>(R.id.pbProd2).progress = 80 // Valor ilustrativo
            
            findViewById<TextView>(R.id.tvBottomProd2Name).text = prod.nome
            findViewById<TextView>(R.id.tvBottomProd2Price).text = "R$ ${prod.valor}"
            carregarImagem(prod.imagem, findViewById(R.id.tvBottomProd2Name))
            findViewById<View>(R.id.layoutProdBottom2)?.setOnClickListener { abrirDetalhesProduto(prod) }
        }

        // PRODUTO 3 (Ranking Topo e Card 3)
        produtos.getOrNull(2)?.let { prod ->
            findViewById<TextView>(R.id.tvProd3Name).apply {
                text = prod.nome
                setOnClickListener { abrirDetalhesProduto(prod) }
            }
            findViewById<TextView>(R.id.tvProd3Count).text = "${prod.quantidade_estoque} un."
            findViewById<ProgressBar>(R.id.pbProd3).progress = 60 // Valor ilustrativo
            
            findViewById<TextView>(R.id.tvBottomProd3Name).text = prod.nome
            findViewById<TextView>(R.id.tvBottomProd3Price).text = "R$ ${prod.valor}"
            carregarImagem(prod.imagem, findViewById(R.id.tvBottomProd3Name))
            findViewById<View>(R.id.layoutProdBottom3)?.setOnClickListener { abrirDetalhesProduto(prod) }
        }
    }

    private fun abrirDetalhesProduto(produto: Produto) {
        val intent = Intent(this, Activity_produto::class.java)
        intent.putExtra("ID_PRODUTO", produto.id)
        intent.putExtra("NOME_PRODUTO", produto.nome)
        intent.putExtra("VALOR_PRODUTO", produto.valor.toString())
        intent.putExtra("DESC_PRODUTO", produto.descricao ?: "Sem descrição")
        intent.putExtra("QUANTIDADE_PRODUTO", produto.quantidade_estoque)
        
        val url = if (produto.imagem?.startsWith("http") == true) produto.imagem else imageBaseUrl + produto.imagem
        intent.putExtra("IMG_PRODUTO", url)
        
        startActivity(intent)
    }

    private fun configurarNavegacao() {
        findViewById<ImageButton>(R.id.btnNavAnalise).setOnClickListener { startActivity(Intent(this, Activity_Sobre::class.java)) }
        findViewById<ImageButton>(R.id.btnNavFuncionarios).setOnClickListener { startActivity(Intent(this, Activity_VisualizarFuncionarios::class.java)) }
        findViewById<ImageButton>(R.id.btnNavClientes).setOnClickListener { startActivity(Intent(this, Activity_VisualizarClientes::class.java)) }
        findViewById<ImageButton>(R.id.btnNavHome).setOnClickListener { Toast.makeText(this, getString(R.string.msg_already_on_home), Toast.LENGTH_SHORT).show() }
        findViewById<ImageButton>(R.id.btnNavEstoque).setOnClickListener { startActivity(Intent(this, Activity_Estoque::class.java)) }
        findViewById<ImageButton>(R.id.ExibiProd_ImgBtnCarinho).setOnClickListener { startActivity(Intent(this, Activity_Carrinho::class.java)) }

        findViewById<ImageButton>(R.id.btnLogout).setOnClickListener { 
            getSharedPreferences("DLMTechPrefs", MODE_PRIVATE).edit().clear().apply()
            startActivity(Intent(this, activity_login::class.java))
            finish()
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
