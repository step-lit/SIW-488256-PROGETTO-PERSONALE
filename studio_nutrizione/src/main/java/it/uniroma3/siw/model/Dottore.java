package it.uniroma3.siw.model;

import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Dottore {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String name;
	private String surname;
	private String email;
	private String phoneNumber;
	
	@OneToMany
	@JoinColumn(name="dottore_id")
	private List<Paziente> pazienti;
	
	@OneToMany(mappedBy="dottore")
	private List<Visita> visite;
	
	@OneToMany(mappedBy="dottore")
	private List<PianoAlimentare> pianiAlimentari;
	
	@OneToMany(mappedBy="dottore")
	private List<PrenotazioneVisita> prenotazioniVisite;
	
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
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public List<Paziente> getPazienti() {
		return pazienti;
	}
	public void setPazienti(List<Paziente> pazienti) {
		this.pazienti = pazienti;
	}
	public List<Visita> getVisite() {
		return visite;
	}
	public void setVisite(List<Visita> visite) {
		this.visite = visite;
	}
	public List<PianoAlimentare> getPianiAlimentari() {
		return pianiAlimentari;
	}
	public void setPianiAlimentari(List<PianoAlimentare> pianiAlimentari) {
		this.pianiAlimentari = pianiAlimentari;
	}
	public List<PrenotazioneVisita> getPrenotazioniVisite() {
		return prenotazioniVisite;
	}
	public void setPrenotazioniVisite(List<PrenotazioneVisita> prenotazioniVisite) {
		this.prenotazioniVisite = prenotazioniVisite;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(email, name, surname);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dottore other = (Dottore) obj;
		return Objects.equals(email, other.email) && Objects.equals(name, other.name)
				&& Objects.equals(surname, other.surname);
	}
	
	
	
}
