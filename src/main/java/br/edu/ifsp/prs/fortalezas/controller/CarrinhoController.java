package br.edu.ifsp.prs.fortalezas.controller;

import br.edu.ifsp.prs.fortalezas.model.Item;
import br.edu.ifsp.prs.fortalezas.service.CarrinhoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/carrinho")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    public CarrinhoController(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    @PostMapping("/adicionar")
    public ResponseEntity<String> adicionarItem(@RequestBody Item item) {
        carrinhoService.criarTabelaTemporaria();
        carrinhoService.adicionarItem(item);
        return ResponseEntity.ok("Item adicionado");
    }

    @GetMapping
    public List<Item> listarItens() {
        carrinhoService.criarTabelaTemporaria();
        return carrinhoService.listarItens();
    }

}
