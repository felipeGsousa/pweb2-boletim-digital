package br.edu.ifpb.pweb2.boletimDigital.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "tb_estudantes")
public class Estudante {

    @Id
    @Column(name = "nu_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nm_nome")
    @NotBlank(message = "Campo Obrigatório")
    private String nome;

    @Column(name = "dt_nascimento")
    @NotNull(message = "Campo Obrigatório")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date nascimento;

    @Column(name = "ft_faltas")
    @Max(value = 100, message = "Valor não pode ser maior que 100")
    private Integer faltas;

    @Column(name = "nt_nota1")
    @Max(value = 100, message = "Valor não pode ser maior que 100")
    private BigDecimal nota1;

    @Column(name = "nt_nota2")
    @Max(value = 100, message = "Valor não pode ser maior que 100")
    private BigDecimal nota2;

    @Column(name = "nt_nota3")
    @Max(value = 100, message = "Valor não pode ser maior que 100")
    private BigDecimal nota3;

    @Column(name = "nf_nota_final")
    @Max(value = 100, message = "Valor não pode ser maior que 100")
    private BigDecimal notaFinal;

    @Column(name = "st_situacao")
    private EnumSituacao situacao;

    @Deprecated
    public Estudante() {
    }

    public Estudante(String nome, Date nascimento, EnumSituacao situacao) {
        this.nome = nome;
        this.nascimento = nascimento;
        this.situacao = situacao;
    }

    public enum EnumSituacao {
        RP, RF, FN, MT, AP;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getNascimento() {
        return nascimento;
    }

    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

    public Integer getFaltas() {
        return faltas;
    }

    public void setFaltas(Integer faltas) {
        this.faltas = faltas;
    }

    public BigDecimal getNota1() {
        return nota1;
    }

    public void setNota1(BigDecimal nota1) {
        this.nota1 = nota1;
    }

    public BigDecimal getNota2() {
        return nota2;
    }

    public void setNota2(BigDecimal nota2) {
        this.nota2 = nota2;
    }

    public BigDecimal getNota3() {
        return nota3;
    }

    public void setNota3(BigDecimal nota3) {
        this.nota3 = nota3;
    }

    public BigDecimal getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(BigDecimal notaFinal) {
        this.notaFinal = notaFinal;
    }

    public EnumSituacao getSituacao() {
        return situacao;
    }

    public void setSituacao(EnumSituacao situacao) {
        this.situacao = situacao;
    }
}
