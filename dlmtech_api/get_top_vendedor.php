<?php
include 'config.php';

// Consulta que junta a tabela de vendas com a de funcionários,
// soma as quantidades vendidas, agrupa por funcionário e pega o maior.
$sql = "SELECT f.nome, SUM(v.quantidade) as total_vendas
        FROM vendas v
        JOIN funcionarios f ON v.id_funcionario = f.id
        GROUP BY v.id_funcionario
        ORDER BY total_vendas DESC
        LIMIT 1";

$result = $conn->query($sql);

$vendedor = null;

if ($result && $result->num_rows > 0) {
    $row = $result->fetch_assoc();

    // Garantir que o total venha como inteiro para o Kotlin ler corretamente
    $row['total_vendas'] = (int)$row['total_vendas'];

    $vendedor = $row;
} else {
    // Caso o banco esteja vazio ou não tenha vendas, retorna um valor padrão
    $vendedor = [
        "nome" => "Sem registros",
        "total_vendas" => 0
    ];
}

header('Content-Type: application/json; charset=utf-8');
echo json_encode($vendedor);
?>