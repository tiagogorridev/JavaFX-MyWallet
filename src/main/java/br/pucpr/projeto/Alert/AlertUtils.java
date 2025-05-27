package br.pucpr.projeto.Alert;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

/**
 * Classe utilitária para gerenciar alertas de forma centralizada
 */
public class AlertUtils {

    /**
     * Exibe um alerta de informação
     */
    public static void mostrarInfo(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Exibe um alerta de aviso
     */
    public static void mostrarAviso(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Exibe um alerta de erro
     */
    public static void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Exibe um alerta de confirmação e retorna a resposta do usuário
     */
    public static boolean mostrarConfirmacao(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);

        Optional<ButtonType> resultado = alert.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }

    /**
     * Versões simplificadas com títulos padrão
     */
    public static void mostrarSucesso(String mensagem) {
        mostrarInfo("Sucesso", mensagem);
    }

    public static void mostrarErroSimples(String mensagem) {
        mostrarErro("Erro", mensagem);
    }

    public static void mostrarAvisoSimples(String mensagem) {
        mostrarAviso("Aviso", mensagem);
    }

    /**
     * Confirmação com título padrão
     */
    public static boolean confirmar(String mensagem) {
        return mostrarConfirmacao("Confirmação", mensagem);
    }

    /**
     * Alerta personalizado com header
     */
    public static void mostrarAlertaCompleto(Alert.AlertType tipo, String titulo, String header, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}