package fr.univ_artois.iut_lens.spaceinvader.client;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateInfos.GameInfo;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateMap.MapData;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateMap.MapData.EntityDataSpawn;
import fr.univ_artois.iut_lens.spaceinvader.network_packet.server.PacketServerUpdateMap.MapData.EntityDataUpdated;
import fr.univ_artois.iut_lens.spaceinvader.sprites_manager.SpriteStore;
import fr.univ_artois.iut_lens.spaceinvader.util.Vector2d;

public class EntityRepresenterManager {
	
	
	
	private Map<Integer, EntityRepresenter> entities = new TreeMap<Integer, EntityRepresenter>();

	
	private final Map<Integer, String> spritesIds = new HashMap<Integer, String>();
	
	
	public synchronized void putUpdateFromServer(MapData data) {
		// ajout des identifiants de sprites envoyé par le serveur
		spritesIds.putAll(data.spritesData);
		
		long time = System.nanoTime();
		
		// remove entities
		for (int id : data.removedEntities) {
			entities.remove(id);
		}
		
		// ajout des entités qui viennent de spawner
		for (EntityDataSpawn newEntity : data.spawningEntities) {
			entities.put(newEntity.id, new EntityRepresenter(
					SpriteStore.get().getSprite(spritesIds.get(newEntity.spriteId)),
					new Vector2d(newEntity.posX, newEntity.posY),
					new Vector2d(newEntity.speedX, newEntity.speedY),
					newEntity.name,
					newEntity.currentLife,
					newEntity.maxLife,
					time))
			;
		}
		
		// update entities
		for (EntityDataUpdated entityData : data.updatingEntities) {
			EntityRepresenter entity = entities.get(entityData.id);
			if (entity == null) continue;
			entity.setPosition(new Vector2d(entityData.posX, entityData.posY));
			entity.setSpeed(new Vector2d(entityData.speedX, entityData.speedY));
			entity.setCurrentLife(entityData.currentLife);
			entity.setUpdateNanoTime(time);
		}
		
		// prediction of moving entities that are not updated with this packet data
		long tickTime = (long) (1000000000/Client.instance.lastGameInfo.get().maxTPS);
		for (EntityRepresenter entity : entities.values()) {
			if (entity.getUpdateNanoTime() == time) continue;
			// on ignore ceux qui ont été mis à jour par le serveur
			
			entity.setPosition(entity.getPosition().add(entity.getSpeed().dotProduct(tickTime/1000000000D)));
			entity.setUpdateNanoTime(time);
		}
	}
	
	
	public synchronized void drawAll(Graphics g, long loop_start) {
		
		// infos pour l'adoucicement du mouvement des entités
		GameInfo gInfo = Client.instance.lastGameInfo.get();
		int defaultTickDuration = (MegaSpaceInvader.CLIENT_SMOOTH_ENTITY_MOVEMENT) ? 1000000000/gInfo.maxTPS : 0;
		float slowDownCoeff = 1; // plus petit = plus lent
		if (MegaSpaceInvader.CLIENT_SMOOTH_ENTITY_MOVEMENT && defaultTickDuration < gInfo.currentTickTime)
			slowDownCoeff = gInfo.currentTickTime/(float)defaultTickDuration;
		// -----
		
		for (EntityRepresenter ent : entities.values()) {
			ent.draw(g, loop_start, defaultTickDuration, slowDownCoeff);
		}
	}
	
	
	public synchronized void clear() {
		entities.clear();
	}
	
}
