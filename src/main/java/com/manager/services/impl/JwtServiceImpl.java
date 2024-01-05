package com.manager.services.impl;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.manager.model.entity.Usuario;
import com.manager.services.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService{
	
	@Value("${jwt.secret-key}")
	private String secretKey;
	@Value("${jwt.expiration}")
	private long jwtExpiration;
	@Value("${jwt.refresh-token.expiration}")
	private long refreshExpiration;
	

	@Override
	public Claims obterClaims(String token) throws ExpiredJwtException {
		return extractAllClaims(token);
	}

	@Override
	public String gerarToken(Usuario usuario) {
		return generateToken(new HashMap<>(), usuario);
	}
	
	@Override
	public String obterLoginUsuario(String token) {
		return extractUsername(token);
	}

	
	@Override
	public boolean isTokenValid(String token, String userName) {
		final String username = extractUsername(token);
		return (username.equals(userName)) && !isTokenExpired(token);
	}
	
	@Override
	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	public String generateToken(Map<String, Object> extraClaims, Usuario usuario) {
	    return buildToken(extraClaims, usuario, jwtExpiration);
	}
	
	public String generateRefreshToken(Usuario usuario) {
	    return buildToken(new HashMap<>(), usuario, refreshExpiration);
	}
	
	private String buildToken(Map<String, Object> extraClaims, 
			  Usuario usuario, long expiration) {
	  return Jwts
	            .builder().setHeaderParam("typ", "JWT")
	            .setClaims(extraClaims)
	            .setSubject(usuario.getEmail())
	            .setIssuedAt(new Date(System.currentTimeMillis()))
	            .setExpiration(new Date(System.currentTimeMillis() + expiration))
	            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
	            .compact();
	}
	
	private Key getSignInKey() {
	  byte[] keyBytes = Decoders.BASE64.decode(secretKey);
	  return Keys.hmacShaKeyFor(keyBytes);
	}

	private Date extractExpiration(String token) {
	  return extractClaim(token, Claims::getExpiration);
	}
	
	public String extractUsername(String token) {
	  return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	  final Claims claims = extractAllClaims(token);
	  return claimsResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
	  return Jwts
	      .parserBuilder()
	      .setSigningKey(getSignInKey())
	      .build()
	      .parseClaimsJws(token)
	      .getBody();
	}
	
}
