package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "divisions")
public class Division {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String divisionName;

    public Division() {}
    
    public Division(String divisionName) {
        this.divisionName = divisionName;
    }

    public Long getId() { 
    	return id; 
    }
    
    public void setId(Long id) { 
    	this.id = id; 
    }
    
    public String getDivisionName() { 
    	return divisionName; 
    }
    
    public void setDivisionName(String divisionName) { 
    	this.divisionName = divisionName; 
    }
}
