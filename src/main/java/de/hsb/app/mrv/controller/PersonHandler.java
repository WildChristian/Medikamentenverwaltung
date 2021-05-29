package de.hsb.app.mrv.controller;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
public class PersonHandler implements Serializable {
	private static final long serialVersionUID = 1L;
	private DataModel<Person> person;
	private Person merkePerson = new Person();
	private Anschrift merkeanschrift = new Anschrift();

	private Krankenkasse merkeKrankenkasse = new Krankenkasse();
	

	public Krankenkasse getMerkeKrankenkasse() {
		return merkeKrankenkasse;
	}

	public void setMerkeKrankenkasse(Krankenkasse merkeKrankenkasse) {
		this.merkeKrankenkasse = merkeKrankenkasse;
	}


	String inputNachname = "";
	String input_doubleAccount = "";
	
	public String getInputNachname() {
		return inputNachname;
	}

	public void setInputNachname(String inputNachname) {
		this.inputNachname = inputNachname;
	}

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
	
	public Rolle[] getRolleAdmin() {
		Rolle [] tmp = {Rolle.values()[0]};
		return tmp;
		}

	@PostConstruct
	public void init() {
		try {
			utx.begin();
			
			person = new ListDataModel<>();
			person.setWrappedData(em.createNamedQuery("SelectPersonen").getResultList());
			utx.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DataModel<Person> getPerson() {
		return person;
	}

	public void setPerson(DataModel<Person> person) {
		this.person = person;
	}

	public String neu(){
		input_doubleAccount = "";
		System.out.println("Ich bin die Methode neu()");
		merkePerson = new Person();
		return "neuePerson";
		
	}

	public Person getMerkePerson() {
		return merkePerson;
	}

	public void setMerkePerson(Person merkePerson) {
		this.merkePerson = merkePerson;
	}
	public Anschrift getMerkeanschrift() {
		return merkeanschrift;
	}

	public void setMerkeanschrift(Anschrift merkeanschrift) {
		this.merkeanschrift = merkeanschrift;
	}
	
	public String getInput_doubleAccount() {
		return input_doubleAccount;
	}

	public void setInput_doubleAccount(String input_doubleAccount) {
		this.input_doubleAccount = input_doubleAccount;
	}

	
	
	@Transactional
	public String speichern_Edit(){
	
			merkePerson = em.merge(merkePerson);
			em.persist(merkePerson);
			person.setWrappedData(em.createNamedQuery("SelectPersonen").getResultList());
			return "allePersonen";
		
		
	}
	@Transactional
	public String speichern(){
		if(checkDoubleRegister()) {
			input_doubleAccount = "Leider ist die E-Mail bereits vergeben";
			return null;
		}else {
			merkePerson = em.merge(merkePerson);
			em.persist(merkePerson);
			person.setWrappedData(em.createNamedQuery("SelectPersonen").getResultList());
			return "allePersonen";
		}
		
	}
	
	@Transactional
	public String delete() {
		merkePerson = person.getRowData();
		merkePerson = em.merge(merkePerson);
		person.setWrappedData(em.createNamedQuery("SelectPersonen").getResultList());
		return "allePersonen";
	}

	@Transactional
	public String edit() {
		merkePerson = person.getRowData();
		if(merkePerson.getRolle().toString().equals("ADMIN")) {
			return "editADMIN";
		}
		return "person_bearbeiten";
	}
	
	@Transactional
	public String editanschrift() {
		merkePerson = person.getRowData();
		return "anschrift";
	}
	
	@Transactional
	public void neuanschrift() {
		merkeanschrift = new Anschrift();
	}

	@Transactional
	public void speichernanschrift() {
		merkePerson.getAnschriften().add(merkeanschrift);
	}
	/*++++++++++++++++++++++++++++++++++++++++*/
		@Transactional
		public String editKrankenkasse() {
			merkePerson = person.getRowData();
			return "krankenkasse";
		}
		
		@Transactional
		public void neukrankenkasse() {
			merkeKrankenkasse = new Krankenkasse();
		}

		@Transactional
		public void speichernKrankenkasse() {
			merkePerson.getKrankenkasse().add(merkeKrankenkasse);
		}
	
	public List<Person> filterAllePersonen(){
		List<Person> tmp_PersonListe = em.createNamedQuery("SelectPerson").getResultList();
		//List<Person> tmp_PersonListeforView = new ArrayList<Person>();
		
		return tmp_PersonListe;
	}
	
	public List<Person> inputvonFilter(){
		List<Person> filtertlist = filterAllePersonen();
		List<Person> tmplist = new ArrayList<Person>();
		
		System.out.println("Aufruf der Filermethode");
		for(Person person : filtertlist) {
			if(person.getNachname().equals(inputNachname)) {
				tmplist.add(person);
			}
		}
		
		return tmplist;
	}
	
	public void resetfilter(){
		inputNachname = "";
	}
		
	public boolean checkDoubleRegister() {
		List<Person> personenListe = em.createNamedQuery("SelectPersonen").getResultList();
		
		for(Person person : personenListe) {
			if(person.getEmailadresse().toString().equals(merkePerson.getEmailadresse())) {
				return true;
			}
		}
		return false;
	}
	
 	
}