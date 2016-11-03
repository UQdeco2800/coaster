package uq.deco2800.coaster.game.items;

import java.util.ArrayList;

import uq.deco2800.coaster.game.entities.weapons.ProjectileType;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.weapons.CustomBullet;
import uq.deco2800.coaster.game.entities.weapons.FireProjectile;
import uq.deco2800.coaster.game.entities.weapons.GenericBlade;
import uq.deco2800.coaster.game.entities.weapons.GenericBullet;
import uq.deco2800.coaster.game.entities.weapons.GrenadeBullet;
import uq.deco2800.coaster.game.entities.weapons.PortalBullet;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * Created by Hayley on 28/08/2016. The weapon class used to create all weapons that entities can use. Extends the item
 * class and uses the same builder pattern. All immutable attributes are set via the builder.
 *
 * More information about creating a weapon can be found on the wiki
 */
public class Weapon extends Item {
	private int damage;
	private int bulletSpeed;
	private int firingRate;
	private ProjectileType projectileType;
	private int grenadeRadius;
	private int grenadeTime;
	private int swingTime;
	private SpriteList actionSprite;
	private SpriteList travelSprite;
	private float travelWidth;
	private float travelHeight;
	private int accuracy = 0; //defaults to 0 if not set
	private float actionWidth;
	private float actionHeight;
	private int range;
	private boolean multishot = false;
	private int shots;
	private int shotArc;
	private boolean soilAdder = false;
	private int ammoDeduction = 1;
	
	
	private boolean portal = true;
	
	/**
	 * Getter method for the weapon's damage. Utilised in tests
	 *
	 * @return weapon's damage.
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * Getter method for the weapon's fire rate. Utilised in player class to influence fire rate.
	 *
	 * @return the weapon's Firing Rate.
	 */
	public int getFiringRate() {
		return firingRate;
	}

	/**
	 * Getter method for the weapon's projectile type. Utilised in tests.
	 *
	 * @return the weapon's projectile type.
	 */
	public ProjectileType getProjectileType() {
		return projectileType;
	}

	/**
	 * Getter method for the weapon's bullet speed. Utilised in tests.
	 *
	 * @return the weapon's bullet speed
	 */
	public int getBulletSpeed() {
		return bulletSpeed;
	}

	/**
	 * Getter method for the weapon's grenade radius if applicable. Utilised in tests.
	 *
	 * @return the weapon's grenade radius
	 */
	public int getGrenadeRadius() {
		return grenadeRadius;
	}

	/**
	 * Getter method for the weapon's grenade tick time if applicable. Utilised in tests.
	 *
	 * @return the weapon's grenade tick time
	 */
	public int getGrenadeTime() {
		return grenadeTime;
	}

	/**
	 * Getter method for the weapon's melee swing time if applicable. Utilised in tests.
	 *
	 * @return the weapon's melee swing time
	 */
	public int getSwingTime() {
		return swingTime;
	}

	/**
	 * Getter method for the weapon's action Sprite ID if applicable. Utilised in tests.
	 *
	 * @return the weapon's action Sprite ID.
	 */
	public SpriteList getActionSprite() {
		return actionSprite;
	}

	/**
	 * Getter method for the weapon's travelling Sprite ID if applicable. Utilised in tests.
	 *
	 * @return the weapon's travel Spirte ID.
	 */
	public SpriteList getTravelSprite() {
		return travelSprite;
	}

	/**
	 * Getter method for the weapon's accuracy. This is the deflection in degrees Utilised in tests.
	 *
	 * @return the weapon's accuracy
	 */
	public int getAccuracy() {
		return accuracy;
	}

	/**
	 * Getter method for the weapon's range.
	 *
	 * @return the weapon's range
	 */
	public int getRange() {
		return range;
	}

	/**
	 * Getter method for the weapon's multishot shot count
	 *
	 * @return the weapon's shotcount
	 */
	public int getShots() {
		return shots;
	}


	/**
	 * Getter method for the weapon's soil adder property
	 *
	 * @return the weapon's soil adder property
	 */
	public boolean getSoilerAdder() {
		return soilAdder;
	}

	/**
	 * Getter method for the weapon's ammo deduction per
	 * attack.
	 *
	 * @return the weapon's ammo deduction count
	 */
	public int getAmmoDeduction() {
		return ammoDeduction;
	}
	
	/**
	 * Method to process the weapon's basic attack.
	 *
	 * The attack generates the appropriate projectile (as according to the projectile type attribute in the weapon. The
	 * generated projectile will contain all of the relevant information as described in the weapon's attributes.
	 * (relevant information differs between various projectile types)
	 *
	 * The player's statistics are mixed with the native weapon stats in this method.
	 *
	 * @param velX       x direction velocity of projectile
	 * @param velY       y direction velocity of projectile
	 * @param statSpeed  Player's statistical affect on projectile speed
	 * @param statDamage Player's statistical affect on projectile damage
	 */
	public int basicAttack(Entity owner, float velX, float velY, int statSpeed, int statDamage) {
		World world = World.getInstance();

		if (this.multishot) {
			int shotNum = this.shots;
			double angle = Math.atan2(velY, velX);
			ArrayList<CustomBullet> bullets = new ArrayList<>();
			for (int i = 0; i < shotNum; i++) {
				double newAngle = angle + ((((i-shotNum/2) * shotArc) + (Math.random() * 2 - 1)) * Math.PI/180) ;
				float newVelX = (float)Math.cos(newAngle);
				float newVelY = (float)Math.sin(newAngle);
				CustomBullet bullet = new CustomBullet(owner, newVelX, newVelY, this.bulletSpeed + statSpeed, 
						(this.damage + statDamage)/shotNum, this.range, this.travelSprite, 0.3f, 0.15f, null, 0, 0);
				bullets.add(bullet);
			}
			for (CustomBullet bullet : bullets) {
				world.addEntity(bullet);
			}

		} else {
			if (this.projectileType == ProjectileType.BULLET) {
				GenericBullet bullet = new GenericBullet(owner, velX, velY, this.bulletSpeed + statSpeed,
						this.damage + statDamage, this.travelSprite);
				world.addEntity(bullet);
			} else if (this.projectileType == ProjectileType.GRENADE) {
				GrenadeBullet nade = new GrenadeBullet(owner, velX, velY, this.bulletSpeed + statSpeed,
						this.grenadeTime, this.damage + statDamage, this.grenadeRadius, this.travelSprite, true);
				world.addEntity(nade);
			} else if (this.projectileType == ProjectileType.MELEE) {
				GenericBlade blade = new GenericBlade(owner, this.damage + statDamage, this.actionSprite,
						this.swingTime);
				world.addEntity(blade);
			} else if (this.projectileType == ProjectileType.ROCKET) { // sivi
				GrenadeBullet rocket = new GrenadeBullet(owner, velX, velY, this.bulletSpeed + statSpeed,
						0, this.damage + statDamage, this.grenadeRadius, this.travelSprite, false);
				world.addEntity(rocket);
			} else if (this.projectileType == ProjectileType.LAZOR) { // sivi
				GenericBullet lazor = new GenericBullet(owner, velX, velY, this.bulletSpeed + statSpeed,
						this.damage + statDamage, this.actionSprite);
				world.addEntity(lazor);
			} else if (this.projectileType == ProjectileType.CUSTOM_BULLET) {
				CustomBullet bullet = new CustomBullet(owner, velX, velY, this.bulletSpeed + statSpeed, this.damage + statDamage
						, this.range, this.travelSprite, this.travelWidth, this.travelHeight,
						this.actionSprite, this.actionWidth, this.actionHeight);
				world.addEntity(bullet);
			} else if (this.projectileType == ProjectileType.PORTAL) {
				PortalBullet bullet = new PortalBullet(owner, velX, velY, this.bulletSpeed + statSpeed, portal);
				if(((Player) owner).getPortal(portal) != null) {
					((Player) owner).killPortal(portal);
				}
				((Player) owner).setPortal(bullet, portal);
				world.addEntity(bullet);
				portal = !portal;
			} else if (this.projectileType == ProjectileType.FLAME) {
				FireProjectile bullet = new FireProjectile(owner, velX, velY, this.bulletSpeed + statSpeed, 10);
				world.addEntity(bullet);
			}
		}
		return 1;
	}

	protected static class WeaponBuilder extends Item.Builder {

		private int damage;
		private int bulletSpeed;
		private int firingRate;
		private ProjectileType projectileType;
		private int grenadeRadius;
		private int grenadeTime;
		private int swingTime;
		private SpriteList actionSprite;
		private SpriteList travelSprite;
		private float travelWidth;
		private float travelHeight;
		private int accuracy;
		private float actionWidth;
		private float actionHeight;
		private int range;
		private boolean multishot;
		private int shots;
		private int shotArc;
		private boolean soilAdder;
		private int ammoDeduction = 1;

		/**
		 * The same builder as used in the item class
		 */
		public WeaponBuilder(String id, String name, Sprite sprite, String description, ItemType type) {
			super(id, name, sprite, description, type);
		}

		public WeaponBuilder damage(int damage) {
			this.damage = damage;
			return this;
		}

		public WeaponBuilder bulletSpeed(int bulletSpeed) {
			this.bulletSpeed = bulletSpeed;
			return this;
		}

		public WeaponBuilder rate(int firingRate) {
			this.firingRate = firingRate;
			return this;
		}

		public WeaponBuilder projectileType(ProjectileType projectileType) {
			this.projectileType = projectileType;
			return this;
		}

		public WeaponBuilder radius(int radius) {
			this.grenadeRadius = radius;
			return this;
		}

		public WeaponBuilder grenadeTime(int time) {
			this.grenadeTime = time;
			return this;
		}

		public WeaponBuilder swingTime(int time) {
			this.swingTime = time;
			return this;
		}

		public WeaponBuilder actionSprite(SpriteList spriteID) {
			this.actionSprite = spriteID;
			return this;
		}

		public WeaponBuilder travelSprite(SpriteList spriteID) {
			this.travelSprite = spriteID;
			return this;
		}

		public WeaponBuilder travelWidth(float travelWidth) {
			this.travelWidth = travelWidth;
			return this;
		}

		public WeaponBuilder travelHeight(float travelHeight) {
			this.travelHeight = travelHeight;
			return this;
		}

		public WeaponBuilder accuracy(int accuracy) {
			this.accuracy = accuracy;
			return this;
		}

		public WeaponBuilder actionWidth(float actionWidth) {
			this.actionWidth = actionWidth;
			return this;
		}

		public WeaponBuilder actionHeight(float actionHeight) {
			this.actionHeight = actionHeight;
			return this;
		}

		public WeaponBuilder range(int range) {
			this.range = range;
			return this;
		}

		public WeaponBuilder multishot() {
			this.multishot = true;
			return this;
		}

		public WeaponBuilder shots(int shots) {
			this.shots = shots;
			return this;
		}

		public WeaponBuilder shotArc(int arc) {
			this.shotArc = arc;
			return this;
		}

		public WeaponBuilder soilAdder(boolean soilerAdder) {
			this.soilAdder = soilerAdder;
			return this;
		}

		public WeaponBuilder ammoDeduction(int deduction) {
			this.ammoDeduction = deduction;
			return this;
		}
		
		public Weapon build() {
			return new Weapon(this);
		}
	}


	public Weapon(WeaponBuilder builder) {
		super(builder);
		damage = builder.damage;
		bulletSpeed = builder.bulletSpeed;
		firingRate = builder.firingRate;
		projectileType = builder.projectileType;
		grenadeRadius = builder.grenadeRadius;
		grenadeTime = builder.grenadeTime;
		swingTime = builder.swingTime;
		actionSprite = builder.actionSprite;
		travelSprite = builder.travelSprite;
		travelWidth = builder.travelWidth;
		travelHeight = builder.travelHeight;
		accuracy = builder.accuracy;
		actionWidth = builder.actionWidth;
		actionHeight = builder.actionHeight;
		range = builder.range;
		multishot = builder.multishot;
		soilAdder = builder.soilAdder;
		shots = builder.shots;
		shotArc = builder.shotArc;
		ammoDeduction = builder.ammoDeduction;
	}

}
