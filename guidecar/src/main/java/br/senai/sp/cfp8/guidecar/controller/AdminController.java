package br.senai.sp.cfp8.guidecar.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senai.sp.cfp8.guidecar.model.Administrador;
import br.senai.sp.cfp8.guidecar.model.RepositoryAdmin;

@Controller
public class AdminController {

	@Autowired
	private RepositoryAdmin repAdm;

	@RequestMapping("formAdmin")
	public String formAdmin() {

		
		return "Admin/FormAdmin";
	}

	// valid faz a validação no back end
	// BindingResult faz com que pegue os erros
	// RedirectAttributes faz com que mostre os erro no form
	@RequestMapping("salvarAdmin")
	public String salvarAdmin(@Valid Administrador adm, BindingResult result, RedirectAttributes redAtt) {
		// verifica se houve erro na validação do projeto

		// verfica se há erros na validaçao
		if (result.hasErrors()) {

			// envia msg de erro via requisição
			redAtt.addFlashAttribute("msgErro", "Verifique Os Campos ...");
			redAtt.addFlashAttribute("admin", adm);
			return "redirect:listarAdmin";
		}

		try {
			repAdm.save(adm);
			redAtt.addFlashAttribute("msgSucesso", "Cadastro Realizado Com Sucesso," + " Adm De Id: " + adm.getId());
			return "redirect:listarAdmin/1";
		} catch (Exception e) {

			// caso ocorra um erro informa ao usuario de forma melhor
			redAtt.addFlashAttribute("EmailRepetido", "Houve Um Erro Ao Cadastrar O administrador" + e.getMessage());
		}
		return "redirect:formAdmin";

	}

	// request mapping para listar informando a página informada
	@RequestMapping("listarAdmin/{pagina}")
	// @PathVariable associando int page a ${page}
	public String listarAdmin(Model model, @PathVariable("pagina") int page) {

		// cria uma pagina que começa na 0, que possuem 6 elementos por paginas e ordena
		// pelo nome
		PageRequest pageble = PageRequest.of(page - 1, 6, Sort.by(Sort.Direction.ASC, "nome"));

		// cria a pagina atual atraves do repository

		Page<Administrador> pagina = repAdm.findAll(pageble);

		// descobrir o total de pagina
		int totalPg = pagina.getTotalPages();

		// cria uma lista de inteiros para representar as paginas
		List<Integer> pageNumbers = new ArrayList<Integer>();

		for (int i = 0; i < totalPg; i++) {

			pageNumbers.add(i + 1);
		}
		// adiciona as variaveis na model

		// pendurando os admins cadastrados
		model.addAttribute("admin", pagina.getContent());
		// pagina atual
		model.addAttribute("pgAtual", page);
		//
		model.addAttribute("numTotalPg", totalPg);
		// quantidade de paginas com base nos cadastros
		model.addAttribute("numPg", pageNumbers);
		// retorna para o html da lista
		return "admin/ListaAdmin";
	}

	@RequestMapping("alterarAdmin")
	public String alterarAdmin(Model model, Long id) {

		model.addAttribute("admin", repAdm.findById(id).get());

		return "forward:formAdmin";

	}

	@RequestMapping("excluirAdmin")
	public String excluirAdmin(Long id) {

		repAdm.deleteById(id);

		return "redirect:listarAdmin/1";

	}

}
