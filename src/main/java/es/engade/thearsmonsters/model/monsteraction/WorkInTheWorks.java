package es.engade.thearsmonsters.model.monsteraction;

import java.util.List;

import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.enums.AttrType;
import es.engade.thearsmonsters.model.entities.monster.enums.MonsterAge;
import es.engade.thearsmonsters.model.entities.room.Room;
import es.engade.thearsmonsters.model.entities.room.enums.RoomType;
import es.engade.thearsmonsters.model.entities.room.enums.WorksType;
import es.engade.thearsmonsters.model.entities.room.exceptions.InWorksException;
import es.engade.thearsmonsters.model.entities.room.state.RoomInWorksState;
import es.engade.thearsmonsters.model.entities.room.state.RoomNormalState;
import es.engade.thearsmonsters.model.entities.room.state.RoomState;



public class WorkInTheWorks extends MonsterAction{
	
	public WorkInTheWorks(Monster monster, Room room) {
		super(monster, room);
	}
	
	public List<MonsterAge> allowedMonsterAges() {
		return ages("Adult");
	}
	
	public List<RoomType> allowedRoomTypes() {
		return rooms("all");
	}

	protected boolean checkExtraConditions() {
		return room.isInWorks(); // TODO: Se comprueban condiciones extras para la action sleep
	}
	
	/**
	 * Esta acci—n modifica el estado de las obras de una sala, aumentando la cantidad de effortDone
	 * realizado. Si la sala supera la cantidad necesaria para aumentar de nivel (o de tama–o), el estado
	 * de la sala vuelve a ser NormalState.
	 */
	protected void doExecute() {
        try {
		        ((RoomInWorksState)room.getState()).setEffortDone(room.getEffortDone() + monster.getAttr(AttrType.Construction).getLevel());
	            if(room.isInInitialState()){
	                if(room.getEffortDone() > room.getEffortBuild()){
	                	room.setSize(1);
	                	room.setState(new RoomNormalState());
	                }
	            }else if(room.getState().getWorksType().equals(WorksType.Enlarging)) {
	                if(room.getEffortDone() > room.getEffortEnlarge()){
	                	room.setSize(room.getSize() + 1);
	                    room.setState(new RoomNormalState());
	                }                
	            }else if(room.getState().getWorksType().equals(WorksType.Upgrading)){
	                if(room.getEffortDone() > room.getEffortUpgrade()){
	                	room.setLevel(room.getLevel() + 1);
	                    room.setState(new RoomNormalState());
	                }                
	            }
        } catch (InWorksException e) {
            throw new RuntimeException(e);
        }
        
	}
           


}
