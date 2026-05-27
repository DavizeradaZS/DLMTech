package com.example.dlmtech.api

data class Endereco(
    val logradouro: String,
    val bairro: String,
    val localidade: String, // Cidade
    val uf: String
)