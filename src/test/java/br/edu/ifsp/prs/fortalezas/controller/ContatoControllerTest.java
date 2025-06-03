package br.edu.ifsp.prs.fortalezas.controller;

import br.edu.ifsp.prs.fortalezas.dto.contato.CadastrarDuvidasDTO;
import br.edu.ifsp.prs.fortalezas.repository.ContatoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

@ExtendWith(SpringExtension.class)
class ContatoControllerTest {
    @InjectMocks
    ContatoController controller;
    @Mock
    private ContatoRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cadastrarDuvida() {
        CadastrarDuvidasDTO dto = new CadastrarDuvidasDTO("email@.com", "duvidas", "usuario", 1);
        ResponseEntity<Map<String, String>> response = controller.cadastrarDuvida(dto);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void cadastrarDuvidaErro() {
        ResponseEntity<Map<String, String>> response = controller.cadastrarDuvida(null);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}