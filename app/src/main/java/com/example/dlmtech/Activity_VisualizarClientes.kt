package com.example.dlmtech

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View // Importação necessária para o View.GONE
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dlmtech.adapter.UsuarioAdapter
import com.example.dlmtech.api.ApiResponse
import com.example.dlmtech.api.RetrofitClient
import com.example.dlmtech.api.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Activity_VisualizarClientes : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAdicionar: ImageButton
    private lateinit var editPesquisa: EditText
    private var adapter: UsuarioAdapter? = null
    private var listaCompleta: List<Usuario> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_clientes)

        // 1. Inicialização de componentes
        recyclerView = findViewById(R.id.Estoque_produtos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnAdicionar = findViewById(R.id.btnAdicionarCliente)
        editPesquisa = findViewById(R.id.ExibiProd_TxtPesquisa)

        // 2. Configuração de Pesquisa (Filtro)
        editPesquisa.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filtrarClientes(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // 3. Botão (+) Adicionar Cliente
        btnAdicionar.setOnClickListener {
            startActivity(Intent(this, activity_cadastro::class.java))
        }

        configurarNavegacao()
        buscarClientes()
    }

    override fun onResume() {
        super.onResume()
        buscarClientes()
    }

    private fun buscarClientes() {
        RetrofitClient.instance.listarClientes().enqueue(object : Callback<List<Usuario>> {
            override fun onResponse(call: Call<List<Usuario>>, response: Response<List<Usuario>>) {
                if (response.isSuccessful) {
                    listaCompleta = response.body() ?: emptyList()
                    configurarAdapter(listaCompleta)
                }
            }
            override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                Toast.makeText(this@Activity_VisualizarClientes, "Erro de conexão", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filtrarClientes(texto: String) {
        val listaFiltrada = listaCompleta.filter {
            it.nome.contains(texto, ignoreCase = true) || it.cpf.contains(texto)
        }
        adapter?.atualizarLista(listaFiltrada)
    }

    private fun configurarAdapter(clientes: List<Usuario>) {
        adapter = UsuarioAdapter(
            lista = clientes,
            onEditClick = { cliente ->
                val intent = Intent(this, Activity_Edit_Cliente::class.java)
                intent.putExtra("ID", cliente.id)
                intent.putExtra("NOME", cliente.nome)
                intent.putExtra("CPF", cliente.cpf)
                startActivity(intent)
            },
            onDeleteClick = { cliente ->
                confirmarExclusao(cliente.id)
            }
        )
        recyclerView.adapter = adapter
    }

    private fun configurarNavegacao() {
        // --- Cabeçalho ---
        findViewById<ImageView>(R.id.ExibiProd_ImgBtnHome).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        findViewById<ImageButton>(R.id.ExibiProd_ImgBtnCarinho).setOnClickListener {
            startActivity(Intent(this, Activity_Carrinho::class.java))
        }

        // --- Barra Inferior ---
        findViewById<ImageButton>(R.id.btnNavHome).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        findViewById<ImageButton>(R.id.btnNavEstoque).setOnClickListener {
            startActivity(Intent(this, Activity_Estoque::class.java))
            finish()
        }
        findViewById<ImageButton>(R.id.btnNavClientes).setOnClickListener {
            Toast.makeText(this, "Você já está em Clientes", Toast.LENGTH_SHORT).show()
        }

        // =======================================================
        // CONTROLE DE ACESSO CENTRALIZADO PARA O BOTÃO FUNCIONÁRIOS
        // =======================================================
        val btnNavFuncionarios = findViewById<ImageButton>(R.id.btnNavFuncionarios)

        val preferences = getSharedPreferences("DLMTechPrefs", MODE_PRIVATE)
        val tipoUsuario = preferences.getString("TIPO_USUARIO", "cliente") ?: "cliente"
        val nivelAcesso = preferences.getString("NIVEL_ACESSO", "") ?: ""

        if (tipoUsuario.equals("cliente", ignoreCase = true) || nivelAcesso.equals("User", ignoreCase = true)) {
            btnNavFuncionarios.visibility = View.GONE
        }

        btnNavFuncionarios.setOnClickListener {
            // NAVEGAÇÃO ATUALIZADA: Agora abre a tela de funcionários
            startActivity(Intent(this, Activity_VisualizarFuncionarios::class.java))
            finish()
        }
    }

    private fun confirmarExclusao(id: Int) {
        AlertDialog.Builder(this)
            .setTitle("Excluir Cliente")
            .setMessage("Tem certeza que deseja excluir este cliente?")
            .setPositiveButton("Sim") { _, _ -> deletarCliente(id) }
            .setNegativeButton("Não", null)
            .show()
    }

    private fun deletarCliente(id: Int) {
        // Alterado de Callback<Usuario> para Callback<ApiResponse>
        RetrofitClient.instance.deletarCliente(id).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Activity_VisualizarClientes, "Excluído com sucesso!", Toast.LENGTH_SHORT).show()
                    buscarClientes()
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@Activity_VisualizarClientes, "Erro ao excluir", Toast.LENGTH_SHORT).show()
            }
        })
    }
}