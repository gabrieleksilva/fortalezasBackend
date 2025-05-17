package br.edu.ifsp.prs.fortalezas.repository;

import br.edu.ifsp.prs.fortalezas.model.Produtos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutosRepository extends JpaRepository<Produtos, Long> {
}
