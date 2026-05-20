package com.example.dlmtech

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dlmtech.api.RetrofitClient
import com.example.dlmtech.api.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Activity_Edit_Func : AppCompatActivity() {
    private var funcId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_func)

        // 1. Mapear todos os IDs do layout
        val edtNome = findViewById<EditText>(R.id.edtNome)
        val edtDataNasc = findViewById<EditText>(R.id.edtDataNasc)
        val edtCpf = findViewById<EditText>(R.id.edtCPF)
        val edtAdm = findViewById<EditText>(R.id.edtAdm)
        val edtDataAdm = findViewById<EditText>(R.id.edtDataAdm)
        val edtSal = findViewById<EditText>(R.id.edtSal)
        val edtCep = findViewById<EditText>(R.id.edtCep)
        val edtRua = findViewById<EditText>(R.id.edtRua)
        val edtBairro = findViewById<EditText>(R.id.edtBairro)
        val edtNum = findViewById<EditText>(R.id.edtNum)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        // Ação do botão Cancelar
        btnCancelar.setOnClickListener {
            Toast.makeText(this, "Cadastro cancelado", Toast.LENGTH_SHORT).show()
            finish() // Fecha a tela e volta para a anterior
        }

        // 2. Receber dados da Intent
        funcId = intent.getIntExtra("ID", -1)
        edtNome.setText(intent.getStringExtra("NOME"))

        // 3. Ação de Salvar
        btnSalvar.setOnClickListener {
            RetrofitClient.instance.updateFuncionario(
                funcId,
                edtNome.text.toString(),
                edtDataNasc.text.toString(),
                edtCpf.text.toString(),
                edtAdm.text.toString(),
                edtDataAdm.text.toString(),
                edtSal.text.toString(),
                edtCep.text.toString(),
                edtRua.text.toString(),
                edtBairro.text.toString(),
                edtNum.text.toString()
            ).enqueue(object : Callback<Usuario> {
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@Activity_Edit_Func, "Sucesso!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    Toast.makeText(this@Activity_Edit_Func, "Falha: ${t.message}", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this, "Você já está na tela de Cadastro de Funcionários", Toast.LENGTH_SHORT).show()
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