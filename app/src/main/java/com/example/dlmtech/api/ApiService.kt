package com.example.dlmtech.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.GET

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
    ): Call<Usuario> // Note que aqui fechamos a função!

    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("email") email: String,
        @Field("senha") senha: String
    ): Call<Usuario>

    @GET("get_produtos.php")
    fun listarProdutos(): Call<List<Produto>>
}