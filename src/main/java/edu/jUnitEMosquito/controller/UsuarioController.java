package edu.jUnitEMosquito.controller;

import edu.jUnitEMosquito.dto.usuario.LoginRequestDTO;
import edu.jUnitEMosquito.dto.usuario.RegisterRequestDTO;
import edu.jUnitEMosquito.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    private UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody LoginRequestDTO request){
        String token = usuarioService.login(request);
        return ResponseEntity.ok().body(Map.of("Token", token));
    }

    @PostMapping("register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO registerRequestDTO){
        usuarioService.register(registerRequestDTO);
        return ResponseEntity.ok().body(Map.of("Sucesso", "Usuário criado com sucesso!"));
    }

}
