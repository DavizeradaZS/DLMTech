package com.example.dlmtech.api

data class ApiResponse(
    val sucesso: Boolean,
    val mensagem: String,
    val id: Int? = null,
    val nome: String? = null,
    val tipo: String? = null, // Recebe "funcionario", "cliente" ou null
    val nivel_acesso: String? = null
)