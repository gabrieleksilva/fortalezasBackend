package br.edu.ifsp.prs.fortalezas.controller;

import br.edu.ifsp.prs.fortalezas.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("loja")
public class FortalezasController {
    @Autowired
    private QuizRepository quizRepository;


    @GetMapping("perguntas")
    public ResponseEntity listar() {
        return ResponseEntity.ok(quizRepository.findAll());
    }


}
