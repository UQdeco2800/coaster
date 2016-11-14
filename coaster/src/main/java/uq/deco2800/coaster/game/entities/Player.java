package uq.deco2800.coaster.game.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.coaster.core.input.GameAction;
import uq.deco2800.coaster.core.input.GameInput;
import uq.deco2800.coaster.core.input.InputManager;
import uq.deco2800.coaster.core.sound.SoundCache;
import uq.deco2800.coaster.game.commerce.PlayerCommerce;
import uq.deco2800.coaster.game.debug.Debug;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;
import uq.deco2800.coaster.game.entities.npcs.DuckKingNPC;
import uq.deco2800.coaster.game.entities.npcs.companions.CompanionNPC;
import uq.deco2800.coaster.game.entities.npcs.mounts.Mount;
import uq.deco2800.coaster.game.entities.npcs.mounts.PlayerMountActions;
import uq.deco2800.coaster.game.entities.particles.Particle;
import uq.deco2800.coaster.game.entities.skills.*;
import uq.deco2800.coaster.game.entities.weapons.LazorParticle;
import uq.deco2800.coaster.game.entities.weapons.PortalBullet;
import uq.deco2800.coaster.game.entities.weapons.ProjectileType;
import uq.deco2800.coaster.game.inventory.Inventory;
import uq.deco2800.coaster.game.items.Armour;
import uq.deco2800.coaster.game.items.ItemRegistry;
import uq.deco2800.coaster.game.items.Weapon;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.mechanics.Side;
import uq.deco2800.coaster.game.tutorial.PlayerInstruction;
import uq.deco2800.coaster.game.world.Chunk;
import uq.deco2800.coaster.game.world.MiniMap;
import uq.deco2800.coaster.game.world.RoomWorld;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.LayerList;
import uq.deco2800.coaster.graphics.Viewport;
import uq.deco2800.coaster.graphics.Window;
import uq.deco2800.coaster.graphics.notifications.IngameText;
import uq.deco2800.coaster.graphics.notifications.Toaster;
import uq.deco2800.coaster.graphics.screens.controllers.SkillTreeController;
import uq.deco2800.coaster.graphics.sprites.AngledSpriteRelation;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;
import uq.deco2800.coaster.graphics.sprites.SpriteRelation;

import java.util.*;

import static uq.deco2800.coaster.core.input.InputManager.justPressed;


public class Player extends BasicMovingEntity {

	Logger logger = LoggerFactory.getLogger(SkillTreeController.class);

	protected static final float EPSILON = 0.00001f;


	protected static final long SPECIAL_ATTACK_THRESHOLD = 2000;
	// Need to hold the button for 2 seconds
	protected static final long SPECIAL_ATTACK_FIRE_DURATION = 1000;
	// The lazor fires for 1 second

	public int viewDistance = 1200; // view distance in pixels

	protected float wallSlidingFallModifier = 0.5f;
	protected float wallSlidingTerminalModifier = 0.2f;

	protected float dashSpeed = 25f;
	protected long dashDuration = 250L; // 250 ms

	protected float slideSpeed = 12.5f;
	protected long slideDuration = 500L; // 500 ms

	protected float wallJumpSpeedModifier = 2f;
	protected long wallJumpDuration = 100L; // 100 ms

	protected long knockBackDuration = 1000L;
	protected long knockBackEndDuration = 750L;
	protected long knockBackRenderTimer = 0L; // used to flip renderFlag
	protected long knockBackLastRenderTime = 0L;
	protected int knockBackRenderGap = 75;

	protected float knockBackSpeedX = 15f;
	protected float knockBackSpeedY = -10f;

	GameInput playerInput = new GameInput();
	PlayerStats stats = new PlayerStats();
	Inventory inv = new Inventory();
	PlayerInstruction tutorial;// = new PlayerInstruction(this);

	protected static final int TARGET_NPC_KILL_COUNT = 100; // Need to kill 100
	// mobs for the boss
	// spawn

	protected long actionTimer = -1L; // timer used for sliding, dashing and air
	// dashing. 1000f = 1s
	protected long wallJumpTimer = -1L; // Timer used to make wall jump motions
	// feel fluid.
	protected long knockBackTimer = -1L;
	protected boolean airDashAvailable = true;
	protected boolean doubleJumpAvailable = true;
	protected boolean jumpAvailable;
	protected boolean invincible = false;
	protected int inputDir;

	// Set directions for mouse shooting and sprite animation
	protected double aimAngle;
	protected int accuracy = 5;// num of degrees deviation of bullet (will be
	// angle +/- random(accuracy)

	protected MutableDouble armRenderAngle = new MutableDouble();
	protected MutableDouble headRenderAngle = new MutableDouble();
	protected MutableDouble weaponRenderAngle = new MutableDouble();

	protected boolean specialAttackCharging;
	protected boolean specialAttackFiring;
	protected long specialAttackChargeTimer;
	protected long specialAttackFireTimer;

	protected int genericBulletDamage;
	protected int bulletSpeed;
	protected long experiencePoints;
	protected long firingRateTracker;


	// Mount CPS
	private boolean onMount = false;
	private Mount mount = null;
	private boolean talking = false;


	// Companion stuff
	private boolean hasCompanion = false;
	private boolean existingCompanion = false;
	CompanionNPC newCompanion = new CompanionNPC(this);
	// End of Companion Stuff

	protected static final float BASE_JUMP_SPEED = -20f;
	protected static final float BASE_MOVE_SPEED = 10f;

	protected static final float BASE_WIDTH = 1f;
	protected static final float BASE_HEIGHT = 2f;
	protected static final float SLIDE_WIDTH = 2f;
	protected static final float SLIDE_HEIGHT = 1f;
	protected static final float CROUCH_WIDTH = 1f;
	protected static final float CROUCH_HEIGHT = 1.5f;


	protected int healing;
	protected int healTickCount = 0;

	// Commerce
	private PlayerCommerce commerce;

	// Spell cooldown timers to be displayed on the screen.
	// These times are not implemented into spell logic at the moment
	private List<Double> cooldowns = new ArrayList<>();
	private boolean healingActivated = false;


	// weapons + armour
	protected ArrayList<String> activeWeapons = new ArrayList<>();
	private Armour equippedHead;
	private Armour equippedChest;
	private Armour equippedPants;
	private Armour equippedBoots;

	protected Weapon equippedWeapon;

	// Barney - Portals
	private PortalBullet portal1;
	private PortalBullet portal2;

	private List<Active> spells = new ArrayList<>(4);
	private List<Integer> spellPhases = new ArrayList<>();
	private List<Integer> currentSpellPhase = new ArrayList<>();
	private List<ArrayList<Integer>> spellLoopIterations = new ArrayList<>();
	private List<ArrayList<Integer>> spellLoopTimings = new ArrayList<>();

	private List<Integer> currentSpellLoopIteration = new ArrayList<>();
	private List<Integer> currentSpellLoopTiming = new ArrayList<>();
	private List<Boolean> usingSpells = new ArrayList<>();

	private List<Passive> passiveSkill = new ArrayList<Passive>();
	private ActivateSkill allSkills = new ActivateSkill(stats);
	private List<PlayerBuff> buffList = new ArrayList<PlayerBuff>();
	Iterator<PlayerBuff> iter = buffList.iterator();

	protected boolean updateHud = false;
	private Sprite drawSkill;
	private Sprite drawSkill2;
	private String spellKey;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	protected String name;

	private String spellKey2;
	private boolean skillswap = false;
	private List<Sprite> skillSprites = Arrays.asList(null, null, null, null);


	protected Map<BodyPart, SpriteRelation> commonSpriteSet = new HashMap<>();
	protected Map<BodyPart, SpriteRelation> sprintSpriteSet = new HashMap<>();

	protected List<AABB> commonHitboxes = new ArrayList<>();
	protected List<AABB> crouchingHitboxes = new ArrayList<>();
	protected List<AABB> slidingHitboxes = new ArrayList<>();

	private boolean checkPointReached = false;

	private boolean checkPointsEnabled = false;


	/**
	 * The Player class is the entity controlled by the user.
	 */
	public Player() {
		layer = LayerList.PLAYERS;
		setCollisionFilter(e -> this.knockBackTimer < 0);

		sprites.put(EntityState.STANDING, new Sprite(SpriteList.KNIGHT_STANDING));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.KNIGHT_JUMPING));
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.KNIGHT_WALKING));
		sprites.put(EntityState.WALL_SLIDING, new Sprite(SpriteList.KNIGHT_WALLSLIDE));
		sprites.put(EntityState.DASHING, new Sprite(SpriteList.KNIGHT_DASH));
		sprites.put(EntityState.AIR_DASHING, new Sprite(SpriteList.KNIGHT_DASH));
		sprites.put(EntityState.SLIDING, new Sprite(SpriteList.KNIGHT_SLIDE));
		sprites.put(EntityState.CROUCHING, new Sprite(SpriteList.KNIGHT_CROUCH));
		sprites.put(EntityState.AIR_CROUCHING, new Sprite(SpriteList.KNIGHT_CROUCH));
		sprites.put(EntityState.SPRINTING, new Sprite(SpriteList.KNIGHT_SPRINT));
		sprites.put(EntityState.DEAD, new Sprite(SpriteList.KNIGHT_SLIDE));
		sprites.put(EntityState.KNOCK_BACK, new Sprite(SpriteList.KNIGHT_KNOCK_BACK));
		sprites.put(EntityState.INVINCIBLE, new Sprite(SpriteList.CARL));

		this.enableManaBar();
		this.addMana(100);
		this.healing = 1;
		
		this.maxHealth = stats.getMaxHealth();
		this.currentHealth = maxHealth;



		for (int i = 0; i < 4; i++) {
			spellLoopIterations.add(i, new ArrayList<>());
			spellLoopTimings.add(i, new ArrayList<>());
			spellPhases.add(i, 0);
			usingSpells.add(false);
			spells.add(i, null);
			currentSpellPhase.add(i, 0);
			currentSpellLoopTiming.add(i, 0);
			currentSpellLoopIteration.add(i, 0);
			cooldowns.add(i, 0d);
		}

		setSprite(sprites.get(EntityState.JUMPING));
		bounds = new AABB(posX, posY, BASE_WIDTH, BASE_HEIGHT); // Size is 1x2
		// for now

		// dud implementation of the obtaining weapons
		// Subject to change with inventory and weapon drop progression

		activeWeapons.add(ItemRegistry.getItem("Gun1").getID());
		activeWeapons.add(ItemRegistry.getItem("Melee1").getID());
		activeWeapons.add(ItemRegistry.getItem("Gun2").getID());
		activeWeapons.add(ItemRegistry.getItem("Gun3").getID());
		activeWeapons.add(ItemRegistry.getItem("Gun4").getID());
		activeWeapons.add(ItemRegistry.getItem("Gun5").getID());
		activeWeapons.add(ItemRegistry.getItem("Gun7").getID());
		activeWeapons.add(ItemRegistry.getItem("Gun8").getID());
		activeWeapons.add(ItemRegistry.getItem("Gun9").getID());

		// Represents the currently equipped weapon.
		equippedWeapon = (Weapon) ItemRegistry.getItem("Gun1");
		equippedHead = null;
		equippedChest = null;
		equippedPants = null;
		equippedBoots = null;

		// adjust firing Rate - currently average of player stats and native gun
		this.firingRateTracker = (stats.getFiringRate() + equippedWeapon.getFiringRate()) / 2;
		this.moveSpeed = BASE_MOVE_SPEED;
		this.jumpSpeed = BASE_JUMP_SPEED;

		// Add sprites for arm and head
		commonSpriteSet.put(BodyPart.HEAD, new AngledSpriteRelation(new Sprite(SpriteList.KNIGHT_HEAD), this,
				headRenderAngle, 0.0f, 0.05f, 0.8f, 0.7f, 0.5f, 0.5f));
		commonSpriteSet.put(BodyPart.ARM, new AngledSpriteRelation(new Sprite(SpriteList.KNIGHT_ARM), this,
				armRenderAngle, 0.25f, 0.7f, 0.5f, 0.5f, 0.1f, 0.075f));

		sprintSpriteSet.put(BodyPart.HEAD, new AngledSpriteRelation(new Sprite(SpriteList.KNIGHT_HEAD), this,
				headRenderAngle, 0.0f, 0.05f, 0.8f, 0.7f, 0.5f, 0.5f));

		// Add this for standing, jumping and moving
		this.additionalSpritesCache.put(EntityState.STANDING, commonSpriteSet);
		this.additionalSpritesCache.put(EntityState.JUMPING, commonSpriteSet);
		this.additionalSpritesCache.put(EntityState.MOVING, commonSpriteSet);
		this.additionalSpritesCache.put(EntityState.SPRINTING, sprintSpriteSet);

		commonHitboxes.add(defineHitbox(BodyPart.HEAD, 0.8f, 0.7f, 0.0f, 0.05f));

		this.hitboxesCache.put(EntityState.STANDING, commonHitboxes);
		this.hitboxesCache.put(EntityState.JUMPING, commonHitboxes);
		this.hitboxesCache.put(EntityState.MOVING, commonHitboxes);
		this.hitboxesCache.put(EntityState.WALL_SLIDING, commonHitboxes);
		this.hitboxesCache.put(EntityState.DASHING, commonHitboxes);
		this.hitboxesCache.put(EntityState.AIR_DASHING, commonHitboxes);
		this.hitboxesCache.put(EntityState.SPRINTING, commonHitboxes);
		this.hitboxesCache.put(EntityState.DEAD, commonHitboxes);
		this.hitboxesCache.put(EntityState.KNOCK_BACK, commonHitboxes);

		this.hitboxesCache.put(EntityState.SLIDING, slidingHitboxes);

		this.hitboxesCache.put(EntityState.CROUCHING, crouchingHitboxes);
		this.hitboxesCache.put(EntityState.AIR_CROUCHING, crouchingHitboxes);

		setState(EntityState.JUMPING);

		this.strafeActive = true;

		this.firingRateTracker = stats.getFiringRate();
		this.bulletSpeed = 60;
		this.genericBulletDamage = stats.getDamage();
		this.accuracy = equippedWeapon.getAccuracy() + 5;
		this.stats.setActivateSkill(allSkills);

		this.commerce = new PlayerCommerce();

	}

	public void initTutorial() {
		tutorial = new PlayerInstruction(this);
	}

	public void clear() {
		playerInput = new GameInput();
		stats = new PlayerStats();
		inv = new Inventory();
		allSkills = new ActivateSkill(stats);
		firstTick = true;
	}

	/**
	 * Returns the players commerce object which provides an interface to
	 * Commerce related functionality.
	 *
	 * @return the players PlayerCommerce instance.
	 */
	public PlayerCommerce getCommerce() {
		return commerce;
	}

	/**
	 * Method to update the list of active weapons in the player's inventory
	 */
	public void updateWeapons() {
		for (int i = 0; i < 4; i++) {
			String id = inv.getItemID("Active Inventory", i);
			activeWeapons.set(i, id);
		}
	}

	/**
	 * Method to update boots armour hit boxes for the player
	 */
	public void updateBootsArmour() {
		String id = inv.getItemID("Passive Inventory", 3);
		int rank;
		if (id == "emptyslot") {
			if (equippedBoots != null) {
				rank = equippedBoots.getRank();
				if (rank == 1) {
					removeHitbox(BodyPart.FOOT_ARMOUR_WEAK);
				} else if (rank == 2) {
					removeHitbox(BodyPart.FOOT_ARMOUR_MED);
				} else {
					removeHitbox(BodyPart.FOOT_ARMOUR_STRONG);
				}
			}
			equippedBoots = null;
		} else if ((equippedBoots == null) || (id != equippedBoots.getID())) {
			equippedBoots = (Armour) ItemRegistry.getItem(id);
			rank = equippedBoots.getRank();
			if (rank == 1) {
				commonHitboxes.add(defineHitbox(BodyPart.FOOT_ARMOUR_WEAK, 0.8f, 0.65f, 0.0f, 0.0f));
			} else if (rank == 2) {
				commonHitboxes.add(defineHitbox(BodyPart.FOOT_ARMOUR_MED, 0.8f, 1f, 0.2f, 0.7f));
			} else {
				commonHitboxes.add(defineHitbox(BodyPart.FOOT_ARMOUR_STRONG, 0.8f, 0.65f, 0.0f, 0.0f));
			}
		}
	}

	/**
	 * Method to update pants armour hit boxes for the player
	 */
	public void updatePantsArmour() {
		String id = inv.getItemID("Passive Inventory", 2);
		int rank;
		if (id == "emptyslot") {
			if (equippedPants != null) {
				rank = equippedPants.getRank();
				if (rank == 1) {
					removeHitbox(BodyPart.LEG_ARMOUR_WEAK);
				} else if (rank == 2) {
					removeHitbox(BodyPart.LEG_ARMOUR_MED);
				} else {
					removeHitbox(BodyPart.LEG_ARMOUR_STRONG);
				}
			}
			equippedPants = null;
		} else if ((equippedPants == null) || (id != equippedPants.getID())) {
			equippedPants = (Armour) ItemRegistry.getItem(id);
			rank = equippedPants.getRank();
			if (rank == 1) {
				commonHitboxes.add(defineHitbox(BodyPart.LEG_ARMOUR_WEAK, 0.9f, 1.5f, 0.3f, 0.8f));
			} else if (rank == 2) {
				commonHitboxes.add(defineHitbox(BodyPart.LEG_ARMOUR_MED, 0.9f, 1.5f, 0.3f, 0.9f));
			} else {
				commonHitboxes.add(defineHitbox(BodyPart.LEG_ARMOUR_STRONG, 0.9f, 1.5f, 0.3f, 0.8f));
			}
		}
	}

	/**
	 * Method to update armour hit boxes for the player
	 */
	public void updateChestArmour() {
		String id = inv.getItemID("Passive Inventory", 1);
		int rank;

		if (id == "emptyslot") {
			if (equippedChest != null) {
				rank = equippedChest.getRank();
				if (rank == 1) {
					removeHitbox(BodyPart.BODY_ARMOUR_WEAK);
				} else if (rank == 2) {
					removeHitbox(BodyPart.BODY_ARMOUR_MED);
				} else {
					removeHitbox(BodyPart.BODY_ARMOUR_STRONG);
				}
			}
			equippedChest = null;
		} else if ((equippedChest == null) || (id != equippedChest.getID())) {
			equippedChest = (Armour) ItemRegistry.getItem(id);
			rank = equippedChest.getRank();
			if (rank == 1) {
				commonHitboxes.add(defineHitbox(BodyPart.BODY_ARMOUR_WEAK, 0.8f, 1f, 0.2f, 0.7f));
			} else if (rank == 2) {
				commonHitboxes.add(defineHitbox(BodyPart.BODY_ARMOUR_MED, 0.8f, 1f, 0.2f, 0.7f));
			} else {
				commonHitboxes.add(defineHitbox(BodyPart.BODY_ARMOUR_STRONG, 0.8f, 1f, 0.2f, 0.7f));
			}
		}
	}

	/**
	 * Method to update armour hit boxes for the player
	 */
	public void updateHeadArmour() {
		String id = inv.getItemID("Passive Inventory", 0);
		int rank;

		if (id == "emptyslot") {
			if (equippedHead != null) {
				rank = equippedHead.getRank();
				if (rank == 1) {
					removeHitbox(BodyPart.HEAD_ARMOUR_WEAK);
				} else if (rank == 2) {
					removeHitbox(BodyPart.HEAD_ARMOUR_MED);
				} else {
					removeHitbox(BodyPart.HEAD_ARMOUR_STRONG);
				}
			}
			equippedHead = null;
		} else if ((equippedHead == null) || (id != equippedHead.getID())) {
			equippedHead = (Armour) ItemRegistry.getItem(id);
			rank = equippedHead.getRank();
			if (rank == 1) {
				commonHitboxes.add(defineHitbox(BodyPart.HEAD_ARMOUR_WEAK, 0.8f, 0.65f, 0.0f, 0.0f));
			} else if (rank == 2) {
				commonHitboxes.add(defineHitbox(BodyPart.HEAD_ARMOUR_MED, 0.8f, 0.65f, 0.0f, 0.0f));
			} else {
				commonHitboxes.add(defineHitbox(BodyPart.HEAD_ARMOUR_STRONG, 0.8f, 0.65f, 0.0f, 0.0f));
			}
		}
	}


	public void setHealing(int healing) {
		this.healing = healing;
	}

	/**
	 * Makes the Player unable to take damage
	 */
	public void setInvincible() {
		this.invincible = true;
	}

	/**
	 * Makes the Player susceptible to damage
	 */
	public void disableInvinciblity() {
		this.invincible = false;
	}

	/**
	 * Method to add current playerBuff's to player
	 */
	public void addPlayerBuff(PlayerBuff playerBuff) {
		buffList.add(playerBuff);
	}

	/**
	 * Getter method for obtaining the player's current chest armour. Used in
	 * tests.
	 *
	 * @return player's currently equipped chest armour
	 */
	public Armour getEquippedChest() {
		return equippedChest;
	}

	/**
	 * Getter method for obtaining the player's current head armour. Used in
	 * tests.
	 *
	 * @return player's currently equipped chest armour
	 */
	public Armour getEquippedHead() {
		return equippedHead;
	}

	/**
	 * Getter method for obtaining the player's current weapon Used in tests and
	 * weapon HUD screen
	 *
	 * @return player's currently equipped weapon
	 */
	public Weapon getEquippedWeapon() {
		return equippedWeapon;
	}

	/**
	 * Getter method for obtaining the player's Inventory. Used to manage items
	 *
	 * @return player's current Inventory
	 */
	public Inventory getInventory() {
		return inv;
	}

	/**
	 * Getter method for obtaining the player's currently active weapons Used in
	 * tests
	 *
	 * @return arraylist of the player's currently active weapons
	 */
	public List<String> getActiveWeapons() {
		return activeWeapons;
	}


	/**
	 * Updates the players state through user button presses.
	 *
	 * @param ms millisecond tick the player attack is being handled on
	 */

	@Override
	protected void stateUpdate(long ms) {

		// Attacks
		boolean basicAttackPressed = InputManager.getActionState(GameAction.BASIC_ATTACK);
		boolean specialAttackPressed = InputManager.getActionState(GameAction.SPECIAL_ATTACK);
		playerAttack(ms, basicAttackPressed, specialAttackPressed);

		List<Boolean> skillKeys = new ArrayList<>();
		skillKeys.add(0, justPressed(GameAction.SKILL_KEY_Q));
		skillKeys.add(1, justPressed(GameAction.SKILL_KEY_E));
		skillKeys.add(2, justPressed(GameAction.SKILL_KEY_R));

		skillKeys.add(3, justPressed(GameAction.SKILL_KEY_W));

		// Blake
		boolean companionModePressed = justPressed(GameAction.CHANGE_MODE);
		boolean companionUpgradePressed = justPressed(GameAction.UPGRADE_COMPANION);
		// callum
		skillState(skillKeys);
		updateRenderAngle();
		playerInput.updateGameInput();
		moveStateEntity(ms);

		// change weapons
		weaponChange(Arrays.asList(justPressed(GameAction.WEAPON_ONE), justPressed(GameAction.WEAPON_TWO),
				justPressed(GameAction.WEAPON_THREE), justPressed(GameAction.WEAPON_FOUR),
				justPressed(GameAction.WEAPON_FIVE), justPressed(GameAction.WEAPON_SIX),
				justPressed(GameAction.WEAPON_SEVEN), justPressed(GameAction.WEAPON_EIGHT),
				justPressed(GameAction.WEAPON_NINE)));

		// add sprites for currently equipped weapons
		if (equippedWeapon.getProjectileType() == ProjectileType.MELEE) {
			commonSpriteSet.put(BodyPart.VOID, new AngledSpriteRelation(equippedWeapon.getSprite(), this,
					weaponRenderAngle, 0f, 0f, 0f, 0f, 0f, 0f));
		} else {
			commonSpriteSet.put(BodyPart.VOID, new AngledSpriteRelation(equippedWeapon.getSprite(), this,
					weaponRenderAngle, 0.40f, 0.6f, 1.0f, 1.0f, 0.2f, 0.090f));
		}
		// Companion Stuff
		if (companionModePressed) {
			newCompanion.setCompanionClass();
			if (!hasCompanion) {
				hasCompanion = true;
			}
		}


		if (companionUpgradePressed && this.getCommerce().getGold() > 300) {
			this.getCommerce().reduceGold(300);
			newCompanion.upgradeCompanion();

		}

		if (justPressed(GameAction.ACTIVATE_MOUNT)) {
			PlayerMountActions.toggleMount(this);
		}


		if (world.getTutorialMode() && !tutorial.checkCompletion()) {
			if (tutorial.tutorialPassed() || justPressed(GameAction.SKIP_TUTORIAL)) {
				tutorial.nextCommand();
			} else {
				tutorial.resendInstruction();
			}
		}
	}

	/**
	 * Returns true if player is on a mount
	 */
	public boolean getOnMountStatus() {
		return this.onMount;
	}

	/**
	 * Sets player mount status
	 */
	public void setOnMountStatus(boolean status) {
		this.onMount = status;
	}

	/**
	 * Get player mount
	 */
	public Mount getMount() {
		return this.mount;
	}

	/**
	 * Sets player mount
	 */
	public void setMount(Mount mount) {
		this.mount = mount;
	}

	/**
	 * Registers player with mount
	 */
	public void saveRider(Mount mount, Player player) {
		mount.registerRider(player);
	}

	/**
	 * Applies passive healing effects
	 */
	private void updateHealing() {
		if (healTickCount == 50) {
			if (healingActivated) {
				addHealth(healing);
				healTickCount = 0;
			}
		} else {
			healTickCount++;
		}
	}

	/**
	 * Applies particle effects for dashing/air dashing
	 */
	private void dashParticleEffects() {
		if (currentState == EntityState.DASHING || currentState == EntityState.AIR_DASHING) {
			for (float y = getY(); y < getY() + getHeight(); y += 0.2) {
				Particle particle = new Particle(0, 0, getX() + (0.5f * getWidth()), y, 302, 10, true, false);
				world.addEntity(particle);
			}
		}
	}

	/**
	 * Code below for applying buffs to player based on ticks If a buff exist
	 * apply it once then countdown time remaining each tick when a buff is
	 * removevd remove the buff effect from the player
	 */
	private void updateBuffs(long ms) {
		if (!buffList.isEmpty()) {
			for (PlayerBuff buff : buffList) {
				if (!buff.getApplied()) {
					buff.setApplied();
					applyBuffEffect(buff);
				}
			}

			List<PlayerBuff> toRemove = new ArrayList<PlayerBuff>();
			for (PlayerBuff buff : buffList) {
				buff.countDown(ms);
				if (buff.getTime() < 10) {
					toRemove.add(buff);
				}
			}
			buffList.removeAll(toRemove);

			for (PlayerBuff buff : toRemove) {
				if (buff.getApplied()) {
					removeBuffEffect(buff);
				}
			}
		}

	}

	/**
	 * Barney used for teleporting with portals
	 */
	private void updatePortals() {
		if (this.portal1 != null && this.portal2 != null) {
			if (this.portal1.collides()) {
				teleportPortal(true);
			} else if (this.portal2.collides()) {
				teleportPortal(false);
			}
		}
		if (this.portal1 != null && this.portal2 != null) {
			if (this.portal1.collides()) {
				teleportPortal(true);
			} else if (this.portal2.collides()) {
				teleportPortal(false);
			}
		}

	}

	/**
	 * Tick handler for player
	 *
	 * @param ms millisecond tick the player attack is being handled on
	 */
	@Override
	protected void tick(long ms) {
		if (this.invincible) {
			setSprite(sprites.get(EntityState.INVINCIBLE));
		}
		addMana(1);
		stateUpdate(ms);
		adjustCooldowns(ms);
		updateChestArmour();
		updateHeadArmour();
		updatePantsArmour();
		updateBootsArmour();
		// Skill Controlling
		tickSpells(ms);
		Window.getEngine().setSkillTreeContent(stats.getSkillPoints());
		applyPassiveSkillEffects(allSkills.getAllActiveSkills());
		updateHealing();
		if (stunned) {
			return;
		}
		// particleFX
		dashParticleEffects();

		this.experiencePoints = stats.getExperiencePoints();

		if (specialAttackFiring) {
			tickSpecialAttackFire(ms);
			return; // You can't do anything if you're firin' your lazor.
		}
		tickSpecialAttack(ms);
		updateTimers(ms);

		// This should be if'd
		tickDebug();

		updateBuffs(ms);

		if (hasCompanion) {
			addCompanion();
		} else {
			removeCompanion();
		}

		// CPS
		if (onMount) {
			PlayerMountActions.positionPlayer(this);
		}

		updatePortals();

		MiniMap.updateVisited(this);
	}

	/***
	 * Method for applying buff to current player depending on the PlayerBuff
	 * class parsed into function
	 *
	 * @require newbuff.getStat() = String
	 */
	private void applyBuffEffect(PlayerBuff newBuff) {
		IngameText ingameText;
		SoundCache.play("PowerUp");
		switch (newBuff.getStat()) {

			case "health":
				ingameText = new IngameText("+ HP", 0, 0, 2000, IngameText.textType.STATIC, 0, 1, 0, 1);
				world.setPlayerText(ingameText);

				addHealth((int) newBuff.getModifier());
				break;
			case "mana":
				addMana((int) newBuff.getModifier());

				ingameText = new IngameText("+ MP", 0, 0, 2000, IngameText.textType.STATIC, 0, 0, 1, 1);

				world.setPlayerText(ingameText);

				break;
			case "shield":
				setShielded(true);

				ingameText = new IngameText("GUARD", 0, 0, 2000, IngameText.textType.STATIC, 1, 1, 0, 1);

				world.setPlayerText(ingameText);

				break;
			case "weapon":
				increaseBaseDamage(getBaseDamage());

				ingameText = new IngameText("+ DMG", 0, 0, 2000, IngameText.textType.STATIC, 0, 0, 0, 1);

				world.setPlayerText(ingameText);
				break;
			case "speed":
				ingameText = new IngameText("+ SPD", 0, 0, 2000, IngameText.textType.STATIC, 0, 0, 0, 1);
				world.setPlayerText(ingameText);

				moveSpeed *= newBuff.getModifier();
				if (moveSpeed > 30) {
					moveSpeed = 30;
				}
				break;
			case "map":
				MiniMap.visitChunk();
				break;
			default:
				Toaster.toast("This is a unfortunate event...");
		}
	}

	/**
	 * Method for removing buff to current player depending on the PlayerBuff
	 * class parsed into function
	 *
	 * @require newbuff.getStat() = String
	 */
	private void removeBuffEffect(PlayerBuff newBuff) {
		if ("shield".equals(newBuff.getStat())) {
			boolean shieldBuffLeft = false;
			for (PlayerBuff buff : buffList) {
				if ("shield".equals(buff.getStat())) {
					shieldBuffLeft = true;
				}
			}
			if (!shieldBuffLeft) {
				setShielded(false);
			}
		} else if ("weapon".equals(newBuff.getStat())) {
			increaseBaseDamage(-getBaseDamage() / 2);
		} else if ("speed".equals(newBuff.getStat())) {
			moveSpeed /= newBuff.getModifier();
			if (moveSpeed < BASE_MOVE_SPEED) {
				moveSpeed = BASE_MOVE_SPEED;
			}
		}
	}

	private void applyPassiveSkillEffects(List<Passive> skills) {
		if (getSkillUnlocked("Healing")) {
			healingActivated = true;
		}
		if (getSkillUnlocked("Health boost")) {
			this.maxHealth = stats.getMaxHealth();
		}
		if (getSkillUnlocked("Mana boost")) {
			this.maxMana = stats.getMana();
		}
	}

	private void addCompanion() {
		if (!existingCompanion && hasCompanion) {
			newCompanion.setPosition(this.getX() + 10f, this.getY() - 15);
			world.addEntity(newCompanion);
			existingCompanion = true;
		}
	}

	/***
	 * Remove NPC companion
	 */
	private void removeCompanion() {
		existingCompanion = false;
		hasCompanion = false;
	}

	/***
	 * Setter method for companion
	 *

	 * @param value Boolean value as to whether the companion should be spawned
	 *            in world or not
	 */
	public void setCompanion(boolean value) {
		this.hasCompanion = value;

	}

	/***
	 * Getter method for hasCompanion
	 *
	 * @return Boolean value of has Companion
	 */
	public boolean getCompanionStatus() {
		return this.hasCompanion;
	}

	/***
	 * Getter method for companion
	 *
	 * @return Returns player's instance of Companion
	 */
	public CompanionNPC getCompanionNPC() {
		return newCompanion;
	}

	/**
	 * Tick handler for special attack
	 *
	 * @param ms millisecond tick the player attack is being handled on
	 */
	private void tickSpecialAttack(long ms) {
		if (justPressed(GameAction.SPECIAL_ATTACK)) {
			specialAttackCharging = true;
		}
		if (InputManager.justReleased(GameAction.SPECIAL_ATTACK)) {
			specialAttackCharging = false;
			specialAttackChargeTimer = 0;
		}

		if (specialAttackCharging && InputManager.getActionState(GameAction.SPECIAL_ATTACK)) {
			specialAttackChargeTimer += ms;
			if (specialAttackChargeTimer > SPECIAL_ATTACK_THRESHOLD) {
				launchSpecialAttack();
			}
		}
	}

	/**
	 * Tick handler for Debug screen
	 */
	protected void tickDebug() {
		Debug debug = world.getDebug();
		if (debug != null) {
			String debugString = "FPS: " + World.getInstance().getFps() + "\n";

			debugString += "# of Entities: " + world.getAllEntities().size() + "\n";
			debugString += "# of Players: " + world.getPlayerEntities().size() + "\n";

			debugString += "Current Seed: " + World.getMapSeed() + "\n";

			debugString += "# of Mobs: " + world.getNpcEntities().size() + "\n";
			debugString += "# of Decorations: " + world.getDecorationEntities().size() + "\n";
			debugString += "# of loaded Chunks: " + world.getTiles().getWidth() / Chunk.CHUNK_WIDTH + "\n";
			debugString += "Current Biome: " + Chunk.getBiomeTypeOfX((int) posX) + "\n\n";
			debugString += "HP: " + getCurrentHealth() + "\n";
			debugString += "XP: " + stats.getExperiencePoints() + "\n";
			debugString += "Gold: " + this.commerce.getGold() + "\n";
			debugString += "X: " + String.format("%.2f", posX) + ", Y: " + String.format("%.2f", posY) + "\n";
			debugString += "velX: " + String.format("%.2f", velX) + ", velY: " + String.format("%.2f", velY) + "\n";
			if (getOnMountStatus()) {
				Mount mount = getMount();
				debugString += "MeasuredMountVelX: " + String.format("%.2f", 1000 * mount.getMeasuredVelX())
						+ ", MeasuredMountVelY: " + String.format("%.2f", 1000 * mount.getMeasuredVelY()) + "\n";
			}
			debugString += "Current Weapon: " + equippedWeapon.getName() + "\n";
			debugString += "AmmoCount:" + inv.getAmount("ammo") + "\n";
			debugString += "Player State: " + currentState + "\n";

			debugString += "Move speed: " + moveSpeed + '\n';

			debugString += "Move speed: " + moveSpeed + "\n";
			debugString += "Crit chance: " + stats.getCritChance() + "\n";
			debugString += "Crit damage: " + stats.getCritDamage() + "\n";

			debug.addToDebugString(debugString);
		}
	}

	/**
	 * Tick handler for special attack
	 */
	private void tickSpecialAttackFire(long ms) {
		specialAttackFireTimer += ms;
		LazorParticle particle = new LazorParticle(this, 150, this.renderFacing);
		world.addEntity(particle);

		if (specialAttackFireTimer > SPECIAL_ATTACK_FIRE_DURATION) {
			specialAttackFiring = false;
			specialAttackFireTimer = 0;
		}
	}

	/**
	 * Tick handler for spells
	 */
	private void tickSpells(long ms) {
		if (!spells.isEmpty()) {
			for (int i = 0; i < spells.size(); i++) {
				// check each spell 1-4 to see if it's currently running
				if (usingSpells.get(i)) {
					// if reached end of phase and still not stopped manually, stopEffect the spell
					if (currentSpellPhase.get(i) > spellPhases.get(i)) {
						usingSpells.set(i, false);
					} else {
						// if the current loop tick has waited too long, move on to the next iteration
						if (currentSpellLoopTiming.get(i) > spellLoopTimings.get(i).get(currentSpellPhase.get(i))
								|| currentSpellLoopTiming.get(i) == 0) {
							// timing of 0 indicates first iteration
							// execute the current phase of the spell
							spells.get(i).getSpell().phase1(currentSpellPhase.get(i));
							// set timing to current tick duration
							currentSpellLoopTiming.set(i, (int) ms);
							// add 1 to number of iterations for the current phase loop
							currentSpellLoopIteration.set(i, currentSpellLoopIteration.get(i) + 1);
						} else {
							// add tick duration to current loop tick timing
							currentSpellLoopTiming.set(i, currentSpellLoopTiming.get(i) + (int) ms);
						}
						// if the loop has run its iterations, move on to the next phase
						if (currentSpellLoopIteration.get(i) >= spellLoopIterations.get(i)
								.get(currentSpellPhase.get(i))) {
							currentSpellPhase.set(i, currentSpellPhase.get(i) + 1);
							currentSpellLoopIteration.set(i, 0);
							currentSpellLoopTiming.set(i, 0);
						}
					}
				}
			}
		}
	}

	/**
	 * Method to handle player's attack and to have a look at the attack type.
	 * <p>
	 * The type of player attack determined. When more types of attacks or
	 * different weapons is added to the game then this function could
	 * distinguish between the attacks. This method also decides if the attack
	 * was a critical hit or not.
	 *
	 * @param ms            millisecond tick the player attack is being handled on
	 * @param basicAttack   true if the basic attack is selected by the player
	 * @param specialAttack true if the special attack is selected by the player
	 */
	protected void playerAttack(long ms, boolean basicAttack, boolean specialAttack) {
		switch (currentState) {
			case DASHING:
			case AIR_DASHING:
			case STUNNED:
			case SLIDING:
			case CROUCHING:
			case AIR_CROUCHING:
			case KNOCK_BACK:
			case SPRINTING:
				return;
			default:
				break;
		}
		if (basicAttack) {
			int criticalHit = new Random().nextInt(200) - 100;
			int criticalHitPercentage = criticalHit + this.stats.getCritChance();
			int additionalDamage = this.stats.getCritDamage();
			float armourDamageBonus = 1;
			float armourCritBonus = 1;
			if (equippedHead != null) {

				armourDamageBonus += equippedHead.getDamageMulti() - 1;
				armourCritBonus += equippedHead.getCritDamageMulti() - 1;
			}
			if (equippedChest != null) {
				armourDamageBonus += equippedChest.getDamageMulti() - 1;
				armourCritBonus += equippedChest.getCritDamageMulti() - 1;

			}
			float prjXVel = getProjectileVelocity()[0];
			float prjYVel = getProjectileVelocity()[1];
			// ammo check
			int ammoCount;
			int shotCount;
			boolean explosive = false;
			boolean melee = false;
			if ((equippedWeapon.getProjectileType() == ProjectileType.GRENADE)
					|| (equippedWeapon.getProjectileType() == ProjectileType.ROCKET)) {
				ammoCount = inv.getAmount("ex_ammo");
				explosive = true;
				shotCount = ammoCount / equippedWeapon.getAmmoDeduction();
			} else if (equippedWeapon.getProjectileType() == ProjectileType.MELEE) {
				melee = true;
				shotCount = 1;
			} else {
				ammoCount = inv.getAmount("ammo");
				shotCount = ammoCount / equippedWeapon.getAmmoDeduction();
			}

			if (this.firingRateTracker <= 0 && criticalHitPercentage < 50 && shotCount >= 1) {
				equippedWeapon.basicAttack(this, prjXVel, prjYVel, this.bulletSpeed,
						(int) armourDamageBonus * this.genericBulletDamage);
				this.firingRateTracker = (stats.getFiringRate() + equippedWeapon.getFiringRate()) / 2;
				if (explosive) {
					inv.removeItem(equippedWeapon.getAmmoDeduction(), "ex_ammo");
				} else if (!melee) {
					inv.removeItem(equippedWeapon.getAmmoDeduction(), "ammo");
				}
			} else if (this.firingRateTracker == 0 && criticalHitPercentage > 0 && shotCount >= 1) {
				equippedWeapon.basicAttack(this, prjXVel, prjYVel, this.bulletSpeed,

						((int) armourDamageBonus * this.genericBulletDamage)
								+ ((int) armourCritBonus * additionalDamage));

				this.firingRateTracker = (stats.getFiringRate() + equippedWeapon.getFiringRate()) / 2;
				if (explosive) {
					inv.removeItem(equippedWeapon.getAmmoDeduction(), "ex_ammo");
				} else if (!melee) {
					inv.removeItem(equippedWeapon.getAmmoDeduction(), "ammo");
				}
			} else {
				this.firingRateTracker -= ms;
			}
		} else {
			this.firingRateTracker -= ms;
		}
	}

	/**
	 * Launches player's special attack
	 */
	private void launchSpecialAttack() {
		specialAttackFiring = true;
		specialAttackCharging = false;
		specialAttackChargeTimer = 0;
	}

	/**
	 * Getter for the current fall rate modifier
	 *
	 * @return the current fall rate modifer
	 */
	public float getFallModifier() {
		return fallModifier;
	}

	/**
	 * Setter for the current fall rate modifier
	 *
	 * @param value the new fall rate modifier
	 */
	public void setFallModifier(float value) {
		fallModifier = value;
	}

	/**
	 * Wrapper for double jump conditions
	 * <p>
	 * Should reduce them stanks a lil
	 *
	 * @return true if the player is able to double jump, otherwise false
	 */
	protected boolean ableToDoubleJump() {
		return (doubleJumpAvailable /*&& getSkillUnlocked("Double jump")*/ || isUnderLiquid) && velY > 0 && ableToJump();
	}

	/**
	 * Transitions the player to a sliding state
	 */               
	protected void transitionToSlide() {
		if (getCurrentMana() >= 30) {
			addMana(-30);
			if (facing != onWall && changeBounds(SLIDE_WIDTH, SLIDE_HEIGHT)) {
				strafeActive = false;
				setState(EntityState.SLIDING);
				actionTimer = slideDuration;
				velX = facing * slideSpeed;
			}
		}
	}

	/**
	 * Transitions the player to a dashing state
	 */
	protected void transitionToDash() {
		if (getCurrentMana() >= 50) {
			addMana(-50);
			if (facing != onWall) {
				strafeActive = false;
				setState(EntityState.DASHING);
				actionTimer = dashDuration;
				velX = facing * dashSpeed;
			}
		}
	}

	/**
	 * Transitions the player to a crouching state
	 */
	protected void transitionToCrouch() {
		if (changeBounds(CROUCH_WIDTH, CROUCH_HEIGHT)) {
			if (Math.abs(velX) > 0.2f) {
				velX *= 0.8f;
			} else {
				velX = 0;
			}
			velY = 0;
			strafeActive = false;
			setState(EntityState.CROUCHING);
		}
	}

	/**
	 * Transitions the player to an air-crouching state
	 */
	protected void transitionToAirCrouch() {
		if (changeBounds(1f, 1.4f)) {
			if (Math.abs(velX) > 0.2f) {
				velX *= 0.8f;
			} else {
				velX = 0;
			}
			strafeActive = false;
			setState(EntityState.AIR_CROUCHING);
		}
	}

	/**
	 * Transitions the player to an air dash in the specified directions
	 */
	protected void transitionToAirDash(boolean left, boolean right, boolean up, boolean down) {
		if (getCurrentMana() >= 50) {
			addMana(-50);
			enableGravity = false;
			strafeActive = false;

			setState(EntityState.AIR_DASHING);
			setVelocity(0, 0);
			if (left && right) {
				actionTimer = dashDuration;
				velX = facing * dashSpeed;
				airDashAvailable = false;
			} else if (left || right) {
				actionTimer = dashDuration;
				velX = playerInput.getInputDirection() * dashSpeed;
				airDashAvailable = false;
			}
			if (up) {
				actionTimer = dashDuration;
				velY = -dashSpeed;
				airDashAvailable = false;
			} else if (down) {
				actionTimer = dashDuration;
				velY = dashSpeed;
				airDashAvailable = false;
			}

			if (airDashAvailable) {
				actionTimer = dashDuration;
				velX = facing * dashSpeed;
				airDashAvailable = false;
			}
		}
	}

	/**
	 * Sets all modifiers to their basic value due to the player landing.
	 */
	protected void transitionOnLanding() {
		setFallModifier(1f);
		setTerminalVelModifier(1f);
		airDashAvailable = true;
		doubleJumpAvailable = true;
		strafeActive = true;
		velY = 0;
		transitionToOnGround();
	}

	/**
	 * Transitions from a non-standing or moving state to standing or moving.
	 */
	protected void transitionToOnGround() {
		EntityState prevState = currentState;
		strafeActive = true;
		if (playerInput.getInputDirection() == 0) {
			setState(EntityState.STANDING);
		} else {
			setState(EntityState.MOVING);
		}
		if (((getWidth() - BASE_WIDTH) > EPSILON) || ((getHeight() - BASE_HEIGHT) > EPSILON)) {
			if (!changeBounds(BASE_WIDTH, BASE_HEIGHT)) {
				currentState = prevState;
			}
		}
	}

	/**
	 * Transitions the player to a knock-back state
	 * <p>
	 */
	protected void transitionToKnockBack(int knockBackDir) {
		if (currentState == EntityState.KNOCK_BACK) {
			return;
		} else if (knockBackTimer > 0 && knockBackTimer < knockBackEndDuration) {
			knockBackTimer = knockBackEndDuration - 100;
			return;
		}
		setState(EntityState.KNOCK_BACK);
		setBlocksOtherEntities(false);
		knockBackTimer = knockBackDuration;
		knockBackRenderTimer = 0L;
		knockBackLastRenderTime = 0L;
		velX = knockBackDir * knockBackSpeedX;
		velY = knockBackSpeedY;
	}

	/**
	 * Returns true if the Player has unlocked a skill with the input name
	 *
	 * @return true if the Player has unlocked a skill with the input name
	 */
	protected boolean getSkillUnlocked(String skillName) {
		passiveSkill = allSkills.getMovementSkills();
		passiveSkill.addAll(allSkills.getAttackSkills());
		passiveSkill.addAll(allSkills.getDefenseSkills());
		for (Passive p : passiveSkill) {
			if (p.getName().equals(skillName)) {
				return true;
			}
		}
		return false;
	}

	public List<Passive> getAllPassiveSkills() {
		return this.allSkills.getAllActiveSkills();
	}

	/**
	 * The ex-hueg state machine that governs how the player moves
	 *
	 * @param ms time since the last tick in ms
	 */
	protected void moveStateEntity(long ms) {
		if (currentState == EntityState.DEAD) {
			return;
		}
		inputDir = playerInput.getInputDirection();

		boolean left = playerInput.getLeftPressed();
		boolean right = playerInput.getRightPressed();
		boolean jump = playerInput.getJumpPressed();
		boolean up = playerInput.getUpPressed();
		boolean down = playerInput.getDownPressed();
		boolean dash = playerInput.getDashPressed();
		boolean slide = playerInput.getSlidePressed();

		jumpAvailable = ableToJump();

		switch (currentState) {
			case DEAD:
				return;
			case KNOCK_BACK:
				entityKnockBack();
				break;
			case STANDING:
				entityStand(left, right, down, jump, dash, slide);
				break;
			case SPRINTING: // intentional fall down
			case MOVING:
				entityMove(left, right, down, jump, dash, slide);
				break;
			case JUMPING:
				entityJump(left, right, up, down, jump, dash);
				break;
			case WALL_SLIDING:
				entityWallSlide(left, right, jump);
				break;
			case DASHING:
				entityDash(jump);
				break;
			case SLIDING:
				entitySlide(down);
				break;
			case AIR_DASHING:
				entityAirDash();
				break;
			case CROUCHING:
				entityCrouch(down, jump, dash, slide);
				break;
			case AIR_CROUCHING:
				entityAirCrouch(left, right, up, down, dash);
				break;
			default:
				break;
		}
	}

	/**
	 * Handles state transitions while crouching in the air
	 * <p>
	 * -> standing/moving: landing without holding down<br>
	 * -> crouching: landing while holding down <br>
	 * -> jumping: down is released while mid air <br>
	 * -> air dash: dash is pressed <br>
	 *
	 * @param left
	 * @param right
	 * @param up
	 * @param down
	 * @param dash
	 */
	protected void entityAirCrouch(boolean left, boolean right, boolean up, boolean down, boolean dash) {
		applyJumpingPhysics();

		if (onGround) {
			if (!down) {
				transitionOnLanding();
				return;
			} else {
				setFallModifier(1f);
				setTerminalVelModifier(1f);
				airDashAvailable = true;
				doubleJumpAvailable = true;
				transitionToCrouch();
				return;
			}
		}
		if (!down && changeBounds(1f, 2f)) {
			setState(EntityState.JUMPING);
			strafeActive = true;
		}
		if (dash /*&& getSkillUnlocked("Dash")*/ && changeBounds(1f, 2f)) {
			transitionToAirDash(left, right, up, down);
		}
	}

	/**
	 * Handles state transitions in knock back
	 * <p>
	 * -> standing/moving: knockback timer expired
	 */
	protected void entityKnockBack() {
		if (knockBackTimer < knockBackEndDuration) {
			transitionOnLanding();
			setBlocksOtherEntities(true);
			return;
		} else if (!onGround) {
			velX *= 0.95f;
		} else {
			setVelocity(0, 0);
		}
	}

	/**
	 * Handles state transitions when crouching
	 * <p>
	 * -> standing: down is released <br>
	 * -> jumping: jump is pressed and its possible to jump, or the player falls
	 * off the ground <br>
	 * -> sliding: dash or slide are pressed
	 *
	 * @param down  is the down button pressed
	 * @param jump  is the jump button pressed
	 * @param dash  is the dash button pressed
	 * @param slide is the slide button pressed
	 */
	protected void entityCrouch(boolean down, boolean jump, boolean dash, boolean slide) {
		if (Math.abs(velX) > 0.2f) {
			velX *= 0.8f;
		} else {
			velX = 0;
		}
		velY = 0;
		if (!down && changeBounds(BASE_WIDTH, BASE_HEIGHT) && onGround) {
			setState(EntityState.STANDING);
			strafeActive = true;
		}
		if (!onGround && changeBounds(BASE_WIDTH, BASE_HEIGHT)) {
			setState(EntityState.JUMPING);
			strafeActive = true;
		}

		if (jump && jumpAvailable) {
			SoundCache.play("jump");
			setState(EntityState.AIR_CROUCHING);
			velY = jumpSpeed;
			return;
		}
		if (dash || slide) {
			transitionToSlide();
			return;
		}
	}

	/**
	 * Handles transitions from the standing state
	 * <p>
	 * -> moving: user input to either side <br>
	 * -> jumping: either user inputs jump, or the player walks off a ledge <br>
	 * -> dashing: user inputs dash (and there is enough mana and the skill is
	 * unlocked) <br>
	 * -> sliding: user inputs dash (and there is enough mana and this doesn't
	 * cause collision problems)
	 */
	protected void entityStand(boolean left, boolean right, boolean down, boolean jump, boolean dash, boolean slide) {
		if (((getWidth() - BASE_WIDTH) > EPSILON) || ((getHeight() - BASE_HEIGHT) > EPSILON)) {
			changeBounds(BASE_WIDTH, BASE_HEIGHT);
		}
		setVelocity(0, 0);
		if (!onGround) {
			setState(EntityState.JUMPING);
			return;
		}
		if (left != right) {
			setState(EntityState.MOVING);
		} else if (jump && jumpAvailable) {
			SoundCache.play("jump");
			setState(EntityState.JUMPING);
			velY = jumpSpeed;
			return;
		}
		if (dash /*&& getSkillUnlocked("Dash")*/) {
			transitionToDash();
			return;
		}
		if (slide) {
			transitionToSlide();
			return;
		}
		if (down) {
			transitionToCrouch();
		}
	}

	/**
	 * Handles transitions from the moving state
	 * <p>
	 * -> standing: no user input to either side <br>
	 * -> sprinting (sub-state of moving): user double taps the left/right
	 * button <br>
	 * -> jumping: either user inputs jump, or the player walks off a ledge <br>
	 * -> dashing: user inputs dash (and there is enough mana and the skill is
	 * unlocked) <br>
	 * -> sliding: user inputs dash (and there is enough mana and this doesn't
	 * cause collision problems)
	 */
	protected void entityMove(boolean left, boolean right, boolean down, boolean jump, boolean dash, boolean slide) {
		boolean sprintLeft = InputManager.getDoublePressed(GameAction.MOVE_LEFT);
		boolean sprintRight = InputManager.getDoublePressed(GameAction.MOVE_RIGHT);
		if (left == right) {
			setState(EntityState.STANDING);
			return;
		} else {
			velX = inputDir * moveSpeed;
		}
		if (inputDir != 0 && (sprintLeft || sprintRight) /*&& getSkillUnlocked("Sprint")*/ && getCurrentMana() >= 2) {
			addMana(-2);
			velX = inputDir * (moveSpeed * 2);
			setState(EntityState.SPRINTING);
		} else {
			setState(EntityState.MOVING);
		}
		if (jump && jumpAvailable) {
			SoundCache.play("jump");
			setState(EntityState.JUMPING);
			velY = jumpSpeed;
			return;
		} else if (!onGround) {
			setState(EntityState.JUMPING);
			return;
		}
		if (dash && getSkillUnlocked("Dash")) {
			transitionToDash();
			return;
		}
		if (slide) {
			transitionToSlide();
			return;
		}
		if (down) {
			transitionToCrouch();
			return;
		}
	}

	/**
	 * Handles transitions from the jumping state
	 * <p>
	 * -> standing: user lands with no horizontal input -> moving: user lands
	 * with horizontal input -> double jumping (sub-state of jumping): user
	 * inputs jump (and their velocity is downwards and the skill is unlocked)
	 * -> wall sliding: player is against a wall and holding the input of that
	 * direction -> air dashing: user inputs dash (and there is enough mana and
	 * the skill is unlocked)
	 */
	protected void entityJump(boolean left, boolean right, boolean up, boolean down, boolean jump, boolean dash) {
		applyJumpingPhysics();
		if (jump && ableToDoubleJump() && getCurrentMana() >= 20) {
			addMana(-20);
			SoundCache.play("jump");
			setState(EntityState.JUMPING);
			// particleFX
			for (float x = getX(); x < getX() + getWidth(); x += 0.2) {
				Particle particle = new Particle(0, 0, x, getY() + (getHeight()), 301, 10, true, false);
				world.addEntity(particle);
			}
			velY = jumpSpeed;
			doubleJumpAvailable = false;
		}
		if ((inputDir == onWall) && (velY > 0) && (inputDir != 0)) {
			setState(EntityState.WALL_SLIDING);
			strafeActive = false;
			setFallModifier(wallSlidingFallModifier);
			setTerminalVelModifier(wallSlidingTerminalModifier);
		}
		if (dash && airDashAvailable && getSkillUnlocked("Dash")) {
			transitionToAirDash(left, right, up, down);
			return;
		}
		if (down) {
			transitionToAirCrouch();
			return;
		}
		if (onGround) { // We hit the ground
			transitionOnLanding();
			return;
		}
		if (onCeiling) {
			velY = 0f;
		}
	}

	/**
	 * Handles transitions from the wall sliding state
	 * <p>
	 * -> standing: user lands with no horizontal input -> moving: user lands
	 * with horizontal input -> jumping: player is no longer against a wall and
	 * holding the input of that direction -> wall jumping (sub-state of
	 * jumping): player inputs jump
	 */
	protected void entityWallSlide(boolean left, boolean right, boolean jump) {
		if ((inputDir != onWall) || (velY < 0)) {
			setState(EntityState.JUMPING);
			setFallModifier(1f);
			setTerminalVelModifier(1f);
			strafeActive = true;
		}
		if (jump && jumpAvailable && !(world instanceof RoomWorld)) {
			setFallModifier(1f);
			setTerminalVelModifier(1f);
			setState(EntityState.JUMPING);
			velY = jumpSpeed * fallModifier;
			velX = -wallJumpSpeedModifier * facing * moveSpeed;
			wallJumpTimer = wallJumpDuration;
			strafeActive = true;
		}
		if (onGround) { // We hit the ground
			transitionOnLanding();
		}
	}

	/**
	 * Handles transitions from the dashing state
	 * <p>
	 * -> standing: user runs into a wall, or the timer runs out with no
	 * horizontal input <br>
	 * -> moving: the timer runs out with horizontal input <br>
	 * -> jumping: user jumps or falls off a ledge <br>
	 * -> wall jumping (sub-state of jumping): player inputs jump
	 */
	protected void entityDash(boolean jump) {
		if (!onGround) {
			strafeActive = true;
			setState(EntityState.JUMPING);
		}
		if (onWall != 0) {
			strafeActive = true;
			setState(EntityState.STANDING);
		}
		if (jump && jumpAvailable) {
			strafeActive = true;
			setState(EntityState.JUMPING);
			velY = jumpSpeed;
		}
		if (actionTimer < 0) {
			transitionToOnGround();
		}
	}

	/**
	 * Handles transitions from the sliding state
	 * <p>
	 * -> standing: user runs into a wall, or the timer runs out with no
	 * horizontal input <br>
	 * -> moving: the timer runs out with horizontal input <br>
	 * -> jumping: user jumps or falls off a ledge <br>
	 * -> wall jumping (sub-state of jumping): player inputs jump
	 */
	protected void entitySlide(boolean down) {
		if (!onGround && jumpAvailable) {
			strafeActive = true;
			setState(EntityState.JUMPING);
			changeBounds(BASE_WIDTH, BASE_HEIGHT);
			// No need to check if transition is possible because of
			// jumpAvailable (in theory)
		}
		if (onWall != 0 && jumpAvailable) {
			strafeActive = true;
			setState(EntityState.STANDING);
			changeBounds(BASE_WIDTH, BASE_HEIGHT);
		} else if (onWall != 0) {
			velX *= -1;
			actionTimer = -1L;
		}
		if (actionTimer < 0 && jumpAvailable) {
			if (down) {
				transitionToCrouch();
			} else {
				changeBounds(BASE_WIDTH, BASE_HEIGHT);
				transitionToOnGround();
			}
		}
	}

	/**
	 * Handles transitions from the air dashing state
	 * <p>
	 * -> standing: user lands with no horizontal input <br>
	 * -> moving: the user lands with horizontal input <br>
	 * -> jumping: the timer runs out
	 */
	protected void entityAirDash() {
		if (onGround) { // We hit the ground
			enableGravity = true;
			strafeActive = true;
			transitionOnLanding();
		}

		if (actionTimer < 0) {
			enableGravity = true;
			strafeActive = true;
			setState(EntityState.JUMPING);
			velY = 0;
			if (inputDir == 0) {
				velX = 0;
			} else {
				velX = facing * moveSpeed;
			}
		}
	}

	/**
	 * Handles horizontal speed while jumping:
	 * <p>
	 * If the player has just wall jumped, they have no control. <br>
	 * If the player releases left/right then drag applies to slow them down
	 * <br>
	 * Otherwise its as you'd expect with left/right controlling velX <br>
	 */

	private void applyJumpingPhysics() {
		if (wallJumpTimer < 0) {
			if (velX <= moveSpeed && velX >= -moveSpeed) {
				velX = inputDir * moveSpeed;
			} else if (facing != inputDir && inputDir != 0) {
				velX = -velX;
			} else if (inputDir == 0) {
				velX *= 0.9f;
			}
		}
	}

	/**
	 * Updates timers that track player states
	 * <p>
	 * action timer: handles dash/slide <br>
	 * wall jump timer: handles wall jumps <br>
	 * knock back timer: handles knock back and associated invulnerability, also
	 * handles toggling the render flag
	 *
	 * @param ms
	 */
	protected void updateTimers(float ms) {
		if (actionTimer >= 0) {
			actionTimer -= ms;
		}

		if (wallJumpTimer >= 0) {
			wallJumpTimer -= ms;
		}

		if (knockBackTimer >= 0) {
			knockBackTimer -= ms;
			knockBackRenderTimer += ms;
			if (knockBackRenderTimer > knockBackLastRenderTime + knockBackRenderGap) {
				renderFlag = !renderFlag;
				knockBackLastRenderTime = knockBackRenderTimer;
			}
		} else {
			renderFlag = true;
		}
	}

	@Override
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

		float playerLabelX = x - 20;
		float playerLabelY = (y + bounds.getHeight() * tileSize) + 5;
		/*
			gc.setFill(new Color(0, 0, 0, 0.5));
			gc.fillRect(playerLabelX, playerLabelY, bounds.getWidth() * tileSize, 20);
			*/

		if (name != null && name.length() > 0) {

			gc.setFill(new Color(0, 0, 0, 0.5));
			gc.fillRect(playerLabelX, playerLabelY, 60, 20);
			gc.setFill(new Color(1, 1, 1, 0.75));
			gc.fillText(name, playerLabelX, playerLabelY + 15, 60);
		}

		//TODO Fill box with playerName, make width suit playerName length

	}

	/**
	 * Currently controls abilities (will be activated by pressing Q W E R)
	 * <p>
	 * This method determines which skill is activated by the player and check
	 * if the player could perform the skill or not
	 * <p>
	 *
	 * @param skillKeys:keys that can activate spells
	 */
	protected void skillState(List<Boolean> skillKeys) {
		for (int i = 0; i < 4; i++) {
			if (skillKeys.get(i)) {
				if (spells.get(i) == null) {
					continue; // spell not equipped, do nothing
				}
				if ((Math.abs(getCooldown(i)) < EPSILON) && !(usingSpells.get(i))
						&& getCurrentMana() >= spells.get(i).getManaCost()) {
					activateSpell(i);
				}
			}
		}
	}

	public void addSpell(int spellIndex, Active activeSpell) {
		setUsingSpell(spellIndex, false);
		spells.set(spellIndex, activeSpell);
		// refresh timing loop stuff
		activeSpell.getSpell().phaseSetup();
	}

	/**
	 * Runs through the processes to start using a spell
	 */
	private void activateSpell(int spellIndex) {
		spells.get(spellIndex).getSpell().phaseSetup();
		addMana(-(spells.get(spellIndex).getManaCost()));
		currentSpellPhase.set(spellIndex, 0);
		currentSpellLoopIteration.set(spellIndex, 0);
		currentSpellLoopTiming.set(spellIndex, 0);
		setCooldown(spells.get(spellIndex).getCooldown(), spellIndex);
		setUsingSpell(spellIndex, true);
	}

	/**
	 * Reduce cool down times if they are currently active
	 */
	private void adjustCooldowns(double ms) {
		double seconds = ms / 1000;
		for (int i = 0; i < spells.size(); i++) {
			if (cooldowns.get(i) > 0) {
				cooldowns.set(i, cooldowns.get(i) - seconds);
			} else {
				cooldowns.set(i, 0d);
			}
		}
	}

	public List<Active> getActiveSkill() {
		return spells;
	}

	/**
	 * @param value
	 * @param cooldownIndex
	 */
	public void setCooldown(double value, int cooldownIndex) {
		cooldowns.set(cooldownIndex, value);
	}


	/**
	 * @param cooldownIndex
	 * @return
	 */
	public double getCooldown(int cooldownIndex) {
		return cooldowns.get(cooldownIndex);
	}

	/**
	 * Get the base damage of the player.
	 *
	 * @return the players base damage.
	 */

	public int getBaseDamage() {
		return stats.getDamage();
	}

	/**
	 * Updates renderFacing to face towards the cursor Updates the arm and head
	 * additional sprites to point to the cursor.
	 */
	private void updateRenderAngle() {
		if (Window.getEngine() != null && Window.getEngine().getRenderer() != null
				&& Window.getEngine().getRenderer().getViewport() != null) {
			if (strafeActive) {
				if (InputManager.getDiffX(posX) > 0) {
					renderFacing = 1;
				} else {
					renderFacing = -1;
				}

				if (inputDir == 0) {
					facing = renderFacing;
				}
			}
			// For player, both the head and arm should be pointing at the
			// mouse.
			if (additionalSprites != null) {
				if (additionalSprites.containsKey(BodyPart.ARM)) {
					additionalSprites.get(BodyPart.ARM).setTarget(InputManager.getMouseTileX(),
							InputManager.getMouseTileY());
				}
				if (additionalSprites.containsKey(BodyPart.HEAD)) {
					additionalSprites.get(BodyPart.HEAD).setTarget(InputManager.getMouseTileX(),
							InputManager.getMouseTileY());
				}
				if (additionalSprites.containsKey(BodyPart.VOID)) {
					additionalSprites.get(BodyPart.VOID).setTarget(InputManager.getMouseTileX(),
							InputManager.getMouseTileY());
				}
			}
		}
	}

	/**
	 * returns the x and y velocity for a projectile that is aimed at the mouse
	 *
	 * @return float[0] = xvel, float[1] = yvel
	 */
	private float[] getProjectileVelocity() {
		// Barney Whiteman
		/*
		 * Angle between player and mouse with accuracy built in
		 *
		 * aim is a random number between -accuracy and +accuracy and then
		 * converted into radians
		 */
		float prjXVel = 0f;
		float prjYVel = 0f;
		if (viewport != null) {
			double aim = (Math.PI / 180) * ((Math.random() * accuracy * 2) - accuracy);
			aimAngle = (Math.atan2(InputManager.getDiffY(posY + this.getHeight() / 2),
					InputManager.getDiffX(posX + this.getWidth() / 2))) + aim;
			prjXVel = (float) (Math.cos(aimAngle));
			prjYVel = (float) (Math.sin(aimAngle));
		}
		return new float[]{prjXVel, prjYVel};
	}

	/**
	 * This method adds player's experience points to the player's stats
	 *
	 * @param points experience points gained by the player
	 */
	public void addExperiencePoint(int points) {
		stats.addExperiencePoints(points);
		if (stats.getExperiencePoints() == 20 * stats.getPlayerLevel()) {
			IngameText ingameText = new IngameText("LEVEL UP", 0, 0, 2000, IngameText.textType.STATIC, 1, 0, 1, 1);
			world.setPlayerText(ingameText);
			stats.levelUp();
			this.genericBulletDamage = stats.getDamage();
			stats.addSkillPoints((20 * stats.getPlayerLevel()) / 10);
		} else if (stats.getExperiencePoints() > 20 * stats.getPlayerLevel()) {
			stats.addExperiencePoints(-20 * stats.getPlayerLevel());
			stats.levelUp();
			this.genericBulletDamage = stats.getDamage();
			stats.addSkillPoints((20 * stats.getPlayerLevel()) / 10);
		}
	}

	/**
	 * Increase Accuracy in the player stats by given int
	 *
	 * @param increase Degrees in which to improve accuracy int increase < 15
	 */
	public void increaseAccuracy(int increase) {
		stats.addFiringAccuracy(increase);
	}

	/**
	 * Increase FireRate in player stat screen
	 *
	 * @param increase Value to increase FireRate by.
	 */
	public void increaseFireRate(int increase) {
		stats.addFiringRate(increase);
	}

	public void increaseBaseDamage(int increase) {
		stats.setBaseDamage(getBaseDamage() + increase);
	}

	public void setSpellPhases(int index, int numPhases) {
		spellPhases.set(index, numPhases);
	}

	public void setSpellLoopIterations(int index, int phaseIndex, int numLoops) {
		spellLoopIterations.get(index).add(phaseIndex, numLoops);
	}

	public void setSpellLoopTimings(int index, int phaseNum, int duration) {
		spellLoopTimings.get(index).add(phaseNum, duration);
	}

	public void setUsingSpell(int index, boolean status) {
		usingSpells.set(index, status);
	}

	/**
	 * Add attack related skill to skill list
	 *
	 * @param skill skill to be added
	 */
	public void addAttackSkills(SkillList skill) {
		this.allSkills.addAttackSkill(skill);
		stats.setBaseDamage(stats.calculateDamage());
	}

	/**
	 * Add defense related skill to skill list
	 *
	 * @param skill skill to be added
	 */
	public void addDefenseSkill(SkillList skill) {
		this.allSkills.addDefenseSkill(skill);
	}

	/**
	 * Add movement related skill
	 *
	 * @param skill skill to be added
	 */
	public void addMovementSkill(SkillList skill) {
		this.allSkills.addMovementSkill(skill);
	}

	/**
	 * A method to get the player's current experience points
	 *
	 * @return player's experience points
	 */
	public long getExperiencePoints() {
		return this.experiencePoints;
	}

	/**
	 * This method adds player's kill count to the player's stats
	 *
	 * @param count kill count gained by the player
	 */
	public void addKillCount(int count) {
		stats.addKillCount(count);
	}

	/**
	 * This method adds player's BossNPC kill count to the player's stats
	 *
	 * @param bossID the ID associated with the boss
	 */
	public void addBossKill(String bossID) {
		stats.addBossKill(bossID);
	}

	/**
	 * Wrapper method for the PlayerStats.addPickUpCount()
	 */
	public void addPickUpCount() {
		stats.addPickUpCount();
	}

	/**
	 * Wrapper method for PlayerStats.addCountCount()
	 */
	public void addCoinCount(int coinValue) {
		stats.addCoinCount(coinValue);
	}

	/**
	 * A method to get the player's current level
	 *
	 * @return player's level
	 */
	public int getPlayerLevel() {
		return stats.getPlayerLevel();
	}

	/**
	 * Entity collision event handler
	 * <p>
	 * We deal knockback only with the first entity for convenience
	 */
	@Override
	protected void onEntityCollide(List<Entity> entities, List<BodyPart> hitLocations) {
		if (entities.isEmpty()) {
			return;
		}
		for (Entity entity : entities) {
			if (entity instanceof CompanionNPC) {
				return;
			} else if (entity instanceof BaseNPC) {
				if (!this.invincible) {
					if (entity instanceof DuckKingNPC) {
						DuckKingNPC duckKingNPC = (DuckKingNPC) entity;
						EntityState duckState = duckKingNPC.getCurrentState();
						if (duckState == EntityState.ATTACKING || duckState == EntityState.STRIKING
								|| duckState == EntityState.SPRINTING) {
							this.addHealth((int) (-5 * world.getDifficulty()), duckKingNPC);
						}
					}
					if (getCurrentState() != EntityState.DEAD) {
						int knockBackDir = (int) Math.signum(entity.getVelX());
						transitionToKnockBack(knockBackDir);
					}
					return;
				} else {
					entity.onDeath(this);
				}
			}
		}
	}

	/**
	 * Terrain collision event handler;
	 */
	@Override
	protected void onTerrainCollide(int tileX, int tileY, Side side) {
	}

	/**
	 * On death event handler;
	 *
	 * @param cause entity that caused the death
	 */
	@Override
	protected void onDeath(Entity cause) {
		velX = 0;

		if (isCheckPointReached() && checkPointsEnabled) {
			Window.getEngine().load("tmp/flagSave.json");
			Toaster.ejectAllToast();
			Toaster.toast("You died! Check point loaded.");
			return;
		}
		setState(EntityState.DEAD);
	}

	/**
	 * Adjusts the BME's health by the input quantity, up to the maximum value.
	 *
	 * @param health the amount of health to add.
	 */
	@Override
	public void addHealth(int health) {
		if (health < 0 && !invincible) {
			IngameText ingameText = new IngameText("" + health, 0, 0, 2000, IngameText.textType.STATIC, 1, 0, 0, 1);
			world.setPlayerText(ingameText);
			SoundCache.play("damaged");
		}
		if (currentState == EntityState.KNOCK_BACK || knockBackTimer > 0) {
			return;
		}
		if (health >= 0 || !shielded) {
			currentHealth += health;
		}

		if (currentHealth > maxHealth) {
			currentHealth = maxHealth;
		}
	}

	/**
	 * returns the player's health
	 *
	 * @return current health
	 */

	public int getHealth() {
		return currentHealth;
	}

	/**
	 * Update skills placeholder
	 */
	protected boolean updateSkills() {
		return this.experiencePoints != 0 && this.experiencePoints % 100 == 0;
	}

	/**
	 * This method check which active skills are activated by the player
	 *
	 * @return a list of player's activated skills
	 */
	public List<Skill> skillCheck() {
		return new ArrayList<>();
	}

	/**
	 * Getter method used in tests
	 *
	 * @return player's dash speed
	 */
	public float getDashSpeed() {
		return dashSpeed;
	}

	/**
	 * Getter method used in tests
	 *
	 * @return player's dash duration
	 */
	public float getDashDuration() {
		return dashDuration;
	}

	/**
	 * Getter method used in tests
	 *
	 * @return player's slide speed
	 */
	public float getSlideSpeed() {
		return slideSpeed;
	}

	/**
	 * Getter method used in tests
	 *
	 * @return player's slide duration
	 */
	public float getSlideDuration() {
		return slideDuration;
	}

	/**
	 * Getter method used in tests
	 *
	 * @return player's wall sliding fall modifier
	 */
	public float getWallSlidingFallModifier() {
		return wallSlidingFallModifier;
	}

	/**
	 * Getter method used in tests
	 *
	 * @return player's wall jump duration
	 */
	public float getWallJumpDuration() {
		return wallJumpDuration;
	}

	public ActivateSkill getActivateSkillClass() {
		return allSkills;
	}

	/**
	 * Returns the players PlayerStats class.
	 *
	 * @return the players stats.
	 */
	public PlayerStats getPlayerStatsClass() {
		return stats;
	}

	/**
	 * Used for the portal gun, set's the current portals so the player knows
	 * where to teleport to
	 *
	 * @param portal - which portal
	 */
	public void setPortal(PortalBullet portal, boolean type) {
		if (portal != null) {
			if (type) {
				this.portal1 = portal;
			} else {
				this.portal2 = portal;
			}
		}
	}

	/**
	 * @param type - which portal
	 * @return portal bullets
	 */
	public PortalBullet getPortal(boolean type) {
		if (type) {
			return this.portal1;
		} else {
			return this.portal2;
		}
	}

	/**
	 * @param type - which portal
	 */
	public void killPortal(boolean type) {
		if (type) {
			this.portal1.kill(null);
		} else {
			this.portal2.kill(null);
		}
	}

	/***
	 * Teleports the player between the two portals in the world.
	 *
	 * @param type Type tells the function which way the player is teleporting,
	 *            i.e. from portal 1 to 2 or from 2 to 1.
	 */
	public void teleportPortal(boolean type) {
		float[] pos;
		if (type) {
			pos = this.portal2.getPos();
		} else {
			pos = this.portal1.getPos();
		}
		this.portal1.teleport(!type);
		this.portal2.teleport(type);
		this.setX(pos[0]);
		this.setY(pos[1]);
	}

	public void setUpdateHud(boolean bool) {
		updateHud = bool;
	}

	public boolean getUpdateHud() {
		return updateHud;
	}

	public void setDrawSKill(Sprite skill) {
		drawSkill = skill;
	}

	/**
	 * @return the DrawSkill sprite.
	 */
	public Sprite getDrawSkill() {
		return drawSkill;
	}


	/**
	 * Set the players spellkey.
	 *
	 * @param key the spellkey to set.
	 */
	public void setSpellKey(String key) {
		spellKey = key;
	}

	/**
	 * Returns the spellkey.
	 *
	 * @return the players spellkey.
	 */
	public String getSpellKey() {
		return spellKey;
	}


	public void setDrawSKill2(Sprite skill) {
		drawSkill2 = skill;
	}

	public Sprite getDrawSkill2() {
		return drawSkill2;
	}

	public String getSpellKey2() {
		return spellKey2;
	}

	public void setSpellKey2(String s) {
		spellKey2 = s;

	}

	public void setSkillSwap(boolean bool) {
		skillswap = bool;
	}

	public boolean getSkillSwap() {
		return skillswap;
	}

	public List<Sprite> getSkillSprites() {
		return skillSprites;
	}

	public void setSkillSprites(Sprite sprite, int index) {
		skillSprites.set(index, sprite);
	}


	/**
	 * Returns the string form of the player This is the sprite, bounds, posX,
	 * posY, experience points and bullet damage, in a line-separated format.
	 *
	 * @return string representation of the player
	 */
	public String toString() {
		String total = "";
		total += "sprite" + this.sprite.toString();
		total += "\nbounds" + this.bounds.toString();
		total += "\nX" + this.posX;
		total += "\nY" + this.posY;
		total += "\nXP: " + this.experiencePoints;
		total += "\nDamage: " + this.genericBulletDamage;
		return total;
	}

	/**
	 * This method determines whether the player has requested to switch the
	 * currently selected weapon and will change the currently equipped weapon.
	 * <p>
	 * The numbers 1 - 5 represent the first 5 weapons in the activeWeapons
	 * array. It is expected that the owned weapon array with be converted to
	 * activeWeapons after addition of inventory.
	 *
	 * @param weaponChange indicates which weapon change has been pressed
	 */
	protected void weaponChange(List<Boolean> weaponChange) {
		for (int i = 0; i < weaponChange.size(); i++) {
			if (weaponChange.get(i) && activeWeapons.size() > i - 1) {
				if (ItemRegistry.getItem(activeWeapons.get(i)) instanceof Weapon) {
					equippedWeapon = (Weapon) ItemRegistry.getItem(activeWeapons.get(i));
				}
			}
		}
		this.accuracy = equippedWeapon.getAccuracy() + 5;
	}

	public void setViewDistance(int viewDistance) {
		this.viewDistance = viewDistance;
	}

	public int getViewDistance() {
		return viewDistance;
	}

	/**
	 * Sets the players mode when talking
	 */
	public void setTalking(boolean input) {
		this.talking = input;
	}

	/**
	 * Sets the players mode when talking
	 *
	 * @return whether or not the player is talking
	 */
	public boolean getTalking() {
		return talking;
	}

	/**
	 * @return the checkPointReached
	 */
	public boolean isCheckPointReached() {
		return checkPointReached;
	}

	/**
	 * @param checkPointReached the checkPointReached to set
	 */
	public void setCheckPointReached(boolean checkPointReached) {
		this.checkPointReached = checkPointReached;
	}

	/**
	 * @return the checkPointsEnabled
	 */
	public boolean isCheckPointsEnabled() {
		return checkPointsEnabled;
	}

	/**
	 * @param checkPointsEnabled the checkPointsEnabled to set
	 */
	public void setCheckPointsEnabled(boolean checkPointsEnabled) {
		this.checkPointsEnabled = checkPointsEnabled;
	}

	/**
	 * @param set the value to set variable to
	 */
	public void setJumpAvailable(boolean set) {
		this.jumpAvailable = set ? true : false;
	}

	/**
	 * @param speed the speed value to give the player
	 */
	public void setJumpSpeed(float speed) {
		this.jumpSpeed = speed;
	}
}
