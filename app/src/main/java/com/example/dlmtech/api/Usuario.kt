package com.example.dlmtech.api

data class Usuario(
    val nome: String,
    val dataNasc: String,
    val cpf: String,
    val cep: String,
    val rua: String,
    val bairro: String,
    val numero: String,
    val mensagem: String? = null // O servidor PHP pode retornar uma mensagem de sucesso
)