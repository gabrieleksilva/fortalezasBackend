package br.edu.ifsp.prs.fortalezas.service;

import br.edu.ifsp.prs.fortalezas.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarrinhoService {

    @Autowired
    private DataSource dataSource;

    public void criarTabelaTemporaria() {
        String sql = """
            CREATE TEMP TABLE IF NOT EXISTS carrinho_temp (
                id SERIAL PRIMARY KEY,
                produto_id INT NOT NULL,
                nome TEXT NOT NULL,
                marca TEXT NOT NULL,
                descQuantidade TEXT NOT NULL,
                vlrUnit NUMERIC(10,2) NOT NULL
            )
        """;

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela temporária", e);
        }
    }

    public void adicionarItem(Item item) {
        String sql = "INSERT INTO carrinho_temp (produto_id, nome, marca, descQuantidade, vlrUnit) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getProdutoId());
            stmt.setString(2, item.getNome());
            stmt.setString(3, item.getMarca());
            stmt.setString(4, item.getDescQuantidade());
            stmt.setBigDecimal(5, item.getVlrUnit());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar item", e);
        }
    }

    public List<Item> listarItens() {
        List<Item> itens = new ArrayList<>();
        String sql = "SELECT * FROM carrinho_temp";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Item item = new Item();
                item.setProdutoId(rs.getInt("produto_id"));
                item.setNome(rs.getString("nome"));
                item.setMarca(rs.getString("marca"));
                item.setDescQuantidade(rs.getString("descQuantidade"));
                item.setVlrUnit(rs.getBigDecimal("vlrUnit"));
                itens.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar itens", e);
        }

        return itens;
    }

}
