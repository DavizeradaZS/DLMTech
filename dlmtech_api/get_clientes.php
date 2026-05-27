<?php
include 'config.php';
$sql = "SELECT id, nome, data_nasc, cpf, cep, rua, bairro, numero FROM clientes";
$result = $conn->query($sql);
$clientes = [];
while($row = $result->fetch_assoc()) { $clientes[] = $row; }
echo json_encode($clientes);
?>