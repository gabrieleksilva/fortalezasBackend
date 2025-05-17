package br.edu.ifsp.prs.fortalezas.dto.contato;

import br.edu.ifsp.prs.fortalezas.enums.TipoProduto;

public record CadastrarDuvidasDTO(
                                   String email,
                                   String mensagem,
                                   String nome,
                                   Integer idCliente

) {
}
