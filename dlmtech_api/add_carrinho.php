<?php
include 'config.php';

$produto_id = (int) $_POST['produto_id'];
$cliente_id = (int) $_POST['cliente_id'];

$sql = "INSERT INTO carrinho (produto_id, cliente_id) VALUES ($produto_id, $cliente_id)";

echo json_encode(["sucesso" => $conn->query($sql)]);
?>