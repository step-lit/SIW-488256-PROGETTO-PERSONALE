package it.uniroma3.siw.controller;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import it.uniroma3.siw.comparator.PrenotazioneVisitaComparator;
import it.uniroma3.siw.comparator.VisitaComparator;
import it.uniroma3.siw.comparator.PianoAlimentareComparator;
import it.uniroma3.siw.dto.PazienteDTO;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Dottore;
import it.uniroma3.siw.model.Paziente;
import it.uniroma3.siw.model.PianoAlimentare;
import it.uniroma3.siw.model.PrenotazioneVisita;
import it.uniroma3.siw.model.Visita;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.DottoreService;
import it.uniroma3.siw.service.PianoAlimentareService;
import it.uniroma3.siw.service.PrenotazioneVisitaService;
import it.uniroma3.siw.service.VisitaService;
import it.uniroma3.siw.validation.OnCreation;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/paziente")
public class PazienteController {
	
	@Autowired
	private DottoreService dottoreService;
	@Autowired
	private CredentialsService credentialsService;
	@Autowired
	private PrenotazioneVisitaService prenotazioneVisitaService;
	@Autowired
	private PianoAlimentareService pianoAlimentareService;
	@Autowired
	private VisitaService visitaService;
	
	/* ritorna il riferimento al paziente attualmente loggato */
	private Paziente getPazienteCorrente() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //ci fornisce i dati di autenticazione
        String username;
        
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername(); 
        }
        else { 
        	//se non è un oggetto UserDetails usiamo una stringa corrispondente.
            username = principal.toString();
        }
        //recuperiamo l'oggetto credentials tramite valore username
        Credentials credentials = this.credentialsService.findByUsername(username);
        return (credentials != null) ? credentials.getPaziente() : null;
	}
	
	
	@GetMapping
    public String homePaziente(Model model) {
		Paziente pazienteCorrente = getPazienteCorrente(); //mantengo il riferimento dopo l'accesso
		PazienteDTO pazienteDTO = new PazienteDTO(pazienteCorrente.getName(), pazienteCorrente.getSurname());
		List<PrenotazioneVisita> listaPrenotazioni = (List<PrenotazioneVisita>) prenotazioneVisitaService.findAllByIdPaziente(pazienteCorrente.getId());
		List<PianoAlimentare> listaPiani = (List<PianoAlimentare>) pianoAlimentareService.findAllByIdPaziente(pazienteCorrente.getId());
		List<Visita> listaVisite = (List<Visita>) visitaService.findAllByIdPaziente(pazienteCorrente.getId());
		Collections.sort(listaPrenotazioni, new PrenotazioneVisitaComparator());
		Collections.sort(listaPiani, new PianoAlimentareComparator());
		Collections.sort(listaVisite, new VisitaComparator());
        model.addAttribute("paziente", pazienteDTO);
        model.addAttribute("pianiPaziente", listaPiani);
        model.addAttribute("visitePaziente", listaVisite);
        model.addAttribute("prenotazioniPaziente", listaPrenotazioni);
        
        return "paziente.html";
    }
	
	@GetMapping("/profilo")
	public String profiloPaziente(Model model) {
		Paziente pC = getPazienteCorrente();
		PazienteDTO pazienteDTO = new PazienteDTO(pC.getName(), pC.getSurname(), pC.getEmail(), pC.getDateOfBirth());
        model.addAttribute("paziente", pazienteDTO);

		return "profilo.html";
	}
	
	@GetMapping("/prenotazioni")
	public String prenotazionePaziente(Model model) {
		model.addAttribute("dottori", this.dottoreService.findAll());
		model.addAttribute("pvisita", new PrenotazioneVisita());
		
		return "prenotazioni.html";
	}
	
	@PostMapping("/prenotazioni")
	public String postPrenotazionePaziente(@Validated(OnCreation.class) @ModelAttribute("pvisita") PrenotazioneVisita prenotazioneVisita,
								           BindingResult bindingResult, RedirectAttributes redirectAttributes,
								           @RequestParam("dottoreId") Long dottoreId, Model model) {
	
		boolean errore = false;
		
		//se esiste già una prenotazione con stessa data ed orario
		if(prenotazioneVisita.getDateTime() != null && prenotazioneVisitaService.existsByDateTime(prenotazioneVisita.getDateTime())) {
			model.addAttribute("erroreDateTime", "Esiste già una prenotazione in data ed orario scelti.");
			errore = true;
		}
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("errore", "Errore generico: controllare i campi inseriti.");
			errore = true;
		}
		
		if(errore) {
			model.addAttribute("dottori", this.dottoreService.findAll()); //per il menu a tendina
			return "prenotazioni.html";
		}
		
		Paziente pazienteCorrente = getPazienteCorrente();
		Dottore dottore = dottoreService.findById(dottoreId);
		prenotazioneVisita.setPaziente(pazienteCorrente);
		prenotazioneVisita.setDottore(dottore);
		prenotazioneVisitaService.save(prenotazioneVisita);
		
		redirectAttributes.addFlashAttribute("success", "Prenotazione effettuata con successo.");
		return "redirect:/paziente";
	}
	
	@GetMapping("/visualizza-piano/{pianoId}")
	public String visualizzaPianoAlimentare(@PathVariable("pianoId") Long pianoId, Model model) {
	    PianoAlimentare piano = pianoAlimentareService.findPianoById(pianoId);
	    model.addAttribute("piano", piano);
		return "pazientevisualizzapiano.html";
	}
	
}
