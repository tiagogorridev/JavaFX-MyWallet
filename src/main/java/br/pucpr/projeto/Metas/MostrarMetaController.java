package br.pucpr.projeto.Metas;

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

public class MostrarMetaController implements Initializable {

    @FXML
    private ListView<Meta> lista;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (lista != null) {
            atualizarMetas();
        }
    }

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

            EditarMetaController controller = loader.getController();
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

    private void navegarPara(ActionEvent event, String arquivo, String titulo) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(arquivo));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setTitle(titulo);
        stage.setScene(new Scene(root, 640, 480));
    }
}