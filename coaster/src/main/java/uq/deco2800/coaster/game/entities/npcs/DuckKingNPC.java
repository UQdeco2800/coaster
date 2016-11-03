package uq.deco2800.coaster.game.entities.npcs;

import java.util.ArrayList;
import java.util.List;

import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.items.ItemRegistry;
import uq.deco2800.coaster.game.mechanics.BodyPart;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

public class DuckKingNPC extends BaseNPC implements AttackableNPC {
	private boolean isPaused = false;
	private long pausedTime = 0;
	private long attackingTime = 0;
	private int chargingDirection = 0;

	public DuckKingNPC() {
		sprites.put(EntityState.STANDING, new Sprite(SpriteList.DUCK_KING_BRACING));
		sprites.put(EntityState.SPRINTING, new Sprite(SpriteList.DUCK_KING_RUNNING));
		sprites.put(EntityState.ATTACKING, new Sprite(SpriteList.DUCK_KING_ATTACKING));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.DUCK_KING_JUMPING));
		sprites.put(EntityState.STRIKING, new Sprite(SpriteList.DUCK_KING_STRIKING));

		setSprite(sprites.get(EntityState.STANDING));

		this.bounds = new AABB(posX, posY, sprite.getWidth() / 32, sprite.getHeight() / 32);
		this.bounds.setActive(false);
		updateHitboxCache();
		setState(EntityState.STANDING);

		maxHealth = 1000;
		currentHealth = maxHealth;
		baseDamage = (int) (40 * difficultyScale);
	}

	private void updateHitboxCache() {
		float width1 = sprites.get(EntityState.STANDING).getWidth()/32;
		float height1 = sprites.get(EntityState.STANDING).getHeight()/32;

		List<AABB> hitboxes1 = new ArrayList<>();
		hitboxes1.add(defineHitbox(BodyPart.MAIN, width1, height1 - 0.1F, 0F, 0F));
		hitboxes1.add(defineHitbox(BodyPart.SHIELD, width1 - 0.1F, 0.1F, 0.05F, height1 - 0.1F));

		hitboxesCache.put(EntityState.STANDING, hitboxes1);
		hitboxesCache.put(EntityState.SPRINTING, hitboxes1);
		hitboxesCache.put(EntityState.JUMPING, hitboxes1);

		float width2 = sprites.get(EntityState.STRIKING).getWidth()/32;
		float height2 = sprites.get(EntityState.STRIKING).getHeight()/32;

		List<AABB> hitboxes2 = new ArrayList<>();
		hitboxes2.add(defineHitbox(BodyPart.MAIN, width2, height2 - 0.1F, 0F, 0F));
		hitboxes2.add(defineHitbox(BodyPart.SHIELD, width2 - 0.1F, 0.1F, 0.05F, height2 - 0.1F));

		hitboxesCache.put(EntityState.ATTACKING, hitboxes2);
		hitboxesCache.put(EntityState.STRIKING, hitboxes2);
	}

	@Override
	protected void tick(long ms) {
		determineNextAction();
	}

	/**
	 * @return Will return the only player in the room, the main target.
	 */
	@Override
	public Entity determinePriorityTarget() {
		return World.getInstance().getFirstPlayer();
	}

	/**
	 * The following are possible things that the duck king could be doing right now
	 * - Nothing/Standing         STANDING state
	 * - Charging at the player   SPRINTING state
	 * - Attacking the player     ATTACKING state
	 * - Pausing momentarily      STANDING state
	 * - Jumping                  JUMPING state
	 * - Striking Down            ATTACKING state
	 */
	@Override
	public void determineNextAction() {
		Player target = (Player) determinePriorityTarget();
		switch(getCurrentState()) {
			case STANDING:
				if (isPaused) {
					handlePaused();
				} else {
					handleBracing(target);
				}
				break;
			case JUMPING:
				handleJumping(target);
				break;
			case SPRINTING:
				handleCharging(target);
				break;
			case STRIKING:
				handleStrikingAttack(target);
				break;
			case ATTACKING:
				handleChargingAttack();
				break;
			default:
				// Do nothing
				break;
		}
	}

	private void handleBracing(Player target) {
		this.setVelocity(0, 0);
		float diffX = target.getX() - this.getX();
		int direction = diffX > 0 ? 1 : -1;
		setFacing(direction);
		setRenderFacing(direction);
		if (Math.abs(diffX) <= 10 && target.isOnGround()) {
			changeState(EntityState.SPRINTING);
			chargingDirection = direction;
		}
	}

	private void handlePaused() {
		long currentTime = System.currentTimeMillis();
		if (currentTime - pausedTime > 2000) {
			changeState(EntityState.STANDING);
			isPaused = false;
			this.setVelocity(0, 0);
		}
	}

	private void handleJumping(Player target) {
		if (getVelY() > 0) {
			changeState(EntityState.STRIKING);
		}
		int direction = target.getX() > this.getX() ? 1 : -1;
		setFacing(direction);
		setRenderFacing(direction);
	}

	private void handleCharging(Player target) {
		this.setVelocity(4 * chargingDirection, 0);
		float diffX = target.getX() - this.getX();
		int direction = diffX > 0 ? 1 : -1;
		if (Math.abs(diffX) <= 1.1) {
			changeState(EntityState.ATTACKING);
			attackingTime = System.currentTimeMillis();
		} else if (direction != chargingDirection) {
			changeState(EntityState.STANDING);
			isPaused = true;
			pausedTime = System.currentTimeMillis();
			this.setVelocity(0, 0);
		}
	}

	private void handleChargingAttack() {
		long currentTime = System.currentTimeMillis();
		if (currentTime - attackingTime > 300) {
			changeState(EntityState.STANDING);
			isPaused = true;
			pausedTime = System.currentTimeMillis();
			this.setVelocity(0, 0);
		}
	}

	private void handleStrikingAttack(Player target) {
		int direction = target.getX() > this.getX() ? 1 : -1;
		setFacing(direction);
		setRenderFacing(direction);
		if (this.getVelY() == 0) {
			changeState(EntityState.STANDING);
			this.setVelocity(0, 0);
		}
	}

	private void changeState(EntityState state) {
		setState(state);
		this.setSize(this.sprite.getWidth() / 32, this.sprite.getHeight() / 32);
	}

	public void hitDuckKingWithProjectile() {
		super.receiveDamage(100, null);
	}

	@Override
	public int receiveDamage(int damage, Entity cause) {
		if (cause instanceof Player && isPaused) {
			if (((Player)cause).getEquippedWeapon().equals(ItemRegistry.getItem("Melee1"))) {
				isPaused = false;
				setState(EntityState.JUMPING);
				this.setVelocity(0, -35);
			}
		}
		return 0;
	}

	@Override
	protected void onDeath(Entity cause) {
		super.onDeath();
		Player player = (Player)determinePriorityTarget();
		// add experience points is broken, rip
		for (int i = 0; i < 1000; i++) {
			player.addExperiencePoint(10);
		}
		player.getInventory().addItem(10000, "ammo");
	}
}
