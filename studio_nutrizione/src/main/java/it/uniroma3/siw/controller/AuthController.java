package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Paziente;
import it.uniroma3.siw.security.AuthHandler;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.PazienteService;
import jakarta.validation.Valid;

@Controller
public class AuthController {
	
	@Autowired
	private CredentialsService credentialsService;
	@Autowired
	private PazienteService pazienteService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
    private AuthHandler authHandler;

	@GetMapping("/register") 
	public String newRegister(Model model){
		model.addAttribute("paziente", new Paziente());
		model.addAttribute("credentials", new Credentials());
		
		return "register.html";
	}
	
	@PostMapping("/register")
	public String completeRegister(@Valid @ModelAttribute("paziente") Paziente paziente, BindingResult pazienteBindingResult,
								   @Valid @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult,
								   RedirectAttributes redirectAttributes, Model model) {
		
		boolean errore = false;
		
		//se esiste già un paziente con l'email inserita
		if(pazienteService.existsByEmail(paziente.getEmail())) {
			model.addAttribute("erroreMail", "Errore: l'email inserita è già esistente.");
			errore = true;
		}
		
		//se esiste già un paziente con lo stesso codice fiscale
		if(pazienteService.existsByCf(paziente.getCf())) {
			model.addAttribute("erroreCf", "Errore: il codice fiscale inserito è già esistente.");
			errore = true;
		}
		
		//se l'username appartiene già a qualche paziente
		if (credentialsService.findByUsername(credentials.getUsername()) != null) {
			model.addAttribute("erroreUsername", "Errore: l'username inserito è già esistente.");
			errore = true;
        }
		
		if (pazienteBindingResult.hasErrors() || credentialsBindingResult.hasErrors()) {
			model.addAttribute("errore", "Errore generico: controllare i campi inseriti.");
			errore = true;
        }
		
		if (errore) {
			return "register.html";
		}
		
		Paziente savedPaziente = this.pazienteService.save(paziente);
		credentials.setPaziente(savedPaziente);
		credentials.setRole(Credentials.RUOLO_PAZIENTE);
		credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
		this.credentialsService.save(credentials);
		
		redirectAttributes.addFlashAttribute("success", "Registrazione effettuata con successo.");
		return "redirect:/login";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
        	String url = authHandler.determinaUrl(authentication);
            return "redirect:" + url;
        }
        
        return "login.html";
    }
	
}
