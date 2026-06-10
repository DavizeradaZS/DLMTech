<?php
header('Content-Type: application/json; charset=utf-8');
require 'config.php';

$id = $_POST['id'] ?? '';

if (empty($id)) {
    echo json_encode(["sucesso" => false, "mensagem" => "ID não fornecido ou inválido."]);
    exit;
}

// Utilizando prepared statements para maior segurança
$stmt = $conn->prepare("DELETE FROM categorias WHERE id = ?");
$stmt->bind_param("i", $id);

if ($stmt->execute()) {
    echo json_encode(["sucesso" => true, "mensagem" => "Categoria removida com sucesso!"]);
} else {
    echo json_encode(["sucesso" => false, "mensagem" => "Erro ao remover categoria: " . $stmt->error]);
}

$stmt->close();
$conn->close();
?>