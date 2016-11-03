package uq.deco2800.coaster.game.entities.npcs;

import java.util.Random;

import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.game.terraindestruction.TerrainDestruction;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * Created by adam on 10/20/16.
 */
public class GhostShipNPC extends FlyingNPC {
	private Random spawnVariation = new Random();
	private boolean moveLeft;
	public GhostShipNPC(){
		super();
		/* Set sprites */
		sprites.put(EntityState.STANDING, new Sprite(SpriteList.GHOSTSHIP));
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.GHOSTSHIP));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.GHOSTSHIP));
		setSprite(sprites.get(EntityState.MOVING));
		bounds = new AABB(posX, posY, 7, 7);
		currentHealth = maxHealth = 100000;
		moveLeft = true;
	}

	@Override
	public void determineNextAction(){
		target = determinePriorityTarget();
		if (moveLeft){ // Moving left
			if (target.getX() - 15 < getX()){
				setVelocity(-getMoveSpeedHor(), target.getY() - getY() - 15);
				setFacing(-1);
			} else {
				moveLeft = false; // Switch directions
			}
		} else { // Moving right
			if (target.getX() + 15 > getX()){
				setVelocity(getMoveSpeedHor(), target.getY() - getY() - 15);
				setFacing(1);
			} else {
				moveLeft = true; // Switch directions
			}
		}
		if (spawnVariation.nextInt(100) == 0) {
			SkeletonNPC skeletonNPC = new SkeletonNPC();
			world.addEntity(skeletonNPC);
			skeletonNPC.setPosition(getX() + (spawnVariation.nextBoolean() ? 1 : -1) * 2, getY());
		}
	}

	@Override
	protected void onTerrainCollide(int tileX, int tileY, Side side) {
		super.onTerrainCollide((int) (bounds.posX() - this.getWidth()/2), (int) (bounds.posX() - this.getHeight()/2), side);
		TerrainDestruction.damageCircle((int) getX() - 5, (int) getY() - 5, 5, 100);
		moveLeft = !moveLeft;
	}
}
