package uq.deco2800.coaster.game.entities.npcs;

import java.util.ArrayList;
import java.util.Random;

import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.npcactions.DefaultMove;
import uq.deco2800.coaster.game.entities.npcs.npcactions.Hover;
import uq.deco2800.coaster.game.entities.npcs.npcactions.Soar;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * FlyingNPC is the default flying NPC implementation. It spawns above players and shoots damaging projectiles.
 */

public class FlyingNPC extends RangedEnemyNPC implements AttackableNPC {
	/**
	 * Constructor for the FlyingNPC
	 */
	public FlyingNPC() {
		/* Define basic properties */
		myType = NPCType.ENEMY;
		maxHealth = 100;

		/* Set sprites */
		sprites.put(EntityState.STANDING, new Sprite(SpriteList.BAT_FLAPPING));
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.BAT_FLAPPING));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.BAT_FLAPPING));
		setSprite(sprites.get(EntityState.MOVING));

		/* Define proportions and attributes */
		bounds = new AABB(posX, posY, 1f, 1f);
		
		moveSpeedHor = 2;
		visionRangeHor = 10;
		visionRangeVer = 10;
		jumpSpeed = -10;

		availableActions.add(new DefaultMove(this));
		availableActions.add(new Hover(this));
		availableActions.add(new Soar(this));
		determineIdleDirection();
	}


	/**
	 * Called each tick to determine the behaviour of this NPC
	 */
	@Override
	protected void stateUpdate(long ms) {
		determineNextAction();
	}

	/**
	 * Makes the flying NPC bob up and down
	 */
	public void flap(){
		Random rand = new Random();
		if (this.getY() < target.getY() && this.getY() > target.getY() - 10 + rand.nextInt(8)) {
			availableActions.get(1).execute();
			setSprite(sprites.get(EntityState.JUMPING));
			setSprite(sprites.get(EntityState.MOVING));
		} else {
			availableActions.get(0).execute();
		}
	}

	/**
	 * Moves the flying NPC in the direction of the player
	 */
	public void hover(){
		setSprite(sprites.get(EntityState.MOVING));
		if (target.getY() < getY()){ // if lower on map than player
			availableActions.get(4).execute();
		} else {
			availableActions.get(3).execute();
		}
	}

	/**
	 * Returns the target which this FlyingNPC will target first.
	 *
	 * @return the closest Player if one exists
	 */
	@Override
	public Player determinePriorityTarget() {
		return (Player) this.getClosest(new ArrayList<>(world.getPlayerEntities()));
	}


	@Override
	/** Calls the original onDeath() method in BaseNPC. Cause of death could be used in particular scenarios.
	 * @param cause Cause of this NPCs death.
	 */
	protected void onDeath(Entity cause) {
		super.onDeath();
	}

	@Override
	/**
	 * Top-level method to run every game tick.
	 */
	protected void tick(long ms) {
		stateUpdate(ms);
	}

	/**
	 * Choose the direction that the NPC will face
	 */
	public void chooseFacing(){
		/* Target the player */
		target = determinePriorityTarget();
		distanceFromPlayer = getDistanceFromPlayer(target);

		/* Check range of player */
		int facing = (target.getX()) > getX() ? 1 : -1;
		setFacing(facing);
	}
}
