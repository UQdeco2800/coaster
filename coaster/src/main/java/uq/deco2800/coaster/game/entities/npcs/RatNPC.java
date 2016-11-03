package uq.deco2800.coaster.game.entities.npcs;

import java.util.Random;

import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.Landmine;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.TreasureChest;
import uq.deco2800.coaster.game.entities.npcs.npcactions.DefaultMove;
import uq.deco2800.coaster.game.entities.npcs.npcactions.Jump;
import uq.deco2800.coaster.game.items.Item;
import uq.deco2800.coaster.game.items.ItemRegistry;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * RatNPC is a Ranged NPC that drops landmines. It inherits from RangedEnemyNPC
 */
public class RatNPC extends RangedEnemyNPC implements AttackableNPC {
	private int selfDestructTime = 5000;
	private int explosionTime = 30;
	private int firingRateTracker;
	private int damage;

	/**
	 * Constructor for the RatNPC
	 */
	public RatNPC() {
		/* Define basic properties */
		myType = NPCType.ENEMY;
		maxHealth = 500;
		currentHealth = maxHealth;

		/* Set sprites */
		sprites.put(EntityState.STANDING, new Sprite(SpriteList.EXPLOSION));
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.SNEAKY_RAT_WALKING));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.SNEAKY_RAT_STANDING));
		setSprite(sprites.get(EntityState.MOVING));

		/* Define proportions and attributes */
		bounds = new AABB(posX, posY, 2.5f, 2.5f);
		moveSpeedHor = 7;
		visionRangeHor = 40;
		visionRangeVer = 40;
		jumpSpeed = -20;
		damage = 10;
		firingRateTracker = 400;
		availableActions.add(new DefaultMove(this));
		availableActions.add(new Jump(this));
		soundType = NPCSound.HIGH;
	}

	@Override
	/**
	 * Unique logic for this NPC. Called every tick.
	 * Targets the player, generates landmines, and performs jumping logic.
	 */
	public void determineNextAction() {
		/* Target the player */
		Player target = determinePriorityTarget();
		distanceFromPlayer = getDistanceFromPlayer(target);

		/* Check range of player */
		boolean withinRange = distanceFromPlayer < Math.sqrt(visionRangeHor * visionRangeHor + visionRangeVer * visionRangeVer);
		if (withinRange && !target.getInvisible()) { //retreating and not invisible
			determineFacingDirection(target, true);
		}

		/* Destruction logic */
		if (selfDestructTime == 0) {
			selfDestruct();
		} else {
			selfDestructTime--;
		}
		
		/* Generates landmine*/
		if (this.firingRateTracker == 0) {
			Random rn = new Random();
			int random = rn.nextInt(10);
			if (random <= 7) {
				/* Generate landmine */
				Item landmine = ItemRegistry.getItem("Landmine1");
				Landmine landmineDrop = new Landmine(landmine, damage);
				landmineDrop.setPosition(this.getX(), this.getY());
				world.addEntity(landmineDrop);
			} else {
				Item chest = ItemRegistry.getItem("Chest1");
				TreasureChest chestDrop = new TreasureChest(chest);
				chestDrop.setPosition(this.getX(), this.getY() - 3);
				world.addEntity(chestDrop);

			}
			firingRateTracker = 600;
		} else {
			firingRateTracker--;
		}
		
		/* Animate NPC */
		if (shouldJump(this.getFacing())) {
			availableActions.get(1).execute();
		} else {
			availableActions.get(0).execute();
		}
	}

	/**
	 * Destroy the NPC if the explosion timer reaches 0. If not, decrement it.
	 */
	public void selfDestruct() {
		if (explosionTime == 0) {
			this.delete();
		} else {
			moveSpeedHor = (int) 0f;
			setSprite(sprites.get(EntityState.STANDING));
			explosionTime--;
		}
	}
}
