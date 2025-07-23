package it.uniroma3.siw.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.uniroma3.siw.repository.PianoAlimentareRepository;
import it.uniroma3.siw.model.PianoAlimentare;

@Service
public class PianoAlimentareService {

	@Autowired
	private PianoAlimentareRepository pianoAlimentareRepository;
	
	public List<PianoAlimentare> findAllByIdPaziente(Long pazienteId){
		return pianoAlimentareRepository.findAllByIdPaziente(pazienteId);
	}
	
	public List<PianoAlimentare> findAllByIdDottore(Long dottoreId){
		return pianoAlimentareRepository.findAllByIdDottore(dottoreId);
	}
	
	public PianoAlimentare save(PianoAlimentare pianoAlimentare) {
		return pianoAlimentareRepository.save(pianoAlimentare);
	}
	
	public boolean existsByStartDateAndEndDateAndPazienteId(LocalDate startDate, LocalDate endDate, Long pazienteId) {
		return pianoAlimentareRepository.findByStartDateAndEndDateAndPazienteId(startDate, endDate, pazienteId) != null;
	}
	
	public boolean existsByStartDateAndPazienteId(LocalDate startDate, Long pazienteId) {
		return pianoAlimentareRepository.findByStartDateAndPazienteId(startDate, pazienteId) != null;
	}
	
    public boolean existsByStartDateAndPazienteIdAndIdNot(LocalDate startDate, Long pazienteId, Long pianoId) {
        return pianoAlimentareRepository.existsByStartDateAndPazienteIdAndIdNot(startDate, pazienteId, pianoId);
    }
	
	public void deleteById(Long id) {
		pianoAlimentareRepository.deleteById(id);
	}
	
	public PianoAlimentare findPianoById(Long id) {
		return pianoAlimentareRepository.findById(id).orElse(null);
	}
	
	
	
}
