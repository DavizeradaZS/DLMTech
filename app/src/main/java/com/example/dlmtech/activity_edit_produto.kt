package com.example.dlmtech

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dlmtech.api.RetrofitClient
import com.example.dlmtech.api.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class activity_edit_produto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_produto)

        val edtNome = findViewById<EditText>(R.id.edtNome)
        val edtValor = findViewById<EditText>(R.id.edtValor)
        val edtDesc = findViewById<EditText>(R.id.edtDesc)
        val edtQuantidade = findViewById<EditText>(R.id.edtQuantidade)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        // Preenche os campos com os dados atuais do produto
        val idProduto = intent.getIntExtra("ID_PRODUTO", -1)
        edtNome.setText(intent.getStringExtra("NOME_PRODUTO"))
        edtValor.setText(intent.getStringExtra("VALOR_PRODUTO"))
        edtDesc.setText(intent.getStringExtra("DESC_PRODUTO"))
        edtQuantidade.setText(intent.getIntExtra("QUANTIDADE_PRODUTO", 0).toString())

        // O botão cancelar apenas fecha a tela
        btnCancelar.setOnClickListener {
            finish()
        }

        // O botão salvar envia os dados novos para o XAMPP
        btnSalvar.setOnClickListener {
            if (idProduto != -1) {
                val nomeAtualizado = edtNome.text.toString()
                val valorAtualizado = edtValor.text.toString()
                val descAtualizada = edtDesc.text.toString()
                val quantidadeAtualizada = edtQuantidade.text.toString().toIntOrNull() ?: 0

                RetrofitClient.instance.updateProduto(idProduto, nomeAtualizado, valorAtualizado, descAtualizada, quantidadeAtualizada)
                    .enqueue(object : Callback<ApiResponse> {
                        override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                            if (response.isSuccessful && response.body()?.sucesso == true) {
                                Toast.makeText(this@activity_edit_produto, "Produto atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                                finish() // Volta para a tela anterior
                            } else {
                                Toast.makeText(this@activity_edit_produto, "Erro ao atualizar", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                            Toast.makeText(this@activity_edit_produto, "Erro de conexão com servidor", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
    }
}