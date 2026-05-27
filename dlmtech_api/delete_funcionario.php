<?php
include 'config.php';

$id = $_POST['id'] ?? '';

if (empty($id)) {
    echo json_encode(["sucesso" => false, "mensagem" => "ID não fornecido ou inválido."]);
    exit;
}

$stmt = $conn->prepare("DELETE FROM funcionarios WHERE id = ?");
$stmt->bind_param("i", $id);

if ($stmt->execute()) {
    echo json_encode(["sucesso" => true, "mensagem" => "Funcionário removido com sucesso!"]);
} else {
    echo json_encode(["sucesso" => false, "mensagem" => "Erro ao remover funcionário: " . $stmt->error]);
}

$stmt->close();
$conn->close();
?>