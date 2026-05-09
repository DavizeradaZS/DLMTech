package com.example.dlmtech.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("cadastro.php")
    fun cadastrarUsuario(
        @Field("nome") nome: String,
        @Field("dataNasc") dataNasc: String,
        @Field("cpf") cpf: String,
        @Field("cep") cep: String,
        @Field("rua") rua: String,
        @Field("bairro") bairro: String,
        @Field("numero") numero: String
    ): Call<Usuario>
}