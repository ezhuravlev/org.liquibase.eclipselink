package com.example.application;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Version;

@Entity
public class Vacancy {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    
    @Version
    private Integer version;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Candidate> candidates = new HashSet<Candidate>();
    
    
    
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
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Set<Candidate> getCandidates() {
        return this.candidates;
    }
    
    public void setCandidates(Set<Candidate> candidates) {
        this.candidates = candidates;
    }
    
    public String toString() {
//        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    	return this.description;
    }

}
