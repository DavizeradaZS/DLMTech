package com.example.dlmtech.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dlmtech.Activity_produto
import com.example.dlmtech.R
import com.example.dlmtech.activity_edit_produto
import com.example.dlmtech.api.ApiResponse
import com.example.dlmtech.api.Produto
import com.example.dlmtech.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProdutoAdapter(private val lista: List<Produto>) :
    RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder>() {

    class ProdutoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNome: TextView = view.findViewById(R.id.txtCarrinho_Nome)
        val txtValor: TextView = view.findViewById(R.id.ExibiProduto_TxtValor)
        val txtQuantidade: TextView = view.findViewById(R.id.txt_quantidade)
        val imgProduto: ImageView = view.findViewById(R.id.imgExibiProduto_Produto)

        // Mapeando os 3 botões do seu item_produto.xml
        val btnAdd: Button = view.findViewById(R.id.ExibiProd_BtnAdicionar)
        val btnEdit: Button = view.findViewById(R.id.ExibiProd_BtnEditar)
        val btnRemove: Button = view.findViewById(R.id.Remover)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produto, parent, false)
        return ProdutoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        try {
            val produto = lista[position]
            val contexto = holder.itemView.context

            val nomeSeguro = produto.nome ?: "Produto sem nome"
            val valorSeguro = produto.valor?.toString() ?: "0.00"
            val descSegura = produto.descricao ?: "Sem descrição"
            val quantidadeSegura = produto.quantidade_estoque

            holder.txtNome.text = nomeSeguro
            holder.txtValor.text = contexto.getString(R.string.currency_symbol) + " " + valorSeguro
            holder.txtQuantidade.text = "Estoque: $quantidadeSegura"

            val baseUrl = "http://192.168.15.5/dlmtech_api/" // Confirme se o IP continua o mesmo!
            val imageUrl = if (!produto.imagem.isNullOrEmpty()) baseUrl + produto.imagem else ""

            Glide.with(contexto)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .centerCrop()
                .into(holder.imgProduto)

            // 1. CLICAR NO FUNDO (Vai para detalhes)
            holder.itemView.setOnClickListener {
                val intent = Intent(contexto, Activity_produto::class.java)
                intent.putExtra("ID_PRODUTO", produto.id)
                intent.putExtra("NOME_PRODUTO", nomeSeguro)
                intent.putExtra("VALOR_PRODUTO", valorSeguro)
                intent.putExtra("DESC_PRODUTO", descSegura)
                intent.putExtra("QUANTIDADE_PRODUTO", quantidadeSegura)
                intent.putExtra("IMG_PRODUTO", imageUrl)
                contexto.startActivity(intent)
            }

            // 2. BOTÃO ADICIONAR (Carrinho)
            holder.btnAdd.setOnClickListener {
                val preferences = contexto.getSharedPreferences("DLMTechPrefs", Context.MODE_PRIVATE)
                val clienteId = preferences.getInt("USER_ID", -1)

                if (clienteId != -1) {
                    RetrofitClient.instance.addCarrinho(produto.id, clienteId).enqueue(object : Callback<ApiResponse> {
                        override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                            if (response.isSuccessful && response.body()?.sucesso == true) {
                                Toast.makeText(contexto, "Adicionado ao carrinho!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(contexto, response.body()?.mensagem ?: "Erro ao adicionar", Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                            Toast.makeText(contexto, "Erro de conexão", Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    Toast.makeText(contexto, "Erro: Usuário não logado.", Toast.LENGTH_SHORT).show()
                }
            }

            // 3. BOTÃO EDITAR
            holder.btnEdit.setOnClickListener {
                val intent = Intent(contexto, activity_edit_produto::class.java)
                intent.putExtra("ID_PRODUTO", produto.id)
                intent.putExtra("NOME_PRODUTO", nomeSeguro)
                intent.putExtra("VALOR_PRODUTO", valorSeguro)
                intent.putExtra("DESC_PRODUTO", descSegura)
                intent.putExtra("QUANTIDADE_PRODUTO", quantidadeSegura)
                contexto.startActivity(intent)
            }

            // 4. BOTÃO REMOVER
            holder.btnRemove.setOnClickListener {
                AlertDialog.Builder(contexto)
                    .setTitle("Excluir Produto")
                    .setMessage("Tem certeza que deseja excluir $nomeSeguro da lista?")
                    .setPositiveButton("Sim") { _, _ ->
                        RetrofitClient.instance.deletarProduto(produto.id).enqueue(object : Callback<ApiResponse> {
                            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                                if (response.isSuccessful && response.body()?.sucesso == true) {
                                    Toast.makeText(contexto, "Produto excluído!", Toast.LENGTH_SHORT).show()
                                    // Truque ninja para recarregar a tela de Estoque instantaneamente
                                    if (contexto is android.app.Activity) {
                                        contexto.recreate()
                                    }
                                }
                            }
                            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {}
                        })
                    }
                    .setNegativeButton("Não", null)
                    .show()
            }

        } catch (e: Exception) {
            holder.txtNome.text = "Erro"
        }
    }

    override fun getItemCount() = lista.size
}
