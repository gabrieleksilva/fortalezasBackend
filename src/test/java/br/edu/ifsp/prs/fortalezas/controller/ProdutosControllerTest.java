package br.edu.ifsp.prs.fortalezas.controller;

import br.edu.ifsp.prs.fortalezas.dto.produtos.CadastrarProdutosDTO;
import br.edu.ifsp.prs.fortalezas.dto.produtos.EditarProdutosDTO;
import br.edu.ifsp.prs.fortalezas.enums.TipoProduto;
import br.edu.ifsp.prs.fortalezas.model.Produtos;
import br.edu.ifsp.prs.fortalezas.repository.ProdutosRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ProdutosControllerTest {

    @InjectMocks
    ProdutosController controller;
    @Mock
    private ProdutosRepository repository;
    @Mock
    private Page<Produtos> page;
    @Mock
    private MultipartFile imagem;
    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarProdutos() {
        ResponseEntity<Map<String, String>> response = controller.listarProdutos();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void listarProduto() {
        ResponseEntity<Map<String, String>> response = controller.listarProduto(1L);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void excluir() {
        ResponseEntity<Map<String, String>> response = controller.excluir(1L);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void listarProdutoPorTipo() {
        Pageable pageable = PageRequest.of(0, 12);
        String tipo = "PISOS";

        TipoProduto tipoEnum = TipoProduto.valueOf(tipo.toUpperCase());
        when(repository.findByTipo(tipoEnum, pageable)).thenReturn(page);
        ResponseEntity<?> response = controller.listarProdutoPorTipo(tipo, 2, 3);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void listarProdutoPorTipoException() {
        String tipo = "outro";
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, controller.listarProdutoPorTipo(tipo, 2, 3).getStatusCode());
    }

    @Test
    void cadastrarProdutoComSucesso() throws Exception {
        byte[] imagemBytes = "fakeimage".getBytes();
        when(imagem.getBytes()).thenReturn(imagemBytes);
        when(imagem.getOriginalFilename()).thenReturn("imagem.png");

        CadastrarProdutosDTO produtoDTO = mock(CadastrarProdutosDTO.class);
        when(produtoDTO.tipoProduto()).thenReturn(TipoProduto.PISOS);

        // Simular retorno da API do Imgur
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("link", "http://imgur.com/fake-link.png");
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("data", dataMap);
        ResponseEntity<Map<String, String>> response = controller.cadastrarProduto(produtoDTO, imagem);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void cadastrarProdutoComErro() throws Exception {
        // Mocks e dados simulados
        byte[] imagemBytes = "fakeimage".getBytes();
        when(imagem.getBytes()).thenReturn(imagemBytes);
        when(imagem.getOriginalFilename()).thenReturn("imagem.png");

        CadastrarProdutosDTO produtoDTO = mock(CadastrarProdutosDTO.class);
        when(produtoDTO.tipoProduto()).thenReturn(TipoProduto.PISOS);

        // Simular retorno da API do Imgur
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("link", "http://imgur.com/fake-link.png");
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("data", dataMap);
        ResponseEntity<Map<String, String>> response = controller.cadastrarProduto(produtoDTO, imagem);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void search() {
        Pageable pageable = PageRequest.of(0, 12);

        when(repository.findByNomeContainingIgnoreCaseOrMarcaContainingIgnoreCase("produto", "produto", pageable)).thenReturn(page);
        ResponseEntity<?> response = controller.search(1, 2, "produto");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void editarProduto() {
        EditarProdutosDTO dto = new EditarProdutosDTO("piso", "marca", "rustica", "clara", "banheiro", "limpeza", "2 pacotes", 2, TipoProduto.PISOS);
        when(repository.getReferenceById(2L)).thenReturn(new Produtos());
        ResponseEntity<?> response = controller.editarProduto(2L, dto);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void editarProdutoErroAoAtualizar() {
        EditarProdutosDTO dto = new EditarProdutosDTO("piso", "marca", "rustica", "clara", "banheiro", "limpeza", "2 pacotes", 2, TipoProduto.PISOS);
        ResponseEntity<?> response = controller.editarProduto(2L, dto);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void editarProdutoEntityNotFoundException() {
        when(repository.getReferenceById(2L)).thenThrow(EntityNotFoundException.class);
        ResponseEntity<?> response = controller.editarProduto(2L, null);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void buscarPorFiltros() {
        ResponseEntity<List<Produtos>> response = controller.buscarPorFiltros("classico", "clara", "banheiro", "resistencia");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}