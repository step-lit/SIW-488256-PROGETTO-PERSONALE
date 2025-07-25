package it.uniroma3.siw.dto;

public class DottoreDTO { //classe di appoggio per la vista (Data Transfer Object)
	
	private Long id;
	private String name;
	private String surname;
	
	public DottoreDTO(Long id, String name, String surname) {
		this(name,surname);
		this.id = id;
	}
	
	public DottoreDTO(String name, String surname) {
		this.name = name;
		this.surname = surname;
	}
	
	public DottoreDTO() {
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
	
}
