package br.pucpr.projeto.Metas;

import br.pucpr.projeto.Alert.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CriarMetaController implements Initializable {

    @FXML
    private TextField inputNomeMeta;
    @FXML
    private DatePicker inputDataInicial;
    @FXML
    private TextField inputValorAtual;
    @FXML
    private TextField inputValorMeta;
    @FXML
    private DatePicker inputDataFinal;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (inputDataInicial != null && inputDataFinal != null) {
            inputDataInicial.setValue(LocalDate.now());
            inputDataFinal.setValue(LocalDate.now().plusMonths(1));

            MetaUtils.desabilitarDigitacaoData(inputDataInicial);
            MetaUtils.desabilitarDigitacaoData(inputDataFinal);
        }
    }

    @FXML
    public void enviarMeta(ActionEvent event) {
        try {
            if (!validarCampos()) return;

            String nome = inputNomeMeta.getText().trim();
            LocalDate dataInicial = inputDataInicial.getValue();
            LocalDate dataFinal = inputDataFinal.getValue();

            double valorAtual = MetaUtils.converterValor(inputValorAtual.getText().trim());
            double valorMeta = MetaUtils.converterValor(inputValorMeta.getText().trim());

            int novoId = MetaDAO.gerarProximoId();
            Meta novaMeta = new Meta(novoId, nome, dataInicial, valorAtual, valorMeta, dataFinal);
            MetaDAO.adicionarMeta(novaMeta);

            AlertUtils.mostrarSucesso("Meta criada com sucesso!");
            voltarParaMetas(event);

        } catch (Exception e) {
            AlertUtils.mostrarErro("Erro", "Erro ao criar meta: " + e.getMessage());
        }
    }

    @FXML
    public void voltarParaMetas(ActionEvent event) {
        try {
            MetaUtils.navegarPara(event, "MinhasMetas.fxml", "My Wallet - Minhas Metas");
        } catch (IOException e) {
            AlertUtils.mostrarErro("Erro", "Erro ao voltar para metas: " + e.getMessage());
        }
    }

    private boolean validarCampos() {
        if (inputNomeMeta == null || inputValorAtual == null ||
                inputValorMeta == null || inputDataInicial == null || inputDataFinal == null) {
            AlertUtils.mostrarErroSimples("Erro interno: campos não inicializados!");
            return false;
        }

        String nome = inputNomeMeta.getText();
        String valorAtualStr = inputValorAtual.getText();
        String valorMetaStr = inputValorMeta.getText();
        LocalDate dataInicial = inputDataInicial.getValue();
        LocalDate dataFinal = inputDataFinal.getValue();

        if (nome == null) nome = "";
        if (valorAtualStr == null) valorAtualStr = "";
        if (valorMetaStr == null) valorMetaStr = "";

        nome = nome.trim();
        valorAtualStr = valorAtualStr.trim();
        valorMetaStr = valorMetaStr.trim();

        if (nome.isEmpty()) {
            AlertUtils.mostrarAvisoSimples("O nome da meta deve ser preenchido!");
            inputNomeMeta.requestFocus();
            return false;
        }

        if (valorAtualStr.isEmpty()) {
            AlertUtils.mostrarAvisoSimples("O valor atual deve ser preenchido!");
            inputValorAtual.requestFocus();
            return false;
        }

        if (valorMetaStr.isEmpty()) {
            AlertUtils.mostrarAvisoSimples("O valor da meta deve ser preenchido!");
            inputValorMeta.requestFocus();
            return false;
        }

        if (dataInicial == null) {
            AlertUtils.mostrarAvisoSimples("A data inicial deve ser selecionada!");
            inputDataInicial.requestFocus();
            return false;
        }

        if (dataFinal == null) {
            AlertUtils.mostrarAvisoSimples("A data final deve ser selecionada!");
            inputDataFinal.requestFocus();
            return false;
        }

        if (dataFinal.isBefore(dataInicial)) {
            AlertUtils.mostrarErroSimples("A data final deve ser posterior à data inicial!");
            inputDataFinal.requestFocus();
            return false;
        }

        if (dataInicial.isBefore(LocalDate.now().minusYears(10))) {
            AlertUtils.mostrarAvisoSimples("A data inicial não pode ser muito antiga!");
            inputDataInicial.requestFocus();
            return false;
        }

        try {
            double valorAtual = MetaUtils.converterValor(valorAtualStr);
            double valorMeta = MetaUtils.converterValor(valorMetaStr);

            if (Double.isNaN(valorAtual)) {
                AlertUtils.mostrarErroSimples("Valor atual inválido!");
                inputValorAtual.requestFocus();
                return false;
            }

            if (Double.isNaN(valorMeta)) {
                AlertUtils.mostrarErroSimples("Valor da meta inválido!");
                inputValorMeta.requestFocus();
                return false;
            }

            if (valorAtual < 0) {
                AlertUtils.mostrarErroSimples("O valor atual não pode ser negativo!");
                inputValorAtual.requestFocus();
                return false;
            }

            if (valorMeta <= 0) {
                AlertUtils.mostrarErroSimples("O valor da meta deve ser maior que zero!");
                inputValorMeta.requestFocus();
                return false;
            }

            if (valorAtual > 999999999.99 || valorMeta > 999999999.99) {
                AlertUtils.mostrarErroSimples("Os valores são muito grandes! Máximo: R$ 999.999.999,99");
                return false;
            }

            if (valorAtual > valorMeta) {
                AlertUtils.mostrarAvisoSimples("O valor atual não pode ser maior que o valor da meta!");
                inputValorAtual.requestFocus();
                return false;
            }

        } catch (NumberFormatException e) {
            AlertUtils.mostrarErroSimples("Os campos 'Valor Atual' e 'Valor da Meta' devem conter apenas números válidos!\n" +
                    "Use apenas números e vírgulas para decimais.\n" +
                    "Exemplo: 1500,50 ou 1500");
            return false;
        }

        return true;
    }
}