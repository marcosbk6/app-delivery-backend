package com.app_delivery_test1.backend.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType; // Para MediaType
import org.springframework.web.multipart.MultipartFile; // Para MultipartFile
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pratos") // URL base para os endpoints
@CrossOrigin(origins = "http://localhost:4200")

public class PratoController {

    private static final Logger logger = LoggerFactory.getLogger(PratoController.class);

    private final List<Prato> pratos = new ArrayList<>(); // Lista para armazenar os pratos
    private final PratoService pratoService;

    @Autowired // Injeção de dependência
    public PratoController(PratoService pratoService) {
        this.pratoService = pratoService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // Indica que o método aceita multipart/form-data

    public Prato adicionarPrato(@RequestParam("nomePrato") String nomePrato,
                                @RequestParam("preco") double preco,
                                @RequestParam("imagem") String imagemUrl) {


        logger.info("Recebendo requisição para adicionar prato. Nome: {}, Preço: {}", nomePrato, preco);

        Prato prato = new Prato();
        prato.setNome(nomePrato);
        prato.setPreco(preco);

        // Aqui você pode adicionar lógica para salvar a imagem, se necessário

        if (imagemUrl != null && !imagemUrl.isEmpty()) {

            prato.setImagemUrl(imagemUrl.trim()); // Armazena a URL da imagem
        } else {
            logger.warn("Nenhuma URL de imagem recebida na requisição");
        }

        pratoService.salvarPrato(prato);

       
        return prato; // Retorna o prato adicionado
    }

    // Método para salvar a imagem e retornar o caminho/URL
    private String salvarImagem(MultipartFile imagem) {
        try {
            // Define o diretório onde as imagens serão salvas

            String diretorio = System.getProperty("user.dir") + "/src/main/resources/static/imagens/";

            File dir = new File(diretorio);
            if (!dir.exists()) {
                boolean created = dir.mkdirs(); // Cria o diretório se não existir
                logger.info("Diretório de imagens criado: {}", created);
            }

            String originalFileName = imagem.getOriginalFilename();
            String sanitizedFileName = originalFileName.replaceAll("[^a-zA-Z0-9.-]", "-"); // Substitui caracteres indesejados


            // Salva o arquivo no diretório especificado
            File file = new File(diretorio + sanitizedFileName);
            imagem.transferTo(file);
            logger.info("Imagem salva em: {}", file.getAbsolutePath());

            // Retorna o caminho da imagem salva (ajuste conforme necessário)
            return "imagens/" + sanitizedFileName; // Ajuste para retornar apenas o nome do arquivo

        } catch (IOException e) {
            logger.error("Erro ao salvar a imagem: {}", e.getMessage());
            return null; // Retorna null se ocorrer um erro
        }
    }

    @GetMapping // Endpoint para obter todos os pratos
    public List<Prato> obterPratos() {
        List<Prato> pratos = pratoService.listarPratos();
        String baseUrl = "http://localhost:8080/"; // Altere se necessário

        // Atualiza a URL da imagem para ser completa
        for (Prato prato : pratos) {
            prato.setImagemUrl(baseUrl + prato.getImagemUrl()); // Concatenando baseUrl com a imagemUrl
        }

        logger.info("Pratos retornados: {}", pratos.size()); // Log do número de pratos retornados
        return pratos;
    }

    @GetMapping("/{nome}") // Endpoint para obter um prato pelo nome
    public Prato obterPratoPorNome(@PathVariable String nome) {
        return pratoService.obterPratoPorNome(nome); // Alterado para usar o método do serviço
    }

    @DeleteMapping("/{nome}") // Endpoint para deletar um prato pelo nome
    public String deletarPrato(@PathVariable String nome) {
        boolean removed = pratoService.deletarPrato(nome); // Alterado para usar o método do serviço
        return removed ? "Prato removido." : "Prato não encontrado."; // Mensagem de sucesso ou falha
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "Backend está funcionando!";
    }

    @GetMapping("/imagens/{nomeImagem}") // Método para obter a imagem
    public ResponseEntity<Resource> obterImagem(@PathVariable String nomeImagem) {
        Path path = Paths.get("src/main/resources/static/imagens/" + nomeImagem);
        Resource resource;

        try {
            resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {

                // Detectar tipo de conteúdo da imagem
                String contentType = Files.probeContentType(path);
                if (contentType == null) {
                    contentType = "application/octet-stream"; // Tipo genérico se não for detectado
                }

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, contentType)
                        .header("X_Content-Type-Options", "nosniff")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
