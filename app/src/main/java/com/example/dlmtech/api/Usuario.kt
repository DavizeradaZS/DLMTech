package com.example.dlmtech.api

data class Usuario(
    val nome: String,
    val email: String,
    val mensagem: String? = null,
    val sucesso: Boolean = false
)