package de.hsb.app.mrv.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@NamedQuery(name = "SelectanRezepte", query = "select r from Rezept r")
@Entity
public class Rezept implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private UUID rezeptNummer;
	
	
	Date datum = new Date();
	
	@OneToOne(cascade = CascadeType.ALL)
	Kunde kunde;
	
	@OneToOne(cascade = CascadeType.ALL)
	Medikament medikament;
	
	@Lob
	Person arzt;
	
	public Rezept() {
		super();
	}
	
	public Rezept(UUID rezeptNummer, Date datum) {
		super();
		this.rezeptNummer = rezeptNummer;
		this.datum = datum;
	}

	public UUID getRezeptNummer() {
		return rezeptNummer;
	}

	public void setRezeptNummer(UUID rezeptNummer) {
		this.rezeptNummer = rezeptNummer;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public Kunde getKunde() {
		return kunde;
	}

	public void setKunde(Kunde kunde) {
		this.kunde = kunde;
	}

	public Medikament getMedikament() {
		return medikament;
	}

	public void setMedikament(Medikament medikament) {
		this.medikament = medikament;
	}

	public Person getArzt() {
		return arzt;
	}

	public void setArzt(Person arzt) {
		this.arzt = arzt;
	}
	
	


	
}
