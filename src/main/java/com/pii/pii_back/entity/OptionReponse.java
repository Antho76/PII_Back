package com.pii.pii_back.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "option_reponse")
public class OptionReponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "int_rang", nullable = false)
    private Integer rang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_option", nullable = false)
    private Options option;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reponse", nullable = false)
    private Reponse reponse;

    public OptionReponse() {
    }

    public OptionReponse(Integer rang, Options option, Reponse reponse) {
        this.rang = rang;
        this.option = option;
        this.reponse = reponse;
    }

    public Long getId() {
        return id;
    }

    public Integer getRang() {
        return rang;
    }

    public void setRang(Integer rang) {
        this.rang = rang;
    }

    public Options getOption() {
        return option;
    }

    public void setOption(Options option) {
        this.option = option;
    }

    public Reponse getReponse() {
        return reponse;
    }

    public void setReponse(Reponse reponse) {
        this.reponse = reponse;
    }
}
