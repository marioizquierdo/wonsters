package es.engade.thearsmonsters.model.entities.common.dao.pmfprovider;

import javax.jdo.PersistenceManagerFactory;

public class PMFProviderImpl implements PMFProvider {

    private PersistenceManagerFactory pmf;
    
    public void setPmf(PersistenceManagerFactory pmf) {
        this.pmf = pmf;
    }

    public PersistenceManagerFactory getPmf() {
        // TODO Auto-generated method stub
        return pmf;
    }

}
