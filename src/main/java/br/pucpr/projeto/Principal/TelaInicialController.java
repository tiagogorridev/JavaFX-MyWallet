package br.pucpr.projeto.Principal;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TelaInicialController {
    public void abrirTelaPrincipal(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TelaPrincipal.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 640, 480);

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        stage.setTitle("My Wallet");
        stage.setScene(scene);
        stage.show();
    }
}