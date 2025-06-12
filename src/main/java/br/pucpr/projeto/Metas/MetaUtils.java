package br.pucpr.projeto.Metas;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class MetaUtils {

    public static void desabilitarDigitacaoData(DatePicker datePicker) {
        datePicker.getEditor().setEditable(false);
        datePicker.getEditor().setStyle("-fx-background-color: #808080;");

        datePicker.getEditor().addEventFilter(KeyEvent.KEY_TYPED, event -> {
            event.consume();
        });

        datePicker.getEditor().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            event.consume();
        });

        datePicker.getEditor().setFocusTraversable(true);
    }


    public static double converterValor(String valor) throws NumberFormatException {
        if (valor == null || valor.trim().isEmpty()) {
            return 0.0;
        }

        String valorNormalizado = normalizarNumero(valor);
        return Double.parseDouble(valorNormalizado);
    }

    public static String normalizarNumero(String numero) {
        if (numero == null || numero.trim().isEmpty()) {
            return "0";
        }

        numero = numero.trim()
                .replace("R$", "")
                .replace(" ", "");

        numero = numero.replaceAll("[^\\d.,]", "");

        if (numero.contains(",")) {
            if (numero.contains(".") && numero.indexOf(".") < numero.lastIndexOf(",")) {
                numero = numero.replaceAll("\\.", "").replace(",", ".");
            } else {
                numero = numero.replace(",", ".");
            }
        }
        else if (numero.contains(".")) {
            String[] partes = numero.split("\\.");
            if (partes.length > 1 && partes[partes.length - 1].length() > 2) {
                numero = numero.replace(".", "");
            }
        }

        return numero;
    }


    public static void navegarPara(ActionEvent event, String arquivo, String titulo) throws IOException {
        Parent root = FXMLLoader.load(MetaUtils.class.getResource(arquivo));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setTitle(titulo);
        stage.setScene(new Scene(root, 640, 480));
    }


    public static String formatarValorMonetario(double valor) {
        return String.format("%.2f", valor).replace(".", ",");
    }
}