package com.example.dlmtech

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dlmtech.adapter.ProdutoAdapter
import com.example.dlmtech.api.Produto
import com.example.dlmtech.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Activity_Estoque : AppCompatActivity() {

    private lateinit var adapter: ProdutoAdapter
    private var listaCompletaProdutos: List<Produto> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estoque)

        val preferences = getSharedPreferences("DLMTechPrefs", MODE_PRIVATE)
        val tipoUsuario = preferences.getString("TIPO_USUARIO", "cliente") ?: "cliente"
        val nivelAcesso = preferences.getString("NIVEL_ACESSO", "") ?: ""

        val categoriaFiltro = intent.getStringExtra("CATEGORIA_FILTRO")
        val txtTituloEstoque = findViewById<TextView>(R.id.estoque)
        if (categoriaFiltro != null) {
            txtTituloEstoque.text = "Estoque: $categoriaFiltro"
        }

        val recyclerView = findViewById<RecyclerView>(R.id.Estoque_produtos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        // Inicializa o adapter com lista vazia
        adapter = ProdutoAdapter(emptyList())
        recyclerView.adapter = adapter

        // Busca produtos da API
        carregarProdutos(categoriaFiltro)

        // ==========================================
        // LÓGICA DE BUSCA (PESQUISA)
        // ==========================================
        val edtPesquisa = findViewById<EditText>(R.id.ExibiProd_TxtPesquisa)
        edtPesquisa.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filtrarProdutos(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        val btnAddProduto = findViewById<ImageButton>(R.id.Estoque_ImgBtnAddProduto)
        btnAddProduto.setOnClickListener {
            startActivity(Intent(this, activity_cadastro_produto::class.java))
        }

        configurarNavegacao(tipoUsuario, nivelAcesso)
    }

    private fun carregarProdutos(categoria: String?) {
        RetrofitClient.instance.listarProdutos(categoria).enqueue(object : Callback<List<Produto>> {
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                if (response.isSuccessful) {
                    listaCompletaProdutos = response.body() ?: emptyList()
                    adapter.updateLista(listaCompletaProdutos)
                }
            }
            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                Toast.makeText(this@Activity_Estoque, getString(R.string.msg_error_loading_stock), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filtrarProdutos(texto: String) {
        val listaFiltrada = if (texto.isEmpty()) {
            listaCompletaProdutos
        } else {
            listaCompletaProdutos.filter { 
                it.nome?.contains(texto, ignoreCase = true) == true || 
                it.descricao?.contains(texto, ignoreCase = true) == true
            }
        }
        adapter.updateLista(listaFiltrada)
    }

    private fun configurarNavegacao(tipoUsuario: String, nivelAcesso: String) {
        val btnNavFuncionarios = findViewById<ImageButton>(R.id.btnNavFuncionarios)
        if (tipoUsuario.equals("cliente", ignoreCase = true) || nivelAcesso.equals("User", ignoreCase = true)) {
            btnNavFuncionarios.visibility = View.GONE
        }

        btnNavFuncionarios.setOnClickListener {
            startActivity(Intent(this, Activity_VisualizarFuncionarios::class.java))
            finish()
        }
        findViewById<ImageButton>(R.id.btnNavEstoque).setOnClickListener {
            Toast.makeText(this, getString(R.string.msg_already_on_stock), Toast.LENGTH_SHORT).show()
        }
        findViewById<ImageButton>(R.id.btnNavHome).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        findViewById<ImageButton>(R.id.btnNavClientes).setOnClickListener {
            startActivity(Intent(this, Activity_VisualizarClientes::class.java))
            finish()
        }
        findViewById<ImageButton>(R.id.btnNavAnalise).setOnClickListener {
            startActivity(Intent(this, Activity_Sobre::class.java))
        }
        findViewById<ImageButton>(R.id.ExibiProd_ImgBtnCarinho).setOnClickListener {
            startActivity(Intent(this, Activity_Carrinho::class.java))
        }
        findViewById<ImageView>(R.id.ExibiProd_ImgBtnHome)?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}