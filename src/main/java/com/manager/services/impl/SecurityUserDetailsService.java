package com.manager.services.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.manager.model.entity.Usuario;
import com.manager.model.repositories.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService{

	private final UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuarioEncontrado = usuarioRepository.findByEmail(email)
				.orElseThrow(()-> new UsernameNotFoundException("Email não encontrado"));
		return User.builder()
						.username(usuarioEncontrado.getEmail())
						.password(usuarioEncontrado.getSenha())
						.roles("USER")
						.build();
	}

}
