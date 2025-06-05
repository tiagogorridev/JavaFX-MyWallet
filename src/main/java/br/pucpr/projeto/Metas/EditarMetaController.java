package br.pucpr.projeto.Metas;

import br.pucpr.projeto.Alert.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EditarMetaController implements Initializable {

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
        desabilitarDigitacaoData(inputDataInicial);
        desabilitarDigitacaoData(inputDataFinal);
    }

    /**
     * Desabilita a digitação nos campos DatePicker, permitindo apenas seleção via calendário
     */
    private void desabilitarDigitacaoData(DatePicker datePicker) {
        // Desabilitar a digitação no campo de texto do DatePicker
        datePicker.getEditor().setEditable(false);

        // Opcional: Adicionar estilo visual para indicar que não é editável
        datePicker.getEditor().setStyle("-fx-background-color: #f4f4f4;");

        // Bloquear eventos de teclado no editor
        datePicker.getEditor().addEventFilter(KeyEvent.KEY_TYPED, event -> {
            event.consume(); // Bloqueia qualquer digitação
        });

        // Bloquear eventos de teclas especiais (Delete, Backspace, etc.)
        datePicker.getEditor().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            event.consume(); // Bloqueia todas as teclas
        });
    }

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

    @FXML
    public void voltarParaMetas(ActionEvent event) {
        try {
            navegarPara(event, "MinhasMetas.fxml", "My Wallet - Minhas Metas");
        } catch (IOException e) {
            AlertUtils.mostrarErro("Erro", "Erro ao voltar para metas: " + e.getMessage());
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

        // Validar se contém letras nos campos de valor
        if (contemLetras(valorAtualStr)) {
            AlertUtils.mostrarAvisoSimples("O campo 'Valor Atual' não pode conter letras!\nUse apenas números e vírgulas para decimais.");
            inputValorAtual.requestFocus();
            return false;
        }

        if (contemLetras(valorMetaStr)) {
            AlertUtils.mostrarAvisoSimples("O campo 'Valor da Meta' não pode conter letras!\nUse apenas números e vírgulas para decimais.");
            inputValorMeta.requestFocus();
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

    public String normalizarNumero(String numero) {
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

    /**
     * Verifica se uma string contém letras
     */
    private boolean contemLetras(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }

        // Remove espaços, R$, vírgulas e pontos para verificar apenas letras
        String textoLimpo = texto.trim()
                .replace("R$", "")
                .replace(" ", "")
                .replace(",", "")
                .replace(".", "");

        // Verifica se contém alguma letra
        return textoLimpo.matches(".*[a-zA-ZÀ-ÿ].*");
    }

    private void navegarPara(ActionEvent event, String arquivo, String titulo) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(arquivo));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setTitle(titulo);
        stage.setScene(new Scene(root, 640, 480));
    }
}