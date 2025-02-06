package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_divisions")
public class UserDivision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "division_id")
    private Division division;

    public UserDivision() {}

    public UserDivision(User user, Division division) {
        this.user = user;
        this.division = division;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Division getDivision() {
		return division;
	}

	public void setDivision(Division division) {
		this.division = division;
	}

   
}
