package br.pucpr.projeto.Metas;

import java.io.*;
import java.util.ArrayList;

public class MetaDAO {

    // Constante com a pasta e o nome do arquivo para persistir o objeto
    private static final String arquivo = "Metas.dat";

    // Escreve uma lista de objetos no arquivo
    public static void salvarLista(ArrayList<Meta> Metas) {
        try {
            File arq = new File(arquivo);
            if (!arq.exists()) {
                arq.createNewFile();
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arq));
            oos.writeObject(Metas);
            oos.close();
            System.out.println("Lista de Metas salva com sucesso.");
        } catch (FileNotFoundException e) {
            System.err.println("Erro ao salvar lista: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro ao salvar lista: " + e.getMessage());
        }
    }

    // Lê uma lista de objetos do arquivo
    public static ArrayList<Meta> lerLista() {
        ArrayList<Meta> lista = new ArrayList<>();
        try  {
            File arq = new File(arquivo);
            if (arq.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo));
                lista = (ArrayList<Meta>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao ler lista: " + e.getMessage());
        }
        return lista;
    }

    // Adiciona uma nova Meta à lista e regrava o arquivo
    public static void adicionarMeta(Meta novaMeta) {
        ArrayList<Meta> Metas = lerLista();
        Metas.add(novaMeta);
        salvarLista(Metas);
    }

}
