package uq.deco2800.coaster.game.entities.npcs;

import java.util.LinkedList;
import java.util.Queue;

import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.npcactions.WormMove;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.game.terraindestruction.TerrainDestruction;

/**
 * Worm NPC is the npc that burrows underground and ascends to the surface to explode on the player.
 */
public class WormNPC extends MeleeEnemyNPC implements AttackableNPC {
	
	/* Extra fields needed for this npc */
	int patrolDirection = 0;
	int tick;
	
	/* Coordinates of the terrain that the npc has eaten (removed from the world) */
	Queue<Integer> eatenX = new LinkedList<Integer>();
	Queue<Integer> eatenY = new LinkedList<Integer>();
	
	/**
	 * Constructor for the WormNPC.
	 */
	public WormNPC() {
		
		/* Define basic properties */
		myType = NPCType.ENEMY;
		maxHealth = 100;
		currentHealth = maxHealth;
		
		moveSpeedHor = (int) 1f;
		moveSpeedVer = (int) 1f;
		visionRangeHor = 3;
		
		availableActions.add(new WormMove(this));
		
		patrolStep = 0;

		
	}
	
	@Override
	/**
	 * Unique logic for this npc to be recalculated each tick. Worm patrols in a figure of eight underground until
	 * it senses the player above it. In which case it ascends to the surface and explodes. 
	 */
	public void determineNextAction() {
		++tick;
		tick %= 120;
		/* Target the player */
		Player target = determinePriorityTarget();
		distanceFromPlayer = getDistanceFromPlayer(target);
		
		if (distanceFromPlayer < visionRangeHor) {
			/* Attack */
			/* Worm will quickly dig straight up and then explode */
		} else {
			patrolArea();
		}
		/* On each tick the worm eats the terrain immediately in front of it, unless it has already done so */
		/* Chunks eaten 2 seconds earlier regenerate behind the worm */
		int x = (int) getX();
		int y = (int) getY();
		Boolean tileExists = world.getTiles().test(x, y);
		if (tileExists && world.getTiles().get(x, y).getTileType().isObstacle()) {
			TerrainDestruction.damageRectangle(x, y, 1, 9999);
			eatenX.add(x);
			eatenY.add(y);
			
		}
		if (tick == 0) {
			x = eatenX.poll();
			y = eatenY.poll();
			TerrainDestruction.placeBlock(null, x, y, Side.VOID);
		}
		
	}
	@Override
	/** unique patrol position code for the worm npc. This NPC adds 2 new rendering states to allow it to face upwards
	 * and downwards as well as left/right. It's patrol positioning is a figure of eight, changing directions every
	 * seconds.
	 */
	public void patrolArea() {
		/* Start/Continue the patrol */
		++patrolStep;
		patrolStep %= 400;
		if (patrolStep == 0) {
			/* Worm does a figure of 8 as its patrol */
			++patrolDirection;
			patrolDirection %= 7;
			if (patrolDirection == 0 || patrolDirection == 4) {
				/* Face right */
				setFacing(1);
				setRenderFacing(1);
			} else if (patrolDirection == 1 || patrolDirection == 3) {
				/* Face down */
				setFacing(2);
				setRenderFacing(2);
			} else if (patrolDirection == 2 || patrolDirection == 6) {
				/* Face left */
				setFacing(-1);
				setRenderFacing(-1);
			} else {
				/* Face upward */
				setFacing(0);
				setRenderFacing(0);
			}
		}
		/* Complete the patrol step by moving in the game */
		availableActions.get(0).execute();
	}
}
