package br.pucpr.projeto.Noticia;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Noticia implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int analistaId;
    private String titulo;
    private String categoria;
    private String conteudo;
    private String status;
    private LocalDateTime dataCriacao;

    public Noticia (int id, int analistaId, String titulo, String categoria, String conteudo, String status, LocalDateTime dataCriacao) {
        this.id = id;
        this.analistaId = analistaId;
        this.titulo = titulo;
        this.categoria = categoria;
        this.conteudo = conteudo;
        this.status = status;
        this.dataCriacao = dataCriacao;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnalistaId() {
        return analistaId;
    }

    public void setAnalistaId(int analistaId) {
        this.analistaId = analistaId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return "ID: " + id +
                "\nAnalista ID: " + analistaId +
                "\nTítulo: " + titulo +
                "\nCategoria: " + categoria +
                "\nConteúdo: " + conteudo +
                "\nStatus: " + status +
                "\nData de Criação: " + dataCriacao.format(formatter);
    }
}