package com.example.dlmtech.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dlmtech.R
import com.example.dlmtech.api.Carrinho

class CarrinhoAdapter(
    private val lista: MutableList<Carrinho>,
    private val onRemoverClick: (Carrinho) -> Unit
) : RecyclerView.Adapter<CarrinhoAdapter.CarrinhoViewHolder>() {

    class CarrinhoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView = view.findViewById(R.id.txtCarrinho_Nome)
        val valor: TextView = view.findViewById(R.id.txtCarrinho_Valor)
        val btnRemover: Button = view.findViewById(R.id.btnCarrinho_Remover)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarrinhoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carrinho, parent, false)
        return CarrinhoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarrinhoViewHolder, position: Int) {
        val item = lista[position]
        holder.nome.text = item.nome
        holder.valor.text = holder.itemView.context.getString(R.string.currency_symbol) + " " + item.valor
        holder.btnRemover.setOnClickListener { onRemoverClick(item) }
    }

    override fun getItemCount() = lista.size
}