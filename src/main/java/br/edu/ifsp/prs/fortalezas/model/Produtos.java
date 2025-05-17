package br.edu.ifsp.prs.fortalezas.model;

import br.edu.ifsp.prs.fortalezas.dto.produtos.CadastrarProdutosDTO;
import br.edu.ifsp.prs.fortalezas.enums.TipoProduto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "produtos")
@Entity(name = "produtos")
@EqualsAndHashCode(of = "id")
public class Produtos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String marca;
    private String estiloDecoracao;
    private String corAmbiente;
    private String localInstalacao;
    private String prioridade;
    private String descQuantidade;
    private float vlrUnit;
    private String imagem;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoProduto tipo;

    public Produtos(CadastrarProdutosDTO cadastrarProdutosDTO) {
        this.nome = cadastrarProdutosDTO.nome();
        this.marca = cadastrarProdutosDTO.marca();
        this.estiloDecoracao = cadastrarProdutosDTO.estiloDecoracao();
        this.corAmbiente = cadastrarProdutosDTO.corAmbiente();
        this.localInstalacao = cadastrarProdutosDTO.localInstalacao();
        this.descQuantidade = cadastrarProdutosDTO.descQuantidade();
        this.prioridade = cadastrarProdutosDTO.prioridade();
        this.vlrUnit = cadastrarProdutosDTO.vlrUnit();
        this.tipo = cadastrarProdutosDTO.tipoProduto();

    }
}
