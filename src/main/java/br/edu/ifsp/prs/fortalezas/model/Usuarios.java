package br.edu.ifsp.prs.fortalezas.model;

import br.edu.ifsp.prs.fortalezas.dto.usuarios.CadastrarUsuariosDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "usuarios")
@Entity(name = "usuarios")
@EqualsAndHashCode(of = "id")
public class Usuarios implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "usuario")
    private String email;

    private String senha;
    private String nome;

    @Column(name= "celular")
    private String telefone;

    public Usuarios(CadastrarUsuariosDTO usuariosDTO) {
        this.email = usuariosDTO.email();
        this.nome = usuariosDTO.nome();
        this.telefone = usuariosDTO.telefone();
    }

    // 👇 Implementação obrigatória da interface UserDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER")); // ou outro papel, se usar
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
