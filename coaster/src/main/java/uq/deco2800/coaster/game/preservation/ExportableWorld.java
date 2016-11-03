package uq.deco2800.coaster.game.preservation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import uq.deco2800.coaster.game.entities.BasicMovingEntity;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.ItemEntity;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.particles.Particle;
import uq.deco2800.coaster.game.world.World;

/**
 * Created by ryanj on 12/08/2016.
 */
public class ExportableWorld {
	private static final Logger logger = LoggerFactory.getLogger(ExportableWorld.class);

	public List<ExportableEntity> entities;
	public List<ExportableMovingEntity> movingEntities;
	public List<ExportablePlayer> playerEntities;
	public List<ExportableItem> itemEntities;

	/**
	 * Converts World to ExportableWorld
	 */
	public ExportableWorld() {
		entities = new ArrayList<>();
		playerEntities = new ArrayList<>();
		movingEntities = new ArrayList<>();
		itemEntities = new ArrayList<>();
		World world = World.getInstance();
		//Don't put moving entities in the entities for saving
		// Put BasicMovingEntities that aren't PlayerEntities in here.
		world.getPlayerEntities().forEach(p -> playerEntities.add(new ExportablePlayer(p)));
		for (Entity e : world.getAllEntities()) {
			if (valid(e)) {
				if (e instanceof ItemEntity) {
					ExportableItem ei = new ExportableItem((ItemEntity) e);
					if ("".equals(ei.itemReference)) {
						continue;
					}
					itemEntities.add(ei);
				} else if (e instanceof BasicMovingEntity) {
					movingEntities.add(new ExportableMovingEntity((BasicMovingEntity) e));
				} else {
					entities.add(new ExportableEntity(e));
				}
			}
		}
	}

	/**
	 * checks if valid entity
	 *
	 * @param e entity to check
	 * @return if it's a valid entity to save
	 */
	private boolean valid(Entity e) {
		if (e.getSprite() == null) {
			logger.error("SPRITE NULL; " + e.getClass());
			return false;
		}
		if (e.getSpriteID() == null) {
			logger.error("SPRITE ID NULL; " + e.getClass());
			return false;
		}
		return !(e instanceof Player || e instanceof Particle);
	}
}
