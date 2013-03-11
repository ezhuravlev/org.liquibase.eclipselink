package com.example.application;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

@Entity
public class Contact {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ContactType type;

    private String val;
    
    @Version
   private Integer version;
    
    
    
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
    
    public ContactType getType() {
        return this.type;
    }
    
    public void setType(ContactType type) {
        this.type = type;
    }
    
    public String getVal() {
        return this.val;
    }
    
    public void setVal(String val) {
        this.val = val;
    }
    
    public String toString() {
    	return this.val;
    }
}
