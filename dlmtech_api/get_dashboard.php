<?php
include 'config.php';

$response = [
    "sucesso" => false,
    "maisVendidos" => [],
    "totalProdutos" => 0,
    "topVendedorNome" => "Nenhum",
    "topVendedorVendas" => 0
];

// 1. CALCULA O TOTAL DE PRODUTOS
$sqlTotal = "SELECT COUNT(*) as total FROM produtos";
$resTotal = $conn->query($sqlTotal);
if ($resTotal && $rowTotal = $resTotal->fetch_assoc()) {
    $response["totalProdutos"] = (int)$rowTotal['total'];
}

// 2. BUSCA OS PRODUTOS MAIS VENDIDOS (LIMIT 3)
// Se houver vendas, pega os mais vendidos.
// Caso contrário, pega os primeiros 3 produtos cadastrados para não deixar a tela vazia.
$sqlProdutosVendas = "SELECT p.id, p.nome, p.valor, p.imagem, p.descricao, SUM(v.quantidade) as quantidade_estoque
                FROM vendas v
                JOIN produtos p ON v.id_produto = p.id
                GROUP BY p.id
                ORDER BY quantidade_estoque DESC
                LIMIT 3";

$resProdutos = $conn->query($sqlProdutosVendas);

if ($resProdutos && $resProdutos->num_rows > 0) {
    while ($rowProd = $resProdutos->fetch_assoc()) {
        $rowProd['id'] = (int)$rowProd['id'];
        $rowProd['quantidade_estoque'] = (int)$rowProd['quantidade_estoque'];
        $response["maisVendidos"][] = $rowProd;
    }
} else {
    // Se não houver vendas, preenche com os primeiros produtos da tabela
    $sqlFallback = "SELECT id, nome, valor, imagem, descricao, 0 as quantidade_estoque FROM produtos LIMIT 3";
    $resFallback = $conn->query($sqlFallback);
    while ($rowFallback = $resFallback->fetch_assoc()) {
        $rowFallback['id'] = (int)$rowFallback['id'];
        $rowFallback['quantidade_estoque'] = 0;
        $response["maisVendidos"][] = $rowFallback;
    }
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