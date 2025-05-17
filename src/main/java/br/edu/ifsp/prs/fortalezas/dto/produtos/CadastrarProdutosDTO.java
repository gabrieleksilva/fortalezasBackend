package br.edu.ifsp.prs.fortalezas.dto.produtos;

import br.edu.ifsp.prs.fortalezas.enums.TipoProduto;

public record CadastrarProdutosDTO(
                                   String nome,
                                   String marca,
                                   String estiloDecoracao,
                                   String corAmbiente,
                                   String localInstalacao,
                                   String prioridade,
                                   String descQuantidade,
                                   float vlrUnit,
                                    TipoProduto tipoProduto
) {
}
