package com.example.application;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

@Entity
public class Role {
	
	@Id
	private long id;
	
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinTable(name = "USER_ROLE_LIST", joinColumns = @JoinColumn(name = "ID_ROLE"), inverseJoinColumns = @JoinColumn(name = "ID_USER"))
	private User user;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
