package br.pucpr.projeto.Noticia;

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

public class MinhasNoticiasController implements Initializable {

    @FXML
    private ListView<Noticia> listaNoticias;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (listaNoticias != null) {
            atualizarNoticias();
        }
    }

    @FXML
    public void criarNoticia(ActionEvent event) throws IOException {
        navegarPara(event, "CriarNoticia.fxml", "My Wallet - Criar Notícia");
    }

    @FXML
    public void editarNoticia(ActionEvent event) {
        Noticia noticiaSelecionada = listaNoticias.getSelectionModel().getSelectedItem();

        if (noticiaSelecionada == null) {
            AlertUtils.mostrarAvisoSimples("Por favor, selecione uma notícia para editar!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditarNoticia.fxml"));
            Parent root = loader.load();

            EditarNoticiaController controller = loader.getController();
            controller.carregarDadosNoticia(noticiaSelecionada);

            Stage stage = new Stage();
            stage.setTitle("My Wallet - Editar Notícia");
            stage.setScene(new Scene(root, 640, 480));
            stage.showAndWait();

            // Atualizar lista após fechar a janela de edição
            atualizarNoticias();

        } catch (IOException e) {
            AlertUtils.mostrarErro("Erro", "Não foi possível abrir a tela de edição: " + e.getMessage());
        }
    }

    @FXML
    public void apagarNoticia() {
        Noticia noticiaSelecionada = listaNoticias.getSelectionModel().getSelectedItem();

        if (noticiaSelecionada == null) {
            AlertUtils.mostrarAvisoSimples("Por favor, selecione uma notícia para excluir!");
            return;
        }

        String mensagem = String.format(
                "Tem certeza que deseja excluir a notícia:\n\n" +
                        "Título: %s\n" +
                        "Categoria: %s\n" +
                        "Status: %s\n" +
                        "Analista ID: %d\n\n" +
                        "Esta ação não pode ser desfeita!",
                noticiaSelecionada.getTitulo(),
                noticiaSelecionada.getCategoria(),
                noticiaSelecionada.getStatus(),
                noticiaSelecionada.getAnalistaId()
        );

        if (AlertUtils.confirmar(mensagem)) {
            try {
                NoticiaDAO.removerNoticiaPorId(noticiaSelecionada.getId());
                atualizarNoticias();
                AlertUtils.mostrarSucesso("Notícia excluída com sucesso!");
            } catch (Exception e) {
                AlertUtils.mostrarErro("Erro", "Erro ao excluir notícia: " + e.getMessage());
            }
        }
    }

    @FXML
    public void atualizarNoticias() {
        try {
            listaNoticias.getItems().clear();
            ArrayList<Noticia> noticias = NoticiaDAO.lerLista();
            listaNoticias.getItems().addAll(noticias);
        } catch (Exception e) {
            AlertUtils.mostrarErro("Erro", "Não foi possível atualizar a lista: " + e.getMessage());
        }
    }

    private void navegarPara(ActionEvent event, String arquivo, String titulo) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(arquivo));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setTitle(titulo);
        stage.setScene(new Scene(root, 640, 480));
    }
}