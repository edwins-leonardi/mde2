package buzz;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.contaazul.invoiceissuer.entities.ActionLog;

@Stateless
public class MyLogService {

	@PersistenceContext
	private EntityManager manager;

	public String getMessage() {
		if (manager != null)
			return getLog().getMessage().getText();

		return "Fuck No";
	}

	public ActionLog getLog() {

		return manager.find( ActionLog.class, 1l );

	}

}
