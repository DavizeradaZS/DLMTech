package com.example.dlmtech

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dlmtech.api.RetrofitClient
import com.example.dlmtech.api.ApiResponse // Importação atualizada para ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class activity_login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val edtEmail = findViewById<EditText>(R.id.editTextText) // ID do seu XML
        val edtSenha = findViewById<EditText>(R.id.editTextText4)
        val btnEntrar = findViewById<Button>(R.id.button)

        btnEntrar.setOnClickListener {
            // O .trim() remove espaços vazios no começo ou final que o usuário possa ter digitado sem querer
            val email = edtEmail.text.toString().trim()
            val senha = edtSenha.text.toString().trim()

            if (email.isNotEmpty() && senha.isNotEmpty()) {
                // Trocamos Callback<Usuario> por Callback<ApiResponse>
                RetrofitClient.instance.login(email, senha).enqueue(object : Callback<ApiResponse> {

                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                        if (response.isSuccessful) {
                            val resposta = response.body()

                            // Verifica se o PHP retornou "sucesso" como true
                            if (resposta != null && resposta.sucesso) {
                                // Exibe a mensagem "Bem-vindo, Nome" vinda do banco
                                Toast.makeText(this@activity_login, resposta.mensagem, Toast.LENGTH_SHORT).show()

                                // =======================================================
                                // SALVANDO AS CREDENCIAIS NA MEMÓRIA LOCAL (SHAREDPREFERENCES)
                                // =======================================================
                                val preferences = getSharedPreferences("DLMTechPrefs", MODE_PRIVATE)
                                val editor = preferences.edit()
                                editor.putInt("USER_ID", resposta.id ?: -1)
                                editor.putString("TIPO_USUARIO", resposta.tipo)         // "funcionario" ou "cliente"
                                editor.putString("NIVEL_ACESSO", resposta.nivel_acesso) // "Admin" ou "User"
                                editor.apply()

                                // Roteamento limpo para a MainActivity
                                val intent = Intent(this@activity_login, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                // Exibe a mensagem de erro ("Email ou senha incorretos") ou o fallback do strings.xml
                                val errorMsg = resposta?.mensagem ?: getString(R.string.msg_invalid_login)
                                Toast.makeText(this@activity_login, errorMsg, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@activity_login, "Erro no servidor da API", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        Toast.makeText(this@activity_login, getString(R.string.msg_connection_error), Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this@activity_login, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}