package br.pucpr.projeto.Noticia;

import br.pucpr.projeto.Alert.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NoticiaController implements Initializable {

    @FXML
    private ListView<Noticia> listaNoticias;

    @FXML
    private TextField inputTitulo;
    @FXML
    private TextField inputAnalistaId;
    @FXML
    private ComboBox<String> inputCategoria;
    @FXML
    private TextArea inputConteudo;
    @FXML
    private ComboBox<String> inputStatus;

    private Noticia noticiaParaEditar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (listaNoticias != null) {
            atualizarNoticias();
        }

        // Inicializar ComboBox de categorias
        if (inputCategoria != null) {
            inputCategoria.getItems().addAll(
                    "Economia",
                    "Mercado Financeiro",
                    "Criptomoedas",
                    "Investimentos",
                    "Política Econômica",
                    "Análise Técnica",
                    "Fundos de Investimento",
                    "Renda Fixa",
                    "Renda Variável",
                    "Internacional"
            );
        }

        // Inicializar ComboBox de status
        if (inputStatus != null) {
            inputStatus.getItems().addAll("Rascunho", "Publicada", "Arquivada");
            inputStatus.setValue("Rascunho");
        }

        // Valor padrão para analista ID
        if (inputAnalistaId != null) {
            inputAnalistaId.setText("1");
        }
    }

    // ========== TELA PRINCIPAL - MINHAS NOTÍCIAS ==========

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

            NoticiaController controller = loader.getController();
            controller.carregarDadosNoticia(noticiaSelecionada);

            Stage stage = new Stage();
            stage.setTitle("My Wallet - Editar Notícia");
            stage.setScene(new Scene(root, 640, 480));
            stage.showAndWait();

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

    // ========== CRIAR NOTÍCIA ==========

    @FXML
    public void enviarNoticia(ActionEvent event) {
        try {
            if (!validarCampos()) return;

            String titulo = inputTitulo.getText().trim();
            int analistaId = Integer.parseInt(inputAnalistaId.getText().trim());
            String categoria = inputCategoria.getValue();
            String conteudo = inputConteudo.getText().trim();
            String status = inputStatus.getValue();
            LocalDateTime dataCriacao = LocalDateTime.now();

            int novoId = NoticiaDAO.gerarProximoId();
            Noticia novaNoticia = new Noticia(novoId, analistaId, titulo, categoria, conteudo, status, dataCriacao);
            NoticiaDAO.adicionarNoticia(novaNoticia);

            AlertUtils.mostrarSucesso("Notícia criada com sucesso!");
            voltarParaNoticias(event);

        } catch (Exception e) {
            AlertUtils.mostrarErro("Erro", "Erro ao criar notícia: " + e.getMessage());
        }
    }

    // ========== EDITAR NOTÍCIA ==========

    public void carregarDadosNoticia(Noticia noticia) {
        this.noticiaParaEditar = noticia;
        inputTitulo.setText(noticia.getTitulo());
        inputAnalistaId.setText(String.valueOf(noticia.getAnalistaId()));
        inputCategoria.setValue(noticia.getCategoria());
        inputConteudo.setText(noticia.getConteudo());
        inputStatus.setValue(noticia.getStatus());
    }

    @FXML
    public void salvarAlteracoes(ActionEvent event) {
        try {
            if (!validarCampos()) return;

            String titulo = inputTitulo.getText().trim();
            int analistaId = Integer.parseInt(inputAnalistaId.getText().trim());
            String categoria = inputCategoria.getValue();
            String conteudo = inputConteudo.getText().trim();
            String status = inputStatus.getValue();

            noticiaParaEditar.setTitulo(titulo);
            noticiaParaEditar.setAnalistaId(analistaId);
            noticiaParaEditar.setCategoria(categoria);
            noticiaParaEditar.setConteudo(conteudo);
            noticiaParaEditar.setStatus(status);

            NoticiaDAO.atualizarNoticia(noticiaParaEditar);
            AlertUtils.mostrarSucesso("Notícia atualizada com sucesso!");
            voltarParaNoticias(event);

        } catch (Exception e) {
            AlertUtils.mostrarErro("Erro", "Erro ao atualizar notícia: " + e.getMessage());
        }
    }

    // ========== MÉTODOS AUXILIARES ==========

    private boolean validarCampos() {
        // Verificar se os campos existem (evitar NullPointerException)
        if (inputTitulo == null || inputAnalistaId == null || inputCategoria == null ||
                inputConteudo == null || inputStatus == null) {
            AlertUtils.mostrarErroSimples("Erro interno: campos não inicializados!");
            return false;
        }

        String titulo = inputTitulo.getText();
        String analistaIdStr = inputAnalistaId.getText();
        String categoria = inputCategoria.getValue();
        String conteudo = inputConteudo.getText();
        String status = inputStatus.getValue();

        // Tratar campos nulos como strings vazias
        if (titulo == null) titulo = "";
        if (analistaIdStr == null) analistaIdStr = "";
        if (conteudo == null) conteudo = "";

        // Fazer trim apenas se não for nulo
        titulo = titulo.trim();
        analistaIdStr = analistaIdStr.trim();
        conteudo = conteudo.trim();

        // Validar campo título
        if (titulo.isEmpty()) {
            AlertUtils.mostrarAvisoSimples("O título da notícia deve ser preenchido!");
            inputTitulo.requestFocus();
            return false;
        }

        if (titulo.length() < 5) {
            AlertUtils.mostrarAvisoSimples("O título deve ter pelo menos 5 caracteres!");
            inputTitulo.requestFocus();
            return false;
        }

        if (titulo.length() > 200) {
            AlertUtils.mostrarAvisoSimples("O título deve ter no máximo 200 caracteres!");
            inputTitulo.requestFocus();
            return false;
        }

        // Validar analista ID
        if (analistaIdStr.isEmpty()) {
            AlertUtils.mostrarAvisoSimples("O ID do analista deve ser preenchido!");
            inputAnalistaId.requestFocus();
            return false;
        }

        try {
            int analistaId = Integer.parseInt(analistaIdStr);
            if (analistaId <= 0) {
                AlertUtils.mostrarErroSimples("O ID do analista deve ser um número positivo!");
                inputAnalistaId.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            AlertUtils.mostrarErroSimples("O ID do analista deve ser um número válido!");
            inputAnalistaId.requestFocus();
            return false;
        }

        // Validar categoria
        if (categoria == null || categoria.trim().isEmpty()) {
            AlertUtils.mostrarAvisoSimples("A categoria deve ser selecionada!");
            inputCategoria.requestFocus();
            return false;
        }

        // Validar conteúdo
        if (conteudo.isEmpty()) {
            AlertUtils.mostrarAvisoSimples("O conteúdo da notícia deve ser preenchido!");
            inputConteudo.requestFocus();
            return false;
        }

        if (conteudo.length() < 10) {
            AlertUtils.mostrarAvisoSimples("O conteúdo deve ter pelo menos 10 caracteres!");
            inputConteudo.requestFocus();
            return false;
        }

        if (conteudo.length() > 5000) {
            AlertUtils.mostrarAvisoSimples("O conteúdo deve ter no máximo 5000 caracteres!");
            inputConteudo.requestFocus();
            return false;
        }

        // Validar status
        if (status == null || status.trim().isEmpty()) {
            AlertUtils.mostrarAvisoSimples("O status deve ser selecionado!");
            inputStatus.requestFocus();
            return false;
        }

        return true;
    }

    private void navegarPara(ActionEvent event, String arquivo, String titulo) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(arquivo));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setTitle(titulo);
        stage.setScene(new Scene(root, 640, 480));
    }

    @FXML
    public void voltarParaNoticias(ActionEvent event) {
        try {
            navegarPara(event, "MinhasNoticias.fxml", "My Wallet - Minhas Notícias");
        } catch (IOException e) {
            AlertUtils.mostrarErro("Erro", "Erro ao voltar para notícias: " + e.getMessage());
        }
    }

}