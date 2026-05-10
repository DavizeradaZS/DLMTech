package com.example.dlmtech.api

data class Produto(
    val id: Int,
    val nome: String,
    val valor: String,
    val descricao: String, // Novo campo para o layout activity_produto.xml
    val imagem_url: String? = null
)