package br.senai.sp.cfp8.guidecar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import br.senai.sp.cfp8.guidecar.model.TipoLoja;

public interface TipoLojaRepository extends PagingAndSortingRepository<TipoLoja, Long> {

	@Query("SELECT b FROM TipoLoja b WHERE b.palavrasChave LIKE %:busca%")
	public List<TipoLoja> buscarPalavraChave(@Param("busca") String Busca);
}
