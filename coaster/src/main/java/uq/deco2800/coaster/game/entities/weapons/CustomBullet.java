package uq.deco2800.coaster.game.entities.weapons;

import java.util.ArrayList;
import java.util.List;

import uq.deco2800.coaster.graphics.notifications.Toaster;
import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.game.terraindestruction.TerrainDestruction;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

public class CustomBullet extends Projectile {
	private float impactWidth;
	private float impactHeight;
	private SpriteList impactSpriteID;
	private SpriteList travelSpriteID;
	private float travelWidth;
	private float travelHeight;
	private int animate = 5;
	private boolean hit = false;
	private int range = 100; //100 default so offscreen
	private boolean impactSprite;
	private static final float EPSILON = 0.0000001f;

	/**
	 * Created by Hayley The projectile class that allows for the user to specific the travel sprite, dimensions of the
	 * travel sprite, the impact sprite (if desired), and the dimensions of the impact sprite and the range of the shot.
	 * This was created to avoid affecting anyone using generic bullet but to still allow for advanced bullet
	 * customisation if desired. <p> More information about how to use this bullet for a weapon can be found on the
	 * wiki
	 */
	public CustomBullet(Entity owner, float velX, float velY, int speed, int damage, int range, SpriteList travelSpriteID,
						float travelWidth, float travelHeight, SpriteList impactSpriteID, float impactWidth, float impactHeight) {

		super(owner, velX, velY, speed, damage);
		this.range = range;

		//set defaults if not specificed
		/**
		 * More information about what the defaults are on available on the wiki
		 */
		if (travelSpriteID == null) {
			this.travelSpriteID = SpriteList.BULLET;
			this.travelHeight = 0.25f;
			this.travelWidth = 0.75f;
		} else {
			this.travelSpriteID = travelSpriteID;
		}

		if ((travelWidth < EPSILON) || (travelHeight < EPSILON)) {
			this.travelHeight = 0.25f;
			this.travelWidth = 0.75f;
		} else {
			this.travelHeight = travelHeight;
			this.travelWidth = travelWidth;
		}

		if (impactSpriteID == null) {
			impactSprite = false;
		} else {
			impactSprite = true;
			this.impactSpriteID = impactSpriteID;
		}

		if ((impactWidth < EPSILON) || (impactHeight < EPSILON)) {
			this.impactHeight = 1f;
			this.impactWidth = 1f;
		} else {
			this.impactHeight = impactHeight;
			this.impactWidth = impactWidth;
		}

		bounds = new AABB(posX, posY, this.travelWidth, this.travelHeight);
		setAngledSprite(this.travelSpriteID);
		enableGravity = false;

		setCollisionFilter(e -> e != owner && e.isHurtByProjectiles());
		initFiringPosition();
		
		createHitboxes();
	}

	@Override
	protected void tick(long ms) {
		range--;
		if (range == 0) {
			this.kill(null);
		}

		if (hit) {
			this.animate--;
			if (this.animate == 0) {
				this.kill(null);
			}
		}
	}

	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			BodyPart location = hitLocations.get(i);
			if (handleDuckKing(entity, location)) {
				return;
			}
			if (location == BodyPart.HEAD) {
				Toaster.lightToast("Headshot");
				createHeadshotParticles();
			}
			float multiplier = location.getMultiplier();
			if (entity instanceof BaseNPC && !(owner instanceof BaseNPC)){
				((BaseNPC) entity).receiveDamage((int) (damage * multiplier), this.owner);
			} else if (entity instanceof Player){
				((Player) entity).addHealth(- (int) (damage * multiplier), owner);
			} else {
				return;
			}
		}
		if (impactSprite) {
			impact();
		} else {
			this.kill(null);
		}
	}

	@Override
	protected void onTerrainCollide(int tileX, int tileY, Side side) {
		this.kill(null);
		TerrainDestruction.damageBlock(tileX, tileY, (int)this.damage);
	}

	/**
	 * Stop projectile and change sprite to impact sprite.
	 */
	private void impact() {
		setAdditionalSprites(null);
		this.setVelocity(0, 0);
		bounds.setAABB(posX, posY, this.impactWidth, this.impactHeight);
		setSprite(new Sprite(this.impactSpriteID));
		hit = true;
	}

	/**
	 * Getter method for the projectile's impact sprite Utilised in tests
	 *
	 * @return SpriteList of the impact sprite
	 */
	public SpriteList getImpactSprite() {
		return impactSpriteID;
	}

	/**
	 * Getter method for the bullet's travel sprite Utilised in tests
	 *
	 * @return the SpriteList of the travel sprite
	 */
	public SpriteList getTravelSprite() {
		return travelSpriteID;
	}

	/**
	 * Getter method for the projectile sprites dimensions. Stored as travel sprite width, height then impact sprite
	 * width height. Used in tests.
	 *
	 * @return ArrayList of the floats referring to the sprite dimensions.
	 */
	public List<Float> getSpriteDimensions() {
		List<Float> dimensions = new ArrayList<Float>();
		dimensions.add(this.travelWidth);
		dimensions.add(this.travelHeight);
		dimensions.add(this.impactWidth);
		dimensions.add(this.impactHeight);
		return dimensions;
	}

	/**
	 * Getter method for the bullet's current range count
	 *
	 * @return range int
	 */
	public int getRange() {
		return range;
	}
}


