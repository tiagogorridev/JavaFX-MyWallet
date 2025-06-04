package br.pucpr.projeto.Carteira;

import br.pucpr.projeto.Alert.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MinhasCarteirasController implements Initializable {
    @FXML
    private ListView<Carteira> lista;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        atualizarLista();
    }

    @FXML
    public void atualizarLista() {
        try {
            lista.getItems().clear();
            ArrayList<Carteira> carteiras = CarteiraDAO.lerLista();
            lista.getItems().addAll(carteiras);
        } catch (RuntimeException e) {
            AlertUtils.mostrarErro("Erro ao carregar",
                    "Não foi possível carregar as carteiras: " + e.getMessage());
        }
    }

    @FXML
    public void criarCarteira(ActionEvent event) {
        trocarTela(event, "CriarCarteira.fxml", "My Wallet - Criar Carteira");
    }

    @FXML
    public void editarCarteira(ActionEvent event) {
        Carteira selecionada = lista.getSelectionModel().getSelectedItem();

        if (selecionada == null) {
            AlertUtils.mostrarAvisoSimples("Selecione uma carteira para editar!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditarCarteira.fxml"));
            Parent root = loader.load();

            EditarCarteiraController controller = loader.getController();
            controller.carregarDados(selecionada);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setTitle("My Wallet - Editar Carteira");
            stage.setScene(new Scene(root, 640, 480));

        } catch (IOException e) {
            AlertUtils.mostrarErro("Erro", "Não foi possível abrir edição: " + e.getMessage());
        }
    }

    @FXML
    public void apagarCarteira() {
        Carteira selecionada = lista.getSelectionModel().getSelectedItem();

        if (selecionada == null) {
            AlertUtils.mostrarAvisoSimples("Selecione uma carteira para excluir!");
            return;
        }

        String msg = String.format("Excluir carteira '%s'?", selecionada.getNomeCarteira());

        if (!AlertUtils.confirmar(msg)) return;

        try {
            CarteiraDAO.removerCarteiraPorId(selecionada.getId());
            atualizarLista();
            AlertUtils.mostrarSucesso("Carteira excluída!");
        } catch (Exception e) {
            AlertUtils.mostrarErro("Erro ao excluir", e.getMessage());
        }
    }

    private void trocarTela(ActionEvent event, String fxml, String titulo) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root, 640, 480));
        } catch (IOException e) {
            AlertUtils.mostrarErro("Erro", "Não foi possível carregar " + fxml);
        }
    }
}