package com.example.dlmtech

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dlmtech.adapter.UsuarioAdapter
import com.example.dlmtech.api.RetrofitClient
import com.example.dlmtech.api.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Activity_VisualizarClientes : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_clientes)

        // Pega o RecyclerView que você definiu no XML
        recyclerView = findViewById(R.id.rvClientes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        buscarClientes()
    }

    // Essa função garante que a lista atualize se você voltar da tela de edição
    override fun onResume() {
        super.onResume()
        buscarClientes()
    }

    private fun buscarClientes() {
        RetrofitClient.instance.listarClientes().enqueue(object : Callback<List<Usuario>> {
            override fun onResponse(call: Call<List<Usuario>>, response: Response<List<Usuario>>) {
                if (response.isSuccessful) {
                    val clientes = response.body() ?: emptyList()
                    configurarAdapter(clientes)
                }
            }

            override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                Toast.makeText(this@Activity_VisualizarClientes, "Erro de conexão", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun configurarAdapter(clientes: List<Usuario>) {
        val adapter = UsuarioAdapter(
            lista = clientes,
            onEditClick = { cliente ->
                // Abre a tela de edição passando o ID do cliente
                val intent = Intent(this, Activity_Edit_Cliente::class.java)

                // Obs: Aqui presumimos que sua classe Usuario.kt tem o campo 'id'
                intent.putExtra("ID_CLIENTE", cliente.id)
                startActivity(intent)
            },
            onDeleteClick = { cliente ->
                // Abre o popup de confirmação
                cliente.id?.let { confirmarExclusao(it) } // Se o seu id não puder ser nulo, use apenas: confirmarExclusao(cliente.id)
            }
        )
        recyclerView.adapter = adapter
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
        RetrofitClient.instance.deletarCliente(id).enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Activity_VisualizarClientes, "Excluído com sucesso!", Toast.LENGTH_SHORT).show()
                    buscarClientes() // Recarrega a lista do banco
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                Toast.makeText(this@Activity_VisualizarClientes, "Erro ao excluir", Toast.LENGTH_SHORT).show()
            }
        })
    }
}