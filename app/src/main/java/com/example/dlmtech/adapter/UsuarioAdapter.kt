package com.example.dlmtech.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dlmtech.R
import com.example.dlmtech.api.Usuario

class UsuarioAdapter(
    private var lista: List<Usuario>,
    private val onEditClick: (Usuario) -> Unit,
    private val onDeleteClick: (Usuario) -> Unit
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    class UsuarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNome: TextView = view.findViewById(R.id.txtNomeUsuario)
        val txtInfoSecundaria: TextView = view.findViewById(R.id.txtInfoSecundaria)
        val btnEditar: ImageButton = view.findViewById(R.id.btnEditarUsuario)
        val btnDeletar: ImageButton = view.findViewById(R.id.btnDeletarUsuario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = lista[position]

        holder.txtNome.text = usuario.nome

        // Como cliente usa CPF e funcionário pode usar CPF ou Nível de Acesso,
        // colocamos o CPF aqui como padrão. Ajuste se sua classe Usuario tiver outro campo melhor!
        //holder.txtInfoSecundaria.text = usuario.cpf ?: "Sem informação"

        // Ações dos botões que serão definidas nas Activities depois
        holder.btnEditar.setOnClickListener { onEditClick(usuario) }
        holder.btnDeletar.setOnClickListener { onDeleteClick(usuario) }
    }

    override fun getItemCount() = lista.size
}