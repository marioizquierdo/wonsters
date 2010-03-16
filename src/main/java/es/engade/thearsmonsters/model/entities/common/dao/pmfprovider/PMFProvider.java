package es.engade.thearsmonsters.model.entities.common.dao.pmfprovider;

import javax.jdo.PersistenceManagerFactory;

public interface PMFProvider {

    public PersistenceManagerFactory getPmf();
        
}
