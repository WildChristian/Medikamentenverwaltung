package de.hsb.app.mrv.model;

import java.util.HashSet;

import javax.persistence.*;

import de.hsb.app.mrv.controller.KundenHandler;
import de.hsb.app.mrv.controller.LoginHandler;

@NamedQuery(name = "SelectKunden", query = "select k from Kunde k")
@Entity
public class Kunde extends Person{
	
	@Lob
	Person arzt;
	
	private static final long serialVersionUID = 1L;
	
	
	@JoinColumn(name = "person_id", nullable = false)
	protected HashSet<Rezept> Rezeptenliste = new HashSet<Rezept>();
	@JoinColumn(name = "person_id", nullable = false)
	protected HashSet<Krankenkasse> krankenkasse =  new HashSet<Krankenkasse>();
	
	public Kunde(String vorname, String nachname, String emailadresse, String passwort, Rolle rolle, Arzt arzt) {
		super();	
	}
	public Kunde() {
		// TODO Auto-generated constructor stub
		super();
	}

	public Anrede getAnrede() {
		return anrede;
	}

	public void setAnrede(Anrede anrede) {
		this.anrede = anrede;
	}

	public Person getArzt() {
		return arzt;
	}
	public void setArzt(Person arzt) {
		this.arzt = arzt;
	}
	public HashSet<Krankenkasse> getKrankenkasse() {
		return krankenkasse;
	}
	public void setKrankenkasse(HashSet<Krankenkasse> krankenkasse) {
		this.krankenkasse = krankenkasse;
	}
	
	
}