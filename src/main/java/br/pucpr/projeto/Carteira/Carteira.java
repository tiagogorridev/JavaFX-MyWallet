package br.pucpr.projeto.Carteira;

import java.io.Serializable;

public class Carteira implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nomeCarteira;
    private String descricaoCarteira;
    private double quantidadeAportada;

    public Carteira(int id, String nomeCarteira, String descricaoCarteira, double quantidadeAportada) {
        this.id = id;
        this.nomeCarteira = nomeCarteira;
        this.descricaoCarteira = descricaoCarteira;
        this.quantidadeAportada = quantidadeAportada;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeCarteira() {
        return nomeCarteira;
    }

    public void setNomeCarteira(String nomeCarteira) {
        this.nomeCarteira = nomeCarteira;
    }

    public String getDescricaoCarteira() {
        return descricaoCarteira;
    }

    public void setDescricaoCarteira(String descricaoCarteira) {
        this.descricaoCarteira = descricaoCarteira;
    }

    public double getQuantidadeAportada() {
        return quantidadeAportada;
    }

    public void setQuantidadeAportada(double quantidadeAportada) {
        this.quantidadeAportada = quantidadeAportada;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                "\nNome: " + nomeCarteira +
                "\nDescrição: " + descricaoCarteira +
                "\nQuantidade Aportada: R$ " + String.format("%.2f", quantidadeAportada);
    }
}