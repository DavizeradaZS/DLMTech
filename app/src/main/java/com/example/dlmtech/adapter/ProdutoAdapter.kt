package com.example.dlmtech.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
        holder.txtValor.text = "R$ ${produto.valor}"


    }

    override fun getItemCount() = lista.size
}