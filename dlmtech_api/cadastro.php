<?php
// 1. Configurações de conexão 
$host = "localhost";
$user = "root";
$pass = "";
$db   = "db_dlmtech";

$conn = new mysqli($host, $user, $pass, $db);

// Verifica a conexão
if ($conn->connect_error) {
    die(json_encode(["mensagem" => "Falha na conexão: " . $conn->connect_error]));
}

// 2. Recebendo os dados via POST (Nomes devem ser iguais aos do @Field no ApiService.kt)
$nome      = $_POST['nome']      ?? '';
$dataNasc  = $_POST['dataNasc']  ?? '';
$cpf       = $_POST['cpf']       ?? '';
$cep       = $_POST['cep']       ?? '';
$rua       = $_POST['rua']       ?? '';
$bairro    = $_POST['bairro']    ?? '';
$numero    = $_POST['numero']    ?? '';

// 3. Validação básica
if (empty($nome) || empty($cpf)) {
    echo json_encode(["mensagem" => "Campos obrigatórios vazios"]);
    exit;
}

// 4. Preparando a SQL para inserir no banco 
$sql = "INSERT INTO clientes (nome, data_nasc, cpf, cep, rua, bairro, numero) 
        VALUES ('$nome', '$dataNasc', '$cpf', '$cep', '$rua', '$bairro', '$numero')";

if ($conn->query($sql) === TRUE) {
    // Resposta em JSON que o Retrofit vai ler
    echo json_encode([
        "nome" => $nome,
        "email" => "", // Opcional, se quiser devolver algo
        "mensagem" => "Cadastro realizado com sucesso!"
    ]);
} else {
    echo json_encode(["mensagem" => "Erro ao cadastrar: " . $conn->error]);
}

$conn->close();
?>