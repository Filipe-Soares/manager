package com.manager.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manager.exceptions.RegraDeNegocioException;
import com.manager.model.entity.Lancamento;
import com.manager.model.enums.StatusLancamento;
import com.manager.model.enums.TipoLancamento;
import com.manager.model.repositories.LancamentoRepository;
import com.manager.services.LancamentoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LancamentoServiceImpl implements LancamentoService {
	
	private final LancamentoRepository repository;

	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		validar(lancamento);
		repository.delete(lancamento);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		Example<Lancamento> example = Example.of(lancamentoFiltro, 
				ExampleMatcher.matching()
					.withIgnoreCase()
					.withStringMatcher(StringMatcher.CONTAINING));
		
		return repository.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);
	}

	@Override
	public void validar(Lancamento lancamento) {

		if(lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
			throw new RegraDeNegocioException("Informe uma descrição válida.");
		}
		if(lancamento.getMes() == null || lancamento.getMes()<1 || lancamento.getMes()>12) {
			throw new RegraDeNegocioException("Informe um mês válido.");
		}
		if(lancamento.getAno() == null || lancamento.getAno().toString().length() != 4
				|| lancamento.getAno() < 1970 
				|| lancamento.getAno() > LocalDate.now().plusYears(5).getYear()) {
			throw new RegraDeNegocioException("Informe um ano válido.");
		}
		if(lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {
			throw new RegraDeNegocioException("Informe um usuário.");
		}
		if(lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO)<1) {
			throw new RegraDeNegocioException("Informe um valor válido.");
		}
		if(lancamento.getTipo() == null) {
			throw new RegraDeNegocioException("Informe um tipo de lançamento.");
		}
		
	}

	@Override
	public Optional<Lancamento> obterPorId(Long id) {
		return repository.findById(id);
	}

	@Override
	public BigDecimal obterSaldoPorUsuario(Long id) {
		BigDecimal receitas = repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.RECEITA);
		BigDecimal despesas = repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.DESPESA);
		
		if(receitas == null)
			receitas = BigDecimal.ZERO;
		if(despesas == null)
			despesas = BigDecimal.ZERO;
		
		return receitas.subtract(despesas);
	}
	
}
