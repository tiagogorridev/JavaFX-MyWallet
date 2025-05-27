package br.pucpr.projeto.Metas;
import br.pucpr.projeto.Alert.AlertUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CriarMetaController {

    @FXML
    private TextField inputNome;

    @FXML
    private TextField inputValorFinal;

    @FXML
    private TextField inputValorInicial;

    @FXML
    private TextField inputDataInicial;

    @FXML
    private TextField inputDataFinal;

    @FXML
    public void enviarMeta(ActionEvent event) {
        try {
            // Validar se todos os campos estão preenchidos
            if (inputNome.getText().trim().isEmpty() ||
                    inputValorFinal.getText().trim().isEmpty() ||
                    inputValorInicial.getText().trim().isEmpty() ||
                    inputDataInicial.getText().trim().isEmpty() ||
                    inputDataFinal.getText().trim().isEmpty()) {

                AlertUtils.mostrarErroSimples("Todos os campos devem ser preenchidos!");
                return;
            }

            // Capturar os dados dos campos
            String nome = inputNome.getText().trim();

            // Converter valores para double
            double valorInicial = Double.parseDouble(inputValorInicial.getText().trim());
            double valorFinal = Double.parseDouble(inputValorFinal.getText().trim());

            // Converter datas (formato esperado: dd/MM/yyyy ou yyyy-MM-dd)
            LocalDate dataInicial = parseData(inputDataInicial.getText().trim());
            LocalDate dataFinal = parseData(inputDataFinal.getText().trim());

            // Validações adicionais
            if (valorFinal <= 0) {
                AlertUtils.mostrarErroSimples("O valor da meta deve ser maior que zero!");
                return;
            }

            if (valorInicial < 0) {
                AlertUtils.mostrarErroSimples("O valor inicial não pode ser negativo!");
                return;
            }

            if (dataFinal.isBefore(dataInicial)) {
                AlertUtils.mostrarErroSimples("A data final deve ser posterior à data inicial!");
                return;
            }

            // Gerar ID único (simples incremento baseado no tamanho da lista)
            int novoId = MetaDAO.lerLista().size() + 1;

            // Criar nova meta
            Meta novaMeta = new Meta(novoId, nome, dataInicial, valorInicial, valorFinal, dataFinal);

            // Salvar no arquivo
            MetaDAO.adicionarMeta(novaMeta);

            // Mostrar confirmação
            AlertUtils.mostrarSucesso("Meta criada com sucesso!");

            // Voltar para a tela de metas
            voltarParaMetas(event);

        } catch (NumberFormatException e) {
            AlertUtils.mostrarErroSimples("Valores inválidos! Verifique os campos numéricos.");
        } catch (DateTimeParseException e) {
            AlertUtils.mostrarErroSimples("Formato de data inválido! Use dd/MM/yyyy ou yyyy-MM-dd");
        } catch (Exception e) {
            AlertUtils.mostrarErro("Erro", "Erro ao criar meta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private LocalDate parseData(String dataTexto) throws DateTimeParseException {
        try {
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(dataTexto, formatter1);
        } catch (DateTimeParseException e1) {
            try {
                DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                return LocalDate.parse(dataTexto, formatter2);
            } catch (DateTimeParseException e2) {
                throw new DateTimeParseException("Formato de data inválido", dataTexto, 0);
            }
        }
    }

    private void voltarParaMetas(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MinhasMetas.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 640, 480);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setTitle("My Wallet - Minhas Metas");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}