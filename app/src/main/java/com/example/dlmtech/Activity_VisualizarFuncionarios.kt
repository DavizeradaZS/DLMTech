package com.example.dlmtech

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

class Activity_VisualizarFuncionarios : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAdicionar: ImageButton
    private lateinit var editPesquisa: EditText
    private var adapter: UsuarioAdapter? = null
    private var listaCompleta: List<Usuario> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_funcionarios)

        // Inicialização
        recyclerView = findViewById(R.id.rvListaFuncionarios)
        recyclerView.layoutManager = LinearLayoutManager(this)
        btnAdicionar = findViewById(R.id.btnAdicionarFuncionario)
        editPesquisa = findViewById(R.id.ExibiProd_TxtPesquisa)

        // Lógica de Pesquisa
        editPesquisa.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filtrarFuncionarios(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Ir para Cadastro de Funcionário
        btnAdicionar.setOnClickListener {
            startActivity(Intent(this, Activity_CadastroFunc::class.java))
        }

        configurarNavegacao()
        buscarFuncionarios()
    }

    override fun onResume() {
        super.onResume()
        buscarFuncionarios()
    }

    private fun buscarFuncionarios() {
        RetrofitClient.instance.listarFuncionarios().enqueue(object : Callback<List<Usuario>> {
            override fun onResponse(call: Call<List<Usuario>>, response: Response<List<Usuario>>) {
                if (response.isSuccessful) {
                    listaCompleta = response.body() ?: emptyList()
                    configurarAdapter(listaCompleta)
                }
            }
            override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                Toast.makeText(this@Activity_VisualizarFuncionarios, "Erro de conexão", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filtrarFuncionarios(texto: String) {
        val listaFiltrada = listaCompleta.filter {
            it.nome.contains(texto, ignoreCase = true) || it.cpf.contains(texto)
        }
        adapter?.atualizarLista(listaFiltrada)
    }

    private fun configurarAdapter(funcionarios: List<Usuario>) {
        adapter = UsuarioAdapter(
            lista = funcionarios,
            onEditClick = { func ->
                val intent = Intent(this, Activity_Edit_Func::class.java)
                intent.putExtra("ID", func.id)
                intent.putExtra("NOME", func.nome)
                intent.putExtra("CPF", func.cpf)
                startActivity(intent)
            },
            onDeleteClick = { func ->
                confirmarExclusao(func.id)
            }
        )
        recyclerView.adapter = adapter
    }

    private fun configurarNavegacao() {
        // Cliques no Cabeçalho
        findViewById<ImageView>(R.id.ExibiProd_ImgBtnHome).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        findViewById<ImageButton>(R.id.ExibiProd_ImgBtnCarinho).setOnClickListener {
            startActivity(Intent(this, Activity_Carrinho::class.java))
        }

        // Barra Inferior
        findViewById<ImageButton>(R.id.btnNavHome).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        findViewById<ImageButton>(R.id.btnNavEstoque).setOnClickListener {
            startActivity(Intent(this, Activity_Estoque::class.java))
            finish()
        }
        findViewById<ImageButton>(R.id.btnNavClientes).setOnClickListener {
            startActivity(Intent(this, Activity_VisualizarClientes::class.java))
            finish()
        }
        findViewById<ImageButton>(R.id.btnNavFuncionarios).setOnClickListener {
            Toast.makeText(this, "Você já está em Funcionários", Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmarExclusao(id: Int) {
        AlertDialog.Builder(this)
            .setTitle("Excluir Funcionário")
            .setMessage("Deseja realmente excluir este funcionário?")
            .setPositiveButton("Sim") { _, _ -> deletarFuncionario(id) }
            .setNegativeButton("Não", null)
            .show()
    }

    private fun deletarFuncionario(id: Int) {
        // Alterado aqui de <Usuario> para <ApiResponse>
        RetrofitClient.instance.deletarFuncionario(id).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Activity_VisualizarFuncionarios, "Funcionário excluído!", Toast.LENGTH_SHORT).show()
                    buscarFuncionarios()
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@Activity_VisualizarFuncionarios, "Erro ao excluir", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
