package com.manager.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import com.manager.model.enums.StatusLancamento;
import com.manager.model.enums.TipoLancamento;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name="lancamento", schema="financas")
public class Lancamento {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="descricao")
	private String descricao;
	
	@Column(name="mes")
	private Integer mes;
	
	@Column(name="ano")
	private Integer ano;
	
	@ManyToOne
	@JoinColumn(name="id_usuario")
	private Usuario usuario;
	
	@Column(name="valor")
	private BigDecimal valor;
	
	@CreatedDate
	@Column(name="data_cadastro")
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	private LocalDate dataCadastro;
	
	@Column(name="tipo")
	@Enumerated(value = EnumType.STRING)
	private TipoLancamento tipo;

	@Column(name="status")
	@Enumerated(value = EnumType.STRING)
	private StatusLancamento status;

	@UuidGenerator
	@Column(name = "uuid", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
	private UUID uuid;

	public void setId(Long id) {
		this.id = id;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public void setTipo(TipoLancamento tipo) {
		this.tipo = tipo;
	}

	public void setStatus(StatusLancamento status) {
		this.status = status;
	}
	
}
