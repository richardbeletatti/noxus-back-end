package com.alemcrm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alemcrm.model.Intent;
import com.alemcrm.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByIntent(Intent intentEscolhida);
    List<Question> findByIntentId(Long intentId);
}

