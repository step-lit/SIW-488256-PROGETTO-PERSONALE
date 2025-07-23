package it.uniroma3.siw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.model.Dottore;
import it.uniroma3.siw.security.AuthHandler;
import it.uniroma3.siw.service.DottoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class MainController {
	
	@Autowired
	private DottoreService dottoreService;
	
	@Autowired
	private AuthHandler authHandler;
	
	@GetMapping({"/", "/index"})
	public String index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
        	String url = authHandler.determinaUrl(authentication);
            return "redirect:" + url;
        }
   
        return "index.html";
    }
	
	@GetMapping({"/contatti"})
	public String contatti(Model model) {
		Iterable<Dottore> listaDottori = this.dottoreService.findAll();
		model.addAttribute("dottori", listaDottori);
        return "contatti.html";
    }
	
}