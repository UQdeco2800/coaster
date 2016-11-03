package uq.deco2800.coaster.game.entities.npcs;

import java.util.List;

import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

public class StoreNPC extends Entity {

	private float storeRange = 3;


	public StoreNPC(float posX, float posY) {
		Sprite storeNpcSprite= new Sprite(SpriteList.STORE_NPC);
		setSprite(storeNpcSprite);
		this.posX = posX;
		this.posY = posY;
		setBlocksOtherEntities(false);
		hurtByProjectiles = false;

		bounds = new AABB(posX, posY, 1.6f, 2f);
	}

	public float getRange() {
		return this.storeRange;
	}

	@Override
	protected void tick(long ms) {
		// Irrelevant
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
