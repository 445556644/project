package br.senai.sp.cfp8.guidecar.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import br.senai.sp.cfp8.guidecar.model.Loja;
import br.senai.sp.cfp8.guidecar.repository.LojaRepository;
import br.senai.sp.cfp8.guidecar.repository.TipoLojaRepository;
import br.senai.sp.cfp8.guidecar.util.FirebaseUtil;

@Controller
public class LojaController {

	@Autowired
	TipoLojaRepository tipoLojaRepository;

	@Autowired
	LojaRepository lojaRepository;

	@Autowired
	FirebaseUtil firebaseUtil;

	@RequestMapping("formLoja")
	public String formLoja(Model model) {

		model.addAttribute("tipos", tipoLojaRepository.findAll());

		return "Loja/FormLoja";

	}

	@RequestMapping("salvarLoja")
	// referenciando o name do input(fotos) a variavel fileFotos
	public String salvarLoja(Loja loja, @RequestParam("fotos") MultipartFile[] filefotos) {
		// variavel para url das fotos
		String fotos = "";

		// percorrer cada arquivo que foi submetido no form

		for (MultipartFile file : filefotos) {

			// verificar se o arquivo esta vazio

			if (file.getOriginalFilename().isEmpty()) {

				// caso tenha um vazio, verifica se esta vazia, vai pra proxima
				continue;
			}

			// faz o upload para a a nuvem e obtem a url gerada

			try {
				fotos += firebaseUtil.uploadFile(file) + ";";
			} catch (IOException e) {
				System.out.println("ERRO AQUI");
				e.printStackTrace();
				throw new RuntimeException(e);
			}

		}

		// atribui a string fotos ao objeto restaurante
		loja.setFoto(fotos);

		lojaRepository.save(loja);

		return "redirect:formLoja";

	}

	// metodo que faz a listagem
	@RequestMapping("listarLojas/{pagina}")
	public String listaTipos(Model model, @PathVariable("pagina") int page) {

		model.addAttribute("loja", lojaRepository.findAll());

		// cria uma pagina que começa na 0, que possuem 6 elementos por paginas e ordena
		// pelo nome
		PageRequest pageble = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "nome"));

		// cria a pagina atual atraves do repository

		Page<Loja> pagina = lojaRepository.findAll(pageble);

		// descobrir o total de pagina
		int totalPg = pagina.getTotalPages();

		// cria uma lista de inteiros para representar as paginas
		List<Integer> pageNumbers = new ArrayList<Integer>();

		for (int i = 0; i < totalPg; i++) {

			pageNumbers.add(i + 1);
		}
		// adiciona as variaveis na model

		// pendurando os admins cadastrados
		model.addAttribute("loja", pagina.getContent());

		// pagina atual
		model.addAttribute("pgAtual", page);

		//
		model.addAttribute("numTotalPg", totalPg);

		// quantidade de paginas com base nos cadastros
		model.addAttribute("numPg", pageNumbers);

		// retorna para o html da lista

		return "Loja/ListaLojas";

	}

	@RequestMapping("alterarLoja")
	public String atualizarLoja(Model model, Long id) {

		model.addAttribute("loja", lojaRepository.findById(id).get());

		return "forward:formLoja";

	}

	@RequestMapping("excluirLoja")
	public String excluirLoja(Long id) {

		lojaRepository.deleteById(id);

		return "redirect:listarLojas/1";
	}
	
	@RequestMapping("excluirFotoLoja")
	public String excluirFoto(Long id, int numFoto, Model model) {
		
		// busca o restauranten no bd
		
		Loja loja = lojaRepository.findById(id).get();
		
		// pegando a string da foto que vai ser excluida
		
		String fotoUrl = loja.verFotos()[numFoto];
		
		// excluir do firebase
		firebaseUtil.deletar(fotoUrl);
		
		// tirando a foto da string foto de loja
		loja.setFoto(loja.getFoto().replace(fotoUrl + "", ""));
		
		// salva no bd
		
		lojaRepository.save(loja);
		
		// adiciona a loja na model
		
		model.addAttribute("loja", loja);
		
		return"forward:formLoja";
	}

	@RequestMapping("verCaixa")
	public String verCaixa() {

		return "Loja/caixa";
	}

	@RequestMapping("visualizar")
	public String mostrarCaixa(Model model, Long id) {

		model.addAttribute("mostrar", lojaRepository.findById(id).get());

		return "forward:verCaixa";
	}

}
