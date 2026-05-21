<?php
include 'config.php';

$id        = $_POST['id'];
$nome      = $_POST['nome'];
$data_nasc = $_POST['data_nasc'];
$cpf       = $_POST['cpf'];
$cep       = $_POST['cep'];
$rua       = $_POST['rua'];
$bairro    = $_POST['bairro'];
$numero    = $_POST['numero'];

$sql = "UPDATE clientes SET 
        nome='$nome', data_nasc='$data_nasc', cpf='$cpf', 
        cep='$cep', rua='$rua', bairro='$bairro', numero='$numero' 
        WHERE id='$id'";

if ($conn->query($sql) === TRUE) {
    echo json_encode(["sucesso" => true, "mensagem" => "Cliente atualizado!"]);
} else {
    echo json_encode(["sucesso" => false, "mensagem" => $conn->error]);
}
?>