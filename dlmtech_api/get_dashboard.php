<?php
include 'config.php';

$response = [
    "sucesso" => false,
    "maisVendidos" => [],
    "totalProdutos" => 0,
    "topVendedorNome" => "Nenhum",
    "topVendedorVendas" => 0
];

// 1. CALCULA O TOTAL DE PRODUTOS (Resolve o erro do contador)
$sqlTotal = "SELECT COUNT(*) as total FROM produtos";
$resTotal = $conn->query($sqlTotal);
if ($resTotal && $rowTotal = $resTotal->fetch_assoc()) {
    $response["totalProdutos"] = (int)$rowTotal['total'];
}

// 2. BUSCA O PRODUTO MAIS VENDIDO
// O truque: 'SUM(v.quantidade) as quantidade_estoque' evita que você precise mexer na classe do Kotlin!
$sqlProdutos = "SELECT p.id, p.nome, p.valor, p.imagem, p.descricao, SUM(v.quantidade) as quantidade_estoque
                FROM vendas v
                JOIN produtos p ON v.id_produto = p.id
                GROUP BY p.id
                ORDER BY quantidade_estoque DESC
                LIMIT 1";

$resProdutos = $conn->query($sqlProdutos);
if ($resProdutos && $rowProd = $resProdutos->fetch_assoc()) {
    $rowProd['id'] = (int)$rowProd['id'];
    $rowProd['quantidade_estoque'] = (int)$rowProd['quantidade_estoque'];
    $response["maisVendidos"][] = $rowProd;
}

// 3. BUSCA O FUNCIONÁRIO DESTAQUE
$sqlVendedor = "SELECT f.nome, SUM(v.quantidade) as total_vendas
                FROM vendas v
                JOIN funcionarios f ON v.id_funcionario = f.id
                GROUP BY v.id_funcionario
                ORDER BY total_vendas DESC
                LIMIT 1";

$resVendedor = $conn->query($sqlVendedor);
if ($resVendedor && $rowVend = $resVendedor->fetch_assoc()) {
    $response["topVendedorNome"] = $rowVend["nome"];
    $response["topVendedorVendas"] = (int)$rowVend["total_vendas"];
}

$response["sucesso"] = true;

header('Content-Type: application/json; charset=utf-8');
echo json_encode($response);
?>