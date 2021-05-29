package de.hsb.app.mrv.controller;
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
import java.io.Serializable;
@Named
@javax.enterprise.context.ApplicationScoped
public class AerzteHandler implements Serializable {
	private static final long serialVersionUID = 1L;
	private DataModel<Arzt> arzt;
	static public Arzt merkeArzt;
	@PersistenceContext(name = "mrv-persistence-unit")
	private EntityManager em;
	@Resource
	private UserTransaction utx;
	

	public DataModel<Arzt> getArzt() {
		return arzt;
	}
	public void setArzt(DataModel<Arzt> arzt) {
		this.arzt = arzt;
	}
	public Arzt getMerkeArzt() {
		return merkeArzt;
	}
	public void setMerkeArzt(Arzt merkeArzt) {
		this.merkeArzt = merkeArzt;
	}


	

	@PostConstruct
	public void init() {
		try {
			utx.begin();
			//Anrede[] tmp = getAnredeValues();
			
			arzt = new ListDataModel<>();
			arzt.setWrappedData(em.createNamedQuery("SelectAerzte").getResultList());
			utx.commit();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public Rolle[] getRolleValues() {
		return Rolle.values();
		}
	public String neu(){
		System.out.println("Ich bin die Methode neu()");
		merkeArzt = new Arzt();
		return "neueAerzte";
		
		
	}
	@Transactional
	public String edit() {
	merkeArzt = arzt.getRowData();
	return "neueAerzte";
	}
	@Transactional
	public String speichern(){
		
		merkeArzt = em.merge(merkeArzt);
		em.persist(merkeArzt);
		arzt.setWrappedData(em.createNamedQuery("SelectAerzte").getResultList());
		return "alleAerzte";
	}
	
	
	@Transactional
	public String delete() {
	merkeArzt = arzt.getRowData();
	merkeArzt = em.merge(merkeArzt);
	em.remove(merkeArzt);
	arzt.setWrappedData(em.createNamedQuery("SelectAerzte").getResultList());
	return "alleAerzte";
	}
}
