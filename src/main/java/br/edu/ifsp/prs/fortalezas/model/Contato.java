package br.edu.ifsp.prs.fortalezas.model;

import br.edu.ifsp.prs.fortalezas.dto.contato.CadastrarDuvidasDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "duvidas_clientes")
@Entity(name = "duvidas_clientes")
@EqualsAndHashCode(of = "id")
public class Contato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String mensagem;
    private String nome;
    @Column(name= "id_cliente")
    private Integer idCliente;

    public Contato(CadastrarDuvidasDTO duvidasDTO) {
        this.email = duvidasDTO.email();
        this.mensagem = duvidasDTO.mensagem();
        this.nome = duvidasDTO.nome();
        this.idCliente = 1;
    }
}
