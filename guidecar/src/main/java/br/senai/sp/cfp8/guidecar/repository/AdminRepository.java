package br.senai.sp.cfp8.guidecar.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.senai.sp.cfp8.guidecar.model.Administrador;

// faz com que pagine e ordene a "lista"
public interface AdminRepository extends PagingAndSortingRepository<Administrador, Long> {

	
	
}
