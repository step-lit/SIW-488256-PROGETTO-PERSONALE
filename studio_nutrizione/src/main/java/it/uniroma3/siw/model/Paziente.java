package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Email;

@Entity
public class Paziente {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String surname;
	
	@NotBlank
	@Email
	private String email;
	
	@Past(message = "La data inserita è nel futuro.")
	private LocalDate dateOfBirth;
	
	@NotBlank
	private String cf;
	
	@OneToMany(mappedBy="paziente")
	private List<Visita> visitePaziente;
	
	@OneToMany(mappedBy="paziente")
	private List<PianoAlimentare> pianiPaziente;
	
	@OneToMany(mappedBy="paziente")
	private List<PrenotazioneVisita> prenotazioniVisite;
	
	
	
	/* getters, setters, hashcode, equals */
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getCf() {
		return this.cf;
	}
	public void setCf(String cf) {
		this.cf = cf;
	}
	
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public List<Visita> getVisitePaziente() {
		return visitePaziente;
	}
	public void setVisitePaziente(List<Visita> visitePaziente) {
		this.visitePaziente = visitePaziente;
	}
	
	public List<PrenotazioneVisita> getPrenotazioniVisite() {
		return prenotazioniVisite;
	}
	public void setPrenotazioniVisite(List<PrenotazioneVisita> prenotazioniVisite) {
		this.prenotazioniVisite = prenotazioniVisite;
	}
	
	public List<PianoAlimentare> getPianiPaziente() {
		return pianiPaziente;
	}
	public void setPianiPaziente(List<PianoAlimentare> pianiPaziente) {
		this.pianiPaziente = pianiPaziente;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(cf, name, surname);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Paziente other = (Paziente) obj;
		return Objects.equals(cf, other.cf) && Objects.equals(name, other.name)
				&& Objects.equals(surname, other.surname);
	}
	
	
	
	
}
