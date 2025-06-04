package br.pucpr.projeto.Carteira;

import java.io.*;
import java.util.ArrayList;

public class CarteiraDAO {

    private static final String arquivo = "Carteiras.dat";

    public static void salvarLista(ArrayList<Carteira> carteiras) {
        // Cria um ObjectOutputStream que grava objetos no arquivo
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            // Escreve a lista de objetos no arquivo
            oos.writeObject(carteiras);
        } catch (IOException e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }

    // Lê a lista de carteiras do arquivo especificado
    public static ArrayList<Carteira> lerLista() {
        // Cria um ObjectInputStream que lê objetos do arquivo
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            // Lê o objeto do arquivo
            return (ArrayList<Carteira>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public static void adicionarCarteira(Carteira novaCarteira) {
        ArrayList<Carteira> carteiras = lerLista();
        carteiras.add(novaCarteira);
        salvarLista(carteiras);
    }

    public static void removerCarteiraPorId(int id) {
        ArrayList<Carteira> carteiras = lerLista();
        carteiras.removeIf(c -> c.getId() == id);
        salvarLista(carteiras);
    }

    public static void atualizarCarteira(Carteira carteiraAtualizada) {
        ArrayList<Carteira> carteiras = lerLista();

        for (int i = 0; i < carteiras.size(); i++) {
            if (carteiras.get(i).getId() == carteiraAtualizada.getId()) {
                carteiras.set(i, carteiraAtualizada); // substitui antiga pela nova
                break;
            }
        }
        salvarLista(carteiras);
    }
}