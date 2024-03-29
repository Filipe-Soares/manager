package com.manager.api.resources;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.manager.api.dto.AtuaizaStatusDto;
import com.manager.api.dto.LancamentoDto;
import com.manager.exceptions.RegraDeNegocioException;
import com.manager.model.entity.Lancamento;
import com.manager.model.entity.Usuario;
import com.manager.model.enums.StatusLancamento;
import com.manager.model.enums.TipoLancamento;
import com.manager.services.LancamentoService;
import com.manager.services.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoResource {
	
	private final LancamentoService service;
	
	private final UsuarioService usuarioService;
	
	@PostMapping
	public ResponseEntity<?> salvar(@RequestBody LancamentoDto dto) {
		try {
			Lancamento entidade = converterToEntity(dto);
			entidade = service.salvar(entidade);
			LancamentoDto lancamentoDto = converterToDto(entidade);
			return new ResponseEntity<LancamentoDto>(lancamentoDto, HttpStatus.CREATED);
		} catch (RegraDeNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDto dto){
		return service.obterPorId(id).map( entity -> {
			try {
				Lancamento lancamento = converterToEntity(dto);
				lancamento.setId(entity.getId());
				service.atualizar(lancamento);
				return ResponseEntity.ok(converterToDto(lancamento));
			} catch(RegraDeNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> 
			new ResponseEntity<String>("Lancamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}
	
	@PutMapping("/{id}/atualiza-status")
	public ResponseEntity<?> atualizarStatus(@PathVariable("id") Long id, @RequestBody AtuaizaStatusDto dto){
		return service.obterPorId(id).map(entity -> {
		StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatus());
		if(statusSelecionado == null) {
			return ResponseEntity.badRequest().body("Não foi posssível atualizar o status do lancamento, corrija o status");
			}
		try {
			entity.setStatus(statusSelecionado);
			service.atualizar(entity);
			return ResponseEntity.ok(converterToDto(entity));
		}catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		}).orElseGet(() -> 
		new ResponseEntity<String>("Lancamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
		
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletar(@PathVariable("id") Long id){
		return service.obterPorId(id).map(entity -> {
			service.deletar(entity);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() ->
			new ResponseEntity<String>("Lancamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}
	
	@GetMapping
	public ResponseEntity<?> buscar(
//			@RequestParam Map<String, String> params,
			@RequestParam(value="descricao", required=false) String descricao,
			@RequestParam(value="mes", required=false) Integer mes,
			@RequestParam(value="ano", required=false) Integer ano,
			@RequestParam(value="usuario", required=false) Long idUsuario) {
		Lancamento lancamentoFiltro = Lancamento.builder()
				.descricao(descricao)
				.mes(mes)
				.ano(ano).build();
		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
		if(!usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Não foi posssível realizar consulta, usuário não encontrado");
		} else {
			lancamentoFiltro.setUsuario(usuario.get());
		}
		List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
		return ResponseEntity.ok(lancamentos);	
	}
	
	public Lancamento converterToEntity(LancamentoDto dto) {
		return Lancamento.builder()
				.id(dto.getId())
				.descricao(dto.getDescricao())
				.mes(dto.getMes())
				.ano(dto.getAno())
				.valor(dto.getValor())
				.usuario(usuarioService.obterPorId(dto.getUsuario())
						.orElseThrow(()-> new RegraDeNegocioException("Usuário não encontrado")))
				.status(dto.getStatus() == null ? null : StatusLancamento.valueOf(dto.getStatus()))
				.tipo(TipoLancamento.valueOf(dto.getTipo()))
				.uuid(dto.getUuid())
				.build();
	}
	
	public LancamentoDto converterToDto(Lancamento dto) {
		return LancamentoDto.builder()
				.id(dto.getId())
				.descricao(dto.getDescricao())
				.mes(dto.getMes())
				.ano(dto.getAno())
				.valor(dto.getValor())
				.usuario(dto.getUsuario().getId())
				.status(dto.getStatus().name())
				.tipo(dto.getTipo().name())
				.uuid(dto.getUuid())
				.build();
	}
	
}
