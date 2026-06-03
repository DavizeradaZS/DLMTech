package com.example.dlmtech.api

data class Carrinho(
    val id: Int,
    val produto_id: Int,
    val cliente_id: Int,
    val nome: String,
    val valor: String,
    val quantidade: Int
)