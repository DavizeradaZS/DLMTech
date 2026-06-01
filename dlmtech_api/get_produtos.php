<?php
include 'config.php';

// Seleciona todos os campos necessários para o modelo Produto.kt
$sql = "SELECT id, nome, valor, descricao, imagem FROM produtos";
$result = $conn->query($sql);

$produtos = [];

if ($result) {
    while($row = $result->fetch_assoc()) {
        // Garantir que o ID seja retornado como inteiro
        $row['id'] = (int)$row['id'];

        // Se desejar retornar a URL completa da imagem descomente a linha abaixo:
        // if ($row['imagem']) {
        //     $row['imagem'] = "https://seu-site.com.br/dlmtech_api/uploads/" . $row['imagem'];
        // }

        $produtos[] = $row;
    }
}

header('Content-Type: application/json; charset=utf-8');
echo json_encode($produtos);
?>