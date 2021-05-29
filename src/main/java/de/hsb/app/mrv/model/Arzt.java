package de.hsb.app.mrv.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;


@NamedQuery(name = "SelectAerzte", query = "select a from Arzt a")
@Entity
public class Arzt extends Person {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String  praxis;
	public Arzt(String vorname, String nachname, String praxis, String emailadresse, String passwort, Rolle rolle) {
		super();
		this.praxis = praxis;
	}
	public Arzt() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getPraxis() {
		return praxis;
	}
	public void setPraxis(String praxis) {
		this.praxis = praxis;
	}
	
	
}
