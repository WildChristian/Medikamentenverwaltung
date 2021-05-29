package de.hsb.app.mrv.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

@NamedQuery(name = "SelectBestellung", query = "select k from Bestellung k")
@Entity
public class Bestellung implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	protected UUID id;
	
	Date date = new Date();
	double summe;
	
	@Lob
	@OneToOne(cascade = CascadeType.ALL)
	Kunde patient;
	
	@Lob
	Person apotheker;
	
	@Lob
	@JoinColumn(name = "bestellung_id", nullable = false)
	protected HashSet<Medikament> medikamentbestellung = new HashSet<Medikament>();

	public Bestellung(Date date, double summe,int id) {
		super();
		this.date = date;
		this.summe = summe;
		
	}

	public Bestellung() {
		super();
	}


	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getSumme() {
		return summe;
	}

	public void setSumme(double summe) {
		this.summe = summe;
	}

	public Kunde getPatient() {
		return patient;
	}

	public void setPatient(Kunde patient) {
		this.patient = patient;
	}



	public HashSet<Medikament> getMedikamentbestellung() {
		return medikamentbestellung;
	}


	public void setMedikamentbestellung(HashSet<Medikament> medikamentbestellung) {
		this.medikamentbestellung = medikamentbestellung;
	}


	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Person getApotheker() {
		return apotheker;
	}

	public void setApotheker(Person apotheker) {
		this.apotheker = apotheker;
	}
	
}