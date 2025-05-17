package br.edu.ifsp.prs.fortalezas.repository;

import br.edu.ifsp.prs.fortalezas.model.Contato;
import br.edu.ifsp.prs.fortalezas.model.Produtos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContatoRepository extends JpaRepository<Contato, Long> {
}
