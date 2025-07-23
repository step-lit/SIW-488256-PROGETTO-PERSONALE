package it.uniroma3.siw.controller;

import java.time.LocalDate;
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
import it.uniroma3.siw.dto.DottoreDTO;
import it.uniroma3.siw.dto.PazienteDTO;
import it.uniroma3.siw.comparator.PianoAlimentareComparator;
import it.uniroma3.siw.comparator.PrenotazioneVisitaComparator;
import it.uniroma3.siw.comparator.VisitaComparator;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Dottore;
import it.uniroma3.siw.model.Paziente;
import it.uniroma3.siw.model.PianoAlimentare;
import it.uniroma3.siw.model.PrenotazioneVisita;
import it.uniroma3.siw.model.Visita;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.PazienteService;
import it.uniroma3.siw.service.PianoAlimentareService;
import it.uniroma3.siw.service.PrenotazioneVisitaService;
import it.uniroma3.siw.service.VisitaService;
import it.uniroma3.siw.validation.OnCreation;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/dottore")
public class DottoreController {
	
	@Autowired
	private CredentialsService credentialsService;
	@Autowired
	private PazienteService pazienteService;
	@Autowired
	private VisitaService visitaService;
	@Autowired
	private PianoAlimentareService pianoAlimentareService;
	@Autowired
	private PrenotazioneVisitaService prenotazioneVisitaService;
	
	/* ritorna il riferimento al dottore attualmente loggato */
	private Dottore getDottoreCorrente() {
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
        return (credentials != null) ? credentials.getDottore() : null;
	}
	
	@GetMapping
    public String homeDottore(Model model) {
		Dottore dottoreCorrente = getDottoreCorrente();
		DottoreDTO dottoreDTO = new DottoreDTO(dottoreCorrente.getName(), dottoreCorrente.getSurname());
		List<PrenotazioneVisita> listaPrenotazioni = (List<PrenotazioneVisita>) prenotazioneVisitaService.findAllByIdDottore(dottoreCorrente.getId());
		List<PianoAlimentare> listaPiani = (List<PianoAlimentare>) pianoAlimentareService.findAllByIdDottore(dottoreCorrente.getId());
		List<Visita> listaVisite = (List<Visita>) visitaService.findAllByIdDottore(dottoreCorrente.getId());
		Collections.sort(listaPrenotazioni, new PrenotazioneVisitaComparator());
		Collections.sort(listaPiani, new PianoAlimentareComparator());
		Collections.sort(listaVisite, new VisitaComparator());
        model.addAttribute("dottore", dottoreDTO);
        model.addAttribute("pianiDottore", listaPiani);
        model.addAttribute("visiteDottore", listaVisite);
        model.addAttribute("prenotazioniDottore", listaPrenotazioni);
        
        return "dottore.html";
    }
	
	@GetMapping("/visita")
	public String paginaVisita(Model model) {
		model.addAttribute("cfPaziente", "");
		return "visita.html";
	}
	
	@PostMapping("/visita")
	public String postPaginaVisita(@RequestParam("cf") String cf, RedirectAttributes redirectAttributes, Model model) { //il parametro ha name cf nel file html
		Paziente paziente = pazienteService.findByCf(cf);
		
		if (paziente == null) {
			redirectAttributes.addFlashAttribute("erroreCf", "Errore: nessun paziente con il codice fiscale inserito.");
			return "redirect:/dottore/visita";
		}
		redirectAttributes.addFlashAttribute("success", "Hai selezionato correttamente il paziente.");
		return "redirect:/dottore/visita/" + paziente.getId();
	}
	
	@GetMapping("/visita/{pazienteId}")
	public String nuovaVisita(@PathVariable("pazienteId") Long pazienteId, Model model) {
		Paziente paziente = pazienteService.findById(pazienteId);
		PazienteDTO pazienteDTO = new PazienteDTO(paziente.getId(), paziente.getName(), paziente.getSurname());
		model.addAttribute("pazienteDTO", pazienteDTO);
		model.addAttribute("visita", new Visita());
		
		return "newvisita.html";
	}
	
	@PostMapping("/visita/{pazienteId}")
	public String postNuovaVisita(@Valid @ModelAttribute("visita") Visita visita, BindingResult bindingResult,
			                      @PathVariable("pazienteId") Long pazienteId, RedirectAttributes redirectAttributes, Model model) {
		
		boolean errore = false;
		Paziente paziente = pazienteService.findById(pazienteId);
		
		//se esiste già una prenotazione con stessa data ed orario
		if(visitaService.existsByDateTime(visita.getDateTime())) {
			model.addAttribute("erroreDateTime", "Esiste già una visita in data ed orario scelti.");
			errore = true;
		}
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("errore", "Errore generico: controllare i campi inseriti.");
			errore = true;
		}
		
		if(errore) {
			PazienteDTO pazienteDTO = new PazienteDTO(paziente.getId(), paziente.getName(), paziente.getSurname());
			model.addAttribute("pazienteDTO", pazienteDTO);
			return "newvisita.html";
		}
		
		Dottore dottoreCorrente = getDottoreCorrente();
		visita.setPaziente(paziente);
		visita.setDottore(dottoreCorrente);
		visitaService.save(visita);
	
		redirectAttributes.addFlashAttribute("successVisita", "La visita è stata inserita correttamente.");
		return "redirect:/dottore";
	}
	
	@GetMapping("/piano")
	public String paginaPiano(Model model) {
		model.addAttribute("cfPaziente", "");
		return "piano.html";
	}
	
	@PostMapping("/piano")
	public String postPaginaPiano(@RequestParam("cf") String cf, RedirectAttributes redirectAttributes, Model model) { //il parametro ha name cf nel file html
		Paziente paziente = pazienteService.findByCf(cf);
		if (paziente == null) {
			redirectAttributes.addFlashAttribute("erroreCf", "Errore: nessun paziente con il codice fiscale inserito.");
			return "redirect:/dottore/piano";
		}
		redirectAttributes.addFlashAttribute("success", "Hai selezionato correttamente il paziente.");
		return "redirect:/dottore/piano/" + paziente.getId();
	}
	
	@GetMapping("/piano/{pazienteId}")
	public String nuovoPiano(@PathVariable("pazienteId") Long pazienteId, Model model) {
		Paziente paziente = pazienteService.findById(pazienteId);
		PazienteDTO pazienteDTO = new PazienteDTO(paziente.getId(),paziente.getName(), paziente.getSurname());
		model.addAttribute("pazienteDTO", pazienteDTO);
		model.addAttribute("piano", new PianoAlimentare());
		
		return "newpiano.html";
	}
	
	@PostMapping("/piano/{pazienteId}")
	public String postNuovoPiano(@Validated(OnCreation.class) @ModelAttribute("piano") PianoAlimentare piano, BindingResult bindingResult,
								 @PathVariable("pazienteId") Long pazienteId, RedirectAttributes redirectAttributes, Model model) {
		
		boolean errore = false;
		Paziente paziente = pazienteService.findById(pazienteId);
		
		//se entrambe le date inserite non sono nulle..
		if(piano.getStartDate() != null && piano.getEndDate() != null) {
			//se la data di inizio è prima della data di fine oppure non rispetta le tempistiche minime del piano alimentare
			if(piano.getStartDate().isAfter(piano.getEndDate())) {
				model.addAttribute("erroreStartDate","Errore: la data di fine non può essere precedente alla data di inizio.");
				errore = true;
			}
			else {
				LocalDate minEndDate = piano.getStartDate().plusMonths(3); //durata di un piano di almeno tre mesi
				if(piano.getEndDate().isBefore(minEndDate)) {
					model.addAttribute("erroreEndDate", "Errore: La data di fine deve essere almeno tre mesi dopo la data di inizio.");
					errore = true;
				}
			}
		}
		
		//se esiste già un piano con stessa data di inizio per il paziente
		if(piano.getStartDate() != null && pianoAlimentareService.existsByStartDateAndPazienteId(piano.getStartDate(), pazienteId) ){
			model.addAttribute("erroreDateEsistenti", "Errore: esiste già un piano alimentare per il paziente con la stessa data di inizio.");
			errore = true;
		}
		
		//se esiste già un piano con entrambe le date uguali per il paziente
		if(piano.getEndDate() != null && pianoAlimentareService.existsByStartDateAndEndDateAndPazienteId(piano.getStartDate(), piano.getEndDate(), pazienteId)){
			model.addAttribute("erroreDateEsistenti", "Errore: esiste già un piano alimentare per il paziente che si sovrappone al periodo.");
			errore = true;
		}
				
		if(bindingResult.hasErrors()) {
			model.addAttribute("errore", "Errore generico: controllare i campi inseriti.");
			errore = true;
		}
				
		if(errore) {
			PazienteDTO pazienteDTO = new PazienteDTO(paziente.getId(), paziente.getName(), paziente.getSurname());
			model.addAttribute("pazienteDTO", pazienteDTO);
			return "newpiano.html";
		}
		
		Dottore dottoreCorrente = getDottoreCorrente();
		piano.setPaziente(paziente);
		piano.setDottore(dottoreCorrente);
		pianoAlimentareService.save(piano);
	
		redirectAttributes.addFlashAttribute("successPiano", "Il piano alimentare è stato inserito correttamente.");
		return "redirect:/dottore";
	}
	
	@GetMapping("/cancella-prenotazione/{id}")
	public String cancellaPrenotazione(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		prenotazioneVisitaService.deleteById(id);
		
		redirectAttributes.addFlashAttribute("successCancellaPrenotazione", "La prenotazione della visita è stata cancellata correttamente.");
		return "redirect:/dottore";
	}
	
	@GetMapping("/modifica-prenotazione/{id}/{pazienteId}")
	public String modificaPrenotazione(@PathVariable("id") Long id, Model model) {
		PrenotazioneVisita prenotazione = prenotazioneVisitaService.getPrenotazioneVisitaById(id);
		model.addAttribute("prenotazione", prenotazione);
		
		return "modificaprenotazione.html";
	}
	
	@PostMapping("/modifica-prenotazione/{id}/{pazienteId}")
		public String postModificaPrenotazione(@PathVariable("pazienteId") Long pazienteId, @PathVariable("id") Long prenotazioneId,
											   @Valid @ModelAttribute("prenotazione") PrenotazioneVisita prenotazione,
											   BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
		
		
		//se la prenotazione ha stessa data e orario
		if(prenotazione.getDateTime() != null && prenotazioneVisitaService.existsByDateTimeAndIdNot(prenotazione.getDateTime(), prenotazioneId)) {
			model.addAttribute("errore", "Errore: esiste già una prenotazione con la stessa data e lo stesso orario.");
		}
		
		if( bindingResult.hasErrors() ) {
			model.addAttribute("errore", "Errore generico: controllare i campi inseriti.");
			return "modificaprenotazione.html";
		}
		
		prenotazione.setDottore(getDottoreCorrente());
		Paziente paziente = pazienteService.findById(pazienteId);
		prenotazione.setPaziente(paziente);
		prenotazioneVisitaService.save(prenotazione);
		
		redirectAttributes.addFlashAttribute("successModificaPrenotazione", "La prenotazione della visita è stata modificata correttamente.");
		return "redirect:/dottore";
		}
	
	@GetMapping("/visualizza-piano/{pianoId}")
	public String visualizzaPianoAlimentare(@PathVariable("pianoId") Long pianoId, Model model) {
	    PianoAlimentare piano = pianoAlimentareService.findPianoById(pianoId);
	    model.addAttribute("piano", piano);
	    
		return "dottorevisualizzapiano.html";
	}
	
	@GetMapping("/cancella-piano/{id}")
	public String cancellaPiano(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		pianoAlimentareService.deleteById(id);
		
		redirectAttributes.addFlashAttribute("successCancellaPiano", "Il piano alimentare è stato cancellato correttamente.");
		return "redirect:/dottore";
	}
	
	@GetMapping("/modifica-piano/{pianoId}/{pazienteId}")
    public String mostraFormModificaPiano(@PathVariable("pianoId") Long pianoId, RedirectAttributes redirectAttributes, Model model) {
        PianoAlimentare piano = pianoAlimentareService.findPianoById(pianoId);
        if (piano == null) {
        	redirectAttributes.addFlashAttribute("errorePianoNullo", "Errore: il piano per la modifica non esiste o il suo id è stato perso.");
            return "redirect:/dottore";
        }
        model.addAttribute("pianoAlimentare", piano);
        
        return "modificapiano";
    }

    @PostMapping("/modifica-piano/{pianoId}/{pazienteId}")
    public String aggiornaPianoAlimentare(@PathVariable("pianoId") Long pianoId, @PathVariable("pazienteId") Long pazienteId,
    									  @Valid @ModelAttribute("pianoAlimentare") PianoAlimentare pianoModificato,
    									  BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
    	
        PianoAlimentare piano = pianoAlimentareService.findPianoById(pianoId);

        if(piano == null) {
        	redirectAttributes.addFlashAttribute("errorePianoNullo", "Errore: il piano per la modifica non esiste o il suo id è stato perso.");
            return "redirect:/dottore";
        }
        
        boolean errore = false;
        
        //se entrambe le date inserite non sono nulle..
      	if(pianoModificato.getStartDate() != null && pianoModificato.getEndDate() != null) {
      		//se la data di inizio è prima della data di fine oppure non rispetta le tempistiche minime del piano alimentare
      		if(pianoModificato.getStartDate().isAfter(pianoModificato.getEndDate())) {
      			model.addAttribute("erroreStartDate","Errore: la data di fine non può essere precedente alla data di inizio.");
      			errore = true;
      		}
      		else {
      			LocalDate minEndDate = pianoModificato.getStartDate().plusMonths(3); //durata di un piano di almeno tre mesi
      			if(pianoModificato.getEndDate().isBefore(minEndDate)) {
      				model.addAttribute("erroreEndDate", "Errore: La data di fine deve essere almeno tre mesi dopo la data di inizio.");
      				errore = true;
      			}
      		}
      	}
      	
      	//se esiste già un piano con la stessa data di inizio per lo stesso paziente
      	if(pianoModificato.getStartDate() != null && pianoAlimentareService.existsByStartDateAndPazienteIdAndIdNot(pianoModificato.getStartDate(), pazienteId, pianoId)) {
      		model.addAttribute("erroreDateEsistenti", "Errore: esiste già un piano alimentare per il paziente con la stessa data di inizio.");
      		errore = true;
      	}
        
        if(bindingResult.hasErrors()) {
        	model.addAttribute("errore", "Errore generico: controllare i campi inseriti.");
        }
        
        if(errore) {
        	return "modificapiano";
        }
        
		pianoModificato.setDottore(getDottoreCorrente());
		Paziente paziente = pazienteService.findById(pazienteId);
		pianoModificato.setPaziente(paziente);
        pianoAlimentareService.save(pianoModificato);
        
        redirectAttributes.addFlashAttribute("successModificaPiano", "Il piano alimentare è stato modificato correttamente.");
        return "redirect:/dottore/visualizza-piano/" + piano.getId();
    }
	
	
}
