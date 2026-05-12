package com.example.dlmtech

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dlmtech.api.RetrofitClient
import com.example.dlmtech.api.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Activity_Edit_Cliente : AppCompatActivity() {
    private var clienteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_cliente)

        // Mapeando os campos
        val edtNome = findViewById<EditText>(R.id.edtNome)
        val edtCpf = findViewById<EditText>(R.id.edtCPF)
        val edtCep = findViewById<EditText>(R.id.edtCep)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)

        // Recebendo dados da Intent (Simulando que vieram da lista)
        clienteId = intent.getIntExtra("ID", -1)
        edtNome.setText(intent.getStringExtra("NOME"))
        edtCpf.setText(intent.getStringExtra("CPF"))

        btnSalvar.setOnClickListener {
            val nome = edtNome.text.toString()
            val cpf = edtCpf.text.toString()
            val cep = edtCep.text.toString()
            // ... pegar os outros campos do layout

            RetrofitClient.instance.updateCliente(clienteId, nome, "", cpf, cep, "", "", "")
                .enqueue(object : Callback<Usuario> {
                    override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@Activity_Edit_Cliente, "Cliente atualizado!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                    override fun onFailure(call: Call<Usuario>, t: Throwable) {
                        Toast.makeText(this@Activity_Edit_Cliente, "Erro na conexão", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}