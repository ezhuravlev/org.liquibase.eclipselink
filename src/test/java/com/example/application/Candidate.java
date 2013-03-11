package com.example.application;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Version;

@Entity
public class Candidate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String surname;

	private String lastName;

	@Version
	private Integer version;

	@ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private Set<Contact> contact = new HashSet<Contact>();

	@ManyToMany(cascade = CascadeType.ALL)
	private Set<Comment> comments = new HashSet<Comment>();

	@ManyToMany(cascade = CascadeType.ALL)
	private Set<Vacancy> vacancys = new HashSet<Vacancy>();

	
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Set<Contact> getContact() {
		return this.contact;
	}

	public void setContact(Set<Contact> contact) {
		this.contact = contact;
	}

	public Set<Comment> getComments() {
		return this.comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public Set<Vacancy> getVacancys() {
		return this.vacancys;
	}

	public void setVacancys(Set<Vacancy> vacancys) {
		this.vacancys = vacancys;
	}

	public String toString() {
		return this.name+" "+this.surname+" "+this.lastName;
	}
}
