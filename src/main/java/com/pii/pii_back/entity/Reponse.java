package com.pii.pii_back.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "reponse")
public class Reponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reponses", nullable = false, columnDefinition = "TEXT")
    private String reponses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_question", nullable = false)
    private Question question;

    @OneToMany(mappedBy = "reponse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OptionReponse> optionReponses;

    @Column(name = "response_id", nullable = true)
    private String responseId;

    public Reponse() {
    }

    public Reponse(String reponses, Question question) {
        this.reponses = reponses;
        this.question = question;
    }

    public Long getId() {
        return id;
    }

    public String getReponses() {
        return reponses;
    }

    public void setReponses(String reponses) {
        this.reponses = reponses;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<OptionReponse> getOptionReponses() {
        return optionReponses;
    }

    public void setOptionReponses(List<OptionReponse> optionReponses) {
        this.optionReponses = optionReponses;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }
}
