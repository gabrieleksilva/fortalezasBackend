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
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Map;

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
        String clientId = System.getenv("IMGUR_CLIENT_ID");

        try {
            // Converte a imagem para Base64
            String base64 = Base64.getEncoder().encodeToString(imagem.getBytes());

            // Cria o corpo da requisição para o Imgur
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", "Client-ID " + clientId);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("image", base64);
            body.add("type", "base64");
            body.add("name", produtoDTO.tipoProduto().toString().toLowerCase() + "_" + imagem.getOriginalFilename());
            body.add("title", produtoDTO.tipoProduto().toString());
            body.add("description", "Imagem do tipo: " + produtoDTO.tipoProduto().toString());

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.postForEntity("https://api.imgur.com/3/image", request, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
                String link = (String) data.get("link");

                Produtos produto = new Produtos(produtoDTO);
                produto.setImagem(link); // Salva a URL da imagem
                produtosRepository.save(produto);

                return ResponseEntity.ok(Map.of("mensagem", "Produto cadastrado com sucesso!"));
            } else {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("erro", "Erro ao fazer upload no Imgur."));
            }

        } catch (Exception e) {
            e.printStackTrace();
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
                                                  @RequestParam int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        try {
            TipoProduto tipoEnum = TipoProduto.valueOf(tipo.toUpperCase());
            Page<Produtos> produtosPage = produtosRepository.findByTipo(tipoEnum, pageable);
            return ResponseEntity.ok(produtosPage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Tipo de produto inválido");
        }
    }

    @GetMapping("produtos/search")
    public ResponseEntity<?> search(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String search
    ) {
        Pageable pageable = PageRequest.of(page, size);
        try {

            Page<Produtos> produtosPage = produtosRepository.findByNomeContainingIgnoreCaseOrMarcaContainingIgnoreCase(search, search, pageable);
            return ResponseEntity.ok(produtosPage);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("busca inválida");
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
