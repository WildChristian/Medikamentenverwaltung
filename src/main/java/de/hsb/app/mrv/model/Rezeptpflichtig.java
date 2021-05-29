package de.hsb.app.mrv.model;
public enum Rezeptpflichtig {
	 JA ("Ja"), NEIN ("nein");
	private final String label;
	private Rezeptpflichtig(String label) {
	this.label = label;
	}
	public String getLabel() {
		return label;
	}
}
