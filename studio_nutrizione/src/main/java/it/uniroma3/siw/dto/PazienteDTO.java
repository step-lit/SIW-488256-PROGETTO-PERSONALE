package it.uniroma3.siw.dto;

import java.time.LocalDate;

public class PazienteDTO { //classe di appoggio per la vista (Data Transfer Object)
	
	private Long id;
	private String name;
	private String surname;
	private String email;
	private LocalDate dateOfBirth;
	private String cf;
	
	public PazienteDTO(Long id, String name, String surname) {
		this(name,surname);
		this.id = id;
	}
	
	public PazienteDTO(String name, String surname, String email) {
		this(name,surname);
		this.email = email;
	}
	
	public PazienteDTO(String name, String surname, String email, LocalDate dateOfBirth) {
		this(name,surname,email);
		this.dateOfBirth = dateOfBirth;
	}
	
	public PazienteDTO(String name, String surname) {
		this.name = name;
		this.surname = surname;
	}
	
	public PazienteDTO() {
	}
	
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

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}
	
}
