package es.engade.thearsmonsters.model.entities.common.dao.pmfprovider;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public final class PMF {
    private static final PersistenceManagerFactory persistentManagerFactory =
        JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private PMF() {}

    public static PersistenceManagerFactory getPersistenceManagerFactory() {
        return persistentManagerFactory;
    }
}