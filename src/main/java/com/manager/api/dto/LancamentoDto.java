package com.manager.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LancamentoDto {
	
	private Long id;
	private String descricao;
	private Integer mes;
	private Integer ano;
	private BigDecimal valor;
	private Long usuario;
	private LocalDate dataCadastro;
	private String tipo;
	private String status;
	private UUID uuid;
	
}
