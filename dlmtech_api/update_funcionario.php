<?php
include 'config.php';

// Recebendo todos os campos do layout de edição
$id             = $_POST['id'];
$nome           = $_POST['nome'];
$data_nasc      = $_POST['data_nasc'];
$cpf            = $_POST['cpf'];
$nivel_acesso   = $_POST['nivel_acesso'];
$data_admissao  = $_POST['data_admissao'];
$salario        = $_POST['salario'];
$cep            = $_POST['cep'];
$rua            = $_POST['rua'];
$bairro         = $_POST['bairro'];
$numero         = $_POST['numero'];

$sql = "UPDATE funcionarios SET 
        nome='$nome', data_nasc='$data_nasc', cpf='$cpf', 
        nivel_acesso='$nivel_acesso', data_admissao='$data_admissao', 
        salario='$salario', cep='$cep', rua='$rua', 
        bairro='$bairro', numero='$numero' 
        WHERE id='$id'";

if ($conn->query($sql) === TRUE) {
    echo json_encode(["sucesso" => true, "mensagem" => "Funcionário atualizado!"]);
} else {
    echo json_encode(["sucesso" => false, "mensagem" => $conn->error]);
}
?>