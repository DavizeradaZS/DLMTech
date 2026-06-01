package com.example.dlmtech.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dlmtech.Activity_produto
import com.example.dlmtech.R
import com.example.dlmtech.api.Produto

class ProdutoAdapter(private val lista: List<Produto>) :
    RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder>() {

    class ProdutoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNome: TextView = view.findViewById(R.id.txtCarrinho_Nome)
        val txtValor: TextView = view.findViewById(R.id.ExibiProduto_TxtValor)
        val imgProduto: ImageView = view.findViewById(R.id.imgExibiProduto_Produto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produto, parent, false)
        return ProdutoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produto = lista[position]

        holder.txtNome.text = produto.nome ?: "Sem nome"
        holder.txtValor.text = "R$ " + (produto.valor ?: "0.00")

        // 1. MONTA A URL COMPLETA (Corrigido para dlmtech_api conforme RetrofitClient)
        val urlBase = "http://192.168.15.5/dlmtech_api/uploads/"
        val urlFinal = if (!produto.imagem.isNullOrEmpty()) {
            urlBase + produto.imagem
        } else {
            null
        }

        // 2. CARREGAR IMAGEM COM GLIDE
        Glide.with(holder.itemView.context)
            .load(urlFinal)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.imgProduto)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, Activity_produto::class.java)
            intent.putExtra("ID_PRODUTO", produto.id)
            intent.putExtra("NOME_PRODUTO", produto.nome)
            intent.putExtra("VALOR_PRODUTO", produto.valor)
            intent.putExtra("DESC_PRODUTO", produto.descricao)
            intent.putExtra("IMG_PRODUTO", urlFinal)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = lista.size
}