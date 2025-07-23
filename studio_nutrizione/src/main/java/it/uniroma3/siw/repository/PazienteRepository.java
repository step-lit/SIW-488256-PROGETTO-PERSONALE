package it.uniroma3.siw.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Paziente;

public interface PazienteRepository extends CrudRepository<Paziente, Long> {
	
	@Query(value = "SELECT * FROM paziente WHERE email = :emailPaziente", nativeQuery = true )
	public Paziente findByEmail(@Param("emailPaziente") String email);
	
	@Query(value = "SELECT * FROM paziente WHERE cf = :cfPaziente", nativeQuery=true )
	public Paziente findByCf(@Param("cfPaziente") String cf);
}
