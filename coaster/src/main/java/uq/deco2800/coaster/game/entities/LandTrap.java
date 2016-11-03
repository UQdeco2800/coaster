package uq.deco2800.coaster.game.entities;

import java.util.List;

import uq.deco2800.coaster.game.entities.npcs.GrenadierNPC;
import uq.deco2800.coaster.game.items.Item;
import uq.deco2800.coaster.game.mechanics.BodyPart;

/**
 *	LandTrap is a trap item that is placed on the ground. If an enemy steps on the trap they are unable to move for
 *	a period of time and take some damage. 
 */
public class LandTrap extends ItemEntity{

	/* Fields to control the trap and apply effects to the player */
	public int countdownTimer;
	public boolean activated;
	private int damage;
	Player player = world.getFirstPlayer();
	private float playerPrevMoveSpeed;
	private float playerPrevJumpSpeed;
	private Entity owner;
	private boolean collidable;
	
	/**
	 * Constructor for the LandTrap
	 * @param item
	 * @param owner
	 */
	public LandTrap(Item item, Entity owner) {
		super(item, 1.5f, 0.6f);
		countdownTimer = 155;
		collidable = true;
		activated = false;
		damage = 10;
		/* Owner of the trap. E.g. GrenadierNPC */
		this.owner = owner;
	}
	
	@Override
	/**
	 * To be called each game tick. Checks if the player needs to be released from the trap. Deletes itself if
	 * the trap time has expired and resets the effects on the player. 
	 */
	protected void tick(long ms) {
		
		/* Release the player after set time */
		if (countdownTimer == 0) {
			
			activated = false;
			/* Return the players movement ability */
			if (playerPrevMoveSpeed == 0) {
				playerPrevMoveSpeed = 10;
			}
			if (playerPrevJumpSpeed == 0) {
				playerPrevJumpSpeed = -20;
			}
			player.setMoveSpeed(playerPrevMoveSpeed);
			player.setJumpSpeed(playerPrevJumpSpeed);
			player.setJumpAvailable(true);
			
			
			/* Notify owner */
			if (owner != null) {
				if (owner instanceof GrenadierNPC) {
					((GrenadierNPC) owner).enemyTrapped = false;
					--((GrenadierNPC) owner).numTraps;
				}
			}
			
			this.delete();
		}
		
		if (activated) {
			--countdownTimer;
		}
	}
	@Override
	/**
	 * Checks if an entity collides with the trap. If the collision was with a player, the trap is activated.
	 */
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		if (!collidable) {
			return;
		}
		/* Check if player steps on trap */
		for (Entity entity: entities) {
			if (entity instanceof Player) {
				activate();
			}
		}
	}
	
	/**
	 * Activation of the trap. Damage and movement effects are applied to the player, and the owner is notified
	 * of the activation.
	 */
	private void activate() {
		
		activated = true;
		/* Player stepped on trap - deal damage and stop it from moving */
		player.addHealth(-damage);
		playerPrevMoveSpeed = player.getMoveSpeed();
		player.setMoveSpeed(0);
		playerPrevJumpSpeed = player.getJumpSpeed();
		player.setJumpSpeed(0);
		player.setJumpAvailable(false);
		
		/* signal owner */
		if (owner != null) {
			if (owner instanceof GrenadierNPC) {
				((GrenadierNPC) owner).listenTrapSignal(this.getX(), this.getY());
			}
		}
		
		collidable = false;
	}
}
