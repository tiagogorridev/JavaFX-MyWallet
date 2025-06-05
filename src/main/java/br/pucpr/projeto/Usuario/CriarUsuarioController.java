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
import java.util.Objects;
import java.util.regex.Pattern;

public class CriarUsuarioController {

    @FXML
    private TextField inputNomeUsuario;
    @FXML
    private TextField inputEmailUsuario;

    private static final Pattern pattern = Pattern.compile("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");

    @FXML
    public void enviarUsuario(ActionEvent event) {
        try {
            String nome = inputNomeUsuario.getText().trim();
            String email = inputEmailUsuario.getText().trim();

            if (nome.isEmpty() || email.isEmpty()) {
                throw new RuntimeException("Todos os campos devem ser preenchidos!");
            }

            if (!pattern.matcher(email).matches()) {
                throw new RuntimeException("Email inválido");
            }

            // Criar e salvar usuario
            int novoId = UsuarioDAO.lerLista().size() + 1;
            Usuario novoUsuario = new Usuario(novoId, nome, email);
            UsuarioDAO.adicionarUsuario(novoUsuario);

            AlertUtils.mostrarSucesso("Usuário criado com sucesso!");
            voltarParaUsuarios(event);

        } catch (Exception e) {
            AlertUtils.mostrarErro("Erro", e.getMessage());
        }
    }

    @FXML
    public void voltarParaUsuarios(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MeusUsuarios.fxml")));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 640, 480));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
