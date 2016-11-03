package uq.deco2800.coaster.game.entities;

import java.util.List;
import java.util.Random;

import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.game.terraindestruction.TerrainDestruction;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * Created by adam on 10/23/16.
 */
public class GenericExplosion extends Entity{
	private float x;
	private float y;
	private int radius;
	private float currentRadius;
	private Random random;

	/**
	 * Generates an explosion at a given position
	 * @param x
	 * @param y
	 * @param radius
	 */
	public GenericExplosion(float x, float y, int radius){
		this.x = x;
		this.y = y;
		bounds = new AABB(x, y, 1, 1);
		setPosition(x,y);
		this.radius = radius;
		this.currentRadius = 1;
		this.random = new Random();

		this.setSprite(new Sprite(SpriteList.EXPLOSION));
	}

	/**
	 * Logic for each tick of the game. Expands the explosion
	 * @param ms millisecond tick the entity is being handled on
	 */
	public void tick(long ms){
		if(currentRadius < radius){
			this.bounds.setAABB(0,0,currentRadius*2,currentRadius*2);
			setPosition(x-currentRadius,y-currentRadius);
			currentRadius++;
		} else {
			TerrainDestruction.damageCircle((int) x - radius,(int) y - radius, radius, 100);
			this.delete();
		}
	};
	public void onDeath(Entity cause){}
	public void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations){};
	public void onTerrainCollide(int tileX, int tileY, Side side){};
}
