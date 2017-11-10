package br.com.mde;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.contaazul.invoiceissuer.entities.ActionLog;

@Stateless
public class TestService {

	@PersistenceContext
	private EntityManager manager;

	public String getMessage() {
		if (manager == null)
			return "em null";
		ActionLog l = getLog();
		return l.getMessage().getText();
	}

	public ActionLog getLog() {

		return manager.find( ActionLog.class, 70113444l );

	}
}
