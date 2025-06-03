package br.edu.ifsp.prs.fortalezas.controller;

import br.edu.ifsp.prs.fortalezas.model.Item;
import br.edu.ifsp.prs.fortalezas.service.CarrinhoService;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class CarrinhoControllerTest {

    @InjectMocks
    private CarrinhoController controller;

    @Mock
    private CarrinhoService service;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void adicionarItem() {
        Item item = new Item();
        ResponseEntity<String> response = controller.adicionarItem(item);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void listarItens() {
        List<Item> response = controller.listarItens();
        Assertions.assertNotNull(response.size());
    }
}