package br.edu.ifsp.prs.fortalezas.controller;

import br.edu.ifsp.prs.fortalezas.dto.usuarios.CadastrarUsuariosDTO;
import br.edu.ifsp.prs.fortalezas.dto.usuarios.ConsultarUsuariosDTO;
import br.edu.ifsp.prs.fortalezas.dto.usuarios.DadosTokenJWT;
import br.edu.ifsp.prs.fortalezas.model.Usuarios;
import br.edu.ifsp.prs.fortalezas.repository.UsuariosRepository;
import br.edu.ifsp.prs.fortalezas.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("loja")
public class UsuariosController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuariosRepository usuarioRepository;

    @Autowired
    private JwtService tokenService;

    @Autowired
    private AuthenticationManager manager;


    @PostMapping("/usuarios")
    public ResponseEntity<Map<String, String>> cadastrarUsuario(@RequestBody CadastrarUsuariosDTO dados) {

        try {
            String senhaCriptografada = passwordEncoder.encode(dados.senha());

            Usuarios usuario = new Usuarios(dados);
            usuario.setSenha(senhaCriptografada);

            usuarioRepository.save(usuario);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao cadastrar duvida."));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> efetuarLogin(@RequestBody ConsultarUsuariosDTO dados) {
        try {

            var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
            var authentication = manager.authenticate(authenticationToken);

            // Gera o token JWT
            var tokenJWT = tokenService.gerarToken((Usuarios) authentication.getPrincipal());

            // Retorna o token
            return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
        } catch (Exception e) {
            // Autenticação falhou
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("erro", "Usuário ou senha inválidos."));
        }
    }

}