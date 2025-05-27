package br.pucpr.projeto.Carteira;

import br.pucpr.projeto.Alert.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CarteiraController implements Initializable {

    // Campos para ListView (MinhasCarteiras)
    @FXML
    private ListView<Carteira> lista;

    // Campos para formulários (CriarCarteira e EditarCarteira)
    @FXML
    private TextField inputNomeCarteira;
    @FXML
    private TextField inputDescricaoCarteira;
    @FXML
    private TextField inputQuantidadeAportada;

    // Carteira sendo editada
    private Carteira carteiraParaEditar;

    // Inicialização para a tela de MinhasCarteiras
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (lista != null) {
            atualizarCarteira();
        }
    }

    // ========== MÉTODOS DE MINHAS CARTEIRAS ==========

    @FXML
    public void criarCarteira(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("CriarCarteira.fxml"));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setTitle("My Wallet - Criar Carteira");
        stage.setScene(new Scene(root, 640, 480));
    }

    @FXML
    public void editarCarteira(ActionEvent event) {
        Carteira carteiraSelecionada = lista.getSelectionModel().getSelectedItem();

        if (carteiraSelecionada == null) {
            AlertUtils.mostrarAvisoSimples("Por favor, selecione uma carteira para editar!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditarCarteira.fxml"));
            Parent root = loader.load();

            // Passa a carteira selecionada para o controller de edição
            CarteiraController controller = loader.getController();
            controller.carregarDadosCarteira(carteiraSelecionada);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setTitle("My Wallet - Editar Carteira");
            stage.setScene(new Scene(root, 640, 480));

        } catch (IOException e) {
            AlertUtils.mostrarErro("Erro", "Não foi possível abrir a tela de edição: " + e.getMessage());
        }
    }

    @FXML
    public void atualizarCarteira() {
        try {
            // PASSO 1: Limpar a lista atual na interface
            lista.getItems().clear();

            // PASSO 2: Buscar dados atualizados do arquivo
            ArrayList<Carteira> carteirasAtualizadas = CarteiraDAO.lerLista();

            // PASSO 3: Adicionar as carteiras na interface (silenciosamente)
            lista.getItems().addAll(carteirasAtualizadas);

        } catch (Exception e) {
            // PASSO 4: Tratar erros (apenas erros críticos)
            AlertUtils.mostrarErro("Erro ao atualizar",
                    "Não foi possível atualizar a lista de carteiras: " + e.getMessage());
        }
    }

    @FXML
    public void apagarCarteira() {
        Carteira carteiraSelecionada = lista.getSelectionModel().getSelectedItem();

        if (carteiraSelecionada == null) {
            AlertUtils.mostrarAvisoSimples("Por favor, selecione uma carteira para excluir!");
            return;
        }

        String mensagemConfirmacao = String.format(
                "Tem certeza que deseja excluir a carteira:\n\n" +
                        "Nome: %s\n" +
                        "Descrição: %s\n" +
                        "Valor: R$ %.2f\n\n" +
                        "Esta ação não pode ser desfeita!",
                carteiraSelecionada.getNomeCarteira(),
                carteiraSelecionada.getDescricaoCarteira(),
                carteiraSelecionada.getQuantidadeAportada()
        );

        boolean confirmado = AlertUtils.confirmar(mensagemConfirmacao);

        if (!confirmado) {
            return;
        }

        try {
            CarteiraDAO.removerCarteiraPorId(carteiraSelecionada.getId());
            atualizarCarteira();
            AlertUtils.mostrarSucesso("Carteira excluída com sucesso!");

        } catch (Exception e) {
            AlertUtils.mostrarErro("Erro ao excluir carteira",
                    "Ocorreu um erro ao tentar excluir a carteira: " + e.getMessage());
        }
    }

    // ========== MÉTODOS DE CRIAR CARTEIRA ==========

    @FXML
    public void enviarCarteira(ActionEvent event) {
        try {
            String nome = inputNomeCarteira.getText().trim();
            String descricao = inputDescricaoCarteira.getText().trim();
            String valorStr = inputQuantidadeAportada.getText().trim();

            if (nome.isEmpty() || descricao.isEmpty() || valorStr.isEmpty()) {
                AlertUtils.mostrarAvisoSimples("Todos os campos devem ser preenchidos!");
                return;
            }

            double valor = Double.parseDouble(valorStr);
            if (valor < 0) {
                AlertUtils.mostrarErroSimples("Valor não pode ser negativo!");
                return;
            }

            int novoId = CarteiraDAO.lerLista().size() + 1;
            Carteira novaCarteira = new Carteira(novoId,nome , descricao, valor);
            CarteiraDAO.adicionarCarteira(novaCarteira);

            AlertUtils.mostrarSucesso("Carteira criada com sucesso!");
            voltarParaCarteiras(event);

        } catch (NumberFormatException e) {
            AlertUtils.mostrarErroSimples("Valor inválido! Use apenas números.");
        } catch (Exception e) {
            AlertUtils.mostrarErro("Erro", e.getMessage());
        }
    }

    // ========== MÉTODOS DE EDITAR CARTEIRA ==========

    public void carregarDadosCarteira(Carteira carteira) {
        this.carteiraParaEditar = carteira;

        // Preenche os campos com os dados atuais da carteira
        inputNomeCarteira.setText(carteira.getNomeCarteira());
        inputDescricaoCarteira.setText(carteira.getDescricaoCarteira());

        // Formatar o valor corretamente (sem notação científica)
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
        df.setMaximumFractionDigits(2);
        df.setGroupingUsed(false);
        inputQuantidadeAportada.setText(df.format(carteira.getQuantidadeAportada()));
    }

    @FXML
    public void salvarAlteracoes(ActionEvent event) {
        try {
            String nome = inputNomeCarteira.getText().trim();
            String descricao = inputDescricaoCarteira.getText().trim();
            String valorStr = inputQuantidadeAportada.getText().trim();

            if (nome.isEmpty() || descricao.isEmpty() || valorStr.isEmpty()) {
                AlertUtils.mostrarAvisoSimples("Todos os campos devem ser preenchidos!");
                return;
            }

            double valor = Double.parseDouble(valorStr);
            if (valor < 0) {
                AlertUtils.mostrarErroSimples("Valor não pode ser negativo!");
                return;
            }

            // Atualiza os dados da carteira (mantém o email original)
            carteiraParaEditar.setNomeCarteira(nome);
            carteiraParaEditar.setDescricaoCarteira(descricao);
            carteiraParaEditar.setQuantidadeAportada(valor);

            // Salva as alterações no arquivo
            CarteiraDAO.atualizarCarteira(carteiraParaEditar);

            AlertUtils.mostrarSucesso("Carteira atualizada com sucesso!");
            voltarParaCarteiras(event);

        } catch (NumberFormatException e) {
            AlertUtils.mostrarErroSimples("Valor inválido! Use apenas números.");
        } catch (Exception e) {
            AlertUtils.mostrarErro("Erro", e.getMessage());
        }
    }

    // ========== MÉTODO COMPARTILHADO ==========

    @FXML
    public void voltarParaCarteiras(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("MinhasCarteiras.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 640, 480));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}