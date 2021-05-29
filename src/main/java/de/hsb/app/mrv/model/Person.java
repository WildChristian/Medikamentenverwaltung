package de.hsb.app.mrv.model;

import java.io.Serializable;

import java.util.Date;
import java.util.HashSet;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.h2.command.dml.Set;

@NamedQuery(name = "SelectPersonen", query = "select p from Person p")
@Entity
public class Person  implements Serializable  {
	protected static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	protected UUID id;
	Anrede anrede;
	protected Date geburtsdatum;

	protected String vorname, nachname, emailadresse, passwort;
	
	
	
	protected Rolle rolle;
	

	//@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "person_id", nullable = false)
	protected HashSet<Anschrift> anschriften =  new HashSet<Anschrift>();
	@JoinColumn(name = "person_id", nullable = false)
	protected HashSet<Krankenkasse> krankenkasse =  new HashSet<Krankenkasse>();
	
	public Person(Anrede anrede, String vorname, String nachname, Date geburtsdatum, String emailadresse, String passwort,
			Rolle rolle) {
		super();
		this.anrede = anrede;
		this.vorname = vorname;
		this.nachname = nachname;
		this.geburtsdatum = geburtsdatum;
		this.emailadresse = emailadresse;
		this.passwort = passwort;
		this.rolle = rolle;
	}
	
	public Person() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}
	
	public Date getGeburtsdatum() {
		return geburtsdatum;
	}
	/*public LocalDate getGeburtsdatum1() {
		DateTimeFormatter f = DateTimeFormatter.ofPattern( "MM/dd/yyyy" ) ;
		LocalDate ld = LocalDate.parse( geburtsdatum , f ) ;
		return ld; 
	} */

	public void setGeburtsdatum(Date geburtsdatum) {
		this.geburtsdatum = geburtsdatum;
	}

	public HashSet<Anschrift> getAnschriften() {
		return anschriften;
	}

	public void setAnschriften(HashSet<Anschrift> anschriften) {
		this.anschriften = anschriften;
	}

	public String getEmailadresse() {
		return emailadresse;
	}

	public void setEmailadresse(String emailadresse) {
		this.emailadresse = emailadresse;
	}

	public String getPasswort() {
		return passwort;
	}

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

	public Rolle getRolle() {
		return rolle;
	}

	public void setRolle(Rolle rolle) {
		this.rolle = rolle;
	}

	public Anrede getAnrede() {
		return anrede;
	}

	public void setAnrede(Anrede anrede) {
		this.anrede = anrede;
	}

	public HashSet<Krankenkasse> getKrankenkasse() {
		return krankenkasse;
	}

	public void setKrankenkasse(HashSet<Krankenkasse> krankenkasse) {
		this.krankenkasse = krankenkasse;
	}
	
	
}
