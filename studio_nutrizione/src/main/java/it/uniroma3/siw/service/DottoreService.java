package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.uniroma3.siw.model.Dottore;
import it.uniroma3.siw.repository.DottoreRepository;

@Service
public class DottoreService {

	@Autowired //per iniettare automaticamente le variabili da cui una classe dipende
	private DottoreRepository dottoreRepository; //il framework si preoccupa di creare un oggetto da assegnare a questa variabile
	
	public Dottore findById(Long id) {
		return dottoreRepository.findById(id).get();
	}
 
	public Iterable<Dottore> findAll() {
		return dottoreRepository.findAll();
	}
	
	public Dottore save(Dottore dottore) {
		return dottoreRepository.save(dottore);
	}
	
}
