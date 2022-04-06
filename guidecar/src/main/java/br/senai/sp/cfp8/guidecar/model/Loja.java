package br.senai.sp.cfp8.guidecar.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Loja {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	@Column(columnDefinition = "TEXT")
	private String descricao;
	private String cep;
	private String endereco;
	private String numero;
	private String complemento;
	private String estado;
	private String cidade;
	private String foto;
	private String redesSociais;
	private boolean estacionamento;
	private boolean delivery;
	private String formasPagamento;
	private String telefone;
	@ManyToOne
	private TipoLoja tipoLoja;
	
}
