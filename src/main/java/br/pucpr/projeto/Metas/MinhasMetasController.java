package br.pucpr.projeto.Metas;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.stage.Stage;



public class MinhasMetasController implements Initializable{

    @FXML
    private ListView<Meta> lista;

    public void mostrarMetas(){

    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        lista.getItems().addAll(MetaDAO.lerLista());
    }

    public void criarMeta(ActionEvent event) throws IOException {
        // Carrega o layout FXML da nova tela
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CriarMeta.fxml"));
        Parent root = fxmlLoader.load();

        // Cria a nova cena
        Scene scene = new Scene(root, 640, 480);

        // Pega a Stage atual através do evento (botão)
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        // Configura a nova cena na Stage
        stage.setTitle("My Wallet");
        stage.setScene(scene);
        stage.show();
    }

    public void atualizarMeta(){

    }

    public void apagarMeta(){

    }



}
