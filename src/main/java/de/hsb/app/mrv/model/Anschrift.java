package de.hsb.app.mrv.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@NamedQuery(name = "Selectanschrift", query = "select a from Anschrift a")
@Entity
public class Anschrift {

	@Id
	@GeneratedValue
	private int id;
	private String Straﬂe;
	private int Postleitzahl;
	private String Ort;
	
	public Anschrift(int id, String straﬂe, int postleitzahl, String ort) {
		super();
		this.id = id;
		Straﬂe = straﬂe;
		Postleitzahl = postleitzahl;
		Ort = ort;
	}
	
	public Anschrift() {
		super();

	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStraﬂe() {
		return Straﬂe;
	}
	public void setStraﬂe(String straﬂe) {
		Straﬂe = straﬂe;
	}
	public int getPostleitzahl() {
		return Postleitzahl;
	}
	public void setPostleitzahl(int postleitzahl) {
		Postleitzahl = postleitzahl;
	}
	public String getOrt() {
		return Ort;
	}
	public void setOrt(String ort) {
		Ort = ort;
	}
	
	

	
	
}
