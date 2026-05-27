<?php
include 'config.php';

$nome     = $_POST['nome']     ?? '';
$dataNasc = $_POST['dataNasc'] ?? '';
$cpf      = $_POST['cpf']      ?? '';
$cep      = $_POST['cep']      ?? '';
$rua      = $_POST['rua']      ?? '';
$bairro   = $_POST['bairro']   ?? '';
$numero   = $_POST['numero']   ?? '';

if (empty($nome) || empty($cpf)) {
    echo json_encode(["sucesso" => false, "mensagem" => "Campos obrigatórios vazios"]);
    exit;
}

// Usando Prepared Statements para segurança
$stmt = $conn->prepare("INSERT INTO clientes (nome, data_nasc, cpf, cep, rua, bairro, numero) VALUES (?, ?, ?, ?, ?, ?, ?)");
$stmt->bind_param("sssssss", $nome, $dataNasc, $cpf, $cep, $rua, $bairro, $numero);

if ($stmt->execute()) {
    echo json_encode(["sucesso" => true, "mensagem" => "Cadastro realizado com sucesso!"]);
} else {
    echo json_encode(["sucesso" => false, "mensagem" => "Erro ao cadastrar: " . $stmt->error]);
}
$stmt->close();
$conn->close();
?>