package de.hsb.app.mrv.controller;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;

import de.hsb.app.mrv.model.Arzt;
import de.hsb.app.mrv.model.Bestellung;
import de.hsb.app.mrv.model.Kunde;
import de.hsb.app.mrv.model.Medikament;
import de.hsb.app.mrv.model.Person;
import de.hsb.app.mrv.model.Rezept;

import java.awt.Component;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@javax.enterprise.context.ApplicationScoped
@Named
public class RezeptenHandler implements Serializable {

	private static final long serialVersionUID = 1L;

	private DataModel<Rezept> rezept;

	private Rezept merkeRezept = new Rezept();

	private String merkeKunde;
	private String merkeArzt;
	private String merkeMedikament;
	private String message = "";

	@PersistenceContext(name = "mrv-persistence-unit")
	private EntityManager em;
	@Resource
	private UserTransaction utx;

	@PostConstruct
	public void init() {
		try {
			utx.begin();
			rezept = new ListDataModel<>();
			rezept.setWrappedData(em.createNamedQuery("SelectanRezepte").getResultList());
			utx.commit();

		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}

	public DataModel<Rezept> getRezept() {
		return rezept;
	}

	public void setRezept(DataModel<Rezept> rezept) {
		this.rezept = rezept;
	}

	public String neueRezepte() {
		merkeRezept = new Rezept();
		merkeRezept.setArzt(javax.enterprise.inject.spi.CDI.current().select(LoginHandler.class).get().getPerson_());
		return "rezept.xhtml";
	}

	
	public String neueRezepte_admin() {
		merkeRezept = new Rezept();
		merkeRezept.setArzt(javax.enterprise.inject.spi.CDI.current().select(LoginHandler.class).get().getPerson_());
		return "rezept_admin.xhtml";
	}

	public Rezept getMerkeRezept() {
		return merkeRezept;
	}

	public void setMerkeRezept(Rezept merkeRezept) {
		this.merkeRezept = merkeRezept;
	}

	public String getMerkeKunde() {
		return merkeKunde;
	}

	public void setMerkeKunde(String merkeKunde) {
		this.merkeKunde = merkeKunde;
	}

	public String getMerkeArzt() {
		return merkeArzt;
	}

	public void setMerkeArzt(String merkeArzt) {
		this.merkeArzt = merkeArzt;
	}

	public String getMerkeMedikament() {
		return merkeMedikament;
	}

	public void setMerkeMedikament(String merkeMedikament) {
		this.merkeMedikament = merkeMedikament;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Transactional
	public String speichern() {
		merkeRezept = em.merge(merkeRezept);
		em.persist(merkeRezept);
		rezept.setWrappedData(em.createNamedQuery("SelectanRezepte").getResultList());
		return "alleRezepte";
	}

	@Transactional
	public String delete() {
		merkeRezept = rezept.getRowData();
		merkeRezept = em.merge(merkeRezept);
		em.remove(merkeRezept);
		rezept.setWrappedData(em.createNamedQuery("SelectanRezepte").getResultList());
		return "alleRezepte";
	}

	@Transactional
	public String edit() {
		merkeRezept = rezept.getRowData();
		return "rezept";

	}

	@Transactional
	public void selectKunde() {

		Kunde kunde = em.find(Kunde.class, UUID.fromString(merkeKunde));
		merkeRezept.setKunde(kunde);

	}

	@Transactional
	public void selectMedikament() {
		if (isUUID(merkeMedikament)) {
			Medikament medikament = em.find(Medikament.class, UUID.fromString(merkeMedikament));
			merkeRezept.setMedikament(medikament);
		} else {

		}

	}

	@Transactional
	public String koordinatorMethodeSpeichern() {
		message = "";

		if (isUUID(merkeKunde) && isUUID(merkeMedikament)) {
			selectKunde();
			selectMedikament();
			speichern();

			return "alleRezepte";
		} else {
			message = "Es wurde keine korrekte UUID eingetragen";
			return "rezept";
		}

	}
	
	@Transactional
	public String koordinatorMethodeSpeichern_admin() {
		message = "";

		if (isUUID(merkeKunde) && isUUID(merkeMedikament)) {
			selectKunde();
			selectMedikament();
			speichern();

			return "alleRezepte_Admin";
		} else {
			message = "Es wurde keine korrekte UUID eingetragen";
			return "rezept_admin";
		}

	}

	public boolean isUUID(String string) {
		try {
			UUID.fromString(string);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

}
