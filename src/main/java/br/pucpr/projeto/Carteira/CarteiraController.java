//package br.pucpr.projeto.Carteira;
//
//import br.pucpr.projeto.Alert.AlertUtils;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.ListView;
//import javafx.scene.control.TextField;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//import java.net.URL;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.ResourceBundle;
//
//public class CarteiraController implements Initializable {
//    @FXML
//    private ListView<Carteira> lista;
//    @FXML
//    private TextField inputNomeCarteira;
//    @FXML
//    private TextField inputDescricaoCarteira;
//    @FXML
//    private TextField inputQuantidadeAportada;
//
//    private Carteira carteiraParaEditar;
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        if (lista != null) {
//            atualizarCarteira();
//        }
//    }
//
//    // Navegação entre telas
//    @FXML
//    public void criarCarteira(ActionEvent event) {
//        trocarTela(event, "CriarCarteira.fxml", "My Wallet - Criar Carteira");
//    }
//
//    @FXML
//    public void voltarParaCarteiras(ActionEvent event) {
//        trocarTela(event, "MinhasCarteiras.fxml", "My Wallet - Minhas Carteiras");
//    }
//
//    private void trocarTela(ActionEvent event, String fxml, String titulo) {
//        try {
//            // Carrega fxml
//            Parent root = FXMLLoader.load(getClass().getResource(fxml));
//            // Faz cast explicito para node e stage
//            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
//            stage.setTitle(titulo);
//            stage.setScene(new Scene(root, 640, 480));
//        } catch (IOException e) {
//            AlertUtils.mostrarErro("Erro de Interface", "Não foi possível carregar a tela '" + fxml + "': " + e.getMessage());
//        } catch (NullPointerException e) {
//            AlertUtils.mostrarErro("Arquivo não encontrado", "O arquivo de interface '" + fxml + "' não foi encontrado!");
//        } catch (ClassCastException e) {
//            AlertUtils.mostrarErro("Erro interno", "Erro ao obter a janela da aplicação: " + e.getMessage());
//        }
//    }
//
//    // Leitura - recarregando interface
//    @FXML
//    public void atualizarCarteira() {
//        try {
//            lista.getItems().clear();
//            ArrayList<Carteira> carteirasAtualizadas = CarteiraDAO.lerLista();
//            lista.getItems().addAll(carteirasAtualizadas);
//        } catch (RuntimeException e) {
//            AlertUtils.mostrarErro("Erro ao carregar dados", "Não foi possível carregar as carteiras: " + e.getMessage());
//        }
//    }
//
//    // Criação
//    @FXML
//    public void enviarCarteira(ActionEvent event) {
//        try {
//            String nome = inputNomeCarteira.getText().trim();
//            String descricao = inputDescricaoCarteira.getText().trim();
//            String valorStr = inputQuantidadeAportada.getText().trim();
//
//            if (nome.isEmpty() || descricao.isEmpty() || valorStr.isEmpty()) {
//                AlertUtils.mostrarAvisoSimples("Todos os campos devem ser preenchidos!");
//                return;
//            }
//
//            double valor = Double.parseDouble(valorStr);
//            if (valor < 0) {
//                AlertUtils.mostrarErroSimples("Valor não pode ser negativo!");
//                return;
//            }
//
//            ArrayList<Carteira> carteiras = CarteiraDAO.lerLista();
//            int novoId = 1;
//
//            if (!carteiras.isEmpty()) {
//                int maiorId = carteiras.stream()
//                        .mapToInt(Carteira::getId)
//                        .max()
//                        .orElse(0);
//                novoId = maiorId + 1;
//            }
//
//            Carteira novaCarteira = new Carteira(novoId, nome, descricao, valor);
//            CarteiraDAO.adicionarCarteira(novaCarteira);
//
//            AlertUtils.mostrarSucesso("Carteira criada com sucesso!");
//            voltarParaCarteiras(event);
//
//        } catch (NumberFormatException e) {
//            AlertUtils.mostrarErroSimples("Valor inválido! Digite apenas números (ex: 1000.50)");
//        } catch (RuntimeException e) {
//            AlertUtils.mostrarErro("Erro inesperado", "Ocorreu um erro ao criar a carteira: " + e.getMessage());
//        }
//    }
//
//    // Edição
//    @FXML
//    public void editarCarteira(ActionEvent event) {
//        Carteira carteiraSelecionada = lista.getSelectionModel().getSelectedItem();
//
//        if (carteiraSelecionada == null) {
//            AlertUtils.mostrarAvisoSimples("Por favor, selecione uma carteira para editar!");
//            return;
//        }
//
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditarCarteira.fxml"));
//            Parent root = loader.load();
//
//            CarteiraController controller = loader.getController();
//            controller.carregarDadosCarteira(carteiraSelecionada);
//
//            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
//            stage.setTitle("My Wallet - Editar Carteira");
//            stage.setScene(new Scene(root, 640, 480));
//
//        } catch (IOException e) {
//            AlertUtils.mostrarErro("Erro de Interface", "Não foi possível abrir a tela de edição: " + e.getMessage());
//        } catch (NullPointerException e) {
//            AlertUtils.mostrarErro("Erro interno", "Erro ao carregar dados da carteira para edição.");
//        }
//    }
//
//    public void carregarDadosCarteira(Carteira carteira) {
//        this.carteiraParaEditar = carteira;
//
//        inputNomeCarteira.setText(carteira.getNomeCarteira());
//        inputDescricaoCarteira.setText(carteira.getDescricaoCarteira());
//
//        DecimalFormat df = new DecimalFormat("#.##");
//        df.setMaximumFractionDigits(2);
//        df.setGroupingUsed(false);
//        inputQuantidadeAportada.setText(df.format(carteira.getQuantidadeAportada()));
//    }
//
//    @FXML
//    public void salvarAlteracoes(ActionEvent event) {
//        try {
//            String nome = inputNomeCarteira.getText().trim();
//            String descricao = inputDescricaoCarteira.getText().trim();
//            String valorStr = inputQuantidadeAportada.getText().trim();
//
//            if (nome.isEmpty() || descricao.isEmpty() || valorStr.isEmpty()) {
//                AlertUtils.mostrarAvisoSimples("Todos os campos devem ser preenchidos!");
//                return;
//            }
//
//            double valor = Double.parseDouble(valorStr);
//            if (valor < 0) {
//                AlertUtils.mostrarErroSimples("Valor não pode ser negativo!");
//                return;
//            }
//
//            carteiraParaEditar.setNomeCarteira(nome);
//            carteiraParaEditar.setDescricaoCarteira(descricao);
//            carteiraParaEditar.setQuantidadeAportada(valor);
//
//            CarteiraDAO.atualizarCarteira(carteiraParaEditar);
//
//            AlertUtils.mostrarSucesso("Carteira atualizada com sucesso!");
//            voltarParaCarteiras(event);
//
//        } catch (NumberFormatException e) {
//            AlertUtils.mostrarErroSimples("Valor inválido! Digite apenas números (ex: 1500.75)");
//        } catch (NullPointerException e) {
//            AlertUtils.mostrarErro("Erro de dados", "Dados da carteira não foram carregados corretamente. Tente novamente.");
//        } catch (RuntimeException e) {
//            AlertUtils.mostrarErro("Erro inesperado", "Erro ao salvar alterações: " + e.getMessage());
//        }
//    }
//
//    // Exclusão
//    @FXML
//    public void apagarCarteira() {
//        Carteira carteiraSelecionada = lista.getSelectionModel().getSelectedItem();
//
//        if (carteiraSelecionada == null) {
//            AlertUtils.mostrarAvisoSimples("Por favor, selecione uma carteira para excluir!");
//            return;
//        }
//
//        String mensagemConfirmacao = String.format(
//                "Tem certeza que deseja excluir a carteira:\n\n" +
//                        "Nome: %s\n" +
//                        "Descrição: %s\n" +
//                        "Valor: R$ %.2f\n\n" +
//                        "Esta ação não pode ser desfeita!",
//                carteiraSelecionada.getNomeCarteira(),
//                carteiraSelecionada.getDescricaoCarteira(),
//                carteiraSelecionada.getQuantidadeAportada()
//        );
//
//        boolean confirmado = AlertUtils.confirmar(mensagemConfirmacao);
//
//        if (!confirmado) {
//            return;
//        }
//
//        try {
//            CarteiraDAO.removerCarteiraPorId(carteiraSelecionada.getId());
//            atualizarCarteira();
//            AlertUtils.mostrarSucesso("Carteira excluída com sucesso!");
//        } catch (SecurityException e) {
//            AlertUtils.mostrarErro("Sem permissão",
//                    "Não há permissão para modificar o arquivo de carteiras.");
//        } catch (RuntimeException e) {
//            AlertUtils.mostrarErro("Erro inesperado",
//                    "Erro ao excluir carteira: " + e.getMessage());
//        }
//    }
//}