package it.uniroma3.siw.model;

import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import it.uniroma3.siw.validation.OnCreation;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
public class PianoAlimentare {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@NotNull
	@Future(message = "La data di inizio inserita è nel passato.", groups = OnCreation.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	
	@Future(message = "La data di fine inserita è nel passato.", groups = OnCreation.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;
	
	@NotBlank
	private String goal;
	
	@NotBlank
	private String description;
	
	@ManyToOne
	private Paziente paziente;
	
	@ManyToOne
	private Dottore dottore;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	public String getGoal() {
		return goal;
	}
	public void setGoal(String goal) {
		this.goal = goal;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Paziente getPaziente() {
		return paziente;
	}
	public void setPaziente(Paziente paziente) {
		this.paziente = paziente;
	}
	
	public Dottore getDottore() {
		return dottore;
	}
	public void setDottore(Dottore dottore) {
		this.dottore = dottore;
	}
	@Override
	public int hashCode() {
		return Objects.hash(endDate, startDate);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PianoAlimentare other = (PianoAlimentare) obj;
		return Objects.equals(endDate, other.endDate) && Objects.equals(startDate, other.startDate);
	}
	
}
