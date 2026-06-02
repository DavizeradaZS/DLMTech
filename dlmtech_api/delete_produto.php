<?php
header('Content-Type: application/json; charset=utf-8');
require 'config.php';

$response = array();

if (isset($_POST['id'])) {
    $id = intval($_POST['id']);

    // PASSO 1: O NOME DA COLUNA CORRETO É 'produto_id'
    $sqlCarrinho = "DELETE FROM carrinho WHERE produto_id = ?";
    $stmtCarrinho = $conn->prepare($sqlCarrinho);
    if ($stmtCarrinho) {
        $stmtCarrinho->bind_param("i", $id);
        $stmtCarrinho->execute();
        $stmtCarrinho->close();
    }

    // PASSO 2: Agora sim, deleta o produto da tabela principal
    $sql = "DELETE FROM produtos WHERE id = ?";
    $stmt = $conn->prepare($sql);
    
    if ($stmt) {
        $stmt->bind_param("i", $id);
        if ($stmt->execute()) {
            $response['sucesso'] = true;
            $response['mensagem'] = "Produto excluído com sucesso!";
        } else {
            $response['sucesso'] = false;
            $response['mensagem'] = "O produto não pôde ser excluído (Erro no banco de dados).";
        }
        $stmt->close();
    }
} else {
    $response['sucesso'] = false;
    $response['mensagem'] = "ID do produto não foi enviado pelo aplicativo.";
}

$conn->close();

// PASSO 3: Blindagem final para garantir que o PHP SEMPRE retorne um JSON válido
$json = json_encode($response);
if ($json === false) {
    echo '{"sucesso": false, "mensagem": "Erro interno de formatação no servidor."}';
} else {
    echo $json;
}
?>