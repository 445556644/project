package br.senai.sp.cfp8.guidecar.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.senai.sp.cfp8.guidecar.model.Loja;
import br.senai.sp.cfp8.guidecar.model.TipoLoja;

public interface LojaRepository extends PagingAndSortingRepository<Loja, Long> {

	public List<TipoLoja> findAllByOrderByNomeAsc();
	
}
