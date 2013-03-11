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
import javax.persistence.OneToMany;
import javax.persistence.Version;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String login;

	private String password;

	@Version
	private Integer version;

	@ManyToMany(cascade = CascadeType.ALL)
	private Set<Candidate> candidates = new HashSet<Candidate>();

	@ManyToMany(cascade = CascadeType.ALL)
	private Set<Comment> comments = new HashSet<Comment>();

	@ManyToMany(cascade = CascadeType.ALL)
	private Set<Vacancy> vacancys = new HashSet<Vacancy>();
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Role> roleList = new HashSet<Role>();

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

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Candidate> getCandidates() {
		return this.candidates;
	}

	public void setCandidates(Set<Candidate> candidates) {
		this.candidates = candidates;
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
	
	public Set<Role> getRoleList()
    {
        return roleList;
    }

    public void setRoleList(Set<Role> roleList)
    {
        this.roleList = roleList;
    }

	public String toString() {
		return this.login;
	}
}
