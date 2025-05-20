package br.edu.ifsp.prs.fortalezas.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Item {
    private int produtoId;
    private String nome;
    private String marca;
    private String descQuantidade;
    private BigDecimal vlrUnit;

}