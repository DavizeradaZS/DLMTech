<?php
include 'config.php';
$produto_id = $_POST['produto_id'];
$sql = "INSERT INTO carrinho (produto_id) VALUES ('$produto_id')";
echo json_encode(["sucesso" => $conn->query($sql)]);
?>