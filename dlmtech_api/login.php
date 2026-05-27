<?php
include 'config.php';

$email = $_POST['email'] ?? '';
$senha = $_POST['senha'] ?? '';

if (empty($email) || empty($senha)) {
    echo json_encode(["sucesso" => false, "mensagem" => "Preencha todos os campos."]);
    exit;
}

// Verifica primeiro se é um FUNCIONÁRIO
$stmtFunc = $conn->prepare("SELECT nome FROM funcionarios WHERE email = ? AND senha = ? LIMIT 1");
$stmtFunc->bind_param("ss", $email, $senha);
$stmtFunc->execute();
$resultFunc = $stmtFunc->get_result();

if ($resultFunc->num_rows > 0) {
    $row = $resultFunc->fetch_assoc();
    echo json_encode(["sucesso" => true, "mensagem" => "Bem-vindo, " . $row['nome'], "tipo" => "funcionario"]);
    $stmtFunc->close();
    exit;
}
$stmtFunc->close();

// Se não for funcionário, verifica se é CLIENTE
$stmtCli = $conn->prepare("SELECT nome FROM clientes WHERE email = ? AND senha = ? LIMIT 1");
$stmtCli->bind_param("ss", $email, $senha);
$stmtCli->execute();
$resultCli = $stmtCli->get_result();

if ($resultCli->num_rows > 0) {
    $row = $resultCli->fetch_assoc();
    echo json_encode(["sucesso" => true, "mensagem" => "Bem-vindo, " . $row['nome'], "tipo" => "cliente"]);
} else {
    // Se não achou em nenhuma das tabelas
    echo json_encode(["sucesso" => false, "mensagem" => "Email ou senha incorretos."]);
}

$stmtCli->close();
$conn->close();
?>