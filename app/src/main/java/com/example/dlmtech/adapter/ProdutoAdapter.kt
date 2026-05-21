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
        val txtNome: TextView = view.findViewById(R.id.ExibiProduto_TxtProduto)
        val txtValor: TextView = view.findViewById(R.id.ExibiProduto_TxtValor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produto, parent, false)
        return ProdutoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produto = lista[position]
        holder.txtNome.text = produto.nome
        holder.txtValor.text = holder.itemView.context.getString(R.string.currency_symbol) + " " + produto.valor

        holder.itemView.setOnClickListener {
            val contexto = holder.itemView.context
            val intent = Intent(contexto, Activity_produto::class.java)

            // Passando os dados com os nomes exatos que a Activity_produto está esperando
            intent.putExtra("NOME_PRODUTO", produto.nome)
            intent.putExtra("VALOR_PRODUTO", produto.valor.toString()) // usando 'valor'

            // Se a sua classe Produto tiver um campo de descrição, passe-o também:
            // intent.putExtra("DESC_PRODUTO", produto.descricao)

            // Passar o ID é ótimo para quando formos fazer o Editar/Deletar funcionarem de verdade
            intent.putExtra("ID_PRODUTO", produto.id)

            contexto.startActivity(intent)
        }
    }

    override fun getItemCount() = lista.size


}

