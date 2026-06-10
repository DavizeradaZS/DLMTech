<?php
include 'config.php';

$response = [
    "sucesso" => false,
    "maisVendidos" => [],
    "topVendedorNome" => "Sem dados",
    "topVendedorVendas" => 0
];

/* * 1. CONSULTA: TOP 3 PRODUTOS
 * Nota: Estou usando uma tabela hipotética 'vendas'.
 * Se os seus pedidos finalizados ficam em outra tabela (ex: 'itens_pedido'), altere o nome abaixo.
 */
$sqlProdutos = "SELECT p.id, p.nome, p.valor, p.imagem, SUM(v.quantidade) as total_vendido
                FROM vendas v
                JOIN produtos p ON v.id_produto = p.id
                GROUP BY p.id
                ORDER BY total_vendido DESC
                LIMIT 3";

$resProdutos = $conn->query($sqlProdutos);
if ($resProdutos) {
    while($row = $resProdutos->fetch_assoc()) {
        $response["maisVendidos"][] = $row;
    }
}

/* * 2. CONSULTA: FUNCIONÁRIO DESTAQUE
 */
$sqlVendedor = "SELECT f.nome, SUM(v.quantidade) as total_vendas
                FROM vendas v
                JOIN funcionarios f ON v.id_funcionario = f.id
                GROUP BY v.id_funcionario
                ORDER BY total_vendas DESC
                LIMIT 1";

$resVendedor = $conn->query($sqlVendedor);
if ($resVendedor && $resVendedor->num_rows > 0) {
    $row = $resVendedor->fetch_assoc();
    $response["topVendedorNome"] = $row["nome"];
    $response["topVendedorVendas"] = (int)$row["total_vendas"];
}

$response["sucesso"] = true;

header('Content-Type: application/json; charset=utf-8');
echo json_encode($response);
?>