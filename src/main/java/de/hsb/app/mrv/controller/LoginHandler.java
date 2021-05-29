package de.hsb.app.mrv.controller;

import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.annotation.ManagedProperty;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import de.hsb.app.mrv.model.*;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.GregorianCalendar;
import java.util.List;

@Named
@SessionScoped
public class LoginHandler implements Serializable {

	private Person person_;	
	private String emailadresse = "";
	private String passwort = "";
	private boolean check = false;
	private boolean arzt = false;
	private boolean admin = false;
	private boolean apotheker = false;
	

	@PersistenceContext(name = "mrv-persistence-unit")
	private EntityManager em;
	@Resource
	private UserTransaction utx;

	public String getEmailadresse() {
		return emailadresse;
	}

	public void setEmailadresse(String emailadresse) {
		this.emailadresse = emailadresse;
	}

	public String getPasswort() {
		return passwort;
	}

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}
	
	
	

	public Person getPerson_() {
		return person_;
	}

	public void setPerson_(Person person_) {
		this.person_ = person_;
	}

	private static final long serialVersionUID = 1L;
	public Anrede[] getAnredeValues() {
		return Anrede.values();
		}
	
	public Rolle[] getRolleValues() {
		return Rolle.values();
		}
	Anrede[] tmp = getAnredeValues();
	Rolle[] rolle = getRolleValues();
	public String login() {
		try {
			utx.begin();
			//em.persist(new Person(tmp[0],  "Admin", "Mustermann",  "01-01-1990", "admin@gmail.com", "asdqwe123", rolle[0]));
			List<Person> resultList = em.createNamedQuery("SelectPersonen").getResultList();
			utx.commit();

			for (Person person : resultList) {
				if (person.getEmailadresse().equals(emailadresse) && person.getPasswort().equals(passwort)) {
					check = true;
					if (person.getRolle().toString().equals("ARZT")) {
						person_  =   person;
						arzt=true;
						return "dashboard_arzt";
					}else if(person.getRolle().toString().equals("APOTHEKER")) {
						person_ = person;
						apotheker=true;
						return "dashboard_apotheke";
					}else if(person.getRolle().toString().equals("ADMIN")) {
						person_ = person;
						admin=true;
						return "Dashboard_Admin";
					}
				}
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
		return "login";
	}
	
	/*das ist nicht wirklich nötig
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	public void zugriff_apotheker() throws IOException {
		if(apotheker) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().redirect("keinzugriff.xhtml");
		}
	}
	
	public void zugriff_arzt() throws IOException {
		if(arzt) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().redirect("keinzugriff.xhtml");
		}
	}
	public void checkLoggedIn() throws IOException {
		if (check) {

		} else {
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().redirect("login.xhtml");
		}
	}

	
	public String logout() {
		check = false;
		person_ = null;
		
		arzt = false;
		admin = false;
		apotheker = false;
		
		return "login";
	}
}
