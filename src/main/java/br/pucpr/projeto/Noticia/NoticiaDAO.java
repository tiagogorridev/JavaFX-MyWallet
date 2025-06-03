package br.pucpr.projeto.Noticia;

import java.io.*;
import java.util.ArrayList;

public class NoticiaDAO {

    private static final String arquivo = "Noticias.dat";

    public static void salvarLista(ArrayList<Noticia> noticias) {
        // Cria um ObjectOutputStream que grava objetos no arquivo
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            // Escreve a lista de objetos no arquivo
            oos.writeObject(noticias);
        } catch (IOException e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }

    // Lê a lista de notícias do arquivo especificado
    public static ArrayList<Noticia> lerLista() {
        // Cria um ObjectInputStream que lê objetos do arquivo
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            // Lê o objeto do arquivo
            return (ArrayList<Noticia>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public static void adicionarNoticia(Noticia novaNoticia) {
        ArrayList<Noticia> noticias = lerLista();
        noticias.add(novaNoticia);
        salvarLista(noticias);
    }

    public static void removerNoticiaPorId(int id) {
        ArrayList<Noticia> noticias = lerLista();
        noticias.removeIf(n -> n.getId() == id);
        salvarLista(noticias);
    }

    public static void atualizarNoticia(Noticia noticiaAtualizada) {
        ArrayList<Noticia> noticias = lerLista();

        for (int i = 0; i < noticias.size(); i++) {
            if (noticias.get(i).getId() == noticiaAtualizada.getId()) {
                noticias.set(i, noticiaAtualizada);
                break;
            }
        }
        salvarLista(noticias);
    }

    public static int gerarProximoId() {
        ArrayList<Noticia> noticias = lerLista();
        int maiorId = 0;
        for (Noticia noticia : noticias) {
            if (noticia.getId() > maiorId) {
                maiorId = noticia.getId();
            }
        }
        return maiorId + 1;
    }
}