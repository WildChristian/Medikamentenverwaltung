package de.hsb.app.mrv.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@NamedQuery(name = "Selectkrankasse", query = "select k from Krankenkasse k")
@Entity
public class Krankenkasse {

	@Id
	@GeneratedValue
	private UUID kartennummer;
	private String name;
	private String gultigbis;
	
	public Krankenkasse(String name, String gultigbis) {
		super();
		
		this.name = name;
		
		this.gultigbis = gultigbis;
	}


	public Krankenkasse() {
		super();

	}
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public UUID getKartennummer() {
		return kartennummer;
	}


	public void setKartennummer(UUID kartennummer) {
		this.kartennummer = kartennummer;
	}


	public String getGultigbis() {
		return gultigbis;
	}

	public void setGultigbis(String gultigbis) {
		this.gultigbis = gultigbis;
	}
	
}
