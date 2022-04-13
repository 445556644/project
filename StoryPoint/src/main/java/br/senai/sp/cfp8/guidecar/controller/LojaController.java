package br.senai.sp.cfp8.guidecar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.senai.sp.cfp8.guidecar.model.Loja;
import br.senai.sp.cfp8.guidecar.repository.LojaRepository;
import br.senai.sp.cfp8.guidecar.repository.TipoLojaRepository;

@Controller
public class LojaController {

	@Autowired
	TipoLojaRepository tipoLojaRepository;
	
	@Autowired
	LojaRepository lojaRepository;
	

	@RequestMapping("formLoja")
	public String formLoja(Model model) {

		model.addAttribute("tipos", tipoLojaRepository.findAll());

		return "Loja/FormLoja";

	}
	
	
	@RequestMapping("salvarLoja")
	// referenciando o name do input(fotos) a variavel fileFotos
	public String salvarLoja(Loja loja, @RequestParam("fotos") MultipartFile[] filefotos) {
		
		System.out.println(filefotos.length);
		//lojaRepository.save(loja);
		
		return "redirect:formLoja";
		
	}
	
}
