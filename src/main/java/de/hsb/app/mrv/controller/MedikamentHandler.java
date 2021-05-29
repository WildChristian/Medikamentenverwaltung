package de.hsb.app.mrv.controller;

import java.io.Serializable;
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
public class MedikamentHandler implements Serializable {
	private static final long serialVersionUID = 1L;
	private DataModel<Medikament> medikament;

	private Medikament merkeMedikament = new Medikament();

	public Rezeptpflichtig[] getRezeptpflichtigValues() {
		return Rezeptpflichtig.values();
	}

	@PersistenceContext(name = "mrv-persistence-unit")
	private EntityManager em;
	@Resource
	private UserTransaction utx;

	@PostConstruct
	public void init() {
		try {
			utx.begin();

			medikament = new ListDataModel<>();
			medikament.setWrappedData(em.createNamedQuery("SelectMedikament").getResultList());
			utx.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public DataModel<Medikament> getMedikament() {
		return medikament;
	}

	public void setMedikament(DataModel<Medikament> medikament) {
		this.medikament = medikament;
	}

	public String neu() {
		merkeMedikament = new Medikament();
		return "neuerMedikament";

	}

	public Medikament getMerkeMedikament() {
		return merkeMedikament;
	}

	public void setMerkeMedikament(Medikament merkeMedikament) {
		this.merkeMedikament = merkeMedikament;
	}

	@Transactional
	public String speichern() {
		merkeMedikament = em.merge(merkeMedikament);
		em.persist(merkeMedikament);
		medikament.setWrappedData(em.createNamedQuery("SelectMedikament").getResultList());
		return "alleMedikamente";
	}
	
	@Transactional
	public String abbrechen() {
		 return "alleMedikamente";
	}

	@Transactional
	public String delete() {
		merkeMedikament = medikament.getRowData();
		merkeMedikament = em.merge(merkeMedikament);
		em.remove(merkeMedikament);
		medikament.setWrappedData(em.createNamedQuery("SelectMedikament").getResultList());
		return "alleMedikamente";
	}

	@Transactional
	public String edit() {
		merkeMedikament = medikament.getRowData();
		return "neuerMedikament";
	}

}
