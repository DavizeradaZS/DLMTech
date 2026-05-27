<?php
include 'config.php';

$sql = "SELECT id, nome, data_nasc, cpf, nivel_acesso, data_admissao, salario, cep, rua, bairro, numero FROM funcionarios";
$result = $conn->query($sql);

$funcionarios = [];
while($row = $result->fetch_assoc()) {
    $funcionarios[] = $row;
}

echo json_encode($funcionarios);
$conn->close();
?>