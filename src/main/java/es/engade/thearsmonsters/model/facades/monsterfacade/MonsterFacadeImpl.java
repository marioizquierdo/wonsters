package es.engade.thearsmonsters.model.facades.monsterfacade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.common.KeyUtils;
import es.engade.thearsmonsters.model.entities.egg.MonsterEgg;
import es.engade.thearsmonsters.model.entities.egg.dao.MonsterEggDao;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.lair.dao.LairDao;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.dao.MonsterDao;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterAge;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterRace;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.dao.RoomDao;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientMoneyException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientVitalSpaceException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.MaxEggsException;
import es.engade.thearsmonsters.model.facades.monsterfacade.exceptions.MonsterGrowException;
import es.engade.thearsmonsters.model.monsteraction.MonsterAction;
import es.engade.thearsmonsters.model.monsteraction.MonsterActionSuggestion;
import es.engade.thearsmonsters.model.monsteraction.MonsterActionType;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class MonsterFacadeImpl implements MonsterFacade {

    private MonsterDao monsterDao;
    private MonsterEggDao monsterEggDao;
    private RoomDao roomDao;
    private LairDao lairDao;
    
    public void setMonsterDao(MonsterDao monsterDao) {
        this.monsterDao = monsterDao;
    }

    public void setMonsterEggDao(MonsterEggDao monsterEggDao) {
        this.monsterEggDao = monsterEggDao;
    }

    public void setRoomDao(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public void setLairDao(LairDao lairDao) {
        this.lairDao = lairDao;
    }

    @Transactional
    public Monster bornMonster(String eggId, String monsterName, Lair lair)
            throws InternalErrorException, InstanceNotFoundException,
            MonsterGrowException, InsuficientVitalSpaceException {

        // Comprueba que se puede parsear la clave
        if (!KeyUtils.isParseable(eggId))
            throw new InstanceNotFoundException(eggId, MonsterEgg.class.getName());
        
        // Buscar el huevo referenciado
        MonsterEgg egg = monsterEggDao.get(KeyUtils.fromString(eggId));
        if(!egg.isReadyToBorn()) throw new MonsterGrowException(null);
        
        // Comprueba que haya suficiente espacio vital en la guarida para la nueva criatura
        if(lair.getVitalSpaceFree() < egg.getRace().getVitalSpace()) {
            throw new InsuficientVitalSpaceException(egg.getRace().getVitalSpace(), lair.getVitalSpaceFree());
        }
        
        // Comprueba que coincide la guarida
        if (! lair.getMonsterEggs().contains(egg))
            throw new InstanceNotFoundException(KeyUtils.fromString(eggId), MonsterEgg.class.getName());
            
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

        // Comprueba que se puede parsear la clave
        if (!KeyUtils.isParseable(monsterId))
            throw new InstanceNotFoundException(monsterId, Monster.class.getName());
        
        return monsterDao.get(KeyUtils.fromString(monsterId));
        
    }

    @Transactional
    public void incubateEgg(String eggId, Lair lair)
            throws InternalErrorException, InstanceNotFoundException,
            InsuficientVitalSpaceException {

        // Comprueba que se puede parsear la clave
        if (!KeyUtils.isParseable(eggId))
            throw new InstanceNotFoundException(eggId, MonsterEgg.class.getName());
        
        MonsterEgg egg = monsterEggDao.get(KeyUtils.fromString(eggId));
        
        // Comprueba que coincide la guarida
        if (! lair.getMonsterEggs().contains(egg))
            throw new InstanceNotFoundException(KeyUtils.fromString(eggId), MonsterEgg.class.getName());
        
        // Comprueba que hay suficiente espacio vital
        int spaceNeeded = egg.getRace().getVitalSpace();
        int spaceAvaliable = 100; // HACK. Hay que calcularlo a partir de los monstruos
        if(spaceNeeded > spaceAvaliable) {
            throw new InsuficientVitalSpaceException(spaceNeeded, spaceAvaliable);
        }
        
        // Pone el huevo a incubar fijando su fecha de nacimiento (date)
        egg.setBorningDate();
        monsterEggDao.update(egg);
        
        // A partir de ahora el huevo esperará en estado de "incubación"

    }

    public Monster metamorphosisToAdult(String monsterId, Lair lair)
            throws InternalErrorException, InstanceNotFoundException,
            MonsterGrowException {

        // Comprueba que se puede parsear la clave
        if (!KeyUtils.isParseable(monsterId))
            throw new InstanceNotFoundException(monsterId, Monster.class.getName());

        // Buscar el monstruo
        Monster m = monsterDao.get(KeyUtils.fromString(monsterId));
        if(!m.getAge().equals(MonsterAge.Child)) throw new MonsterGrowException(m.getId());
        
        // Comprueba que coincide la guarida
        if (! lair.getMonsters().contains(m))
            throw new InstanceNotFoundException(KeyUtils.fromString(monsterId), Monster.class.getName());
            
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
        
        // Comprueba que se puede parsear la clave
        if (!KeyUtils.isParseable(eggId))
            throw new InstanceNotFoundException(eggId, MonsterEgg.class.getName());
        
        MonsterEgg egg = monsterEggDao.get(KeyUtils.fromString(eggId));
        int eggSalePrice = egg.getRace().getShellEggPrice();
        
        // Comprueba que coincide la guarida
        if (! lair.getMonsterEggs().contains(egg))
            throw new InstanceNotFoundException(KeyUtils.fromString(eggId), MonsterEgg.class.getName());
        
        // Se añade el precio de venta en la guarida y se guarda el resultado en la BBDD
        lair.setMoney(lair.getMoney() + eggSalePrice);
        lair.removeMonsterEgg(egg);
        
        lairDao.update(lair);
        
        // Y se elimina el huevo del usuario
        monsterEggDao.remove(egg.getId());
        
        return eggSalePrice;
    }
    
    public List<MonsterActionSuggestion> suggestMonsterActions(Key monsterId) throws InstanceNotFoundException{ 	
    	return suggestMonsterActions(monsterDao.get(monsterId));
    }
    
    public List<MonsterActionSuggestion> suggestMonsterActions(Monster monster) throws InstanceNotFoundException{ 	

    	Lair lair = monster.getLair();
    	List<MonsterActionSuggestion> suggestedActions = new ArrayList<MonsterActionSuggestion>();
    	
    	
    	// Para cada sala se comprueba si se puede ejecutar alguna acción en ella,
    	// para ello hay que comprobar todas las combinaciones posibles room - monsterAction.
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
    
    @Transactional
    public boolean executeMonsterAction(MonsterActionType actionType, Key monsterId, RoomType roomType) throws InstanceNotFoundException {
    	
    	/* Al estilo de findMonster, a lo mejor se puede quitar el argumento
    	 * Key del metodo y enviar directamente el monster en ese caso borrar la linea siguiente
    	 */
    	Monster monster = monsterDao.get(monsterId);
    	Lair lair = monster.getLair();
    	Room room = lair.getRoom(roomType);
    	
    	
    	// Ejecuta la acción
    	boolean success = actionType.execute(monster, room);
    	
    	if(success) { // guarda los resultados
    		monsterDao.save(monster);
    		lairDao.save(lair);
    		roomDao.save(room);
    		return true;
    	} else {
    		return false;
    	}
    }

}
