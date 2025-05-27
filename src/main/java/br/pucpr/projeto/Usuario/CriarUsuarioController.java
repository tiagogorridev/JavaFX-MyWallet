package br.pucpr.projeto.Usuario;

import br.pucpr.projeto.Alert.AlertUtils;

import br.pucpr.projeto.Usuario.Usuario;
import br.pucpr.projeto.Usuario.UsuarioDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CriarUsuarioController {

    @FXML
    private TextField inputNomeUsuario;
    @FXML
    private TextField inputEmailUsuario;

    @FXML
    public void enviarUsuario(ActionEvent event) {
        try {
            String nome = inputNomeUsuario.getText().trim();
            String email = inputEmailUsuario.getText().trim();

            if (nome.isEmpty() || email.isEmpty()) {
                AlertUtils.mostrarAvisoSimples("Todos os campos devem ser preenchidos!");
                return;
            }

            // Criar e salvar usuario
            int novoId = UsuarioDAO.lerLista().size() + 1;
            Usuario novoUsuario = new Usuario(novoId, nome, email);
            UsuarioDAO.adicionarUsuario(novoUsuario);

            AlertUtils.mostrarSucesso("Usuário criado com sucesso!");
            voltarParaUsuarios(event);

        } catch (NumberFormatException e) {
            AlertUtils.mostrarErroSimples("Valor inválido! Use apenas números.");
        } catch (Exception e) {
            AlertUtils.mostrarErro("Erro", e.getMessage());
        }
    }

    @FXML
    public void voltarParaUsuarios(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("MeusUsuarios.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 640, 480));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
