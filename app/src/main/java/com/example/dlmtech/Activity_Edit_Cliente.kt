package com.example.dlmtech

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dlmtech.api.RetrofitClient
import com.example.dlmtech.api.ApiResponse // Adicionado importação
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Activity_Edit_Cliente : AppCompatActivity() {
    private var clienteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_cliente)

        val edtNome = findViewById<EditText>(R.id.edtNome)
        val edtCpf = findViewById<EditText>(R.id.edtCPF)
        val edtCep = findViewById<EditText>(R.id.edtCep)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        btnCancelar.setOnClickListener {
            Toast.makeText(this, getString(R.string.msg_registration_cancelled), Toast.LENGTH_SHORT).show()
            finish()
        }

        clienteId = intent.getIntExtra("ID", -1)
        edtNome.setText(intent.getStringExtra("NOME"))
        edtCpf.setText(intent.getStringExtra("CPF"))

        btnSalvar.setOnClickListener {
            val nome = edtNome.text.toString()
            val cpf = edtCpf.text.toString()
            val cep = edtCep.text.toString()

            // Alterado para ApiResponse
            RetrofitClient.instance.updateCliente(clienteId, nome, "", cpf, cep, "", "", "")
                .enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                        if (response.isSuccessful) {
                            val resposta = response.body()
                            if (resposta != null && resposta.sucesso) {
                                Toast.makeText(this@Activity_Edit_Cliente, resposta.mensagem, Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(this@Activity_Edit_Cliente, resposta?.mensagem ?: "Erro ao atualizar", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        Toast.makeText(this@Activity_Edit_Cliente, getString(R.string.msg_connection_error), Toast.LENGTH_SHORT).show()
                    }
                })
        }

        // NAVEGAÇÃO
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
            Toast.makeText(this, getString(R.string.msg_in_development, getString(R.string.label_analysis)), Toast.LENGTH_SHORT).show()
        }
        findViewById<ImageButton>(R.id.ExibiProd_ImgBtnCarinho)?.setOnClickListener {
            startActivity(Intent(this, Activity_Carrinho::class.java))
        }
    }
}