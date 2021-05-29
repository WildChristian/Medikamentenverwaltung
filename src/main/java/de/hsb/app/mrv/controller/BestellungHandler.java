package de.hsb.app.mrv.controller;

import javax.annotation.PostConstruct;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;

import de.hsb.app.mrv.model.*;

import java.io.Serializable;
import java.util.*;

@Named
@ApplicationScoped
public class BestellungHandler implements Serializable {

	@PersistenceContext(name = "mrv-persistence-unit")
	private EntityManager em;
	@Resource
	private UserTransaction utx;

	private String message_Kunde = "";
	private String message_Medikament = "";

	private DataModel<Bestellung> bestellung;
	private DataModel<Medikament> medikamente;

	private Bestellung merkeBestellung = new Bestellung();
	private String merkeMedikament;
	private String merkePatient;
	private String merkeStatusRezept;

	private Person merkeApotheker;
	private static final long serialVersionUID = 1L;

	public DataModel<Bestellung> getBestellung() {
		return bestellung;
	}

	public void setBestellung(DataModel<Bestellung> bestellung) {
		this.bestellung = bestellung;
	}

	public Bestellung getMerkeBestellung() {
		return merkeBestellung;
	}

	public void setMerkeBestellung(Bestellung merkeBestellung) {
		this.merkeBestellung = merkeBestellung;
	}

	public String getMerkeMedikament() {
		return merkeMedikament;
	}

	public void setMerkeMedikament(String merkeMedikament) {
		this.merkeMedikament = merkeMedikament;
	}

	public String getMerkePatient() {
		return merkePatient;
	}

	public void setMerkePatient(String merkePatient) {
		this.merkePatient = merkePatient;
	}

	public String getMerkeStatusRezept() {
		return merkeStatusRezept;
	}

	public void setMerkeStatusRezept(String merkeStatusRezept) {
		this.merkeStatusRezept = merkeStatusRezept;
	}

	public DataModel<Medikament> getMedikamente() {
		return medikamente;
	}

	public void setMedikamente(DataModel<Medikament> medikamente) {
		this.medikamente = medikamente;
	}

	public String getMessage_Kunde() {
		return message_Kunde;
	}

	public void setMessage_Kunde(String message_Kunde) {
		this.message_Kunde = message_Kunde;
	}

	public String getMessage_Medikament() {
		return message_Medikament;
	}

	public void setMessage_Medikament(String message_Medikament) {
		this.message_Medikament = message_Medikament;
	}

	@PostConstruct
	public void init() {
		try {
			utx.begin();
			bestellung = new ListDataModel<>();
			bestellung.setWrappedData(filterAlleBestellungenActivrUser());
			utx.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}

	@Transactional
	public String delete() {
		merkeBestellung = bestellung.getRowData();
		merkeBestellung = em.merge(merkeBestellung);
		em.remove(merkeBestellung);
		bestellung.setWrappedData(em.createNamedQuery("SelectBestellung").getResultList());
		return "alleBestellungen";
	}

	public String neu() {
		 merkeMedikament ="";
		 merkePatient="";
		merkeStatusRezept="";
		merkeBestellung = new Bestellung();
		medikamente = new ListDataModel<>();
		return "neueBestellung";

	}

	public String neu_admin() {
		merkeMedikament ="";
		merkePatient="";
		merkeStatusRezept="";
		merkeBestellung = new Bestellung();
		medikamente = new ListDataModel<>();
		return "neueBestellung_admin";

	}
	@Transactional
	public String edit() {
		merkeBestellung = bestellung.getRowData();
		return "neueBestellung";
	}

	
	@Transactional
	public String edit_admin() {
		merkeBestellung = bestellung.getRowData();
		return "neueBestellung_admin";
	}
	
	@Transactional
	public String speichern_admin() {

		merkeBestellung.setApotheker(merkeApotheker);
		merkeBestellung = em.merge(merkeBestellung);
		em.persist(merkeBestellung);
		bestellung.setWrappedData(filterAlleBestellungenActivrUser());
		return "alleBestellungen_admin";
	}
	
	@Transactional
	public String speichern() {

		merkeBestellung.setApotheker(merkeApotheker);
		merkeBestellung = em.merge(merkeBestellung);
		em.persist(merkeBestellung);
		bestellung.setWrappedData(filterAlleBestellungenActivrUser());
		return "alleBestellungen";
	}

	@Transactional
	public void selectMedikament() {
		message_Medikament = "";
		try{
			if (isUUID(merkeMedikament)) {
				Medikament medikament = em.find(Medikament.class, UUID.fromString(merkeMedikament));
				if(pickRezept(medikament)) {
					merkeBestellung.setSumme(medikament.getPreis() + merkeBestellung.getSumme());
					merkeBestellung.getMedikamentbestellung().add(medikament);
					
					ArrayList<Medikament> tmp = new ArrayList<Medikament>();
					
					for(Medikament medikamente : merkeBestellung.getMedikamentbestellung()) {
						tmp.add(medikamente);
					}
					medikamente.setWrappedData(tmp);
				}else {
					message_Medikament = "Das Medikament konnte nicht hinzugefügt werden, da es entweder nicht vorhanden ist oder kein Rezept für diesen Patienten vorliegt";
				}
				
			} else {
				message_Medikament = "Der eingegebene Wert war keine UUID";
			}
		}catch(Exception e) {
			message_Medikament = "Die Medikamentenliste ist gerade Leer";
		}
		

	}

	@Transactional
	public void selectPatient() {
		message_Kunde = "";

		if (isUUID(merkePatient)) {
			Kunde patient = em.find(Kunde.class, UUID.fromString(merkePatient));
			merkeBestellung.setPatient(patient);
		} else {
			message_Kunde = "Der eingegebene Wert war keine UUID";
		}

	}

	@Transactional
	public boolean pickRezept(Medikament medikament) {
		List<Rezept> rezeptliste = em.createNamedQuery("SelectanRezepte").getResultList();
		if (medikament.getRezeptPflichtig().toString().equals("JA")) {
			for (Rezept rezept : rezeptliste) {
				if (merkeMedikament.equals(rezept.getMedikament().getId().toString())
						&& merkePatient.equals(rezept.getKunde().getId().toString())) {
					return true;
				}
			}
			return false;
		} else {
			return true;
		}
	}

	public List<Bestellung> filterAlleBestellungenActivrUser() {

		merkeApotheker = javax.enterprise.inject.spi.CDI.current().select(LoginHandler.class).get().getPerson_();

		List<Bestellung> tmp_BestellungListe = em.createNamedQuery("SelectBestellung").getResultList();
		List<Bestellung> tmp_BestellungListeforView = new ArrayList<Bestellung>();

		if (merkeApotheker != null) {
			if (merkeApotheker.getRolle().equals("ADMIN")) {
				return tmp_BestellungListe;
			} else {
				for (Bestellung bestellung : tmp_BestellungListe) {
					if (bestellung.getApotheker() != null) {
						if (bestellung.getApotheker().getId().toString().equals(merkeApotheker.getId().toString())) {
							tmp_BestellungListeforView.add(bestellung);
						}
					}
				}
			}

		}
		return tmp_BestellungListeforView;
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
