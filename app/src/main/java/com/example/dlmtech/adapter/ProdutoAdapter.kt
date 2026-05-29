package com.example.dlmtech.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dlmtech.Activity_produto
import com.example.dlmtech.R
import com.example.dlmtech.api.Produto

class ProdutoAdapter(private val lista: List<Produto>) :
    RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder>() {

    class ProdutoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // =======================================================
        // O ERRO ESTAVA AQUI! Atualizado para o ID real do seu XML
        // =======================================================
        val txtNome: TextView = view.findViewById(R.id.txtCarrinho_Nome)
        val txtValor: TextView = view.findViewById(R.id.ExibiProduto_TxtValor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produto, parent, false)
        return ProdutoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        try {
            val produto = lista[position]

            val nomeSeguro = produto.nome ?: "Produto sem nome"
            val valorSeguro = produto.valor?.toString() ?: "0.00"
            val descSegura = produto.descricao ?: "Sem descrição"

            holder.txtNome.text = nomeSeguro
            holder.txtValor.text = holder.itemView.context.getString(R.string.currency_symbol) + " " + valorSeguro

            // Ação de clicar no quadrado do produto para ver os detalhes
            holder.itemView.setOnClickListener {
                val contexto = holder.itemView.context
                val intent = Intent(contexto, Activity_produto::class.java)

                intent.putExtra("ID_PRODUTO", produto.id)
                intent.putExtra("NOME_PRODUTO", nomeSeguro)
                intent.putExtra("VALOR_PRODUTO", valorSeguro)
                intent.putExtra("DESC_PRODUTO", descSegura)
                intent.putExtra("IMG_PRODUTO", produto.imagem)

                contexto.startActivity(intent)
            }
        } catch (e: Exception) {
            holder.txtNome.text = "Erro ao carregar"
            holder.txtValor.text = "R$ 0.00"
        }
    }

    override fun getItemCount() = lista.size
}