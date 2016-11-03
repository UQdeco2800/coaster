package uq.deco2800.coaster.game.entities.npcs;

import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.npcs.npcactions.DefaultMove;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * Created by adam on 9/30/16.
 */
public class TestingNPC extends BaseNPC implements AttackableNPC{
	public TestingNPC() {
		/* Define basic properties */
		bounds = new AABB(posX, posY, 1.3f, 1.3f);
		myType = NPCType.ENEMY;
		maxHealth = 100;

		sprites.put(EntityState.STANDING, new Sprite(SpriteList.BAT_FLAPPING));
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.BAT_FLAPPING));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.BAT_FLAPPING));
		setSprite(sprites.get(EntityState.MOVING));

		availableActions.add(new DefaultMove(this));
		soundType = NPCSound.HIGH;
	}

	@Override
	protected void onDeath(Entity cause) {
		super.onDeath();
	}

	@Override
	protected void tick(long ms) {
		stateUpdate(ms);
		checkStateUpdate();
		patrolArea();
		ableToJump();
	}

	@Override
	public void determineNextAction() {}

	@Override
	public Entity determinePriorityTarget() {
		return world.getFirstPlayer();
	}

	public void maxHealth(){
		currentHealth = maxHealth;
	}
}
