package de.hsb.app.mrv.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@NamedQuery(name = "SelectMedikament", query = "select m from Medikament m")
@Entity
public class Medikament implements Serializable {
	protected static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	protected UUID id;
	protected String medikamentenBezeichnung;
	protected double preis;
	protected int menge;
	protected Rezeptpflichtig rezeptPflichtig;
	
	
	public Medikament(UUID id, String medikamentenBezeichnung, double preis, int menge, Rezeptpflichtig rezeptPflichtig) {
		super();
		this.id = id;
		this.medikamentenBezeichnung = medikamentenBezeichnung;
		this.preis = preis;
		this.menge = menge;
		this.rezeptPflichtig = rezeptPflichtig;
	}
	
	public Medikament() {
		super();
	
	}
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getMedikamentenBezeichnung() {
		return medikamentenBezeichnung;
	}
	public void setMedikamentenBezeichnung(String medikamentenBezeichnung) {
		this.medikamentenBezeichnung = medikamentenBezeichnung;
	}
	public double getPreis() {
		return preis;
	}
	public void setPreis(double preis) {
		this.preis = preis;
	}
	
	
	
	public int getMenge() {
		return menge;
	}

	public void setMenge(int menge) {
		this.menge = menge;
	}

	public Rezeptpflichtig getRezeptPflichtig() {
		return rezeptPflichtig;
	}
	public void setRezeptPflichtig(Rezeptpflichtig rezeptPflichtig) {
		this.rezeptPflichtig = rezeptPflichtig;
	}
	
	

	
	
	
}
