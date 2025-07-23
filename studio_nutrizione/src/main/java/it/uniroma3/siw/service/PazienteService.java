package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.uniroma3.siw.model.Paziente;
import it.uniroma3.siw.repository.PazienteRepository;

@Service
public class PazienteService {

	@Autowired
	private PazienteRepository pazienteRepository;
	
	public Paziente findById(Long id) {
		return pazienteRepository.findById(id).get();
	}
	
	public Paziente save(Paziente paziente) {
		return pazienteRepository.save(paziente);
	}
	
	public boolean existsByEmail(String email) {
		return pazienteRepository.findByEmail(email) != null;
	}
	
	public boolean existsByCf(String cf) {
		return pazienteRepository.findByCf(cf) != null;
	}
	
	public Paziente findByCf(String cf) {
		return pazienteRepository.findByCf(cf);
	}
	
}


