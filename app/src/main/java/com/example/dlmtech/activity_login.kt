package com.example.dlmtech

import android.content.Intent
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

class activity_login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val edtEmail = findViewById<EditText>(R.id.editTextText) // ID do seu XML
        val edtSenha = findViewById<EditText>(R.id.editTextText4)
        val btnEntrar = findViewById<Button>(R.id.button)

        btnEntrar.setOnClickListener {
            val email = edtEmail.text.toString()
            val senha = edtSenha.text.toString()

            RetrofitClient.instance.login(email, senha).enqueue(object : Callback<Usuario> {
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    if (response.isSuccessful && response.body()?.sucesso == true) {
                        startActivity(Intent(this@activity_login, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@activity_login, getString(R.string.msg_invalid_login), Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    Toast.makeText(this@activity_login, getString(R.string.msg_connection_error), Toast.LENGTH_SHORT).show()
                }
            })
        }


    }
}