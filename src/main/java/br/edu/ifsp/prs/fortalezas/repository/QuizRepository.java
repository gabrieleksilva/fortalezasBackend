package br.edu.ifsp.prs.fortalezas.repository;

import br.edu.ifsp.prs.fortalezas.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
