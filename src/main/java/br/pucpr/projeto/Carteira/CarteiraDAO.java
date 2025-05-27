package br.pucpr.projeto.Carteira;

import java.io.*;
import java.util.ArrayList;

public class CarteiraDAO {

    private static final String arquivo = "Carteiras.dat";

    public static void salvarLista(ArrayList<Carteira> carteiras) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(carteiras);
        } catch (IOException e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }

    public static ArrayList<Carteira> lerLista() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
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
                carteiras.set(i, carteiraAtualizada);
                break;
            }
        }

        salvarLista(carteiras);
    }
}