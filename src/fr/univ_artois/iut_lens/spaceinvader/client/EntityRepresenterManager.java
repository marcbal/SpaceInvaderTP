package fr.univ_artois.iut_lens.spaceinvader.client;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateMap.MapData;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateMap.MapData.EntityDataSpawn;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateMap.MapData.EntityDataUpdated;
import fr.univ_artois.iut_lens.spaceinvader.sprites_manager.SpriteStore;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityRepresenterManager {
	
	
	
	private Map<Integer, EntityRepresenter> entities = new HashMap<Integer, EntityRepresenter>();

	
	private final Map<Integer, String> spritesIds = new HashMap<Integer, String>();
	
	
	public synchronized void getUpdateFromServer(MapData data) {
		// ajout des identifiants de sprites envoyé par le serveur
		spritesIds.putAll(data.spritesData);
		
		// ajout des entités qui viennent de spawner
		for (EntityDataSpawn newEntity : data.spawningEntities) {
			entities.put(newEntity.id, new EntityRepresenter(
					SpriteStore.get().getSprite(spritesIds.get(newEntity.spriteId)),
					new Vector2d(newEntity.posX, newEntity.posY),
					new Vector2d(newEntity.speedX, newEntity.speedY),
					newEntity.name,
					newEntity.currentLife,
					newEntity.maxLife))
			;
		}
		
		// update entities
		for (EntityDataUpdated entityData : data.updatingEntities) {
			EntityRepresenter entity = entities.get(entityData.id);
			entity.setPosition(new Vector2d(entityData.posX, entityData.posY));
			entity.setSpeed(new Vector2d(entityData.speedX, entityData.speedY));
			entity.setCurrentLife(entityData.currentLife);
		}
		
		// remove entities
		for (int id : data.removedEntities) {
			entities.remove(id);
		}
	}
	
	
	public synchronized void drawAll(Graphics g) {
		for (EntityRepresenter ent : entities.values()) {
			ent.draw(g);
		}
	}
	
	
	public synchronized void clear() {
		entities.clear();
	}
	
}
