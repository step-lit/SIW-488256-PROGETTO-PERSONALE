package it.uniroma3.siw.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import it.uniroma3.siw.model.PianoAlimentare;
import it.uniroma3.siw.model.Visita;


public interface VisitaRepository extends CrudRepository<Visita, Long>{
	
	public boolean existsByDateTime(LocalDateTime dateTime);
	
	@Query(value = "SELECT * FROM visita WHERE paziente_id = :pazienteId", nativeQuery = true)
	public List<Visita> findAllByIdPaziente(@Param("pazienteId") Long pazienteId);
	
	@Query(value = "SELECT * FROM visita WHERE dottore_id = :dottoreId", nativeQuery = true)
	public List<Visita> findAllByIdDottore(@Param("dottoreId") Long dottoreId);
	
}
