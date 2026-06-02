<?php
header('Content-Type: application/json; charset=utf-8');
require 'config.php';

$response = array();

if (isset($_POST['id'])) {
    $id = intval($_POST['id']);
    $nome = isset($_POST['nome']) ? $_POST['nome'] : '';
    $valor = isset($_POST['valor']) ? $_POST['valor'] : '';
    $descricao = isset($_POST['descricao']) ? $_POST['descricao'] : '';

    // Atualiza os dados do produto específico
    $sql = "UPDATE produtos SET nome = ?, valor = ?, descricao = ? WHERE id = ?";
    $stmt = $conn->prepare($sql);

    if ($stmt) {
        $stmt->bind_param("sssi", $nome, $valor, $descricao, $id);
        if ($stmt->execute()) {
            $response['sucesso'] = true;
            $response['mensagem'] = "Produto atualizado com sucesso!";
        } else {
            $response['sucesso'] = false;
            $response['mensagem'] = "Erro ao atualizar no banco de dados.";
        }
        $stmt->close();
    }
} else {
    $response['sucesso'] = false;
    $response['mensagem'] = "ID do produto não informado.";
}

$conn->close();
echo json_encode($response);
?>