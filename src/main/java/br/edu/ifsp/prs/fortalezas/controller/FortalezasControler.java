package br.edu.ifsp.prs.fortalezas.controller;

import br.edu.ifsp.prs.fortalezas.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("loja")
public class FortalezasControler {
    @Autowired
    private QuizRepository repository;
    @GetMapping("perguntas")
    public ResponseEntity listar(){
        return ResponseEntity.ok(repository.findAll());
    }
}
