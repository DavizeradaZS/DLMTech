<?php
include 'config.php';
$id = $_POST['id'] ?? '';
$stmt = $conn->prepare("DELETE FROM carrinho WHERE id = ?");
$stmt->bind_param("i", $id);
if ($stmt->execute()) {
    echo json_encode(["sucesso" => true, "mensagem" => "Produto removido do carrinho!"]);
} else {
    echo json_encode(["sucesso" => false, "mensagem" => "Erro ao remover"]);
}
?>