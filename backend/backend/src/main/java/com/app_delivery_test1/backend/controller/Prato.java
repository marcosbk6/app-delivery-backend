package com.app_delivery_test1.backend.controller;

import jakarta.persistence.*;

@Entity
@Table(name = "pratos")
public class Prato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador unico do prato

    private String nome;
    private double preco;
    private String imagemUrl;
    private String categoria;

    public Prato() {

    }

    public Prato(String nome, double preco, String imagemUrl, String categoria) {
        this.nome = nome;
        this.preco = preco;
        this.imagemUrl = imagemUrl;
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getImagemUrl() {

        return imagemUrl; // Método para obter a URL da imagem
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl; // Método para definir a URL da imagem
    }

    public String getCategoria() {
        return categoria; // Método para obter a categoria
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria; // Método para definir a categoria
    }
}
