<?php
header('Content-Type: application/json; charset=utf-8');
require 'config.php';

$response = array();

// Recebendo os dados do aplicativo
$nome = $_POST['nome'] ?? '';

// Validação básica
if (empty($nome)) {
    echo json_encode(["sucesso" => false, "mensagem" => "O nome da categoria não pode estar vazio."]);
    exit;
}

// Montando a inserção no banco de dados
$sql = "INSERT INTO categorias (nome) VALUES ('$nome')";

if (mysqli_query($conn, $sql)) {
    $response['sucesso'] = true;
    $response['mensagem'] = "Categoria cadastrada com sucesso!";
} else {
    $response['sucesso'] = false;
    $response['mensagem'] = "Erro ao cadastrar: " . mysqli_error($conn);
}

echo json_encode($response);
?>