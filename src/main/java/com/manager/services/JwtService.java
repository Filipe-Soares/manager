package com.manager.services;


import com.manager.model.entity.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

public interface JwtService {
	
	String gerarToken(Usuario usuario);
	
	Claims obterClaims(String token) throws ExpiredJwtException;
	
	boolean isTokenExpired(String token);
	
	String obterLoginUsuario(String token);	
	
	boolean isTokenValid(String token, String userName);
}
