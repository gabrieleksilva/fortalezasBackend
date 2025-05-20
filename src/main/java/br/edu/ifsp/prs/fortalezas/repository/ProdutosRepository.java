package br.edu.ifsp.prs.fortalezas.repository;

import br.edu.ifsp.prs.fortalezas.enums.TipoProduto;
import br.edu.ifsp.prs.fortalezas.model.Produtos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutosRepository extends JpaRepository<Produtos, Long> {
    Page<Produtos> findByTipo(TipoProduto tipoProduto, Pageable pageable);

    @Query("""
        SELECT p FROM produtos p
        WHERE (:estilo IS NULL OR p.estiloDecoracao = :estilo)
          AND (:cor IS NULL OR p.corAmbiente = :cor)
          AND (:local IS NULL OR p.localInstalacao = :local)
          AND (:prioridade IS NULL OR p.prioridade = :prioridade)
    """)
    List<Produtos> buscarPorFiltros(
            @Param("estilo") String estilo,
            @Param("cor") String cor,
            @Param("local") String local,
            @Param("prioridade") String prioridade
    );
}
