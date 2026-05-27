<?php
include 'config.php';

$id        = $_POST['id']       ?? '';
$nome      = $_POST['nome']     ?? '';
$dataNasc  = $_POST['dataNasc'] ?? ''; // Nomenclatura igual a do Android!
$cpf       = $_POST['cpf']      ?? '';
$cep       = $_POST['cep']      ?? '';
$rua       = $_POST['rua']      ?? '';
$bairro    = $_POST['bairro']   ?? '';
$numero    = $_POST['numero']   ?? '';

$stmt = $conn->prepare("UPDATE clientes SET nome=?, data_nasc=?, cpf=?, cep=?, rua=?, bairro=?, numero=? WHERE id=?");
$stmt->bind_param("sssssssi", $nome, $dataNasc, $cpf, $cep, $rua, $bairro, $numero, $id);

if ($stmt->execute()) {
    echo json_encode(["sucesso" => true, "mensagem" => "Cliente atualizado!"]);
} else {
    echo json_encode(["sucesso" => false, "mensagem" => "Erro: " . $stmt->error]);
}
?>