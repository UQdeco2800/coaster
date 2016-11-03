package uq.deco2800.coaster.game.entities.npcs.companions;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import uq.deco2800.coaster.graphics.notifications.Toaster;
import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.ItemEntity;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.PowerUp;
import uq.deco2800.coaster.game.entities.npcs.AttackableNPC;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;
import uq.deco2800.coaster.game.entities.npcs.NPCType;
import uq.deco2800.coaster.game.entities.npcs.npcactions.DefaultMove;
import uq.deco2800.coaster.game.entities.npcs.npcactions.Jump;
import uq.deco2800.coaster.game.items.Item;
import uq.deco2800.coaster.game.items.ItemRegistry;
import uq.deco2800.coaster.game.items.ItemType;
import uq.deco2800.coaster.graphics.Viewport;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * TreasureNPC is the base class for Enemy NPCs classed as Ranged attackers.
 * TreadureNPC extends from BaseNPC and includes its associated methods. It
 * implements AttackableNPC in order for it to target players. It provides
 * fields for NPC attributes and implements an defined attacking structure. It
 * provides public methods to retrieve such information about the NPC as well as
 * those to manipulate NPC and its attributes. Additionally it contains default
 * methods for implementing behavior which is similar across multiple NPCs, and
 * helper methods to determine such behavior.
 *
 * NPC attacking behaviour is based on a targeting system that looks for the
 * closest entities to engage with projectiles.
 */
public class CompanionNPC extends BaseNPC implements AttackableNPC {
	// private boolean idleMoveRight; // true if right, false if left
	private Player owner;
	private long timetospawn = 25000;
	private int npclevel = 1;
	private CompanionType companionStyle;

	/**
	 * Constructor for the TreasureNPC
	 */
	public CompanionNPC(Player owner) {
		/* Define basic properties */
		currentHealth = 75;
		myType = NPCType.ALLY;
		maxHealth = 75 * npclevel;
		this.owner = owner;
		companionStyle = CompanionType.ATTACK;
		/* Set sprites */
		hurtByProjectiles = false;
		sprites.put(EntityState.STANDING, new Sprite(SpriteList.COMPANION_ATTACK_STAND));
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.COMPANION_ATTACK_MOVING));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.COMPANION_ATTACK_STAND));
		setSprite(sprites.get(EntityState.MOVING));

		/* Define proportions and attributes */
		bounds = new AABB(posX, posY, 2f, 2f);
		moveSpeedHor = 6;
		visionRangeHor = 30;
		visionRangeVer = 30;
		attackRangeHor = 1;
		jumpSpeed = -15;
		baseDamage = 5;
		availableActions.add(new DefaultMove(this));
		availableActions.add(new Jump(this));
	}

	/**
	 * Called each tick to determine the behaviour of this MeleeEnemyNPC
	 */
	@Override
	protected void stateUpdate(long ms) {
		determineNextAction();
	}

	@Override
	/**
	 * Unique logic for this NPC. Called every tick, this method determines how
	 * the NPC should behave right now. Whether that be from deriving a new
	 * action based on the current state, or completing the sequence of actions
	 * defined for this NPC in a previous call to this method. The priority is
	 * completing the actions already in the queue.
	 *
	 */
	public void determineNextAction() {

		/* Sets the player and teleports companion if it strays to far */
		Player target = determinePriorityTarget();
		distanceFromPlayer = getDistanceFromPlayer(target);
		BaseNPC enemyTarget = determineNPCEnemyTarget();
		ItemEntity itemToTarget = determineNPCItemTarget();
		if (distanceFromPlayer > 40f) {
			this.setX(owner.getX());
			this.setY(owner.getY());
		}

		// Enemy to Target

		if (this.companionStyle.ordinal() == 0 && enemyTarget != null) {
			// Attacking Enemy Mobs
			int distanceFromTarget = this.getDistanceFromTarget(enemyTarget);
			/* Check range of player */
			boolean withinRange = distanceFromTarget < Math
					.sqrt(visionRangeHor * visionRangeHor + visionRangeVer * visionRangeVer);
			if (withinRange) {
				determineFacingDirectionNPC(enemyTarget, false);
			} else {
				determineFacingDirection(this.owner, false);
			}
			if (attackRangeHor >= distanceFromTarget) {
				enemyTarget.receiveDamage(baseDamage * npclevel, this);
			}
			if (this.getDistanceFromPlayer(this.owner) > 10) {
				determineFacingDirection(this.owner, false);

			}
			animateMovement();
		} else if (this.companionStyle.ordinal() == 2 && itemToTarget != null) {
			determineFacingDirectionItemEntity(itemToTarget, false);
			animateMovement();
		} else {
			// Buff Generation
			/* Check range of player */
			boolean withinRange = distanceFromPlayer < Math
					.sqrt(visionRangeHor * visionRangeHor + visionRangeVer * visionRangeVer);
			if (withinRange) {
				determineFacingDirection(target, false);
			}

			animateMovement();

		}
	}

	/**
	 * Returns the target which this MeleeEnemyNPC will target first. Currently
	 *
	 * @return the closest Player if one exists
	 */
	@Override
	public Player determinePriorityTarget() {
		return (Player) this.getClosest(new ArrayList<>(world.getPlayerEntities()));
	}

	/***
	 * Method that determines the nearest instance of BaseNPC to target and
	 * attack
	 * 
	 * @return Returns the nearest BaseNPC which is attackable if no enemy is
	 *         nearby then return an instance of its self.
	 */
	public BaseNPC determineNPCEnemyTarget() {
		if (!this.getNearbyAttackableNPCs(visionRangeHor).isEmpty()) {
			ArrayList<Entity> otherNPCEntities = new ArrayList<>(this.getNearbyAttackableNPCs(visionRangeHor));
			otherNPCEntities.remove(this);
			return (BaseNPC) this.getClosest(otherNPCEntities);

		} else {
			return this;

		}

	}

	/***
	 * Method that determines the nearest instance of ItemEntity for the
	 * follower to approach and collect
	 * 
	 * @return Returns the nearest item entity to the CompanionNPC if no
	 *         ItemEntities are around returns null;
	 */
	public ItemEntity determineNPCItemTarget() {
		if (world.getItemEntities().size() > 0) {
			ArrayList<Entity> itemEntityList = new ArrayList<>(world.getItemEntities());
			return (ItemEntity) this.getClosest(itemEntityList);
		} else {
			return null;
		}
	}

	/***
	 * Method for moving the Companion each tick.
	 */
	private void animateMovement() {
		/* Animate NPC */
		if (shouldJump(this.getFacing())) {
			availableActions.get(1).execute();
		} else {
			if (distanceFromPlayer < 4) {
				// Do not move closer
			} else {
				availableActions.get(0).execute();
			}

		}

	}

	@Override
	/**
	 * Top-level method to run every game tick.
	 */
	protected void tick(long ms) {
		stateUpdate(ms);
		if (this.getCurrentHealth() <= 0) {
			// The companion can never DIE MWHAHAHA!
			this.currentHealth = maxHealth;

		}
		switch (this.companionStyle.ordinal()) {

		case 0:
			setSpritesAttack();
			break;
		case 1:
			setSpritesSupport();
			timetospawn = timetospawn - ms;
			if (timetospawn < 30) {
				generatePowerUp();
			}
			break;
		case 2:
			setSpritesDefense();
			break;

		default:
			setSpritesAttack();
			break;

		}
	}

	@Override
	protected void onDeath(Entity cause) {
		// Method for the rare death of the companion
		Toaster.toast("You killed me...");
		this.delete();
		this.owner.setCompanion(false);

	}

	/***
	 * Getter method for the owner of the companion, will return a player
	 * instance
	 * 
	 * @return Player who the companion is linked to.
	 */
	public Player getOwner() {
		return this.owner;
	}

	@SuppressWarnings("static-access")
	/***
	 * Setter method for the companion class on each call it advanced to the
	 * next CompanionType.
	 * 
	 */
	public void setCompanionClass() {

		if (this.companionStyle.ordinal() < this.companionStyle.values().length - 1) {
			this.companionStyle = this.companionStyle.values()[this.companionStyle.ordinal() + 1];

		} else {
			this.companionStyle = this.companionStyle.values()[0];

		}

	}

	/***
	 * Method to return the ordinal of the companion class
	 * 
	 * @return Int corresponding to the ENUM type of the class.
	 */
	public int getCompanionClass() {
		return this.companionStyle.ordinal();
	}

	/***
	 * Abstracted method to set the attack sprites
	 */
	private void setSpritesAttack() {
		sprites.put(EntityState.STANDING, new Sprite(SpriteList.COMPANION_ATTACK_STAND));
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.COMPANION_ATTACK_MOVING));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.COMPANION_ATTACK_STAND));
		setSprite(sprites.get(EntityState.MOVING));

	}

	/***
	 * Abstracted method to set the support sprites
	 */
	private void setSpritesSupport() {
		sprites.put(EntityState.STANDING, new Sprite(SpriteList.COMPANION_SUPPORT_STAND));
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.COMPANION_SUPPORT_MOVING));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.COMPANION_SUPPORT_STAND));
		setSprite(sprites.get(EntityState.MOVING));

	}

	/***
	 * Abstracted method to set the defensive sprites
	 */
	private void setSpritesDefense() {
		// Defense
		sprites.put(EntityState.STANDING, new Sprite(SpriteList.COMPANION_DEFENSE_STAND));
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.COMPANION_DEFENSE_MOVING));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.COMPANION_DEFENSE_STAND));
		setSprite(sprites.get(EntityState.MOVING));

	}

	/***
	 * Method that upgrades the companion.
	 */
	public void upgradeCompanion() {
		if (this.npclevel < 3) {
			this.npclevel += 1;
			Toaster.toast("Companion has been upgraded to level " + this.npclevel);
			adjustStats(this.npclevel);
		}
	}

	/***
	 * Getter method for NPC level
	 * 
	 * @return INT of current NPC Level
	 */
	public int getNPCLevel() {
		return this.npclevel;
	}

	/***
	 * Method that abstracts the generation of powerup for the companion class.
	 */
	public void generatePowerUp() {
		// Generation of powerups for the companion to spawn
		Random rn = new Random();
		int criticalThrow = rn.nextInt(5);
		boolean directionBool = rn.nextBoolean();
		int direction;
		if (directionBool) {
			direction = 1;
		} else {
			direction = -1;
		}

		Item testItem = ItemRegistry.randomItem(ItemType.POWERUP);
		PowerUp newdrop;
		if (testItem.getID().contains("Health")) {
			newdrop = new PowerUp(testItem, "health", 55, 15, 10000);
		} else if (testItem.getID().contains("Speed")) {
			newdrop = new PowerUp(testItem, "speed", 1.5f, 10000, 10000);
		} else if (testItem.getID().contains("Mana")) {
			newdrop = new PowerUp(testItem, "mana", 100, 15, 10000);
		} else if (testItem.getID().contains("Shield")) {
			newdrop = new PowerUp(testItem, "shield", 1f, 10000, 10000);
		} else if (testItem.getID().contains("Weapon")) {
			newdrop = new PowerUp(testItem, "weapon", 2, 15000, 10000);
		} else {
			newdrop = new PowerUp(testItem, "health", 55, 15, 10000);
		}
		newdrop.setPosition(this.getX() + direction * criticalThrow, this.getY());
		world.addEntity(newdrop);
		timetospawn = 15000;

	}

	/***
	 * Setter method to set the new attributes of the companion on upgrade
	 * relative to NPClevel
	 * 
	 * @param npclevel
	 *            The current level of the npc that the stats should reflect
	 */
	private void adjustStats(int npclevel) {
		/* Define proportions and attributes */
		this.bounds = new AABB(posX, posY, 2f * npclevel, 2f * npclevel);
		this.moveSpeedHor = 3 * npclevel;
		this.visionRangeHor = 30 * npclevel;
		this.visionRangeVer = 30 * npclevel;
		this.attackRangeHor = 1 * npclevel;
		this.jumpSpeed = -15 * npclevel;
		this.baseDamage = 5 * npclevel;

	}

	@Override
	/***
	 * overwrittem method to place healthbar above the companion and set the
	 * colour of it so that it matches with the player.
	 */
	public void render(GraphicsContext gc, Viewport viewport, long ms) {
		super.render(gc, viewport, ms);
		float tileSize = viewport.getTileSideLength();
		int leftBorder = viewport.getLeftBorder();
		int topBorder = viewport.getTopBorder();

		int left = (int) Math.floor(viewport.getLeft());
		int top = (int) Math.floor(viewport.getTop());

		float subTileShiftX = (viewport.getLeft() - left) * tileSize;
		float subTileShiftY = (viewport.getTop() - top) * tileSize;

		float x = (posX - left) * tileSize + leftBorder - subTileShiftX;
		float y = (posY - top) * tileSize + topBorder - subTileShiftY;

		float healthPercent = currentHealth / (float) maxHealth;
		// Green bit
		gc.setFill(Color.web("#7FFF00"));
		gc.fillRect(x, y - (tileSize / 2), bounds.getWidth() * tileSize * healthPercent, tileSize / 10);

		// Empty bit
		gc.setFill(new Color(0.3, 0.3, 0.3, 1));
		gc.fillRect(x + bounds.getWidth() * tileSize * healthPercent, y - (tileSize / 2),
				bounds.getWidth() * tileSize * (1 - healthPercent), tileSize / 10);
	}
}