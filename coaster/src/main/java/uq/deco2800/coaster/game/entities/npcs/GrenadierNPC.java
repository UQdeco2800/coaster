package uq.deco2800.coaster.game.entities.npcs;

import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.LandTrap;
import uq.deco2800.coaster.game.entities.Landmine;
import uq.deco2800.coaster.game.entities.npcs.npcactions.DefaultMove;
import uq.deco2800.coaster.game.entities.npcs.npcactions.Jump;
import uq.deco2800.coaster.game.items.Item;
import uq.deco2800.coaster.game.items.ItemRegistry;
import uq.deco2800.coaster.game.items.Weapon;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * GrenadierNPC is the NPC which lays down traps and lobs grenades at its target.
 */
public class GrenadierNPC extends BaseNPC implements AttackableNPC {

	/* Extra fields needed for this npc */
	
	public int numTraps = 0;
	private int maxTraps = 3;
	private int trapCooldown;
	/* To delay throwing grenades */ 
	private int firingRateTracker;
	//private int throwDelay = 0;
	public boolean enemyTrapped = false;
	public Weapon grenade;
	/* Trajectories of grenades */
	double xVel;
	double yVel;
	boolean leftOfTarget;
	
	/* The current trap the player is standing in */
	float[] activeTrap = new float[2];
	
	/**
	 * Constructor for GrenadierNPC
	 */
	public GrenadierNPC() {
		/* Define basic properties */
		myType = NPCType.ENEMY;
		maxHealth = 100;
		currentHealth = maxHealth;
		
		moveSpeedHor =  2;
		jumpSpeed = -15;
		visionRangeHor = 27;
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.SNEAKY_RAT_WALKING));
		setSprite(sprites.get(EntityState.MOVING));
		bounds = new AABB(posX, posY, 2f, 2f);
		availableActions.add(new DefaultMove(this));
		availableActions.add(new Jump(this));
		grenade = (Weapon) ItemRegistry.getItem("Gun4");
		firingRateTracker = 0;
		trapCooldown = 0;
	}
	
	@Override
	/** Unique logic for this NPC. Flows as follows: Acquires target, drops a trap in path of player if haven't
	 * used maximum amount, if a player has been trapped it lobs grenades at it at a faster rate, otherwise throws
	 * a grenade periodically. NPC repositions between dropping traps and throwing the periodic grenades.
	 */
	public void determineNextAction() {
		
		/* Target acquisition*/
		determinePriorityTarget();
		leftOfTarget = (getX() < target.getX()) ? true: false;
		
		/* Signal acquired that a player has stepped in one of the traps */
		if (enemyTrapped) {
			
			/* Lob grenades at a faster rate - standing still */
			moveSpeedHor = 0;
			if (firingRateTracker <= 0) {
				determineProjectileVars();
				grenade.basicAttack(this, (float) xVel, (float) yVel, 0, 0);
				firingRateTracker = 15;
			} else {
				--firingRateTracker;
			}
			return;
		}
		
		/* Patrol and lob a grenade as normal */
		if (numTraps < maxTraps) {
			/* Place more traps */
			if (trapCooldown == 0) {

				//place trap left or right of current position depending on where player is facing
				/* Generate trap */
				Item trap = ItemRegistry.getItem("Landtrap1");
				LandTrap landtrapDrop = new LandTrap(trap, this);
				landtrapDrop.setPosition(this.getX(), this.getY());					
				world.addEntity(landtrapDrop);
				++numTraps;
				trapCooldown = 180;
			} else {
				--trapCooldown;
			}
			reposition();
			} else {
				//Throw grenades at normal rate such that the player is between a trap and a grenade and reposition
				if (firingRateTracker <= 0) {
					if (leftOfTarget) {
						setFacing(1);
					} else {
						setFacing(-1);
					}
					determineProjectileVars();
					grenade.basicAttack(this, (float) xVel, (float) yVel, 0, 0);
					firingRateTracker = 180;
				} else {
					--firingRateTracker;
				}
				reposition();
			}
	}
	
	/**
	 * Unique repositioning logic for this npc is as follows:
	 * 		If the npc is out of vision range of the player completely, it doesn't move.
	 * 		If the npc is in vision range but the player isn't moving, the npc doesn't reposition.
	 * 		If the player is in range of the npc but is running away from it, the npc turns to chase.
	 * 		If the player is in range of the npc and is chasing the npc, the npc runs away from the player.
	 * 		The npc jumps if it requires repositioning as above but is against a wall.
	 */
	private void reposition() {
		int facing = getFacing();
		moveSpeedHor = (target.getMoveSpeed() > 0) ? 2: 0;
		if (getDistanceFromPlayer(target) > visionRangeHor) {
			moveSpeedHor = 0;
		} else if ((leftOfTarget && target.getFacing() == -1) || (!leftOfTarget && target.getFacing() == 1)) {
			facing = target.getFacing();
		} else {
			facing = leftOfTarget ? 1: -1;
		}
		setFacing(facing);

		if (shouldJump(getFacing())) {
			availableActions.get(1).execute();
		} else {
			availableActions.get(0).execute();
		}
		
	}

	@Override
	/**
	 * As usual, the current implementation for this is to return the current player.
	 */
	public Entity determinePriorityTarget() {
		target = world.getFirstPlayer();
		return target;
	}

	@Override
	/**
	 * To be called on each game tick. Requires the npc to determine it's next behavior.
	 */
	protected void tick(long ms) {
		determineNextAction();
	}

	@Override
	/**
	 * Nothing special to happen on death for this NPC
	 */
	protected void onDeath(Entity cause) {
		super.onDeath();
	}
	
	/**
	 * Listens for signals from the npc's traps that it lays down. If an enemy has been trapped this method is
	 * called and sets up the behavior required for the next ticks.
	 * @param x x position of the active trap
	 * @param y y position of the active trap
	 */
	public void listenTrapSignal(float x, float y) {
		activeTrap[0] = x;
		activeTrap[1] = y;
		enemyTrapped = true;
		firingRateTracker = 0;
		leftOfTarget = (getX() < target.getX()) ? true: false;
		if (leftOfTarget) {
			setFacing(1);
		} else {
			setFacing(-1);
		}
		
	}
	
	/**
	 * Determines the x and y velocity required for the npc to throw grenades at the desired location.
	 * Will always be in the direction of the player. Factors in the current distance and height difference
	 * between the npc and the target/desired position.
	 */
	private void determineProjectileVars() {
		float diff = Math.abs(getX() - target.getX());
		if (diff < 10) {
			xVel = (leftOfTarget) ? 2: -2;
		} else if (diff > 10 && diff < 20) {
			xVel = (leftOfTarget) ? 10: -10;
		} else {
			xVel = (leftOfTarget) ? 20: -20;
		}
		
		yVel = -8;
//		double angle;
//		if (getY() == target.getY()) {
//			angle = 45;
//		} else {
//			angle = (getY() > target.getY()) ? 25: 65;
//		}
//		angle = Math.toRadians(angle);
//		float distHor = getX() - target.getX();
//		float distVer = getY() - target.getY();
//		System.out.println("xd:" + distHor);
//		System.out.println("yd:" + distVer);
//		double vel = (1/Math.cos(angle)) * Math.sqrt(((1/2)* 10 * distHor * distHor) / (distHor * Math.tan(angle) + distVer));
//		xVel = vel / Math.cos(angle);
//		yVel = vel/ Math.sin(angle);
//		System.out.println("velocity: " + vel);
//		System.out.println("x vel:" + xVel);
//		System.out.println("y vel:" + yVel);
	}

}
