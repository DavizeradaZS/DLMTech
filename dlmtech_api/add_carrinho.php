<?php
include 'config.php';

$id = (int) ($_POST['id'] ?? 0);
$produto_id = (int) ($_POST['produto_id'] ?? 0);
$cliente_id = (int) ($_POST['cliente_id'] ?? 0);
$funcionario_id = (int) ($_POST['funcionario_id'] ?? 0);
$quantidade = (int) ($_POST['quantidade'] ?? 1);
$valor = $_POST['valor'] ?? '0.00';

if ($id > 0) {
    // Update existing entry
    $sql = "UPDATE carrinho SET
            produto_id = $produto_id,
            cliente_id = $cliente_id,
            funcionario_id = $funcionario_id,
            quantidade = $quantidade,
            valor = '$valor'
            WHERE id = $id";
} else if ($produto_id > 0) {
    // Insert new entry
    $sql = "INSERT INTO carrinho (produto_id, cliente_id, funcionario_id, quantidade, valor)
            VALUES ($produto_id, $cliente_id, $funcionario_id, $quantidade, '$valor')";
} else {
    echo json_encode(["sucesso" => false, "mensagem" => "Dados insuficientes."]);
    exit;
}

if ($conn->query($sql)) {
    echo json_encode(["sucesso" => true, "mensagem" => "Venda cadastrada com sucesso!"]);
} else {
    echo json_encode(["sucesso" => false, "mensagem" => "Erro ao processar: " . $conn->error]);
}

$conn->close();
?>