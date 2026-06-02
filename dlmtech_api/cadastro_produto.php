<?php
header('Content-Type: application/json; charset=utf-8');
require 'config.php';

$response = array();

// Recebe os textos normais
$nome = $_POST['nome'] ?? '';
$descricao = $_POST['descricao'] ?? '';
$valor = $_POST['valor'] ?? '';

$caminhoImagem = null;

// LÓGICA DE UPLOAD DA IMAGEM
// Verifica se uma imagem foi enviada pelo aplicativo
if (isset($_FILES['imagem']) && $_FILES['imagem']['error'] === UPLOAD_ERR_OK) {
    
    // Pasta onde as fotos serão salvas
    $diretorioUpload = 'uploads/';
    
    // Cria a pasta se você esquecer de criar no Passo A
    if (!is_dir($diretorioUpload)) {
        mkdir($diretorioUpload, 0777, true);
    }

    // Pega a extensão do arquivo (ex: .jpg, .png)
    $extensao = pathinfo($_FILES['imagem']['name'], PATHINFO_EXTENSION);
    
    // Cria um nome único e criptografado para a foto não sobrescrever outra
    $nomeArquivoUnico = uniqid('prod_') . '.' . $extensao;
    $caminhoCompleto = $diretorioUpload . $nomeArquivoUnico;

    // Move a foto da memória temporária para a pasta uploads/
    if (move_uploaded_file($_FILES['imagem']['tmp_name'], $caminhoCompleto)) {
        // Se deu certo, salva apenas "uploads/nome_da_foto.jpg" no banco
        $caminhoImagem = $caminhoCompleto;
    } else {
        echo json_encode(["sucesso" => false, "mensagem" => "Erro ao salvar o arquivo da imagem."]);
        exit;
    }
}

// Inserindo no Banco de Dados (Usando Prepared Statement para evitar erros se a descrição tiver aspas)
$sql = "INSERT INTO produtos (nome, descricao, valor, imagem) VALUES (?, ?, ?, ?)";
$stmt = $conn->prepare($sql);
$stmt->bind_param("ssds", $nome, $descricao, $valor, $caminhoImagem);

if ($stmt->execute()) {
    $response['sucesso'] = true;
    $response['mensagem'] = "Produto e imagem cadastrados com sucesso!";
} else {
    $response['sucesso'] = false;
    $response['mensagem'] = "Erro no banco de dados: " . $stmt->error;
}

$stmt->close();
$conn->close();

echo json_encode($response);
?>