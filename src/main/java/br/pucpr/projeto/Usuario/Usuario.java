package br.pucpr.projeto.Usuario;

import java.io.Serializable;

public class Usuario implements Serializable {
    private static final long seriaoVersionUID = 1L;

    private int id;
    private String nomeUsuario;
    private String emailUsuario;

    public Usuario(int id, String nomeUsuario, String emailUsuario) {
        this.id = id;
        this.nomeUsuario = nomeUsuario;
        this.emailUsuario = emailUsuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    @Override
    public String toString() {

        return "ID: " + id +
                "; Nome: " + nomeUsuario +
                "; Email: " + emailUsuario;
    }
}
