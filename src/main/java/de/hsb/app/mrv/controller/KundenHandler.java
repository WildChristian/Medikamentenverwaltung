package de.hsb.app.mrv.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.annotation.ManagedProperty;
import javax.faces.context.FacesContext;
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

@javax.enterprise.context.ApplicationScoped
@Named
public class KundenHandler implements Serializable {
	private static final long serialVersionUID = 1L;
	private DataModel<Kunde> kunde;
	private Kunde merkeKunde = new Kunde();
	private Anschrift merkeanschrift = new Anschrift();
	private Krankenkasse merkeKrankenkasse = new Krankenkasse();

	String input_doubleAccount = "";

	public Krankenkasse getMerkeKrankenkasse() {
		return merkeKrankenkasse;
	}

	public void setMerkeKrankenkasse(Krankenkasse merkeKrankenkasse) {
		this.merkeKrankenkasse = merkeKrankenkasse;
	}

	public Person getMerkeArzt() {
		return merkeArzt;
	}

	public void setMerkeArzt(Person merkeArzt) {
		this.merkeArzt = merkeArzt;
	}

	private boolean noAdmin = true;
	private boolean admin1 = true;
	String inputNachname = "";

	Person merkeArzt;

	// Verbindung H2 Datenbank
	@PersistenceContext(name = "mrv-persistence-unit")
	private EntityManager em;
	@Resource
	private UserTransaction utx;

	public Anrede[] getAnredeValues() {
		return Anrede.values();
	}

	public Rolle[] getRolleValues() {
		return Rolle.values();
	}

	public Rolle[] getRollePatient() {
		Rolle[] tmp = { Rolle.values()[1] };
		return tmp;
	}

	//
	public Rolle[] AdminRolle() {
		Rolle[] tmp = { Rolle.values()[0] };
		return tmp;
	}

//
	public Rolle[] getBestimmteRolle() {
		Rolle[] tmp = { Rolle.values()[2], Rolle.values()[3] };
		return tmp;
	}

	@PostConstruct
	public void init() {
		try {
			utx.begin();
			kunde = new ListDataModel<>();
			if (inputNachname.equals("")) {
				kunde.setWrappedData(filterAlleKunden());
			} else {
				kunde.setWrappedData(inputvonFilter());
			}

			utx.commit();

		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public DataModel<Kunde> getKunde() {
		return kunde;
	}

	public void setKunde(DataModel<Kunde> kunde) {
		this.kunde = kunde;
	}

	public String neu() {
		input_doubleAccount = "";
		merkeKunde = new Kunde();
		return "neuerKunde";

	}

	public Kunde getMerkeKunde() {
		return merkeKunde;
	}

	public void setMerkeKunde(Kunde merkeKunde) {
		this.merkeKunde = merkeKunde;
	}

	public Anschrift getMerkeanschrift() {
		return merkeanschrift;
	}

	public void setMerkeanschrift(Anschrift merkeanschrift) {
		this.merkeanschrift = merkeanschrift;
	}

	public String getInputNachname() {
		return inputNachname;
	}

	public void setInputNachname(String inputNachname) {
		this.inputNachname = inputNachname;
	}

	public String getInput_doubleAccount() {
		return input_doubleAccount;
	}

	public void setInput_doubleAccount(String input_doubleAccount) {
		this.input_doubleAccount = input_doubleAccount;
	}

	@Transactional
	public String speichern_Edit() {

		merkeKunde = em.merge(merkeKunde);
		em.persist(merkeKunde);
		kunde.setWrappedData(em.createNamedQuery("SelectPersonen").getResultList());
		return "alleKunden";

	}

	@Transactional
	public String speichern() {

		if (checkDoubleRegister()) {
			input_doubleAccount = "Leider ist die E-Mail bereits vergeben";
			return null;
		} else {
			noAdmin = false;
			admin1 = false;
			merkeArzt = javax.enterprise.inject.spi.CDI.current().select(LoginHandler.class).get().getPerson_();
			merkeKunde.setArzt(merkeArzt);
			merkeKunde = em.merge(merkeKunde);
			em.persist(merkeKunde);
			kunde.setWrappedData(filterAlleKunden());

			return "alleKunden";
		}

	}

	@Transactional
	public String delete() {
		merkeKunde = kunde.getRowData();
		merkeKunde = em.merge(merkeKunde);
		em.remove(merkeKunde);
		kunde.setWrappedData(filterAlleKunden());
		return "alleKunden";
	}

	@Transactional
	public String edit() {
		merkeKunde = kunde.getRowData();
		return "kunde_bearbeiten";
	}

	/*-------------------------------------Anschrift-----------------------------------------*/
	@Transactional
	public String editanschrift() {
		merkeKunde = kunde.getRowData();
		return "anschrift";
	}

	@Transactional
	public void neuanschrift() {
		merkeanschrift = new Anschrift();
	}

	@Transactional
	public void speichernanschrift() {
		merkeKunde.getAnschriften().add(merkeanschrift);
	}

	/*-------------------------------------Krankenkasse-----------------------------------------*/

	@Transactional
	public String editKrankenkasse() {
		merkeKunde = kunde.getRowData();
		return "krankenkasse";
	}

	@Transactional
	public void neueKrankenkasse() {
		merkeKrankenkasse = new Krankenkasse();
	}

	@Transactional
	public void speicherKrankenkasse() {
		merkeKunde.getKrankenkasse().add(merkeKrankenkasse);
	}

	public List<Kunde> filterAlleKunden() {
		List<Kunde> tmp_KundenListe = em.createNamedQuery("SelectKunden").getResultList();
		List<Kunde> tmp_KundenListeforView = new ArrayList<Kunde>();

		if (merkeArzt != null) {
			if (merkeKunde.getRolle().toString().equals("ADMIN")
					|| merkeKunde.getRolle().toString().equals("APOTHEKER")) {
				return tmp_KundenListe;
			} else {
				for (Kunde kunde : tmp_KundenListe) {
					if (kunde.getArzt() != null) {
						if (kunde.getArzt().getId().toString().equals(merkeArzt.getId().toString())) {

							tmp_KundenListeforView.add(kunde);
						}
					}
				}
			}

		}
		return tmp_KundenListeforView;
	}

	public String registrieren() {
		merkeKunde = new Kunde();
		return "register";

	}

	public boolean isNoAdmin() {
		return noAdmin;
	}

	public void setNoAdmin(boolean noAdmin) {
		this.noAdmin = noAdmin;
	}

	public boolean isAdmin() {
		return admin1;
	}

	public void setAdmin(boolean noAdmin) {
		this.noAdmin = admin1;
	}

	public List<Kunde> inputvonFilter() {
		List<Kunde> filtertlist = filterAlleKunden();
		List<Kunde> tmplist = new ArrayList<Kunde>();

		System.out.println("huhu");
		for (Kunde kunde : filtertlist) {
			if (kunde.getNachname().equals(inputNachname)) {
				tmplist.add(kunde);
			}
		}

		return tmplist;
	}

	public void noAdmin() throws IOException {
		if (noAdmin) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().redirect("adminRegister.xhtml");
		} else {

		}
	}

	public void admin1() throws IOException {
		if (admin1) {

		} else {
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().redirect("keinzugriff.xhtml");
		}
	}

	public void resetfilter() {
		inputNachname = "";
	}

	public boolean checkDoubleRegister() {
		List<Person> personenListe = em.createNamedQuery("SelectPersonen").getResultList();

		for (Person person : personenListe) {
			if (person.getEmailadresse().toString().equals(merkeKunde.getEmailadresse())) {
				return true;
			}
		}
		return false;
	}

	@Transactional
	public String init_apotheke() {
		
		List<Kunde> tmp = em.createNamedQuery("SelectKunden").getResultList();
		List<Kunde> tmp_forwrap = new ArrayList<Kunde>();
		for (Kunde kunde : tmp) {
			if (kunde.getRolle().toString().equals("PATIENT")) {
				tmp_forwrap.add(kunde);
			}
		}
		System.out.println("huhu");
		kunde.setWrappedData(tmp_forwrap);

		return "alleKunden_apotheke";
	}
}