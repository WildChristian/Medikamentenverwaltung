package de.hsb.app.mrv.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;

@NamedQuery(name = "SelectApotheker", query = "select ap from Arzt ap")
@Entity
public class Apotheker extends Person{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Apotheker(String vorname, String nachname, String emailadresse, String passwort, Rolle rolle) {
		super();
	}
	public Apotheker() {
		super();
		// TODO Auto-generated constructor stub
	}

}
