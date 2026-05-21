<?php
include 'config.php';
// Usamos JOIN para pegar o nome e valor direto da tabela de produtos
$sql = "SELECT c.id, c.produto_id, p.nome, p.valor, c.quantidade 
        FROM carrinho c 
        JOIN produtos p ON c.produto_id = p.id";
$result = $conn->query($sql);
$itens = [];
while($row = $result->fetch_assoc()) { $itens[] = $row; }
echo json_encode($itens);
?>