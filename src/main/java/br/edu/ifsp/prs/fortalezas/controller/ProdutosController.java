package br.edu.ifsp.prs.fortalezas.controller;

import br.edu.ifsp.prs.fortalezas.dto.produtos.CadastrarProdutosDTO;
import br.edu.ifsp.prs.fortalezas.model.Produtos;
import br.edu.ifsp.prs.fortalezas.repository.ProdutosRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("loja")
public class ProdutosController {
    @Autowired
    private ProdutosRepository produtosRepository;

    @PostMapping("produtos")
    @Transactional
    public ResponseEntity<Map<String, String>> cadastrarProduto(
            @RequestPart("produto") CadastrarProdutosDTO produtoDTO,
            @RequestPart("imagem") MultipartFile imagem) {

        try {
            // Salva imagem
            String nomeImagem = UUID.randomUUID() + "_" + imagem.getOriginalFilename();
            String caminhoImagem = "src/main/resources/static/image/" + produtoDTO.tipoProduto() + "/" + nomeImagem;
            Files.write(Paths.get(caminhoImagem), imagem.getBytes());

            // Cria produto
            Produtos produto = new Produtos(produtoDTO);

            produto.setImagem("/image/" + nomeImagem);

            produtosRepository.save(produto);

            return ResponseEntity.ok(Map.of("mensagem", "Produto cadastrado com sucesso!"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao cadastrar produto."));
        }
    }

    @GetMapping("produtos")
    public ResponseEntity listarProdutos() {
        return ResponseEntity.ok(produtosRepository.findAll());
    }

    @GetMapping("produtos/{id}")
    public ResponseEntity listarProduto(@PathVariable Long id) {
        return ResponseEntity.ok(produtosRepository.findById(id));
    }

    @DeleteMapping("/produtos/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        Produtos produtos = produtosRepository.getReferenceById(id);
        produtosRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
