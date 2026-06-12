package com.example.dlmtech

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dlmtech.api.RetrofitClient
import com.example.dlmtech.api.ApiResponse // Adicionado importação
import com.example.dlmtech.api.ApiService
import com.example.dlmtech.api.Endereco
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Activity_Edit_Cliente : AppCompatActivity() {
    private var clienteId: Int = -1
    // Declarar os EditTexts aqui para que a função buscarCep consiga acessá-los
    private lateinit var editRua: EditText
    private lateinit var editBairro: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_cliente)

        val edtNome = findViewById<EditText>(R.id.edtNome)
        val edtCpf = findViewById<EditText>(R.id.edtCPF)
        val edtCep = findViewById<EditText>(R.id.edtCep)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)


        editRua = findViewById(R.id.edtRua)
        editBairro = findViewById(R.id.edtBairro)

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
            startActivity(Intent(this, Activity_Sobre::class.java))
        }
        findViewById<ImageButton>(R.id.ExibiProd_ImgBtnCarinho)?.setOnClickListener {
            startActivity(Intent(this, Activity_Carrinho::class.java))
        }

        // Listener do CEP
        edtCep.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Remove traços ou espaços acidentais
                val cepLimpo = s.toString().replace("-", "").replace(" ", "")

                // Se tiver exatamente 8 números, ele busca sozinho!
                if (cepLimpo.length == 8) {
                    buscarCep(cepLimpo)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun buscarCep(cep: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://viacep.com.br/ws/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)
        api.buscarCep("$cep/json/").enqueue(object : Callback<Endereco> {
            override fun onResponse(call: Call<Endereco>, response: Response<Endereco>) {
                if (response.isSuccessful) {
                    val end = response.body()
                    if (end != null) {
                        editRua.setText(end.logradouro)
                        editBairro.setText(end.bairro)
                    }
                }
            }
            override fun onFailure(call: Call<Endereco>, t: Throwable) {
                Toast.makeText(this@Activity_Edit_Cliente, "Erro ao buscar CEP", Toast.LENGTH_SHORT).show()
            }
        })

    }

}