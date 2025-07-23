package it.uniroma3.siw.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.uniroma3.siw.model.PrenotazioneVisita;
import it.uniroma3.siw.repository.PrenotazioneVisitaRepository;

@Service
public class PrenotazioneVisitaService {

	@Autowired
	private PrenotazioneVisitaRepository prenotazioneVisitaRepository;
	
	public List<PrenotazioneVisita> findAllByIdPaziente(Long pazienteId){
		return prenotazioneVisitaRepository.findAllByIdPaziente(pazienteId);
	}
	
	public List<PrenotazioneVisita> findAllByIdDottore(Long dottoreId){
		return prenotazioneVisitaRepository.findAllByIdDottore(dottoreId);
	}
	
	public PrenotazioneVisita save(PrenotazioneVisita prenotazioneVisita) {
		return prenotazioneVisitaRepository.save(prenotazioneVisita);
	}
	
	public boolean existsByDateTime(LocalDateTime dateTime) {
		return prenotazioneVisitaRepository.existsByDateTime(dateTime);
	}
		
	public boolean existsByDateTimeAndIdNot(LocalDateTime dateTime, Long id) {
        return this.prenotazioneVisitaRepository.existsByDateTimeAndIdNot(dateTime, id);
    }
	
	public PrenotazioneVisita getPrenotazioneVisitaById(Long id) {
		return prenotazioneVisitaRepository.findById(id).get();
	}
	
	public void deleteById(Long id) {
		prenotazioneVisitaRepository.deleteById(id);
	}
	
}
