package br.edu.ifsp.prs.fortalezas.controller;

import br.edu.ifsp.prs.fortalezas.dto.produtos.CadastrarProdutosDTO;
import br.edu.ifsp.prs.fortalezas.dto.produtos.EditarProdutosDTO;
import br.edu.ifsp.prs.fortalezas.enums.TipoProduto;
import br.edu.ifsp.prs.fortalezas.model.Produtos;
import br.edu.ifsp.prs.fortalezas.repository.ProdutosRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

            produto.setImagem("/image/" + produtoDTO.tipoProduto().toString().toLowerCase() + "/" + nomeImagem);

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

    @GetMapping("produtos/tipo")
    public ResponseEntity<?> listarProdutoPorTipo(@RequestParam String tipo,
                                                               @RequestParam int page,
                                                               @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            TipoProduto tipoEnum = TipoProduto.valueOf(tipo.toUpperCase());
            Page<Produtos> produtosPage = produtosRepository.findByTipo(tipoEnum, pageable);
            return ResponseEntity.ok(produtosPage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Tipo de produto inválido");
        }

    }
    @PutMapping("/produtos/{id}")
    @Transactional
    public ResponseEntity<?> editarProduto(@PathVariable Long id, @RequestBody EditarProdutosDTO produtoDTO) {
        try {
            Produtos produto = produtosRepository.getReferenceById(id);
            produto.atualizarInformacoes(produtoDTO);
            return ResponseEntity.ok().body(Map.of("mensagem", "Produto atualizado com sucesso!"));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Produto não encontrado."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao atualizar o produto."));
        }
    }

    @GetMapping("quiz")
    public ResponseEntity<List<Produtos>> buscarPorFiltros(
            @RequestParam(required = false) String estilo,
            @RequestParam(required = false) String cor,
            @RequestParam(required = false) String local,
            @RequestParam(required = false) String prioridade
    ) {
        List<Produtos> resultados = produtosRepository.buscarPorFiltros(estilo, cor, local, prioridade);
        return ResponseEntity.ok(resultados);
    }

}
