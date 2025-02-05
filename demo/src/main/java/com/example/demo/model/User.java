package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String email;
	private Integer nik;

	public User() {}
	
	public User(String name, String email, Integer nik) {
	    this.name = name;
	    this.email = email;
	    this.nik = nik;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public Integer getNik() {
		return nik;
	}

	public void setNik(Integer nik) {
		this.nik = nik;
	}
}
