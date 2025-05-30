package br.pucpr.projeto.Metas;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Meta implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nome;
    private double valorAtual;
    private LocalDate dataInicial;
    private double valorMeta;
    private LocalDate dataFinal;

    public Meta(int id, String nome, LocalDate dataInicial, double valorAtual, double valorMeta, LocalDate dataFinal) {
        this.id = id;
        this.nome = nome;
        this.dataInicial = dataInicial;
        this.valorAtual = valorAtual;
        this.valorMeta = valorMeta;
        this.dataFinal = dataFinal;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValorAtual() {
        return valorAtual;
    }

    public void setValorAtual(double valorAtual) {
        this.valorAtual = valorAtual;
    }

    public LocalDate getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(LocalDate dataInicial) {
        this.dataInicial = dataInicial;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(LocalDate dataFinal) {
        this.dataFinal = dataFinal;
    }

    public double getValorMeta() {
        return valorMeta;
    }

    public void setValorMeta(double valorMeta) {
        this.valorMeta = valorMeta;
    }

    // Métodos de cálculo
    public double getProgresso() {
        return valorMeta > 0 ? (valorAtual / valorMeta) * 100 : 0;
    }

    public boolean isCompleta() {
        return valorAtual >= valorMeta;
    }

    public boolean isVencida() {
        return LocalDate.now().isAfter(dataFinal) && !isCompleta();
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        double progresso = getProgresso();
        String status;

        if (isCompleta()) {
            status = "✓ COMPLETA";
        } else if (isVencida()) {
            status = "✗ VENCIDA";
        } else {
            status = String.format("%.1f%%", progresso);
        }

        return String.format(
                "Nome: %s\nStatus: %s\nProgresso: R$ %.2f / R$ %.2f\nPeríodo: %s → %s",
                nome, status, valorAtual, valorMeta,
                dataInicial.format(formatter), dataFinal.format(formatter)
        );
    }
}