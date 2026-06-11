<?php
include 'config.php';

// Pegamos todos os campos da tabela carrinho + o nome do produto da tabela produtos
$sql = "SELECT c.id, c.produto_id, c.cliente_id, c.funcionario_id, c.quantidade, c.valor, p.nome
        FROM carrinho c 
        JOIN produtos p ON c.produto_id = p.id";

$result = $conn->query($sql);
$itens = [];

while($row = $result->fetch_assoc()) {
    $row['id'] = (int)$row['id'];
    $row['produto_id'] = (int)$row['produto_id'];
    $row['cliente_id'] = (int)$row['cliente_id'];
    $row['funcionario_id'] = (int)$row['funcionario_id'];
    $row['quantidade'] = (int)$row['quantidade'];
    $itens[] = $row;
}

header('Content-Type: application/json; charset=utf-8');
echo json_encode($itens);
?>