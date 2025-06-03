package br.edu.ifsp.prs.fortalezas.controller;

import br.edu.ifsp.prs.fortalezas.dto.usuarios.CadastrarUsuariosDTO;
import br.edu.ifsp.prs.fortalezas.dto.usuarios.ConsultarUsuariosDTO;
import br.edu.ifsp.prs.fortalezas.dto.usuarios.DadosTokenJWT;
import br.edu.ifsp.prs.fortalezas.model.Usuarios;
import br.edu.ifsp.prs.fortalezas.repository.UsuariosRepository;
import br.edu.ifsp.prs.fortalezas.service.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

@ExtendWith(SpringExtension.class)
class UsuariosControllerTest {

    @InjectMocks
    UsuariosController controller;
    @Mock
    UsuariosRepository repository;
    @Mock
    private AuthenticationManager manager;

    @Mock
    private JwtService tokenService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void efetuarLogin() {
        ConsultarUsuariosDTO dto = new ConsultarUsuariosDTO("email@hotmail.com", "senha");

        Usuarios usuarioMock = new Usuarios();
        Authentication authMock = new UsernamePasswordAuthenticationToken(usuarioMock, null);

        Mockito.when(manager.authenticate(Mockito.any())).thenReturn(authMock);
        Mockito.when(tokenService.gerarToken(Mockito.any())).thenReturn("fake-jwt-token");

        ResponseEntity<?> response = controller.efetuarLogin(dto);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody() instanceof DadosTokenJWT);
        Assertions.assertEquals("fake-jwt-token", ((DadosTokenJWT) response.getBody()).token());
    }

    @Test
    void efetuarLoginNaoAutorizado() {
        ConsultarUsuariosDTO dto = new ConsultarUsuariosDTO("email@hotmail.com", "senha");
        ResponseEntity<?> response = controller.efetuarLogin(dto);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void cadastrarUsuario() {
        CadastrarUsuariosDTO dto = new CadastrarUsuariosDTO(
                "teste@email.com", "teste", "teste",
                "0000");
        Mockito.when(passwordEncoder.encode("senha123")).thenReturn("senhaCriptografada");
        ResponseEntity<Map<String, String>> response = controller.cadastrarUsuario(dto);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void cadastrarUsuarioComErro() {
        ResponseEntity<Map<String, String>> response = controller.cadastrarUsuario(null);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}