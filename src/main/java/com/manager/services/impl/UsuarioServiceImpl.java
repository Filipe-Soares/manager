package com.manager.services.impl;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manager.exceptions.ErroAutenticacao;
import com.manager.exceptions.RegraDeNegocioException;
import com.manager.model.entity.Usuario;
import com.manager.model.repositories.UsuarioRepository;
import com.manager.services.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{
	
	private UsuarioRepository usuarioRepository;
	private PasswordEncoder encoder;
	
	public UsuarioServiceImpl(UsuarioRepository repository, PasswordEncoder encoder) {
		super();
		this.usuarioRepository = repository;
		this.encoder = encoder;
		
	}
	
	@Override
	public Usuario autenticar(String email, String senha) {
		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(()-> new ErroAutenticacao("Usuário não encontrado"));
		if(!usuario.getSenha().equals(senha))
			throw new ErroAutenticacao("Senha inválida");
		
		return usuario;
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		criptografarSenha(usuario);
		return usuarioRepository.save(usuario);
	}

	private void criptografarSenha(Usuario usuario) {
		String senha = usuario.getSenha();
		String senhaCripto = encoder.encode(senha);
		usuario.setSenha(senhaCripto);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = usuarioRepository.existsByEmail(email);
		if(existe) 
			throw new RegraDeNegocioException("Já existe um usuário com este email");
	}

	@Override
	public Optional<Usuario> obterPorId(Long id) {
		
		return usuarioRepository.findById(id);
	}

}
