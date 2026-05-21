<?php
include 'config.php';

$email = $_POST['email'] ?? '';
$senha = $_POST['senha'] ?? '';

$sql = "SELECT nome FROM funcionarios WHERE email = '$email' AND senha = '$senha' LIMIT 1";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    echo json_encode(["sucesso" => true, "mensagem" => "Logado!"]);
} else {
    echo json_encode(["sucesso" => false, "mensagem" => "Falha!"]);
}
?>