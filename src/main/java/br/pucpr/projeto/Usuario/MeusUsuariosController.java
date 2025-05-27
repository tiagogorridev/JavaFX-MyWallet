package br.pucpr.projeto.Usuario;

import br.pucpr.projeto.Alert.AlertUtils;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MeusUsuariosController implements Initializable {

    @FXML
    private ListView<Usuario> lista;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        atualizarUsuarios();
    }

    @FXML
    public void criarUsuario(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("CriarUsuario.fxml"));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setTitle("My Wallet - Criar Usuário");
        stage.setScene(new Scene(root, 640, 480));
    }

    @FXML
    public void editarUsuario(ActionEvent event) {
        Usuario usuarioSelecionado = lista.getSelectionModel().getSelectedItem();

        if (usuarioSelecionado == null) {
            AlertUtils.mostrarAvisoSimples("Por favor, selecione um usuário para editar!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditarUsuarios.fxml"));
            Parent root = loader.load();

            // Passa a carteira selecionada para o controller de edição
            EditarUsuarioController controller = loader.getController();
            controller.carregarDadosUsuario(usuarioSelecionado);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setTitle("My Wallet - Editar Usuário");
            stage.setScene(new Scene(root, 640, 480));

        } catch (IOException e) {
            AlertUtils.mostrarErro("Erro", "Não foi possível abrir a tela de edição: " + e.getMessage());
        }
    }

    @FXML
    public void atualizarUsuarios() {
        try {
            // PASSO 1: Limpar a lista atual na interface
            lista.getItems().clear();

            // PASSO 2: Buscar dados atualizados do arquivo
            ArrayList<Usuario> usuariosAtualizados = UsuarioDAO.lerLista();

            // PASSO 3: Adicionar as carteiras na interface (silenciosamente)
            lista.getItems().addAll(usuariosAtualizados);

        } catch (Exception e) {
            // PASSO 4: Tratar erros (apenas erros críticos)
            AlertUtils.mostrarErro("Erro ao atualizar",
                    "Não foi possível atualizar a lista de usuários: " + e.getMessage());
        }
    }

    @FXML
    public void apagarUsuario() {
        Usuario usuarioSelecionado = lista.getSelectionModel().getSelectedItem();

        if (usuarioSelecionado == null) {
            AlertUtils.mostrarAvisoSimples("Por favor, selecione um usuário para excluir!");
            return;
        }

        String mensagemConfirmacao = String.format(
                "Tem certeza que deseja excluir o usuário:\n\n" +
                        "Nome: %s\n" +
                        "Email: %s\n" +
                        "Esta ação não pode ser desfeita!",
                usuarioSelecionado.getNomeUsuario(),
                usuarioSelecionado.getEmailUsuario()
        );

        boolean confirmado = AlertUtils.confirmar(mensagemConfirmacao);

        if (!confirmado) {
            return;
        }

        try {
            UsuarioDAO.removerUsuarioPorId(usuarioSelecionado.getId());
            atualizarUsuarios();
            AlertUtils.mostrarSucesso("Usuário excluída com sucesso!");

        } catch (Exception e) {
            AlertUtils.mostrarErro("Erro ao excluir usuário",
                    "Ocorreu um erro ao tentar excluir o usuário: " + e.getMessage());
        }
    }
}
