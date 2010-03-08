package es.engade.thearsmonsters.model.entities.room.dao;

import org.springframework.transaction.annotation.Transactional;

import com.google.appengine.api.datastore.Key;

import es.engade.thearsmonsters.model.entities.common.dao.GenericDaoJdo;
import es.engade.thearsmonsters.model.entities.room.Room;

@Transactional
public class RoomDaoJdo extends GenericDaoJdo<Room, Key> implements RoomDao {

    public RoomDaoJdo() {
    }

}