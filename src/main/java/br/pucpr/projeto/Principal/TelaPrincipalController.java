package br.pucpr.projeto.Principal;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TelaPrincipalController {
    public void abrirTelaMinhasMetas() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/br/pucpr/projeto/Metas/MinhasMetas.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("My Wallet - Minhas Metas");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void abrirTelaMinhasCarteiras() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/br/pucpr/projeto/Carteira/MinhasCarteiras.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("My Wallet - Minhas Carteiras");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void abrirTelaMeusUsuarios() {
        try {
            FXMLLoader fxmlLoad = new FXMLLoader(getClass().getResource("/br/pucpr/projeto/Usuario/MeusUsuarios.fxml"));
            Scene scene = new Scene(fxmlLoad.load());
            Stage stage = new Stage();
            stage.setTitle("My Wallet - Meus Usuarios");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}