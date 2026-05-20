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
import com.example.dlmtech.api.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class activity_cadastro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastro)

        // 1. Mapeando os componentes do XML
        val editNome = findViewById<EditText>(R.id.edtNome)
        val editDataNasc = findViewById<EditText>(R.id.edtDataNasc)
        val editCpf = findViewById<EditText>(R.id.edtCPF)
        val editCep = findViewById<EditText>(R.id.edtCep)
        val editRua = findViewById<EditText>(R.id.edtRua)
        val editBairro = findViewById<EditText>(R.id.edtBairro)
        val editNum = findViewById<EditText>(R.id.edtNum)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        // Ação do botão Cancelar
        btnCancelar.setOnClickListener {
            Toast.makeText(this, "Cadastro cancelado", Toast.LENGTH_SHORT).show()
            finish() // Fecha a tela e volta para a anterior
        }

        // 2. Ação do botão Salvar
        btnSalvar.setOnClickListener {
            val nome = editNome.text.toString()
            val dataNasc = editDataNasc.text.toString()
            val cpf = editCpf.text.toString()
            val cep = editCep.text.toString()
            val rua = editRua.text.toString()
            val bairro = editBairro.text.toString()
            val numero = editNum.text.toString()

            // Validação simples
            if (nome.isEmpty() || cpf.isEmpty()) {
                Toast.makeText(this, "Preencha os campos obrigatórios!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 3. Chamando a API via Retrofit
            RetrofitClient.instance.cadastrarUsuario(nome, dataNasc, cpf, cep, rua, bairro, numero)
                .enqueue(object : Callback<Usuario> {
                    override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@activity_cadastro, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show()
                            finish() // Fecha a tela após salvar
                        } else {
                            Toast.makeText(this@activity_cadastro, "Erro no servidor: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Usuario>, t: Throwable) {
                        Toast.makeText(this@activity_cadastro, "Falha de conexão: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
        }



        // ==========================================
        // NAVEGAÇÃO DA BARRA INFERIOR
        // ==========================================
        val btnNavFuncionarios = findViewById<ImageButton>(R.id.btnNavFuncionarios)
        val btnNavClientes = findViewById<ImageButton>(R.id.btnNavClientes)
        val btnNavHome = findViewById<ImageButton>(R.id.btnNavHome)
        val btnNavEstoque = findViewById<ImageButton>(R.id.btnNavEstoque)
        val btnNavAnalise = findViewById<ImageButton>(R.id.btnNavAnalise)

        btnNavFuncionarios.setOnClickListener {
            startActivity(Intent(this, Activity_CadastroFunc::class.java))
            finish()
        }
        btnNavEstoque.setOnClickListener {
            startActivity(Intent(this, Activity_Estoque::class.java))
            finish()
        }
        btnNavHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        btnNavClientes.setOnClickListener {
            Toast.makeText(this, "Tela de Clientes em desenvolvimento", Toast.LENGTH_SHORT).show()
        }
        btnNavAnalise.setOnClickListener {
            Toast.makeText(this, "Tela de Análise em desenvolvimento", Toast.LENGTH_SHORT).show()
        }

        // ==========================================
        // NAVEGAÇÃO DO CABEÇALHO (HEADER)
        // ==========================================
        val btnCarrinho = findViewById<ImageButton>(R.id.ExibiProd_ImgBtnCarinho)
        btnCarrinho?.setOnClickListener {
            startActivity(Intent(this, Activity_Carrinho::class.java))
        }


    }
}