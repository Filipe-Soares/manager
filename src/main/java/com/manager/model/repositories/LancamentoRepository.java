package com.manager.model.repositories;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.manager.model.entity.Lancamento;
import com.manager.model.enums.TipoLancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

	@Query(value="select sum(lan.valor) from Lancamento lan join lan.usuario usu "
			+ " where usu.id = :idUsuario and lan.tipo = :tipo group by usu ")
	BigDecimal obterSaldoPorTipoLancamentoEUsuario(Long idUsuario, TipoLancamento tipo);
}
