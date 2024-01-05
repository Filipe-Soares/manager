package com.manager.api.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UsuarioDto {
	
	private Long id;
	private String nome;
	private String email;
	private String senha;
	private UUID uuid;
	
}
