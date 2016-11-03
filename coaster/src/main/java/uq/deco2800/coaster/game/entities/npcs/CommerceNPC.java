package uq.deco2800.coaster.game.entities.npcs;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import uq.deco2800.coaster.game.commerce.Store;
import uq.deco2800.coaster.game.commerce.StoreType;
import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * This class serves as the base for Banks, Traders and other in world Commerce NPC's.
 *
 * Author: jamesthompson275
 */
public class CommerceNPC extends Entity {

	private static final Integer STORE_RANGE = 2;
	private TraderType traderType;
	private Store store = null;
	protected Map<TraderStatus, Sprite> sprites = new EnumMap<TraderStatus, Sprite>(TraderStatus.class);
	private Random random = new Random();

	public enum TraderType {
		STORE,
		BANK
	}

	public enum TraderStatus {
		NORMAL,
		ACTIVE
	}

	/**
	 * Constuctor fot CommerceNPC
	 * @param type The TraderType of commerce npc
	 */
	public CommerceNPC(TraderType type) {
		Sprite traderSprite = null;
		Sprite traderActiveSprite = null;
		switch (type) {
			case STORE:
				if (random.nextInt(2) == 0) {
					traderSprite = new Sprite(SpriteList.STORE_NPC);
					traderActiveSprite = new Sprite(SpriteList.STORE_ACT_NPC);
				} else {
					traderSprite = new Sprite(SpriteList.STORE_NPC_2);
					traderActiveSprite = new Sprite(SpriteList.STORE_ACT_NPC_2);
				}
				break;
			case BANK:
				traderSprite = new Sprite(SpriteList.BANK_NPC);
				traderActiveSprite = new Sprite(SpriteList.BANK_ACT_NPC);
				break;
			default:
				break;
		}
		setSprite(traderSprite);
		sprites.put(TraderStatus.NORMAL, traderSprite);
		sprites.put(TraderStatus.ACTIVE, traderActiveSprite);
		this.traderType = type;
		setBlocksOtherEntities(false);
		hurtByProjectiles = false;
		setCollisionFilter(e -> false);

		bounds = new AABB(posX, posY, 1.6f, 3f);
	}

	/**
	 * Returns the instance of the store associated with the trader.
	 *
	 * @return instance of store.
	 */
	public Store getStore() {
		return store;
	}

	/**
	 * Returns the type of the trader that is being interacted with.
	 *
	 * @return the type of the CommerceNPC.
	 */
	public TraderType getTraderType() {
		return this.traderType;
	}

	/**
	 * Returns the distance/proximity of the CommerceNPC to the player.
	 *
	 * @return distance from player.
	 */
	public static float getRange() {
		return STORE_RANGE;
	}

	@Override
	protected void tick(long ms) {
		boolean playerNear = false;
		for (Entity entity : this.getNearbyEntities(CommerceNPC.STORE_RANGE)) {
			if (entity instanceof Player) {
				playerNear = true;
			}
		}
		if (playerNear) {
			if (this.traderType == TraderType.STORE && store == null) {
				store = new Store(StoreType.GENERAL);
			}
			setSprite(sprites.get(TraderStatus.ACTIVE));
		} else {
			setSprite(sprites.get(TraderStatus.NORMAL));
		}
	}

	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		// Irrelevant
	}

	@Override
	protected void onTerrainCollide(int tileX, int tileY, Side side) {
		// Irrelevant
	}

	@Override
	protected void onDeath(Entity cause) {
		// Irrelevant
	}

}
