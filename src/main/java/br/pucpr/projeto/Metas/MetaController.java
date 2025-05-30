package br.pucpr.projeto.Metas;

import br.pucpr.projeto.Alert.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MetaController implements Initializable {

    @FXML
    private ListView<Meta> lista;
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

    private Meta metaParaEditar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (lista != null) {
            atualizarMetas();
        }

        if (inputDataInicial != null && inputDataFinal != null) {
            inputDataInicial.setValue(LocalDate.now());
            inputDataFinal.setValue(LocalDate.now().plusMonths(1));
        }
    }

    // ========== TELA PRINCIPAL - MINHAS METAS ==========

    @FXML
    public void criarMeta(ActionEvent event) throws IOException {
        navegarPara(event, "CriarMeta.fxml", "My Wallet - Criar Meta");
    }

    @FXML
    public void editarMeta(ActionEvent event) {
        Meta metaSelecionada = lista.getSelectionModel().getSelectedItem();

        if (metaSelecionada == null) {
            AlertUtils.mostrarAvisoSimples("Por favor, selecione uma meta para editar!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditarMeta.fxml"));
            Parent root = loader.load();

            MetaController controller = loader.getController();
            controller.carregarDadosMeta(metaSelecionada);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setTitle("My Wallet - Editar Meta");
            stage.setScene(new Scene(root, 640, 480));

        } catch (IOException e) {
            AlertUtils.mostrarErro("Erro", "Não foi possível abrir a tela de edição: " + e.getMessage());
        }
    }

    @FXML
    public void apagarMeta() {
        Meta metaSelecionada = lista.getSelectionModel().getSelectedItem();

        if (metaSelecionada == null) {
            AlertUtils.mostrarAvisoSimples("Por favor, selecione uma meta para excluir!");
            return;
        }

        String statusMeta = metaSelecionada.isCompleta() ? "COMPLETA" :
                metaSelecionada.isVencida() ? "VENCIDA" :
                        String.format("%.1f%% concluída", metaSelecionada.getProgresso());

        String mensagem = String.format(
                "Tem certeza que deseja excluir a meta:\n\n" +
                        "Nome: %s\n" +
                        "Status: %s\n" +
                        "Progresso: R$ %.2f / R$ %.2f\n\n" +
                        "Esta ação não pode ser desfeita!",
                metaSelecionada.getNome(),
                statusMeta,
                metaSelecionada.getValorAtual(),
                metaSelecionada.getValorMeta()
        );

        if (AlertUtils.confirmar(mensagem)) {
            try {
                MetaDAO.removerMetaPorId(metaSelecionada.getId());
                atualizarMetas();
                AlertUtils.mostrarSucesso("Meta excluída com sucesso!");
            } catch (Exception e) {
                AlertUtils.mostrarErro("Erro", "Erro ao excluir meta: " + e.getMessage());
            }
        }
    }

    @FXML
    public void atualizarMetas() {
        try {
            lista.getItems().clear();
            ArrayList<Meta> metas = MetaDAO.lerLista();
            lista.getItems().addAll(metas);
        } catch (Exception e) {
            AlertUtils.mostrarErro("Erro", "Não foi possível atualizar a lista: " + e.getMessage());
        }
    }

    // ========== CRIAR META ==========

    @FXML
    public void enviarMeta(ActionEvent event) {
        try {
            if (!validarCampos()) return;

            String nome = inputNomeMeta.getText().trim();
            LocalDate dataInicial = inputDataInicial.getValue();
            LocalDate dataFinal = inputDataFinal.getValue();

            // Usar método auxiliar para converter valores com vírgula
            double valorAtual = converterValor(inputValorAtual.getText().trim());
            double valorMeta = converterValor(inputValorMeta.getText().trim());

            int novoId = MetaDAO.gerarProximoId();
            Meta novaMeta = new Meta(novoId, nome, dataInicial, valorAtual, valorMeta, dataFinal);
            MetaDAO.adicionarMeta(novaMeta);

            AlertUtils.mostrarSucesso("Meta criada com sucesso!");
            voltarParaMetas(event);

        } catch (Exception e) {
            AlertUtils.mostrarErro("Erro", "Erro ao criar meta: " + e.getMessage());
        }
    }

    // ========== EDITAR META ==========

    public void carregarDadosMeta(Meta meta) {
        this.metaParaEditar = meta;
        inputNomeMeta.setText(meta.getNome());
        inputDataInicial.setValue(meta.getDataInicial());
        inputDataFinal.setValue(meta.getDataFinal());
        inputValorAtual.setText(String.format("%.2f", meta.getValorAtual()).replace(".", ","));
        inputValorMeta.setText(String.format("%.2f", meta.getValorMeta()).replace(".", ","));
    }

    @FXML
    public void salvarAlteracoes(ActionEvent event) {
        try {
            if (!validarCampos()) return;

            String nome = inputNomeMeta.getText().trim();
            LocalDate dataInicial = inputDataInicial.getValue();
            LocalDate dataFinal = inputDataFinal.getValue();

            // Usar método auxiliar para converter valores com vírgula
            double valorAtual = converterValor(inputValorAtual.getText().trim());
            double valorMeta = converterValor(inputValorMeta.getText().trim());

            metaParaEditar.setNome(nome);
            metaParaEditar.setDataInicial(dataInicial);
            metaParaEditar.setValorAtual(valorAtual);
            metaParaEditar.setValorMeta(valorMeta);
            metaParaEditar.setDataFinal(dataFinal);

            MetaDAO.atualizarMeta(metaParaEditar);
            AlertUtils.mostrarSucesso("Meta atualizada com sucesso!");
            voltarParaMetas(event);

        } catch (Exception e) {
            AlertUtils.mostrarErro("Erro", "Erro ao atualizar meta: " + e.getMessage());
        }
    }

    // ========== MÉTODOS AUXILIARES ==========

    /**
     * Converte uma string com formato brasileiro (vírgula decimal) para double
     */
    private double converterValor(String valor) throws NumberFormatException {
        if (valor == null || valor.trim().isEmpty()) {
            return 0.0;
        }

        String valorNormalizado = normalizarNumero(valor);
        return Double.parseDouble(valorNormalizado);
    }

    private boolean validarCampos() {
        // Verificar se os campos existem (evitar NullPointerException)
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

        // Tratar campos nulos como strings vazias
        if (nome == null) nome = "";
        if (valorAtualStr == null) valorAtualStr = "";
        if (valorMetaStr == null) valorMetaStr = "";

        // Fazer trim apenas se não for nulo
        nome = nome.trim();
        valorAtualStr = valorAtualStr.trim();
        valorMetaStr = valorMetaStr.trim();

        // Validar campo nome (deve aceitar texto, incluindo números)
        if (nome.isEmpty()) {
            AlertUtils.mostrarAvisoSimples("O nome da meta deve ser preenchido!");
            inputNomeMeta.requestFocus();
            return false;
        }

        // Validar campos de valor
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

        // Validar datas
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

        // Validar sequência de datas
        if (dataFinal.isBefore(dataInicial)) {
            AlertUtils.mostrarErroSimples("A data final deve ser posterior à data inicial!");
            inputDataFinal.requestFocus();
            return false;
        }

        // Validar se as datas não são muito antigas
        if (dataInicial.isBefore(LocalDate.now().minusYears(10))) {
            AlertUtils.mostrarAvisoSimples("A data inicial não pode ser muito antiga!");
            inputDataInicial.requestFocus();
            return false;
        }

        // Validar conversão dos valores numéricos
        try {
            double valorAtual = converterValor(valorAtualStr);
            double valorMeta = converterValor(valorMetaStr);

            // Validar se são números válidos (não NaN ou infinito)
            if (Double.isNaN(valorAtual) || Double.isInfinite(valorAtual)) {
                AlertUtils.mostrarErroSimples("Valor atual inválido!");
                inputValorAtual.requestFocus();
                return false;
            }

            if (Double.isNaN(valorMeta) || Double.isInfinite(valorMeta)) {
                AlertUtils.mostrarErroSimples("Valor da meta inválido!");
                inputValorMeta.requestFocus();
                return false;
            }

            // Validar valores negativos e zero
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

            // Validar valores muito grandes (evitar overflow)
            if (valorAtual > 999999999.99 || valorMeta > 999999999.99) {
                AlertUtils.mostrarErroSimples("Os valores são muito grandes! Máximo: R$ 999.999.999,99");
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

    private String normalizarNumero(String numero) {
        if (numero == null || numero.trim().isEmpty()) {
            return "0";
        }

        numero = numero.trim()
                .replace("R$", "")   // remove símbolo de real
                .replace(" ", "");   // remove espaços

        // Remove qualquer caractere que não seja número, vírgula ou ponto
        numero = numero.replaceAll("[^\\d.,]", "");

        // Se contém vírgula, assumir formato brasileiro (vírgula = decimal)
        if (numero.contains(",")) {
            // Verifica se há ponto antes da vírgula (formato: 1.234,56)
            if (numero.contains(".") && numero.indexOf(".") < numero.lastIndexOf(",")) {
                // Remove pontos (separadores de milhares) e converte vírgula para ponto
                numero = numero.replaceAll("\\.", "").replace(",", ".");
            } else {
                // Apenas converte vírgula para ponto
                numero = numero.replace(",", ".");
            }
        }
        // Se contém apenas ponto, verificar se é decimal ou separador de milhares
        else if (numero.contains(".")) {
            String[] partes = numero.split("\\.");
            // Se tem mais de uma parte e a última parte tem mais de 2 dígitos,
            // provavelmente é separador de milhares
            if (partes.length > 1 && partes[partes.length - 1].length() > 2) {
                numero = numero.replace(".", "");
            }
        }

        return numero;
    }

    private void navegarPara(ActionEvent event, String arquivo, String titulo) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(arquivo));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setTitle(titulo);
        stage.setScene(new Scene(root, 640, 480));
    }

    @FXML
    public void voltarParaMetas(ActionEvent event) {
        try {
            navegarPara(event, "MinhasMetas.fxml", "My Wallet - Minhas Metas");
        } catch (IOException e) {
            AlertUtils.mostrarErro("Erro", "Erro ao voltar para metas: " + e.getMessage());
        }
    }
}