package com.pii.pii_back.entity;

import jakarta.persistence.*;
import java.util.List;
import com.pii.pii_back.entity.enums.FormulaireEtat;

@Entity
@Table(name = "formulaire")
public class Formulaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_auteur", nullable = false)
    private Administrateur auteur;

    @OneToMany(mappedBy = "formulaire", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat", nullable = false)
    private FormulaireEtat etat = FormulaireEtat.PENDING;

    @Column(name = "share_token", unique = true)
    private String shareToken;

    public Formulaire() {
    }

    public Formulaire(String libelle, Administrateur auteur) {
        this.libelle = libelle;
        this.auteur = auteur;
    }

    public Formulaire(String libelle, String description, Administrateur auteur) {
        this.libelle = libelle;
        this.description = description;
        this.auteur = auteur;
    }

    public Long getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Administrateur getAuteur() {
        return auteur;
    }

    public void setAuteur(Administrateur auteur) {
        this.auteur = auteur;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public FormulaireEtat getEtat() {
        return etat;
    }

    public void setEtat(FormulaireEtat etat) {
        this.etat = etat;
    }

    public String getShareToken() {
        return shareToken;
    }

    public void setShareToken(String shareToken) {
        this.shareToken = shareToken;
    }
}
