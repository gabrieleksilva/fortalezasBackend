package br.edu.ifsp.prs.fortalezas.service;

import br.edu.ifsp.prs.fortalezas.model.Usuarios;
import br.edu.ifsp.prs.fortalezas.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public class UsuarioService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuariosRepository usuarioRepository;

    public void cadastrarUsuario(Usuarios usuario) {
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        usuarioRepository.save(usuario);
    }
    public boolean autenticar(String senhaDigitada, String senhaNoBanco) {
        return passwordEncoder.matches(senhaDigitada, senhaNoBanco);
    }
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Usuarios usuario = usuarioRepository.findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
//
//        return new SecurityProperties.User(usuario.getEmail(), usuario.getSenha(), /* authorities */ List.of());
//    }

}
