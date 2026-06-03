package com.example.dlmtech

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dlmtech.api.ApiService
import com.example.dlmtech.api.ApiResponse
import com.example.dlmtech.api.Endereco
import com.example.dlmtech.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Activity_CadastroFunc : AppCompatActivity() {

    private lateinit var editRua: EditText
    private lateinit var editBairro: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastro_func)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editNome = findViewById<EditText>(R.id.edtNome)
        val editDataNasc = findViewById<EditText>(R.id.edtDataNasc)
        val editCPF = findViewById<EditText>(R.id.edtCPF)
        val editEmail = findViewById<EditText>(R.id.edtEmail) // Novo campo!
        val editSenha = findViewById<EditText>(R.id.edtSenha) // Novo campo!
        val editAdm = findViewById<EditText>(R.id.edtAdm)
        val editDataAdm = findViewById<EditText>(R.id.edtDataAdm)
        val editSal = findViewById<EditText>(R.id.edtSal)
        val editCep = findViewById<EditText>(R.id.edtCep)
        val editNum = findViewById<EditText>(R.id.edtNum)

        editRua = findViewById(R.id.edtRua)
        editBairro = findViewById(R.id.edtBairro)

        val btnCancelar = findViewById<Button>(R.id.btnCancelar)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)

        btnCancelar.setOnClickListener { finish() }

        btnSalvar.setOnClickListener {
            val nome = editNome.text.toString()
            val email = editEmail.text.toString().trim()
            val senha = editSenha.text.toString().trim()
            val dataNasc = editDataNasc.text.toString()
            val cpf = editCPF.text.toString()
            val nivelAcesso = editAdm.text.toString()
            val dataAdmissao = editDataAdm.text.toString()
            val salario = editSal.text.toString()
            val cep = editCep.text.toString()
            val rua = editRua.text.toString()
            val bairro = editBairro.text.toString()
            val numero = editNum.text.toString()

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha pelo menos Nome, Email e Senha!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            RetrofitClient.instance.cadastrarFuncionario(
                nome, email, senha, dataNasc, cpf, nivelAcesso, dataAdmissao, salario, cep, rua, bairro, numero
            ).enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful) {
                        val resposta = response.body()
                        if (resposta != null && resposta.sucesso) {
                            Toast.makeText(this@Activity_CadastroFunc, resposta.mensagem, Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            Toast.makeText(this@Activity_CadastroFunc, resposta?.mensagem ?: "Erro ao cadastrar", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Toast.makeText(this@Activity_CadastroFunc, "Erro de conexão com a API", Toast.LENGTH_LONG).show()
                }
            })
        }

        editCep.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val cepLimpo = s.toString().replace("-", "").replace(" ", "")
                if (cepLimpo.length == 8) {
                    buscarCep(cepLimpo)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // NAVEGAÇÃO
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
            finish()
        }
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
                    if (end != null && end.logradouro != null) {
                        editRua.setText(end.logradouro)
                        editBairro.setText(end.bairro)
                    }
                }
            }
            override fun onFailure(call: Call<Endereco>, t: Throwable) {
                Toast.makeText(this@Activity_CadastroFunc, "Erro ao buscar CEP", Toast.LENGTH_SHORT).show()
            }
        })
    }
}