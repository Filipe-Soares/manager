package com.manager.api.resources;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manager.api.dto.TokenDto;
import com.manager.api.dto.UsuarioDto;
import com.manager.exceptions.ErroAutenticacao;
import com.manager.exceptions.RegraDeNegocioException;
import com.manager.model.entity.Usuario;
import com.manager.services.JwtService;
import com.manager.services.LancamentoService;
import com.manager.services.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioResource {

	private final UsuarioService service;
	
	private final LancamentoService lancamentoService;
	
	private final JwtService jwtService;
	
	@PostMapping
	public ResponseEntity<?> salvar(@RequestBody UsuarioDto dto) {
		Usuario usuario = Usuario.builder()
				.nome(dto.getNome())
				.email(dto.getEmail())
				.senha(dto.getSenha()).build();
		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.CREATED);
		} catch (RegraDeNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PostMapping("/autenticar")
	public ResponseEntity<?> autenticar(@RequestBody UsuarioDto dto) {
		try {
			Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
			TokenDto tokenDto = new TokenDto(usuarioAutenticado.getNome(), jwtService.gerarToken(usuarioAutenticado));
			return ResponseEntity.ok(tokenDto);
		}catch (ErroAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping("/{id}/saldo")
	public ResponseEntity<?> obterSaldo(@PathVariable("id") Long id) {
		service.obterPorId(id).orElseThrow(()-> new RegraDeNegocioException("Usuário não localizado"));
		BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);
		return ResponseEntity.ok(saldo);
	}
	
}
