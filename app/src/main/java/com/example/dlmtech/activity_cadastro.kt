package com.example.dlmtech

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.dlmtech.api.RetrofitClient
import com.example.dlmtech.api.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.text.TextWatcher
import android.text.Editable
import com.example.dlmtech.api.ApiService
import com.example.dlmtech.api.Endereco
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class activity_cadastro : AppCompatActivity() {

    // Declarar os EditTexts aqui para que a função buscarCep consiga acessá-los
    private lateinit var editRua: EditText
    private lateinit var editBairro: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastro)

        val editNome = findViewById<EditText>(R.id.edtNome)
        val editDataNasc = findViewById<EditText>(R.id.edtDataNasc)
        val editCpf = findViewById<EditText>(R.id.edtCPF)
        val editCep = findViewById<EditText>(R.id.edtCep)

        editRua = findViewById(R.id.edtRua)
        editBairro = findViewById(R.id.edtBairro)

        val editNum = findViewById<EditText>(R.id.edtNum)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        btnCancelar.setOnClickListener { finish() }

        findViewById<ImageButton>(R.id.btnNavFuncionarios).setOnClickListener {
            startActivity(Intent(this, Activity_VisualizarFuncionarios::class.java))
            finish()
        }
        findViewById<ImageButton>(R.id.btnNavClientes).setOnClickListener {
            startActivity(Intent(this, Activity_VisualizarClientes::class.java))
            finish()
        }
        findViewById<ImageButton>(R.id.btnNavHome).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        findViewById<ImageButton>(R.id.btnNavEstoque).setOnClickListener {
            startActivity(Intent(this, Activity_Estoque::class.java))
            finish()
        }
        findViewById<ImageButton>(R.id.ExibiProd_ImgBtnCarinho).setOnClickListener {
            startActivity(Intent(this, Activity_Carrinho::class.java))
        }
        findViewById<ImageButton>(R.id.btnNavAnalise).setOnClickListener {
            startActivity(Intent(this, Activity_Sobre::class.java))
        }


        btnSalvar.setOnClickListener {
            val nome = editNome.text.toString()
            val dataNasc = editDataNasc.text.toString()
            val cpf = editCpf.text.toString()
            val cep = editCep.text.toString()
            val rua = editRua.text.toString()
            val bairro = editBairro.text.toString()
            val numero = editNum.text.toString()

            if (nome.isEmpty() || cpf.isEmpty()) {
                Toast.makeText(this, "Preencha os campos obrigatórios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            RetrofitClient.instance.cadastrarUsuario(nome, dataNasc, cpf, cep, rua, bairro, numero)
                .enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                        if (response.isSuccessful) {
                            val resposta = response.body()
                            if (resposta != null && resposta.sucesso) {
                                Toast.makeText(this@activity_cadastro, resposta.mensagem, Toast.LENGTH_LONG).show()
                                finish()
                            } else {
                                Toast.makeText(this@activity_cadastro, resposta?.mensagem ?: "Erro ao cadastrar", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        Toast.makeText(this@activity_cadastro, "Erro de conexão", Toast.LENGTH_LONG).show()
                    }
                })
        }

        // Listener do CEP
        editCep.addTextChangedListener(object : TextWatcher {
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

    } // Fim do onCreate


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
                Toast.makeText(this@activity_cadastro, "Erro ao buscar CEP", Toast.LENGTH_SHORT).show()
            }
        })

    }
}