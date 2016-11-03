package uq.deco2800.coaster.game.entities.weapons;

import java.util.ArrayList;
import java.util.List;

import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.BasicMovingEntity;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.lighting.TileLight;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;
import uq.deco2800.coaster.game.entities.particles.Particle;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.game.tiles.Tile;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

public class SuperBullet extends Projectile {
	List<TileLight> affectedTiles = new ArrayList<>();
	int radius = 2;
	int maxLight = 45;
	boolean hitTerrain = false;

	public SuperBullet(Entity owner, float velX, float velY, int speed, int damage, SpriteList sprite) {
		super(owner, velX, velY, speed, damage);
		bounds = new AABB(posX, posY, 0.25f, 0.0625f); //Default bullet size
		setAngledSprite(sprite);
		setCollisionFilter(e -> e != owner && e.isHurtByProjectiles());
		initFiringPosition();
	}

	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			BodyPart location = hitLocations.get(i);
			if (handleDuckKing(entity, location)) {
				return;
			}
			float multiplier = location.getMultiplier();
			if (entity instanceof BasicMovingEntity) {
				((BasicMovingEntity) entity).addHealth((int) (-this.damage * multiplier), this.owner);
			} else if (entity instanceof BaseNPC) {
				((BaseNPC) entity).receiveDamage((int) (damage * multiplier), this.owner);
			} else {
				this.onDeath(null);
			}
		}
	}

	@Override
	protected void onTerrainCollide(int tileX, int tileY, Side side) {
		hitTerrain = true;
	}

	@Override
	protected void onDeath(Entity cause) {
		for (TileLight oldTile : affectedTiles) {
			oldTile.getTile().removeLightLevel(oldTile.getLight());
		}
		this.delete();
	}

	@Override
	protected void tick(long ms) {
		if (!hitTerrain) {
			Particle particleUp = new Particle(0, -20, this.getX(), this.getY(), 301, 50, false, false);
			Particle particleDown = new Particle(0, 20, this.getX(), this.getY(), 301, 50, false, false);
			world.addEntity(particleUp);
			world.addEntity(particleDown);
		}
		if (this.damage > 0) {
			this.damage -= this.decay;
		} else {
			this.onDeath(null);
		}


		List<TileLight> oldTiles = new ArrayList<>(affectedTiles);
		/* experimental for now */
		affectedTiles = new ArrayList<>();
		int flooredLeft = (int) Math.floor(this.bounds.ownerLeft());
		int ceilRight = (int) Math.ceil(this.bounds.ownerRight());
		int flooredTop = (int) Math.floor(this.bounds.ownerTop());
		int ceilBottom = (int) Math.ceil(this.bounds.ownerBottom());
		for (int x = flooredLeft - radius; x < ceilRight + radius; x++) {
			for (int y = flooredTop - radius; y < ceilBottom + radius; y++) {
				if (!world.getTiles().test(x, y)) {
					// check for invalid lookups
					continue;
				}
				Tile currentTile = world.getTiles().get(x, y);
				float deltaX = x - this.getX();
				float deltaY = y - this.getY();
				double distance = Math.sqrt((Math.pow(deltaX, 2)) + (Math.pow(deltaY, 2)));
				int light = (int)(maxLight + 20*(distance/Math.sqrt(2*radius^2)));
				if (!oldTiles.contains(currentTile)) {
					currentTile.setLightLevel(light);
				}
				affectedTiles.add(new TileLight(currentTile, light));
			}
		}

		for (TileLight oldTile : oldTiles) {
			if (!affectedTiles.contains(oldTile)) {
				oldTile.getTile().removeLightLevel(oldTile.getLight());
			}
		}
	}
}
