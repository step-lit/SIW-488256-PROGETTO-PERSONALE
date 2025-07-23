package it.uniroma3.siw.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.PianoAlimentare;
import it.uniroma3.siw.model.Visita;
import it.uniroma3.siw.repository.VisitaRepository;


@Service
public class VisitaService {

	@Autowired
	private VisitaRepository visitaRepository;
	
	public boolean existsByDateTime(LocalDateTime dateTime) {
		return visitaRepository.existsByDateTime(dateTime);
	}
	
	public Visita save(Visita visita) {
		return visitaRepository.save(visita);
	}
	
	public List<Visita> findAllByIdPaziente(Long pazienteId){
		return visitaRepository.findAllByIdPaziente(pazienteId);
	}
	
	public List<Visita> findAllByIdDottore(Long dottoreId){
		return visitaRepository.findAllByIdDottore(dottoreId);
	}
	
}
