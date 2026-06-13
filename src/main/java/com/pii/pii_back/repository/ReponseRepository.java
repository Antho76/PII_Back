package com.pii.pii_back.repository;

import com.pii.pii_back.entity.Reponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReponseRepository extends JpaRepository<Reponse, Long> {
	@Query("select count(r) from Reponse r where r.question.formulaire.id = :formulaireId")
	long countByFormulaireId(Long formulaireId);

	List<Reponse> findByQuestion_Formulaire_Id(Long formulaireId);
}
