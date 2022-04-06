package br.senai.sp.cfp8.guidecar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
	public String salvarLoja(Loja loja) {
		
		lojaRepository.save(loja);
		
		return "redirect:formLoja";
		
	}
	
}
