package com.example.dlmtech

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog // Importação do alerta!
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.dlmtech.api.RetrofitClient
import com.example.dlmtech.api.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Activity_produto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produto)

        // Mapeando os campos do layout
        val txtNome = findViewById<TextView>(R.id.ExibiProduto_TxtProduto)
        val txtValor = findViewById<TextView>(R.id.ExibiProduto_TxtValor)
        val txtQuantidade = findViewById<TextView>(R.id.txt_quantidade)
        val txtDesc = findViewById<TextView>(R.id.ExibiProd_TxtDesc)
        val imgProdutoDetalhe = findViewById<ImageView>(R.id.imgExibiProduto_Produto)

        // Recebendo dados
        val idProduto = intent.getIntExtra("ID_PRODUTO", -1)
        val nome = intent.getStringExtra("NOME_PRODUTO") ?: getString(R.string.title_produto)
        val valor = intent.getStringExtra("VALOR_PRODUTO") ?: "0.00"
        val desc = intent.getStringExtra("DESC_PRODUTO") ?: getString(R.string.label_no_description)
        val quantidade = intent.getIntExtra("QUANTIDADE_PRODUTO", 0)
        val imageUrl = intent.getStringExtra("IMG_PRODUTO") ?: ""

        txtNome.text = nome
        txtValor.text = getString(R.string.currency_symbol) + " " + valor
        txtQuantidade.text = "Estoque: $quantidade"
        txtDesc.text = desc

        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .centerCrop()
            .into(imgProdutoDetalhe)

        // ==========================================
        // AÇÕES DOS BOTÕES (ADICIONAR, EDITAR, REMOVER)
        // ==========================================
        val btnAdicionar = findViewById<android.widget.Button>(R.id.TxtAdicionarAoProduto)
        val btnEditar = findViewById<android.widget.Button>(R.id.ExibiProd_BtnEditar)
        val btnRemover = findViewById<android.widget.Button>(R.id.Remover)

        btnAdicionar.setOnClickListener {
            if (idProduto != -1) {
                RetrofitClient.instance.addCarrinho(idProduto).enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                        if (response.isSuccessful) {
                            val resposta = response.body()
                            if (resposta != null && resposta.sucesso) {
                                Toast.makeText(this@Activity_produto, resposta.mensagem, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@Activity_produto, resposta?.mensagem ?: "Erro ao adicionar", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        Toast.makeText(this@Activity_produto, "Erro de conexão", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        // ==========================================
        // LIGANDO O BOTÃO DE EDITAR
        // ==========================================
        btnEditar.setOnClickListener {
            val intentEdit = Intent(this, activity_edit_produto::class.java)
            // Passamos os dados atuais para a tela de edição preencher os campos automaticamente
            intentEdit.putExtra("ID_PRODUTO", idProduto)
            intentEdit.putExtra("NOME_PRODUTO", nome)
            intentEdit.putExtra("VALOR_PRODUTO", valor)
            intentEdit.putExtra("DESC_PRODUTO", desc)
            intentEdit.putExtra("QUANTIDADE_PRODUTO", quantidade)
            startActivity(intentEdit)
        }

        // ==========================================
        // LIGANDO O BOTÃO DE EXCLUIR COM ALERTA
        // ==========================================
        btnRemover.setOnClickListener {
            if (idProduto != -1) {
                // Cria uma janela de confirmação bonita
                AlertDialog.Builder(this)
                    .setTitle("Excluir Produto")
                    .setMessage("Tem certeza que deseja excluir o produto $nome? Esta ação não pode ser desfeita.")
                    .setPositiveButton("Sim") { _, _ ->
                        deletarProdutoNaApi(idProduto) // Chama a função que criamos abaixo
                    }
                    .setNegativeButton("Não", null) // Não faz nada se cancelar
                    .show()
            } else {
                Toast.makeText(this, getString(R.string.msg_invalid_product_id), Toast.LENGTH_SHORT).show()
            }
        }

        configurarNavegacao()
    }

    // =======================================================
    // FUNÇÃO QUE FAZ O REQUEST DE DELETE PARA O XAMPP
    // =======================================================
    private fun deletarProdutoNaApi(id: Int) {
        RetrofitClient.instance.deletarProduto(id).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body()?.sucesso == true) {
                    Toast.makeText(this@Activity_produto, "Produto excluído com sucesso!", Toast.LENGTH_SHORT).show()
                    finish() // Fecha a tela de detalhes e volta para o estoque atualizado
                } else {
                    Toast.makeText(this@Activity_produto, "Erro ao excluir o produto.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                // Alteramos o Toast para mostrar o t.message
                Toast.makeText(this@Activity_produto, "Erro real: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun configurarNavegacao() {
        val btnNavFuncionarios = findViewById<ImageButton>(R.id.btnNavFuncionarios)
        val preferences = getSharedPreferences("DLMTechPrefs", MODE_PRIVATE)
        val tipoUsuario = preferences.getString("TIPO_USUARIO", "cliente") ?: "cliente"
        val nivelAcesso = preferences.getString("NIVEL_ACESSO", "") ?: ""

        if (tipoUsuario.equals("cliente", ignoreCase = true) || nivelAcesso.equals("User", ignoreCase = true)) {
            btnNavFuncionarios.visibility = View.GONE
        }

        btnNavFuncionarios.setOnClickListener {
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
        findViewById<ImageView>(R.id.ExibiProd_ImgBtnHome)?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}