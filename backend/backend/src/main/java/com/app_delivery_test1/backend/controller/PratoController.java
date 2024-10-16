package com.app_delivery_test1.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType; // Para MediaType
import org.springframework.web.multipart.MultipartFile; // Para MultipartFile
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pratos") // URL base para os endpoints
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
                                @RequestParam("imagem") MultipartFile imagem) {
        logger.info("Recebendo requisição para adicionar prato. Nome: {}, Preço: {}", nomePrato, preco);

        Prato prato = new Prato();
        prato.setNome(nomePrato);
        prato.setPreco(preco);

        // Aqui você pode adicionar lógica para salvar a imagem, se necessário

        if (imagem != null && !imagem.isEmpty()) {
            String imagemUrl = salvarImagem(imagem); // Método para salvar a imagem e obter a URL
            if (imagemUrl != null) {
                prato.setImagemUrl(imagemUrl); // Defina a URL da imagem no prato
            } else {
                logger.error("Erro ao salvar a imagem."); // Log de erro caso a imagem não tenha sido salva
            }
        } else {
            logger.warn("Nenhuma imagem recebida na requisição");
        }

        pratos.add(prato); // Adiciona o prato à lista
        return prato; // Retorna o prato adicionado
    }

    // Método para salvar a imagem e retornar o caminho/URL
    private String salvarImagem(MultipartFile imagem) {
        try {
            // Define o diretório onde as imagens serão salvas

            String diretorio = System.getProperty("user.dir") + "/src/main/resources/imagens/";

            File dir = new File(diretorio);
            if (!dir.exists()) {
                dir.mkdirs(); // Cria o diretório se não existir
            }

            // Salva o arquivo no diretório especificado
            File file = new File(diretorio + imagem.getOriginalFilename());
            imagem.transferTo(file);
            logger.info("Imagem salva em: {}", file.getAbsolutePath());

            // Retorna o caminho da imagem salva (ajuste conforme necessário)
            return "imagens/" + imagem.getOriginalFilename(); // Ajuste para retornar apenas o nome do arquivo
        } catch (IOException e) {
            logger.error("Erro ao salvar a imagem: {}", e.getMessage());
            return null; // Retorna null se ocorrer um erro
        }
    }

    @GetMapping // Endpoint para obter todos os pratos
    public List<Prato> obterPratos() {
        return pratos; // Retorna a lista de pratos
    }

    @GetMapping("/{nome}") // Endpoint para obter um prato pelo nome
    public Prato obterPratoPorNome(@PathVariable String nome) {
        return pratos.stream()
                .filter(prato -> prato.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null); // Retorna o prato se encontrado ou null
    }

    @DeleteMapping("/{nome}") // Endpoint para deletar um prato pelo nome
    public String deletarPrato(@PathVariable String nome) {
        boolean removed = pratos.removeIf(prato -> prato.getNome().equalsIgnoreCase(nome));
        return removed ? "Prato removido." : "Prato não encontrado."; // Mensagem de sucesso ou falha
    }
}
