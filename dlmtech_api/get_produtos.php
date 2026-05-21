<?php
include 'config.php';

$sql = "SELECT nome, valor, descricao FROM produtos";
$result = $conn->query($sql);

$produtos = [];
while($row = $result->fetch_assoc()) {
    $produtos[] = $row;
}

echo json_encode($produtos);
?>