package com.example.dlmtech.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Url
import retrofit2.http.Path


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
    ): Call<ApiResponse>

    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("email") email: String,
        @Field("senha") senha: String
    ): Call<ApiResponse>

    @GET("get_produtos.php")
    fun listarProdutos(): Call<List<Produto>>

    @FormUrlEncoded
    @POST("update_cliente.php")
    fun updateCliente(
        @Field("id") id: Int,
        @Field("nome") nome: String,
        @Field("dataNasc") dataNasc: String,
        @Field("cpf") cpf: String,
        @Field("cep") cep: String,
        @Field("rua") rua: String,
        @Field("bairro") bairro: String,
        @Field("numero") numero: String
    ): Call<ApiResponse>

    @FormUrlEncoded
    @POST("update_funcionario.php")
    fun updateFuncionario(
        @Field("id") id: Int,
        @Field("nome") nome: String,
        @Field("data_nasc") data_nasc: String,
        @Field("cpf") cpf: String,
        @Field("nivel_acesso") nivel_acesso: String,
        @Field("data_admissao") data_admissao: String,
        @Field("salario") salario: String,
        @Field("cep") cep: String,
        @Field("rua") rua: String,
        @Field("bairro") bairro: String,
        @Field("numero") numero: String
    ): Call<ApiResponse>

    @FormUrlEncoded
    @POST("add_carrinho.php")
    fun addCarrinho(@Field("produto_id") produtoId: Int): Call<ApiResponse>

    @GET("get_carrinho.php")
    fun listarCarrinho(): Call<List<Carrinho>>

    @FormUrlEncoded
    @POST("remove_carrinho.php")
    fun removerDoCarrinho(@Field("id") id: Int): Call<ApiResponse>

    // ==========================================
    // ROTAS PARA CLIENTES E FUNCIONÁRIOS
    // ==========================================

    @GET("get_clientes.php")
    fun listarClientes(): Call<List<Usuario>>

    @FormUrlEncoded
    @POST("delete_cliente.php")
    fun deletarCliente(@Field("id") id: Int): Call<ApiResponse>

    @GET("get_funcionarios.php")
    fun listarFuncionarios(): Call<List<Usuario>>

    @FormUrlEncoded
    @POST("delete_funcionario.php")
    fun deletarFuncionario(@Field("id") id: Int): Call<ApiResponse>

    @GET
    fun buscarCep(@Url url: String): Call<Endereco>

    @FormUrlEncoded
    @POST("cadastro_funcionario.php")
    fun cadastrarFuncionario(
        @Field("nome") nome: String,
        @Field("email") email: String,
        @Field("senha") senha: String,
        @Field("data_nasc") data_nasc: String,
        @Field("cpf") cpf: String,
        @Field("nivel_acesso") nivel_acesso: String,
        @Field("data_admissao") data_admissao: String,
        @Field("salario") salario: String,
        @Field("cep") cep: String,
        @Field("rua") rua: String,
        @Field("bairro") bairro: String,
        @Field("numero") numero: String
    ): Call<ApiResponse>
}