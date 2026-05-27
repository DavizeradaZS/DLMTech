<?php
header('Content-Type: application/json; charset=utf-8');
require 'config.php'; // Usa o seu arquivo de conexão atual

$response = array();

// Recebendo os dados do aplicativo
$nome = $_POST['nome'] ?? '';
$data_nasc = $_POST['data_nasc'] ?? '';
$cpf = $_POST['cpf'] ?? '';
$nivel_acesso = $_POST['nivel_acesso'] ?? '';
$data_admissao = $_POST['data_admissao'] ?? '';
$salario = $_POST['salario'] ?? '';
$cep = $_POST['cep'] ?? '';
$rua = $_POST['rua'] ?? '';
$bairro = $_POST['bairro'] ?? '';
$numero = $_POST['numero'] ?? '';

// Montando a inserção no banco de dados (Atenção: verifique se a sua tabela se chama 'funcionarios' mesmo)
$sql = "INSERT INTO funcionarios (nome, data_nasc, cpf, nivel_acesso, data_admissao, salario, cep, rua, bairro, numero) 
        VALUES ('$nome', '$data_nasc', '$cpf', '$nivel_acesso', '$data_admissao', '$salario', '$cep', '$rua', '$bairro', '$numero')";

if (mysqli_query($conn, $sql)) {
    $response['sucesso'] = true;
    $response['mensagem'] = "Funcionário cadastrado com sucesso!";
} else {
    $response['sucesso'] = false;
    $response['mensagem'] = "Erro ao cadastrar: " . mysqli_error($conn);
}

echo json_encode($response);
?>