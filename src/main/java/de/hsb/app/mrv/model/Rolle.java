package de.hsb.app.mrv.model;

public enum Rolle {
	ADMIN("Admin"), PATIENT("Patient"), ARZT("Arzt"), APOTHEKER("Apotheker");
	private final String label;
	
	private Rolle(String label) {
	this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
}
