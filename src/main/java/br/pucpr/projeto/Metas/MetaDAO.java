package br.pucpr.projeto.Metas;

import java.io.*;
import java.util.ArrayList;

public class MetaDAO {

    private static final String ARQUIVO = "Metas.dat";

    public static void salvarLista(ArrayList<Meta> metas) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            oos.writeObject(metas);
        } catch (IOException e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }

    public static ArrayList<Meta> lerLista() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO))) {
            return (ArrayList<Meta>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public static void adicionarMeta(Meta novaMeta) {
        ArrayList<Meta> metas = lerLista();
        metas.add(novaMeta);
        salvarLista(metas);
    }

    public static void removerMetaPorId(int id) {
        ArrayList<Meta> metas = lerLista();
        metas.removeIf(m -> m.getId() == id);
        salvarLista(metas);
    }

    public static void atualizarMeta(Meta metaAtualizada) {
        ArrayList<Meta> metas = lerLista();
        for (int i = 0; i < metas.size(); i++) {
            if (metas.get(i).getId() == metaAtualizada.getId()) {
                metas.set(i, metaAtualizada);
                break;
            }
        }
        salvarLista(metas);
    }

    public static int gerarProximoId() {
        ArrayList<Meta> metas = lerLista();
        return metas.stream()
                .mapToInt(Meta::getId)
                .max()
                .orElse(0) + 1;
    }
}