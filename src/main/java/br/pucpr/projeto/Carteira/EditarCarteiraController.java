package br.pucpr.projeto.Carteira;

import br.pucpr.projeto.Alert.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DecimalFormat;

public class EditarCarteiraController {
    @FXML
    private TextField inputNomeCarteira;
    @FXML
    private TextField inputDescricaoCarteira;
    @FXML
    private TextField inputQuantidadeAportada;

    private Carteira carteiraParaEditar;

    public void carregarDados(Carteira carteira) {
        this.carteiraParaEditar = carteira;

        inputNomeCarteira.setText(carteira.getNomeCarteira());
        inputDescricaoCarteira.setText(carteira.getDescricaoCarteira());

        DecimalFormat df = new DecimalFormat("#.##");
        inputQuantidadeAportada.setText(df.format(carteira.getQuantidadeAportada()));
    }

    @FXML
    public void salvarAlteracoes(ActionEvent event) {
        try {
            String nome = inputNomeCarteira.getText().trim();
            String descricao = inputDescricaoCarteira.getText().trim();
            String valorStr = inputQuantidadeAportada.getText().trim();

            if (nome.isEmpty() || descricao.isEmpty() || valorStr.isEmpty()) {
                AlertUtils.mostrarAvisoSimples("Preencha todos os campos!");
                return;
            }

            double valor = Double.parseDouble(valorStr);
            if (valor < 0) {
                AlertUtils.mostrarErroSimples("Valor não pode ser negativo!");
                return;
            }

            carteiraParaEditar.setNomeCarteira(nome);
            carteiraParaEditar.setDescricaoCarteira(descricao);
            carteiraParaEditar.setQuantidadeAportada(valor);

            CarteiraDAO.atualizarCarteira(carteiraParaEditar);

            AlertUtils.mostrarSucesso("Carteira atualizada!");
            voltarParaMinhasCarteiras(event);

        } catch (NumberFormatException e) {
            AlertUtils.mostrarErroSimples("Valor inválido! Use apenas números.");
        } catch (Exception e) {
            AlertUtils.mostrarErro("Erro", "Não foi possível salvar: " + e.getMessage());
        }
    }

    @FXML
    public void voltarParaMinhasCarteiras(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("MinhasCarteiras.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setTitle("My Wallet - Minhas Carteiras");
            stage.setScene(new Scene(root, 640, 480));
        } catch (IOException e) {
            AlertUtils.mostrarErro("Erro", "Não foi possível voltar: " + e.getMessage());
        }
    }
}