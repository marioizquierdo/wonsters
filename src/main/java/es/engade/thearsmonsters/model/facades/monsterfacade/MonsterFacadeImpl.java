package es.engade.thearsmonsters.model.facades.monsterfacade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.egg.MonsterEgg;
import es.engade.thearsmonsters.model.entities.egg.dao.MonsterEggDao;
import es.engade.thearsmonsters.model.entities.lair.Lair;
import es.engade.thearsmonsters.model.entities.lair.dao.LairDao;
import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.dao.MonsterDao;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterRace;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.dao.RoomDao;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.entities.user.dao.UserDao;
import es.engade.thearsmonsters.model.facades.common.ThearsmonstersFacade;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientMoneyException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.InsuficientVitalSpaceException;
import es.engade.thearsmonsters.model.facades.lairfacade.exception.MaxEggsException;
import es.engade.thearsmonsters.model.facades.monsterfacade.exceptions.MonsterGrowException;
import es.engade.thearsmonsters.model.monsteraction.MonsterAction;
import es.engade.thearsmonsters.model.monsteraction.MonsterActionSuggestion;
import es.engade.thearsmonsters.model.monsteraction.MonsterActionType;
import es.engade.thearsmonsters.model.util.DateTools;
import es.engade.thearsmonsters.model.util.GameConf;
import es.engade.thearsmonsters.util.exceptions.InstanceNotFoundException;
import es.engade.thearsmonsters.util.exceptions.InternalErrorException;

public class MonsterFacadeImpl extends ThearsmonstersFacade implements MonsterFacade {

    private MonsterDao monsterDao;
    private MonsterEggDao monsterEggDao;
    private RoomDao roomDao;
    private LairDao lairDao;
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

    public void setRoomDao(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public void setLairDao(LairDao lairDao) {
        this.lairDao = lairDao;
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
            throw new MonsterGrowException(null);
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

    public List<Monster> findLairMonsters(Lair lair) throws InternalErrorException {
        // return monsterDao.findMonstersByLair(lair); // No hace falta buscar en el datastore porque se supone que lair está siempre actualizado
    	return lair.getMonsters();
    }

    public Monster findMonster(Lair lair, String monsterIdAsString) throws InternalErrorException, InstanceNotFoundException {
        Key monsterId = getKeyFromString(monsterIdAsString, Monster.class);
        return monsterDao.get(monsterId);
    }

    @Transactional
    public void incubateEgg(Lair lair, String eggIdAsString)
            throws InternalErrorException, InstanceNotFoundException,
            InsuficientVitalSpaceException {
        
        Key eggId = getKeyFromString(eggIdAsString, MonsterEgg.class);
        MonsterEgg egg = lair.getMonsterEgg(eggId); // Se supone que la lair está actualizada
        
        // Comprueba que hay suficiente espacio vital
        int spaceNeeded = egg.getRace().getVitalSpace();
        int spaceAvaliable = lair.getVitalSpaceFree();
        if(spaceNeeded > spaceAvaliable) {
            throw new InsuficientVitalSpaceException(spaceNeeded, spaceAvaliable);
        }
        
        // Pone el huevo a incubar fijando su fecha de nacimiento (date)
        egg.setBorningDate();
        userDao.update(lair.getUser());
//        monsterEggDao.update(egg);
        
        // A partir de ahora el huevo esperará en estado de "incubación"

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
    
    public List<MonsterActionSuggestion> suggestMonsterActions(Lair lair, String monsterId) throws InstanceNotFoundException{
        
        //TODO: En TODOS los casos de uso de las fachadas, al igual que aquí, hay que hacer lo siguiente:
        // 1. Mirar en lair si se encuentra un monstruo con ese monsterId
        // 2. Si se encuentra, ese es el monstruo (y nos ahorramos la consulta a BBDD)
        // 3. Si no se encuentra, devuelve un InstanceNotFoundException, excepto casos raros en los que puede
        //    acceder usando el DAO.
        Monster monster;
        try {
            monster = lair.getMonster(getKeyFromString(monsterId, Monster.class));
        } catch (InstanceNotFoundException ex) {
            monster = monsterDao.get(getKeyFromString(monsterId, Monster.class));
        }
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
    public boolean executeMonsterAction(Lair lair, MonsterActionType actionType, Key monsterId, RoomType roomType) throws InstanceNotFoundException {
    	
    	/* Al estilo de findMonster, a lo mejor se puede quitar el argumento
    	 * Key del metodo y enviar directamente el monster en ese caso borrar la linea siguiente
    	 */
    	Monster monster = monsterDao.get(monsterId);
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
