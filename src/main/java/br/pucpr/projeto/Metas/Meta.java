package br.pucpr.projeto.Metas;

import java.io.Serializable;
import java.time.LocalDate;

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValorAtual() {
        return valorAtual;
    }

    public void setValorInicial(double valorAtual) {
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

    @Override
    public String toString() {
        return String.format(
                "ID: %d\nNome: %s\nData Inicial: %s\nValor Inicial: R$ %.2f\nValor Meta: R$ %.2f\nData Final: %s",
                id, nome, dataInicial, valorAtual, valorMeta, dataFinal
        );
    }

}
