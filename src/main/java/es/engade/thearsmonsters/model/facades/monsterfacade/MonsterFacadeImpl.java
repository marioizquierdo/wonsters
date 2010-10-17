package es.engade.thearsmonsters.model.facades.monsterfacade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.egg.MonsterEgg;
import es.engade.thearsmonsters.model.entities.egg.dao.MonsterEggDao;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.dao.MonsterDao;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterAge;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterRace;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.user.dao.UserDao;
import es.engade.thearsmonsters.model.facades.common.ThearsmonstersFacade;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientMoneyException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientVitalSpaceException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.MaxEggsException;
import es.engade.thearsmonsters.model.facades.monsterfacade.exceptions.MonsterGrowException;
import es.engade.thearsmonsters.model.monsteraction.MonsterAction;
import es.engade.thearsmonsters.model.monsteraction.MonsterActionSuggestion;
import es.engade.thearsmonsters.model.monsteraction.MonsterActionToDo;
import es.engade.thearsmonsters.model.monsteraction.MonsterActionType;
import es.engade.thearsmonsters.model.monsteraction.MonsterActionsToDo;
import es.engade.thearsmonsters.model.util.GameConf;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class MonsterFacadeImpl extends ThearsmonstersFacade implements MonsterFacade {

    private MonsterDao monsterDao;
    private MonsterEggDao monsterEggDao;
    private UserDao userDao;
    
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    
    public void setMonsterDao(MonsterDao monsterDao) {
        this.monsterDao = monsterDao;
    }

    public void setMonsterEggDao(MonsterEggDao monsterEggDao) {
        this.monsterEggDao = monsterEggDao;
    }

    @Transactional
    public Monster bornMonster(Lair lair, String eggIdAsString, String monsterName)
            throws InternalErrorException, InstanceNotFoundException,
            MonsterGrowException, InsuficientVitalSpaceException {

        // Comprueba que se puede parsear la clave y la obtiene
    	Key eggId = getKeyFromString(eggIdAsString, MonsterEgg.class);
        
        // Obtiene el huevo referenciado, que debe estar en la guarida
        MonsterEgg egg = lair.getMonsterEgg(eggId);
        if(!egg.isReadyToBorn()) {
            throw new MonsterGrowException(eggId, MonsterAge.Child);
        }
        
        // Comprueba que haya suficiente espacio vital en la guarida para la nueva criatura
        if(lair.getVitalSpaceFree() < egg.getRace().getVitalSpace()) {
            throw new InsuficientVitalSpaceException(egg.getRace().getVitalSpace(), lair.getVitalSpaceFree());
        }
            
        // Traer la nueva criatura al mundo
        Monster baby = new Monster(lair, egg.getRace(), monsterName);
        lair.addMonster(baby);
        lair.removeMonsterEgg(egg);
        lair.refreshVitalSpaceOccupied();
        
        // Hace los cambios persistentes en la BBDD
        // Guarda el monstruo, predefine sus nuevas tareas y elimina el huevo.
        userDao.update(lair.getUser());
        monsterEggDao.remove(eggId);
        
        return baby;
    }
    
    @Transactional
    public void buryMonster(Lair lair, String monsterId) 
    	throws InstanceNotFoundException, InternalErrorException {

    	Monster monster = findMonster(lair, monsterId);
    	lair.removeMonster(monster);
    	userDao.update(lair.getUser());
    	monsterDao.remove(monster.getIdKey());
    }

    @Transactional
    public MonsterEgg buyEgg(Lair lair, MonsterRace race)
            throws InternalErrorException, InsuficientMoneyException,
            MaxEggsException {

        // Comprueba que se sitio para guardar más huevos
        int eggsCount = lair.getMonsterEggs().size(); // no hace falta comprobarlo en el datastore, se supone que lair está actualizada
        if(eggsCount >= GameConf.getMaxEggs()) throw new MaxEggsException();
        
        // Resta la cantidad de dinero que vale el huevo
        int eggPrice = race.getBuyEggPrice();
        int money = lair.getMoney();
        if (eggPrice > money) throw new InsuficientMoneyException(eggPrice, money);
        lair.setMoney(money - eggPrice);
        
        // Almacena el huevo adquirido en la BBDD
        MonsterEgg egg = new MonsterEgg(lair, race);
        lair.addMonsterEgg(egg);

//        monsterEggDao.save(egg);
        // Guarda los cambios en el datastore
        userDao.update(lair.getUser());
        return egg;  
    }

    public List<MonsterEgg> findEggs(Lair lair) throws InternalErrorException {
        // return  monsterEggDao.findEggsByLair(lair); // No hace falta buscar en el datastore porque se supone que lair está siempre actualizado
    	return lair.getMonsterEggs();
    }

    // Por defecto se ordenan por edad (de joven a viejo)
    public List<Monster> findLairMonsters(Lair lair) throws InternalErrorException {
    	List<Monster> orderedMonsters = new ArrayList<Monster>();
    	
    	orderedMonsters.addAll(findLairOrderedMonstersByAge(lair, MonsterAge.Child));
    	orderedMonsters.addAll(findLairOrderedMonstersByAge(lair, MonsterAge.Cocoon));
    	orderedMonsters.addAll(findLairOrderedMonstersByAge(lair, MonsterAge.Adult));
    	orderedMonsters.addAll(findLairOrderedMonstersByAge(lair, MonsterAge.Old));
    	orderedMonsters.addAll(findLairOrderedMonstersByAge(lair, MonsterAge.Dead));
    	
    	return orderedMonsters;
    }
    
    
    public List<Monster> findLairOrderedMonstersByAge(Lair lair, MonsterAge age) {
    	List<Monster> lairMonsters = lair.getMonsters();
    	List<Monster> lairMonstersByAge = new ArrayList<Monster>();
    	List<Monster> lairOrderedMonstersByAge = new ArrayList<Monster>();
    	for(Monster monster : lairMonsters) {
    		if(age.equals(monster.getAge())) {
    			lairMonstersByAge.add(monster);
    		}
    	}
    	lairOrderedMonstersByAge = orderMonsters(lairMonstersByAge);
    	return lairOrderedMonstersByAge;
    }
    
    public List<Monster> orderMonsters(List<Monster> monsters){
    	Monster monsterYounger = null,monsterToCompare = null;
    	List<Monster> orderedMonsters = new ArrayList<Monster>();

    	while(monsters.size() > orderedMonsters.size()) {
 
    		for(int j=0; j<monsters.size(); j++) {
	    		monsterYounger = monsters.get(j);
		    	if (!orderedMonsters.contains(monsterYounger)) break;
    		}
    		
    		for(int i = 0; i<monsters.size(); i++){
    			monsterToCompare = monsters.get(i);
    			/* Si has nacido despues eres mas joven */
    			if (monsterToCompare.getBorningDate().after(monsterYounger.getBorningDate())  
    					&& !orderedMonsters.contains(monsterToCompare))
    				monsterYounger = monsterToCompare;
    		}
    		orderedMonsters.add(monsterYounger);
    		
    	}
    	return orderedMonsters;
    }
    
    
    public Monster findMonster(Lair lair, String monsterIdAsString) throws InternalErrorException, InstanceNotFoundException {
        Key monsterId = getKeyFromString(monsterIdAsString, Monster.class);
        return monsterDao.get(monsterId);
    }

    @Transactional
    public void incubateEgg(Lair lair, String eggIdAsString)
            throws InternalErrorException, InstanceNotFoundException {
        
        Key eggId = getKeyFromString(eggIdAsString, MonsterEgg.class);
        MonsterEgg egg = lair.getMonsterEgg(eggId); // Se supone que la lair está actualizada
        
        // Pone el huevo a incubar fijando su fecha de nacimiento (date)
        egg.setBorningDate(); // A partir de ahora el huevo esperará en estado de "incubación"
        
        // En esta versión alfa, las razas no se desbloquean en las misiones, simplemente se desbloquean cuando el usuario
        // incuba el primer huevo de esa raza. Y por ahora las razas desbloqueadas se usan solo para el ranking.
        lair.unlockMonsterRace(egg.getRace());
        
        // guardar cambios
        userDao.update(lair.getUser());
//        monsterEggDao.update(egg);
        
        
        
    }

    public Monster metamorphosisToAdult(Lair lair, String monsterIdAsString)
            throws InternalErrorException, InstanceNotFoundException,
            MonsterGrowException {

    	Key monsterId = getKeyFromString(monsterIdAsString, Monster.class);
    	Monster m = lair.getMonster(monsterId);
    	
    	// Convertir a monstruo adulto
        m.metamorphosisToAdult();
        
        // Hace los cambios persistentes en la BBDD
        userDao.update(lair.getUser());
//        monsterDao.update(m);
        return m;
        
    }

    @Transactional
    public Integer sellEgg(Lair lair, String eggIdAsString)
            throws InternalErrorException, InstanceNotFoundException {
        
        Key eggId = getKeyFromString(eggIdAsString, MonsterEgg.class);
        MonsterEgg egg = lair.getMonsterEgg(eggId); // Se supone que la lair está actualizada
        int eggSalePrice = egg.getRace().getSellEggPrice();
        
        // Se elimina el huevo de la guarida y se suma el precio de venta
        lair.removeMonsterEgg(egg);
        lair.setMoney(lair.getMoney() + eggSalePrice);
        
        userDao.update(lair.getUser());
        
        // Y se elimina el huevo del usuario
        monsterEggDao.remove(egg.getIdKey());
        
        return eggSalePrice;
    }
    
    public List<MonsterActionSuggestion> suggestMonsterActions(Lair lair, String monsterId) throws InstanceNotFoundException {
    	
        Monster monster = lair.getMonster(getKeyFromString(monsterId, Monster.class));
        List<MonsterActionSuggestion> suggestedActions = new ArrayList<MonsterActionSuggestion>();
        
        
        // Para cada sala se comprueba si se puede ejecutar alguna acción en ella,
        // para ello hay que comprobar todas las combinaciones posibles entre rooms y monsterActions.
        for (Room room : lair.getRooms()) {
            for (MonsterActionType actionType : MonsterActionType.values()) {
                MonsterAction action = actionType.build(monster, room);
                if(action.isValid()) {
                    // solo se sugieren posibles acciones válidas
                    suggestedActions.add(action.getSuggestion());
                }
            }
        }
        
        return suggestedActions;
        
    }
    
    public boolean executeMonsterActions(Lair lair, MonsterActionsToDo actionsToDo) {
    	boolean successAll = true;
    	boolean success;
    	
    	for(MonsterActionToDo actionToDo : actionsToDo.getList()) {
        	
        	// Ejecuta cada action tantas veces como actionToDo.getTurnsToUse()
        	success = true;
        	int turn = 0;
        	while(success && turn < actionToDo.getTurnsToUse()) {
        		MonsterAction action = actionToDo.buildMonsterAction(lair);
        		if(action.execute()) {
        			actionsToDo.addAllNotificationMessages(action.getNotifications());
        			turn += 1;
        		} else {
        			actionsToDo.addAllErrorMessages(action.getErrors());
        			success = false;
        			break; // sale del bucle para cancelar la ejecucion de las acciones restantes.
        		}
        	}
    		if(!success) successAll = false;
    	}
    	
    	// Guardar cambios
    	userDao.update(lair.getUser());
    	return successAll;
    }

}
