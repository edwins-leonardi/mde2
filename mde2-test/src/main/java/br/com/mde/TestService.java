package br.com.mde;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.gize.swarm.model.Customer;

@Stateless
public class TestService {

	@PersistenceContext
	private EntityManager manager;

	public String getMessage() {
		if (manager == null)
			return "em null";
		Customer l = getLog();
		return l.getName();
	}

	public Customer getLog() {

		return manager.find( Customer.class, 1l );

	}
}
