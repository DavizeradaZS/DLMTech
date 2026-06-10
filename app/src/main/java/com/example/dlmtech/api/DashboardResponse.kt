package com.example.dlmtech.api

data class DashboardResponse(
    var sucesso: Boolean,
    var maisVendidos: List<Produto>, // Reutiliza a sua classe Produto já existente
    var topVendedorNome: String?,
    var topVendedorVendas: Int?
)
