package es.engade.thearsmonsters.model.facades.monsterfacade;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.Transactional;

import es.engade.thearsmonsters.model.entities.common.KeyUtils;
import es.engade.thearsmonsters.model.entities.egg.MonsterEgg;
import es.engade.thearsmonsters.model.entities.egg.dao.MonsterEggDao;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.lair.dao.LairDao;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.dao.MonsterDao;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterAge;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterRace;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientMoneyException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientVitalSpaceException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.MaxEggsException;
import es.engade.thearsmonsters.model.facades.monsterfacade.exceptions.MonsterGrowException;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class MonsterFacadeImpl implements MonsterFacade {

    private MonsterDao monsterDao;
    private MonsterEggDao monsterEggDao;
    private LairDao lairDao;
    
    public void setMonsterDao(MonsterDao monsterDao) {
        this.monsterDao = monsterDao;
    }

    public void setMonsterEggDao(MonsterEggDao monsterEggDao) {
        this.monsterEggDao = monsterEggDao;
    }

    public void setLairDao(LairDao lairDao) {
        this.lairDao = lairDao;
    }

    @Transactional
    public Monster bornMonster(String eggId, String monsterName, Lair lair)
            throws InternalErrorException, InstanceNotFoundException,
            MonsterGrowException, InsuficientVitalSpaceException {

     // Buscar el huevo referenciado
        MonsterEgg egg = monsterEggDao.get(KeyUtils.fromString(eggId));
        if(!egg.isReadyToBorn()) throw new MonsterGrowException(null);
        
        // Comprueba que haya suficiente espacio vital en la guarida para la nueva criatura
        if(lair.getVitalSpaceFree() < egg.getRace().getVitalSpace()) {
            throw new InsuficientVitalSpaceException(egg.getRace().getVitalSpace(), lair.getVitalSpaceFree());
        }
        
        // Comprueba que el loginName es del dueño del huevo
        //TODO: ??
//        if(!loginName.equals(egg..getLoginName())) {
//            throw new InstanceNotFoundException(eggId, MonsterEggVO.class.getName());
//        }
            
        // Se crea el monstruo recién nacido
        //TODO: Como se crea esto??
        Monster littleMonster = new Monster(lair, egg.getRace(),
                monsterName, new Date(), new Date(), MonsterAge.Child);
        
        // Y se aumenta el espacio vital ocupado en la guarida
        lair.removeMonsterEgg(egg);
        lair.addMonster(littleMonster);
        
        // Hace los cambios persistentes en la BBDD
        // Guarda el monstruo, predefine sus nuevas tareas y elimina el huevo.
        lairDao.update(lair);
        
        return littleMonster;
    }

    @Transactional
    public MonsterEgg buyEgg(MonsterRace race, Lair lair)
            throws InternalErrorException, InsuficientMoneyException,
            MaxEggsException {

     // Comprueba que se sitio para guardar más huevos
        int MaxEggs = 10; // TODO: Tomar de config.
        int eggsCount = monsterEggDao.getNumberOfEggsByLair(lair);
        if(eggsCount >= MaxEggs) {
            throw new MaxEggsException();
        }
        
     // Resta la cantidad de dinero que vale el huevo
        int eggPrice = race.getBuyEggPrice();
        int money = lair.getMoney();
        if (eggPrice > money) {
            throw new InsuficientMoneyException(eggPrice, money);
        }
        lair.setMoney(money - eggPrice);
        
     // Almacena el huevo adquirido en la BBDD
        MonsterEgg egg = new MonsterEgg(
                lair, race, new Date());
        lair.addMonsterEgg(egg);
        
        // TODO Creación del huevo en cascada al actualizar???
        lairDao.update(lair);
        
        return egg;  
    }

    public List<MonsterEgg> findEggs(Lair lair) throws InternalErrorException {

        return  monsterEggDao.findEggsByLair(lair);
        
    }

    public List<Monster> findLairMonsters(Lair lair)
            throws InternalErrorException {

        return monsterDao.findMonstersByLair(lair);
        
    }

    public Monster findMonster(String monsterId) throws InternalErrorException,
            InstanceNotFoundException {

        return monsterDao.get(KeyUtils.fromString(monsterId));
        
    }

    @Transactional
    public void incubateEgg(String eggId, Lair lair)
            throws InternalErrorException, InstanceNotFoundException,
            InsuficientVitalSpaceException {

        MonsterEgg egg = monsterEggDao.get(KeyUtils.fromString(eggId));
        
        // Comprueba que coincide la guarida
        if (! egg.getLair().equals(lair))
            throw new InstanceNotFoundException(eggId, MonsterEgg.class.getName());
        
        // Comprueba que hay suficiente espacio vital
        int spaceNeeded = egg.getRace().getVitalSpace();
        int spaceAvaliable = 100; // HACK. Hay que calcularlo a partir de los monstruos
        if(spaceNeeded > spaceAvaliable) {
            throw new InsuficientVitalSpaceException(spaceNeeded, spaceAvaliable);
        }
        
        // Comprueba que el loginName es del dueño del huevo
//        if(!loginName.equals(egg.getLoginName())) {
//            throw new InstanceNotFoundException(eggId, MonsterEggVO.class.getName());
//        }
        
        // Pone el huevo a incubar fijando su fecha de nacimiento (date)
        egg.setBorningDate();
        monsterEggDao.update(egg);
        
        // A partir de ahora el huevo esperará en estado de "incubación"

    }

    public Monster metamorphosisToAdult(String monsterId, Lair lair)
            throws InternalErrorException, InstanceNotFoundException,
            MonsterGrowException {

        // Buscar el monstruo
        Monster m = monsterDao.get(KeyUtils.fromString(monsterId));
        if(!m.getAge().equals(MonsterAge.Child)) throw new MonsterGrowException(m.getId());
        
        // Comprueba que coincide la guarida
        if (! m.getLair().equals(lair))
            throw new InstanceNotFoundException(monsterId, Monster.class.getName());
        
        // Comprueba que el loginName es del dueño del monstruo
//        if(!loginName.equals(m.getLoginName())) {
//            throw new InstanceNotFoundException(monsterId, Monster.class.getName());
//        }
            
        // Se cambia la edad del monstruo y se fija una fecha para cuando salga del capuyo
        // TODO:
//        m.metamorphosisToAdult();
        
        // Hace los cambios persistentes en la BBDD
        monsterDao.update(m);
        
        return m;
        
    }

    @Transactional
    public Integer shellEgg(String eggId, Lair lair)
            throws InternalErrorException, InstanceNotFoundException {
        
        MonsterEgg egg = monsterEggDao.get(KeyUtils.fromString(eggId));
        int eggSalePrice = egg.getRace().getShellEggPrice();
        
     // Comprueba que coincide la guarida
        if (! egg.getLair().equals(lair))
            throw new InstanceNotFoundException(eggId, MonsterEgg.class.getName());
        
        // Se añade el precio de venta en la guarida y se guarda el resultado en la BBDD
        lair.setMoney(lair.getMoney() + eggSalePrice);
        lair.removeMonsterEgg(egg);
        
        lairDao.update(lair);
        
        // Y se elimina el huevo del usuario
        monsterEggDao.remove(egg.getId());
        
        return eggSalePrice;
        
    }

}
