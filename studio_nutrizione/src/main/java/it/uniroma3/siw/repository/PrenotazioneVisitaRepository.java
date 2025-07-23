package it.uniroma3.siw.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import it.uniroma3.siw.model.PrenotazioneVisita;

public interface PrenotazioneVisitaRepository extends CrudRepository<PrenotazioneVisita, Long> {
	
	public boolean existsByDateTime(LocalDateTime dateTime);
	
	boolean existsByDateTimeAndIdNot(LocalDateTime dateTime, Long id);
	
	@Query(value = "SELECT * FROM prenotazione_visita WHERE paziente_id = :pazienteId", nativeQuery = true)
	public List<PrenotazioneVisita> findAllByIdPaziente(@Param("pazienteId") Long pazienteId);
	
	@Query(value = "SELECT * FROM prenotazione_visita WHERE dottore_id = :dottoreId", nativeQuery = true)
	public List<PrenotazioneVisita> findAllByIdDottore(@Param("dottoreId") Long dottoreId);
	
}

