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

class Activity_Edit_Func : AppCompatActivity() {
    private var funcId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_func)

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

        btnCancelar.setOnClickListener {
            Toast.makeText(this, getString(R.string.msg_registration_cancelled), Toast.LENGTH_SHORT).show()
            finish()
        }

        funcId = intent.getIntExtra("ID", -1)
        edtNome.setText(intent.getStringExtra("NOME"))

        btnSalvar.setOnClickListener {
            // Alterado para ApiResponse
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
            ).enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful) {
                        val resposta = response.body()
                        if (resposta != null && resposta.sucesso) {
                            Toast.makeText(this@Activity_Edit_Func, resposta.mensagem, Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@Activity_Edit_Func, resposta?.mensagem ?: "Erro ao atualizar", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Toast.makeText(this@Activity_Edit_Func, getString(R.string.msg_failure_with_reason, t.message), Toast.LENGTH_SHORT).show()
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