package com.example.dlmtech.api

data class Produto(
    val id: Int = 0,
    val nome: String? = "Produto sem nome",
    val descricao: String? = "Sem descrição",
    val valor: String? = "0.00",
    val imagem: String? = null
)