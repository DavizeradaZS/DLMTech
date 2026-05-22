package com.example.dlmtech.api

data class Usuario(
    val id: Int,
    val nome: String,
    val cpf: String,
    val email: String,
    val mensagem: String? = null,
    val sucesso: Boolean = false
)