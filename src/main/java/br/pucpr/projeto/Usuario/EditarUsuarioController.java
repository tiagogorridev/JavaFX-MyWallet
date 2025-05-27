package br.pucpr.projeto.Usuario;

import br.pucpr.projeto.Alert.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class EditarUsuarioController {

    @FXML
    private TextField inputNomeUsuario;
    @FXML
    private TextField inputEmailUsuario;

    private Usuario usuarioParaEditar;

    public void carregarDadosUsuario(Usuario usuario) {
        this.usuarioParaEditar = usuario;

        // Preenche os campos com os dados atuais da carteira
        inputNomeUsuario.setText(usuario.getNomeUsuario());
        inputEmailUsuario.setText(usuario.getEmailUsuario());
    }

    @FXML
    public void salvarAlteracoes(ActionEvent event) {
        try {
            String nome = inputNomeUsuario.getText().trim();
            String email = inputEmailUsuario.getText().trim();

            if (nome.isEmpty() || email.isEmpty()) {
                AlertUtils.mostrarAvisoSimples("Todos os campos devem ser preenchidos!");
                return;
            }

            // Atualiza os dados da carteira (mantém o email original)
            usuarioParaEditar.setNomeUsuario(nome);
            usuarioParaEditar.setEmailUsuario(email);

            // Salva as alterações no arquivo
            UsuarioDAO.atualizarUsuario(usuarioParaEditar);

            AlertUtils.mostrarSucesso("Usuário atualizado com sucesso!");
            voltarParaUsuarios(event);

        }
        catch (Exception e) {
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
