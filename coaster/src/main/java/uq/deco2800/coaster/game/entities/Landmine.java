package uq.deco2800.coaster.game.entities;
 
import java.util.List;

import uq.deco2800.coaster.game.entities.npcs.BaseNPC;
import uq.deco2800.coaster.game.items.Item;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.terraindestruction.TerrainDestruction;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * The entity class for the landmine that RatNPC will drop.
 *
 */
public class Landmine extends ItemEntity {
	private int radius;
	public int countdownTimer;
	public int fadeTimer;
	private int damage;
	public boolean activated;
	private Entity owner;
	
	/**
	 * Constructor for the Landmine
	 */
	public Landmine(Item item, int value) {
		/* Define proportions and attributes */
		super(item, 1f, 0.6f);
		fadeTimer = 5 ;
		countdownTimer = 35;
		radius = 3;
		activated = false;
		damage = value;
	}


	@Override
	/**
	 * Top-level method to run every game tick.
	 */
	protected void tick(long ms) {
		
		if (this.countdownTimer == 0 && activated) {//explode
			explode();//animate the explosion
		} else if (activated) {
			this.countdownTimer--;//count down to explosion
		}
	}
	
	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		for (Entity entity : entities) {
			if (entity instanceof Player) {
				activated = true;
			}
		}
	}
	
	public void explode(){
		fadeTimer--;
		this.setPosition(this.getX() - ((this.getX() - (this.getX() - 1))/3), this.getY());
		List<Entity> entities = this.getNearbyEntities(radius);
		for (Entity entity : entities) {//BOOM!
			if (entity instanceof BaseNPC) {
				((BaseNPC) entity).receiveDamage(damage, this.owner);
			} else if (entity instanceof Player) {
				((Player) entity).addHealth(-damage, this.owner);
			}
		}
		bounds.setAABB(posX, posY, 4, 4);
		setSprite(new Sprite(SpriteList.EXPLOSION));
		if (fadeTimer == 0) {
			this.delete();
			TerrainDestruction.damageCircle((int) this.getX(), (int) this.getY(), 2, 40);
		} 
	}
	
}
