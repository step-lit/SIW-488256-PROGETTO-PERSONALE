package it.uniroma3.siw.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import it.uniroma3.siw.model.PianoAlimentare;

public interface PianoAlimentareRepository extends CrudRepository<PianoAlimentare, Long> {
	
	@Query(value = "SELECT * FROM piano_alimentare WHERE paziente_id = :pazienteId", nativeQuery = true)
	public List<PianoAlimentare> findAllByIdPaziente(@Param("pazienteId") Long pazienteId);
	
	@Query(value = "SELECT * FROM piano_alimentare WHERE dottore_id = :dottoreId", nativeQuery = true)
	public List<PianoAlimentare> findAllByIdDottore(@Param("dottoreId") Long dottoreId);
	
	@Query(value = "SELECT * FROM piano_alimentare WHERE start_date = :startDate AND end_date = :endDate AND paziente_id = :pazienteId", nativeQuery = true)
	public PianoAlimentare findByStartDateAndEndDateAndPazienteId(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("pazienteId") Long pazienteId);
	
	@Query(value = "SELECT * FROM piano_alimentare WHERE start_date = :startDate AND paziente_id = :pazienteId", nativeQuery = true)
	public PianoAlimentare findByStartDateAndPazienteId(@Param("startDate") LocalDate startDate, @Param("pazienteId") Long pazienteId);
	
	boolean existsByStartDateAndPazienteIdAndIdNot(LocalDate startDate, Long pazienteId, Long id);
}
