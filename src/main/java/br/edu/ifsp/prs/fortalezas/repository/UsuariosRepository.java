package br.edu.ifsp.prs.fortalezas.repository;

import br.edu.ifsp.prs.fortalezas.model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {

    Usuarios findByEmail(String email);
}
