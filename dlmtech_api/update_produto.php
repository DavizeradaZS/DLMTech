<?php
header('Content-Type: application/json; charset=utf-8');
require 'config.php';

$response = array();

if (isset($_POST['id'])) {
    $id = intval($_POST['id']);
    $nome = isset($_POST['nome']) ? $_POST['nome'] : '';
    $valor = isset($_POST['valor']) ? $_POST['valor'] : '';
    $descricao = isset($_POST['descricao']) ? $_POST['descricao'] : '';

    // 1. Recebe o novo campo (quantidade_estoque)
    // Usamos intval() por segurança, para garantir que sempre será um número inteiro
    $quantidade_estoque = isset($_POST['quantidade_estoque']) ? intval($_POST['quantidade_estoque']) : 0;

    // 2. Atualiza o comando SQL para incluir o 'quantidade_estoque = ?'
    $sql = "UPDATE produtos SET nome = ?, valor = ?, descricao = ?, quantidade_estoque = ? WHERE id = ?";
    $stmt = $conn->prepare($sql);

    if ($stmt) {
        // 3. Atualizando o bind_param
        // s = nome (string)
        // d = valor (double/decimal)
        // s = descricao (string)
        // i = quantidade_estoque (integer) -> NOSSO NOVO CAMPO
        // i = id (integer) -> O ID fica por último porque ele está no final do SQL (WHERE id = ?)
        $stmt->bind_param("sdsii", $nome, $valor, $descricao, $quantidade_estoque, $id);

        if ($stmt->execute()) {
            $response['sucesso'] = true;
            $response['mensagem'] = "Produto atualizado com sucesso!";
        } else {
            $response['sucesso'] = false;
            $response['mensagem'] = "Erro ao atualizar no banco de dados.";
        }
        $stmt->close();
    } else {
        // Adicionei isso aqui para ajudar a debugar caso o SQL tenha algum erro de sintaxe
        $response['sucesso'] = false;
        $response['mensagem'] = "Erro no SQL: " . $conn->error;
    }
} else {
    $response['sucesso'] = false;
    $response['mensagem'] = "ID do produto não informado.";
}

$conn->close();
echo json_encode($response);
?>