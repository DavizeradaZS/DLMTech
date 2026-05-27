<?php
$host = "thyagoquintas.com.br"; // Host do banco online
$user = "engenharia_191";          // Usuário do banco
$pass = "loboguara";            // Senha do banco
$db   = "engenharia_191";       // Nome da base de dados

$conn = new mysqli($host, $user, $pass, $db);
$conn->set_charset("utf8mb4");

if ($conn->connect_error) {
    die(json_encode(["sucesso" => false, "mensagem" => "Falha na conexão: " . $conn->connect_error]));
}
?>