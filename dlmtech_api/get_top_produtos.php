<?php
include 'config.php';

// Consulta que junta a tabela de vendas com a de produtos,
// soma as quantidades vendidas, agrupa por produto e pega os 5 mais vendidos.
$sql = "SELECT p.nome, SUM(v.quantidade) as total_vendido
        FROM vendas v
        JOIN produtos p ON v.id_produto = p.id
        GROUP BY v.id_produto
        ORDER BY total_vendido DESC
        LIMIT 5";

$result = $conn->query($sql);

$top_produtos = [];

if ($result) {
    while($row = $result->fetch_assoc()) {

        // Garantir que o total venha como inteiro
        $row['total_vendido'] = (int)$row['total_vendido'];

        $top_produtos[] = $row;
    }
}

header('Content-Type: application/json; charset=utf-8');
echo json_encode($top_produtos);
?>