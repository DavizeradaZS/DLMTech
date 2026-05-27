<?php
include 'config.php';
$id = $_POST['id'] ?? '';
$stmt = $conn->prepare("DELETE FROM clientes WHERE id = ?");
$stmt->bind_param("i", $id);
if ($stmt->execute()) {
    echo json_encode(["sucesso" => true, "mensagem" => "Cliente deletado!"]);
} else {
    echo json_encode(["sucesso" => false, "mensagem" => "Erro ao deletar"]);
}
?>