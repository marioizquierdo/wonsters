package es.engade.thearsmonsters.model.entities.common.dao.pmfprovider;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public class PMFProviderGae implements PMFProvider {

    private static final PersistenceManagerFactory persistentManagerFactory =
        JDOHelper.getPersistenceManagerFactory("transactions-optional");

    
    public PersistenceManagerFactory getPmf() {
        return persistentManagerFactory;
    }

}
