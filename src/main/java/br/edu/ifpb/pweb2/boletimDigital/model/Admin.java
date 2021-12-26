package br.edu.ifpb.pweb2.boletimDigital.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tb_admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_nu_id")
    private Long id;

    @Column(name = "ad_nm_nome")
    @NotNull(message = "Campo nome não pode ser vazio")
    private String nome;

    @Column(name = "sn_senha")
    @NotNull(message = "Campo senha não pode ser vazio")
    private String senha;

    @Deprecated
    public Admin() {
    }

    public Admin(String nome, String senha) {
        this.nome = nome;
        this.senha = senha;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
