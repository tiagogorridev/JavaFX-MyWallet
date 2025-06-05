package br.pucpr.projeto.Usuario;

import java.io.*;
import java.util.ArrayList;

public class    UsuarioDAO {

    private static final String arquivo = "Usuarios.dat";

    public static void salvarLista(ArrayList<Usuario> usuarios) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(usuarios);
        } catch (IOException e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }

    public static ArrayList<Usuario> lerLista() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (ArrayList<Usuario>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public static void adicionarUsuario(Usuario novoUsuario) {
        ArrayList<Usuario> usuarios = lerLista();
        usuarios.add(novoUsuario);
        salvarLista(usuarios);
    }

    public static void removerUsuarioPorId(int id) {
        ArrayList<Usuario> usuarios = lerLista();
        usuarios.removeIf(c -> c.getId() == id);
        salvarLista(usuarios);
    }

    public static void atualizarUsuario(Usuario usuarioAtualizado) {
        ArrayList<Usuario> usuarios = lerLista();

        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == usuarioAtualizado.getId()) {
                usuarios.set(i, usuarioAtualizado);
                break;
            }
        }

        salvarLista(usuarios);
    }
}