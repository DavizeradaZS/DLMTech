package com.example.dlmtech.api

import com.google.gson.annotations.SerializedName

data class Usuario(
    val id: Int,
    val nome: String,

    @SerializedName("data_nasc")
    val dataNasc: String? = null,

    val cpf: String,
    val cep: String? = null,
    val rua: String? = null,
    val bairro: String? = null,
    val numero: String? = null,

    @SerializedName("nivel_acesso")
    val nivelAcesso: String? = null,

    @SerializedName("data_admissao")
    val dataAdmissao: String? = null,

    val salario: String? = null,
    val email: String? = null
)