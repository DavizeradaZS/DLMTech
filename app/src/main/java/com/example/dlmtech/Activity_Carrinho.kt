package com.example.dlmtech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dlmtech.adapter.CarrinhoAdapter
import com.example.dlmtech.api.ApiResponse
import com.example.dlmtech.api.Carrinho
import com.example.dlmtech.api.RetrofitClient
import com.example.dlmtech.api.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Activity_Carrinho : AppCompatActivity() {
    private lateinit var rv: RecyclerView
    private lateinit var txtTotal: TextView
    private lateinit var edtCliente: AutoCompleteTextView
    private lateinit var edtFuncionario: EditText
    private var listaClientes: List<Usuario> = listOf()
    private var itensCarrinho: List<Carrinho> = listOf()
    private var idFuncionarioLogado: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrinho)

        rv = findViewById(R.id.rvCarrinho)
        txtTotal = findViewById(R.id.txtCarrinho_Total)
        edtCliente = findViewById(R.id.edtCliente)
        edtFuncionario = findViewById(R.id.edtFuncionario)
        val btnSalvar = findViewById<Button>(R.id.Carrinho_BtnSalvar)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        rv.layoutManager = LinearLayoutManager(this)

        carregarDadosUsuarioLogado()
        carregarCarrinho()
        carregarClientes()

        // Configuração da Pesquisa no Topo (Filtra itens do carrinho)
        val edtPesquisa = findViewById<EditText>(R.id.ExibiProd_TxtPesquisa)
        edtPesquisa.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filtrarItensCarrinho(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnSalvar.setOnClickListener { finalizarVenda() }
        btnCancelar.setOnClickListener {
            Toast.makeText(this, getString(R.string.msg_registration_cancelled), Toast.LENGTH_SHORT).show()
            finish()
        }

        configurarNavegacao()
    }

    private fun carregarDadosUsuarioLogado() {
        val preferences = getSharedPreferences("DLMTechPrefs", Context.MODE_PRIVATE)
        val nomeFuncionario = preferences.getString("USER_NAME", "N/A") ?: "N/A"
        idFuncionarioLogado = preferences.getInt("USER_ID", -1)

        if (idFuncionarioLogado != -1) {
            edtFuncionario.setText(getString(R.string.fmt_funcionario_logado, nomeFuncionario, idFuncionarioLogado))
        } else {
            edtFuncionario.setText(getString(R.string.msg_user_not_identified))
        }
        edtFuncionario.isEnabled = false
    }

    private fun carregarClientes() {
        RetrofitClient.instance.listarClientes().enqueue(object : Callback<List<Usuario>> {
            override fun onResponse(call: Call<List<Usuario>>, response: Response<List<Usuario>>) {
                if (response.isSuccessful) {
                    listaClientes = response.body() ?: listOf()
                    val nomesClientes = listaClientes.map { it.nome }
                    val adapter = ArrayAdapter(this@Activity_Carrinho, android.R.layout.simple_dropdown_item_1line, nomesClientes)
                    edtCliente.setAdapter(adapter)
                }
            }
            override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                Toast.makeText(this@Activity_Carrinho, "Erro ao carregar clientes", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun carregarCarrinho() {
        RetrofitClient.instance.listarCarrinho().enqueue(object : Callback<List<Carrinho>> {
            override fun onResponse(call: Call<List<Carrinho>>, response: Response<List<Carrinho>>) {
                if (response.isSuccessful) {
                    itensCarrinho = response.body() ?: listOf()
                    exibirItensNoRecycler(itensCarrinho)
                }
            }
            override fun onFailure(call: Call<List<Carrinho>>, t: Throwable) {}
        })
    }

    private fun exibirItensNoRecycler(lista: List<Carrinho>) {
        rv.adapter = CarrinhoAdapter(lista.toMutableList()) { item -> removerItem(item.id) }
        val total = lista.sumOf { it.valor.toDouble() }
        txtTotal.text = getString(R.string.label_total).format(total)
    }

    private fun filtrarItensCarrinho(texto: String) {
        val listaFiltrada = if (texto.isEmpty()) {
            itensCarrinho
        } else {
            itensCarrinho.filter { it.nome.contains(texto, ignoreCase = true) }
        }
        exibirItensNoRecycler(listaFiltrada)
    }

    private fun finalizarVenda() {
        val nomeSelecionado = edtCliente.text.toString()
        val cliente = listaClientes.find { it.nome == nomeSelecionado }

        if (cliente == null) {
            Toast.makeText(this, "Selecione um cliente válido da lista", Toast.LENGTH_SHORT).show()
            return
        }

        if (itensCarrinho.isEmpty()) {
            Toast.makeText(this, "O carrinho está vazio", Toast.LENGTH_SHORT).show()
            return
        }

        val clienteId = cliente.id
        var sucessoCount = 0

        itensCarrinho.forEach { item ->
            RetrofitClient.instance.addCarrinho(
                item.id, item.produto_id, clienteId, idFuncionarioLogado, item.quantidade, item.valor
            ).enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful && response.body()?.sucesso == true) {
                        sucessoCount++
                        if (sucessoCount == itensCarrinho.size) {
                            Toast.makeText(this@Activity_Carrinho, "Venda cadastrada com sucesso!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@Activity_Carrinho, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            })
                            finish()
                        }
                    }
                }
                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {}
            })
        }
    }

    private fun removerItem(id: Int) {
        RetrofitClient.instance.removerDoCarrinho(id).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                Toast.makeText(this@Activity_Carrinho, getString(R.string.msg_removed), Toast.LENGTH_SHORT).show()
                carregarCarrinho()
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {}
        })
    }

    private fun configurarNavegacao() {
        findViewById<ImageButton>(R.id.btnNavFuncionarios).setOnClickListener {
            startActivity(Intent(this, Activity_VisualizarFuncionarios::class.java))
            finish()
        }
        findViewById<ImageButton>(R.id.btnNavEstoque).setOnClickListener {
            startActivity(Intent(this, Activity_Estoque::class.java))
            finish()
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

        // Cabeçalho
        findViewById<android.widget.ImageView>(R.id.ExibiProd_ImgBtnHome)?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        findViewById<ImageButton>(R.id.ExibiProd_ImgBtnCarinho)?.setOnClickListener {
            Toast.makeText(this, getString(R.string.msg_already_on_cart), Toast.LENGTH_SHORT).show()
        }
    }
}
