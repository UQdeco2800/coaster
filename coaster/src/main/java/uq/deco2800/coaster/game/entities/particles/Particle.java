package uq.deco2800.coaster.game.entities.particles;

import java.util.List;

import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.graphics.LayerList;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;


public class Particle extends Entity {
	protected int decayTicks;
	protected int currentTicks = 0;
	protected float size = 0.5f;
	protected float decay;
	protected boolean deletable;

	public Particle(float velX, float velY, float posX, float posY, int particleID, int decayTicks,
					boolean gravity, boolean deletable) {
		layer = LayerList.PARTICLES; 
		bounds = new AABB(posX, posY, size, size);
		enableGravity = gravity;
		this.deletable = deletable;
		switch (particleID) {
			case 302:
				setSprite(new Sprite(SpriteList.PARTICLE1));
				break;
			case 301:
				setSprite(new Sprite(SpriteList.PARTICLE2));
				break;
			case 300:
				setSprite(new Sprite(SpriteList.PARTICLE3));
				break;
			case 299:
				setSprite(new Sprite(SpriteList.PORTAL_PARTICLE1));
				break;
			case 298:
				setSprite(new Sprite(SpriteList.PORTAL_PARTICLE2));
				break;
			case 297:
				setSprite(new Sprite(SpriteList.PARTICLE_HEADSHOT));
				break;
			default:
				break;
		}
		this.velX = velX;
		this.velY = velY;
		decay = size / decayTicks;
		hurtByProjectiles = false;
		setBlocksOtherEntities(false);
		setPosition(posX, posY);
		this.decayTicks = decayTicks;

		setCollisionFilter(e -> false);
	}

	@Override
	protected void tick(long ms) {
		int flooredLeft = (int) Math.floor(this.bounds.ownerLeft());
		int ceilRight = (int) Math.ceil(this.bounds.ownerRight());
		int flooredTop = (int) Math.floor(this.bounds.ownerTop());
		int ceilBottom = (int) Math.ceil(this.bounds.ownerBottom());
		for (int x = flooredLeft; x < ceilRight && x < world.getTiles().getWidth(); x++) {
			for (int y = flooredTop; y < ceilBottom && y < world.getTiles().getHeight(); y++) {
				try {
					if (world.getTiles().get(x, y).getTileType().isObstacle()) {
						if (deletable) {
							this.kill(null);
						} else {
							velY = -velY;
						}
					}
				} catch (IndexOutOfBoundsException invalidXY) {
					this.kill(null);
				}
			}
		}
		if (currentTicks >= decayTicks) {
			this.kill(null);
		} else {
			currentTicks++;
			size -= decay;
			bounds = new AABB(posX, posY, size, size);
		}
	}

	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		//do nothing
	}

	@Override
	protected void onTerrainCollide(int tileX, int tileY, Side side) {
		this.kill(null);
	}

	@Override
	protected void onDeath(Entity cause) {
		this.delete();
	}

	@Override
	public void entityLoop(long ms) {
		float seconds = ms / (float) 1000;
		float newX = posX + velX * seconds;
		float newY = posY + velY * seconds;
		setPosition(newX, newY);
		tick(ms);
	}

}
