package br.edu.ifsp.prs.fortalezas.controller;

import br.edu.ifsp.prs.fortalezas.dto.contato.CadastrarDuvidasDTO;
import br.edu.ifsp.prs.fortalezas.model.Contato;
import br.edu.ifsp.prs.fortalezas.repository.ContatoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("loja")
public class ContatoController {
    @Autowired
    private ContatoRepository contatoRepository;


    @PostMapping("contato")
    @Transactional
    public ResponseEntity<Map<String, String>>  cadastrarDuvida(@RequestBody CadastrarDuvidasDTO cadastrarDuvidasDTO) {
        try {

            Contato contato = new Contato(cadastrarDuvidasDTO);
            contatoRepository.save(contato);

            return ResponseEntity.ok(Map.of("mensagem", "Duvida cadastrada com sucesso!"));
        } catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao cadastrar duvida."));
        }
    };

}
