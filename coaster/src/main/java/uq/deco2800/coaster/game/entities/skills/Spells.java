package uq.deco2800.coaster.game.entities.skills;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uq.deco2800.coaster.game.entities.BasicMovingEntity;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.PlayerBuff;
import uq.deco2800.coaster.game.entities.npcs.AttackableNPC;
import uq.deco2800.coaster.game.entities.npcs.BaseNPC;
import uq.deco2800.coaster.game.entities.particles.Particle;
import uq.deco2800.coaster.game.entities.particles.ParticleSource;
import uq.deco2800.coaster.game.entities.weapons.CritBullet;
import uq.deco2800.coaster.game.entities.weapons.GenericBullet;
import uq.deco2800.coaster.game.entities.weapons.GrenadeBullet;
import uq.deco2800.coaster.game.entities.weapons.SuperBullet;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.sprites.SpriteList;


/**
 * Spells that both players and mobs can potentially use
 */
public class Spells {

	private static Logger logger = LoggerFactory.getLogger(Spells.class);

	private Spells() {
	}

	public static void teleport(Entity entity, float x, float y) {
		entity.setPosition(x, y);
	}

	/**
	 * Deals damage around an entity in a given radius
	 *
	 * @param caster : the entity to deal the damage
	 * @param damage : how much damage to each enemy
	 */
	public static void retribution(Entity caster, int damage) {
		List<BaseNPC> nearbyEnemyNPCs = caster.getNearbyAttackableNPCs(5);

		for (BaseNPC target: nearbyEnemyNPCs) {
			target.receiveDamage(damage, caster);
		}
	}

	/**
	 * Teleport to a random enemy and deal damage
	 *
	 * @param entity : the entity to teleport
	 */
	public static boolean blinkStrike(Entity entity, World world) {
		//find a random npc within 5 units
		Random random = new Random();
		List<BaseNPC> targetNPCs = entity.getNearbyAttackableNPCs(5);
		if(entity instanceof BaseNPC && entity instanceof AttackableNPC){
			targetNPCs.add(targetNPCs.size(), (BaseNPC) entity); // Add the targeted NPC as well
		}
		if (!targetNPCs.isEmpty()) {
			int entityIndex = random.nextInt(targetNPCs.size());
			BaseNPC strikeNPC = targetNPCs.get(entityIndex);
			//teleport the player to that location
			//Particle FX
			float deltaY = entity.getY() - strikeNPC.getY();
			float deltaX = entity.getX() - strikeNPC.getX();
			double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
			float angleOfAttack = (float) Math.atan2(deltaY, deltaX);
			for (int i = 0; i < 5; i++) {
				float partX = (float) (entity.getX() - i * Math.cos(angleOfAttack) / distance);
				float partY = (float) (entity.getY() - i * Math.sin(angleOfAttack) / distance);
				ParticleSource.addParticleSource(partX, partY, 301, 10, 2, true, false);
			}

			teleport(entity, strikeNPC.getX(), (float) (strikeNPC.getY()
					- strikeNPC.getHeight() * 0.5));

			//deal 50 damage to mob
			strikeNPC.receiveDamage(50, entity);
		} else {
			return false;
		}
		return true;
	}

	public static class BaseSpell {
		protected int spellLevel;
		public void phase1(int phase) {
			//This is left empty on purpose
		}

		public void phaseSetup() {
			//This is also empty
		}
		
		public void setSpellLevel(int level) {
			this.spellLevel = level;
		} 
	}

	/**
	 * Freeze in air and fire a super bullet that pierces any enemies in its path
	 *
	 * @param caster     : the entity to deal the damage
	 * @param spellIndex : index of the spell
	 */
	public static class SniperShot extends BaseSpell {
		private Player caster;
		private World world = World.getInstance();
		private int spellIndex;

		public SniperShot(Player caster, int spellIndex) {
			logger.debug("Sniper Shot casted");
			this.caster = caster;
			this.spellIndex = spellIndex;
		}

		@Override
		public void phaseSetup() {
			caster.setSpellPhases(spellIndex, 2);
			caster.setSpellLoopIterations(spellIndex, 0, 1);
			caster.setSpellLoopIterations(spellIndex, 1, 2);
			caster.setSpellLoopIterations(spellIndex, 2, 1);
			caster.setSpellLoopTimings(spellIndex, 0, 0);
			caster.setSpellLoopTimings(spellIndex, 1, 300);
			caster.setSpellLoopTimings(spellIndex, 2, 0);
		}

		@Override
		public void phase1(int phase) {
			if (phase == 0) {
				//stun player
				caster.setStunned(true);
			} else {
				phase2(phase);
			}
		}

		public void phase2(int phase) {
			if (phase == 2) {
				//wait for next phase - sniper shot charging
				phase3();
			}
		}

		public void phase3() {
			//launch super bullet
			SuperBullet bullet = new SuperBullet(caster, caster.getRenderFacing(), 0, 200, 100, SpriteList.BULLET);
			world.addEntity(bullet);
			//bring player back to normal
			caster.setStunned(false);
			caster.setUsingSpell(spellIndex, false);
		}
	}

	/**
	 * Teleport to multiple random enemies and deal damage.
	 *
	 * @param caster     : the entity to cast the spell
	 * @param numBounces : number of enemies to teleport to
	 */
	public static class Omnislash extends BaseSpell {
		private Player caster;
		private int spellIndex;
		private int numBounces;
		private World world = World.getInstance();

		public Omnislash(Player caster, int numBounces, int spellIndex) {
			this.caster = caster;
			this.spellIndex = spellIndex;
			this.numBounces = numBounces;
		}

		@Override
		public void phaseSetup() {
			caster.setSpellPhases(spellIndex, 1);
			caster.setSpellLoopIterations(spellIndex, 0, numBounces);
			caster.setSpellLoopIterations(spellIndex, 1, 0);
			caster.setSpellLoopTimings(spellIndex, 0, 200);
			caster.setSpellLoopTimings(spellIndex, 1, 0);
		}

		@Override
		public void phase1(int phase) {
			if (phase == 0) {
				//keep running blink strikes for the given number of times
				//unless a target cannot be found, in which case cancel
				if (!blinkStrike(caster, world)) {
					phase2();
				}
			} else {
				phase2();
			}
		}

		public void phase2() {
			//return player to normal
			caster.setUsingSpell(spellIndex, false);
		}
	}

	/**
	 * Fire a burst of bullets in both direction.
	 *
	 * @param caster     : the entity to cast the spell
	 * @param spellIndex : index of spell
	 * @param spellLevel : the power level of spell
	 */
	public static class DeathBlossom extends BaseSpell {
		private Player caster;
		private World world = World.getInstance();
		private int spellIndex;

		public DeathBlossom(Player caster, int spellIndex, int spellLevel) {
			this.caster = caster;
			this.spellIndex = spellIndex;
			this.spellLevel = spellLevel;
		}
		
		@Override
		public void phaseSetup() {
			caster.setSpellPhases(spellIndex, 1);
			caster.setSpellLoopIterations(spellIndex, 0, 100);
			caster.setSpellLoopIterations(spellIndex, 1, 0);
			caster.setSpellLoopTimings(spellIndex, 0, 10);
			caster.setSpellLoopTimings(spellIndex, 1, 0);
		}

		@Override
		public void phase1(int phase) {
			if (phase == 1) {
				phase2();
			} else {
				Random direction = new Random();
				Random height = new Random();
				float randomY;
				//determine bullet spray by changing y-velocity
				if (direction.nextBoolean()) {
					randomY = height.nextFloat() * 0.1f;
				} else {
					randomY = height.nextFloat() * -0.1f;
				}
				if (spellLevel == 1) {
					//swap bullet direction randomly
					if (direction.nextBoolean()) {
						caster.setRenderFacing(1);
						GenericBullet bullet = new GenericBullet(caster, 1, randomY, 50, 20, SpriteList.BULLET);
						world.addEntity(bullet);
					} else {
						caster.setRenderFacing(-1);
						GenericBullet bullet = new GenericBullet(caster, -1, randomY, 50, 20, SpriteList.BULLET);
						world.addEntity(bullet);
					}
				} else if (spellLevel == 2) {
					if (direction.nextBoolean()) {
						caster.setRenderFacing(1);
						CritBullet bullet = new CritBullet(caster, (float)1, (float)randomY, 50, 200);
						//SuperBullet bullet = new SuperBullet(caster, 1, randomY, 50, 100, SpriteList.BULLET);
						world.addEntity(bullet);
					} else {
						caster.setRenderFacing(-1);
						CritBullet bullet = new CritBullet(caster, (float)-1, (float)randomY, 50, 200);
						//SuperBullet bullet = new SuperBullet(caster, -1, randomY, 50, 100, SpriteList.BULLET);
						world.addEntity(bullet);
					}
				}
			}
		}

		public void phase2() {
			//return player to normal
			caster.setUsingSpell(spellIndex, false);
		}
	}

	/**
	 * Drop a bomb that eliminates every enemy in a radius
	 *
	 * @param caster     : the entity to cast the spell
	 * @param spellIndex : index of spell
	 */
	public static class Nuke extends BaseSpell {
		private Player caster;
		private int spellIndex;
		private World world = World.getInstance();
		private float tempFallModifier;

		public Nuke(Player caster, int spellIndex) {
			this.caster = caster;
			this.spellIndex = spellIndex;
		}

		@Override
		public void phaseSetup() {
			caster.setSpellPhases(spellIndex, 2);
			caster.setSpellLoopIterations(spellIndex, 0, 1);
			caster.setSpellLoopIterations(spellIndex, 1, 5);
			caster.setSpellLoopIterations(spellIndex, 2, 1);
			caster.setSpellLoopTimings(spellIndex, 0, 0);
			caster.setSpellLoopTimings(spellIndex, 1, 100);
			caster.setSpellLoopTimings(spellIndex, 2, 0);
		}

		@Override
		public void phase1(int phase) {
			if (phase == 0) {
				//lift player off ground
				tempFallModifier = caster.getFallModifier();
				caster.setFallModifier(-1);
			} else {
				phase2(phase);
			}
		}

		public void phase2(int phase) {
			if (phase != 1) {
				phase3();
			}
			for (int i = 0; i < 366; i += 5) {
				//show charging particles in a circle around player
				double angle = Math.toRadians(i);
				float xVector = (float) (2 * Math.cos(angle));
				float yVector = (float) (2 * Math.sin(angle));
				float xVel = -5 * xVector;
				float yVel = -5 * yVector;
				float xPos = (float) (xVector + caster.getX() + 0.5 * caster.getWidth());
				float yPos = (float) (yVector + caster.getY() + 0.5 * caster.getHeight());
				Particle chargeUp = new Particle(xVel, yVel, xPos, yPos, 302, 20, false, true);
				world.addEntity(chargeUp);
			}
		}

		public void phase3() {
			//detonate
			//send bullets in every direction around player
			for (int i = 0; i < 366; i+= 10) {
				double angle = Math.toRadians(i);
				float xVelocity = (float) (Math.cos(angle));
				float yVelocity = (float) (Math.sin(angle));
				GenericBullet bullet = new GenericBullet(caster, xVelocity, yVelocity, 10, 100, SpriteList.BULLET);
				world.addEntity(bullet);
			}
			//set player gravity back to normal
			caster.setFallModifier(tempFallModifier);
			caster.setUsingSpell(spellIndex, false);
		}
	}

	/**
	 * Fire a bullet that does default damage, after a while it splits into three bullets that deal 1/3 damage
	 *
	 * @param caster : the entity to cast the spell
	 */
	public static class SplitShot extends BaseSpell {
		private Player caster;
		private World world = World.getInstance();
		private int spellIndex;
		private GenericBullet bullet;
		private int direction;

		public SplitShot(Player caster, int spellIndex) {
			this.caster = caster;
			this.spellIndex = spellIndex;
		}
		
		@Override
		public void phaseSetup() {
			caster.setSpellPhases(spellIndex, 2);
			caster.setSpellLoopIterations(spellIndex, 0, 1);
			caster.setSpellLoopIterations(spellIndex, 1, 10);
			caster.setSpellLoopIterations(spellIndex, 2, 1);
			caster.setSpellLoopTimings(spellIndex, 0, 0);
			caster.setSpellLoopTimings(spellIndex, 1, 10);
			caster.setSpellLoopTimings(spellIndex, 2, 0);
		}
		
		@Override
		public void phase1(int phase) {
			if (phase == 0) {
				//shoot the parent bullet
				direction = caster.getRenderFacing();
				bullet = new GenericBullet(caster, direction, 0, 50, 30, SpriteList.BULLET);
				world.addEntity(bullet);
			}
		}

		public void phase2(int phase) {
			if (phase != 1) {
				phase3();
			}
		}

		public void phase3() {
			//split the bullet into three sections, each with a different y velocity
			GenericBullet bullet1 = new GenericBullet(bullet, direction, -10, 50, 10, SpriteList.BULLET);
			world.addEntity(bullet1);
			GenericBullet bullet2 = new GenericBullet(bullet, direction, 0, 50, 10, SpriteList.BULLET);
			world.addEntity(bullet2);
			GenericBullet bullet3 = new GenericBullet(bullet, direction, 10, 50, 10, SpriteList.BULLET);
			world.addEntity(bullet3);
			bullet.kill(null);

			//player no longer casting
			caster.setUsingSpell(spellIndex, false);
		}
	}

	/**
	 * Freezes all nearby enemies
	 *
	 * @param caster     : the entity to cast the spell
	 * @param spellIndex : index of spell
	 */
	public static class TimeLock extends BaseSpell {
		private Player caster;
		private int spellIndex;
		private List<Entity> nearbyMobs;
		private ArrayList<BaseNPC> freezeTargets = new ArrayList<>();
		private float zoneX;
		private float zoneY;
		private boolean firstRun;

		public TimeLock(Player caster, int spellIndex) {
			this.caster = caster;
			this.spellIndex = spellIndex;
		}

		@Override
		public void phaseSetup() {
			firstRun = true;
			caster.setSpellPhases(spellIndex, 2);
			caster.setSpellLoopIterations(spellIndex, 0, 2);
			caster.setSpellLoopIterations(spellIndex, 1, 20);
			caster.setSpellLoopIterations(spellIndex, 2, 1);
			caster.setSpellLoopTimings(spellIndex, 0, 1);
			caster.setSpellLoopTimings(spellIndex, 1, 50);
			caster.setSpellLoopTimings(spellIndex, 2, 1);
		}

		@Override
		public void phase1(int phase) {
			if (phase != 0) {
				phase2(phase);
			} else {
				if (firstRun) {
					//store the point at which timelock was cast: player can move around
					//as normal during its duration
					zoneX = (float) (caster.getX() + 0.5 * caster.getWidth());
					zoneY = (float) (caster.getY() + 0.5 * caster.getHeight());
					firstRun = false;
				} else {
					//freeze mobs if they fall inside the radius
					nearbyMobs = caster.getNearbyEntities(20);	
					//sort mobs into valid ones only
					for (Entity freezeTarget : nearbyMobs) {
						if (freezeTarget instanceof BaseNPC) {
							freezeTargets.add((BaseNPC)freezeTarget);
						}
					}
					//freeze every valid mob
					for (BaseNPC freezeTarget : freezeTargets) {
						(freezeTarget).setState(EntityState.STUNNED, 100);
					}
				}
			}
		}

		public void phase2(int phase) {
			if (phase == 2) {
				phase3();
			} else {
				//particleFX
				ParticleSource.addParticleSource(zoneX, zoneY, 300, 1, 15, false, true);
			}
		}

		public void phase3() {
			//stop freezing mobs, player no longer casting
			for (BaseNPC freezeTarget : freezeTargets) {
//				((BasicMovingEntity) freezeTarget).setStunned(true);
				freezeTarget.setState(EntityState.DEFAULT);
			}
			this.clean(freezeTargets);
			caster.setUsingSpell(spellIndex, false);
		}

		/**
		 * Cleans up this spell
		 *
		 * @param nearbyEnemyNPCs mobs to be unfrozen
		 */
		private void clean(ArrayList<BaseNPC> nearbyEnemyNPCs) {
			for (BaseNPC freezeTarget : nearbyEnemyNPCs) {
				freezeTarget.setState(EntityState.DEFAULT);
			}
		}
	}

	/**
	 * Lock onto nearby enemies, firing a lethal bullet to each one
	 *
	 * @param caster : the entity to cast the spell
	 */
	public static class HighNoon extends BaseSpell {
		private Player caster;
		private int spellIndex;
		private World world = World.getInstance();
		List<Entity> validTargets = new ArrayList<>();

		public HighNoon(Player caster, int spellIndex) {
			this.caster = caster;
			this.spellIndex = spellIndex;
		}

		@Override
		public void phaseSetup() {
			caster.setSpellPhases(spellIndex, 1);
			caster.setSpellLoopIterations(spellIndex, 0, 1);
			caster.setSpellLoopIterations(spellIndex, 1, 10);
			caster.setSpellLoopIterations(spellIndex, 2, 1);
			caster.setSpellLoopTimings(spellIndex, 0, 0);
			caster.setSpellLoopTimings(spellIndex, 1, 100);
			caster.setSpellLoopTimings(spellIndex, 1, 0);
		}

		@Override
		public void phase1(int phase) {
			//look for mobs to target
			if (phase == 0) {
				List<Entity> nearbyMobs = caster.getNearbyEntities(10);
				int maxTargets = 5;
				int targetCount = 0;
				if (nearbyMobs.size() > 0){
					for (Entity target : nearbyMobs) {
						//cast to BasicMovingEntity, SHOULD cover all mobs
						if (!(target instanceof BasicMovingEntity)) {
							nearbyMobs.remove(target);
						} else {
							targetCount++;
							//prune list to 5 mobs
							if (targetCount > maxTargets) {
								break;
							}
							//show that they are marked: red rectangle overlay
							((BasicMovingEntity) target).setMarked(true);
							validTargets.add(target);
						}
					}
				}

			} else {
				phase2(phase);
			}
		}

		public void phase2(int phase) {
			if (phase != 1) {
				//waiting
				phase3();
			}
		}

		public void phase3() {
			//send a bullet at each enemy until no more enemies need to be shot
			if (!validTargets.isEmpty()) {
				Entity target = validTargets.get(0);
				validTargets.remove(0);
				float casterX = caster.getX();
				float casterY = caster.getY();
				float targetX = target.getX();
				float targetY = target.getY();
				double angle = Math.atan2(targetY - casterY, targetX - casterX);
				float xVelocity = (float) (Math.cos(angle));
				float yVelocity = (float) (Math.sin(angle));
				GenericBullet bullet = new GenericBullet(caster, xVelocity, yVelocity, 100, 500, SpriteList.BULLET);
				world.addEntity(bullet);
				((BasicMovingEntity) target).setMarked(false);
			} else {
				//targeting set empty, player no longer casting
				caster.setUsingSpell(spellIndex, false);
			}
		}
	}

	/**
	 * Shields the player from all damage for limited period of time
	 *
	 * @param caster     : the entity to cast the spell
	 * @param spellIndex : index of spell
	 */
	public static class Shield extends BaseSpell {
		private Player caster;
		private int spellIndex;

		public Shield(Player caster, int spellIndex) {
			this.caster = caster;
			this.spellIndex = spellIndex;
		}
		
		@Override
		public void phaseSetup() {
			caster.setSpellPhases(spellIndex, 1);
			caster.setSpellLoopIterations(spellIndex, 0, 10);
			caster.setSpellLoopIterations(spellIndex, 1, 0);
			caster.setSpellLoopTimings(spellIndex, 0, 100); //currently lasts for 1 seconds, should be changed
			caster.setSpellLoopTimings(spellIndex, 1, 0);
		}

		@Override
		public void phase1(int phase) {
			if (phase == 1) {
				phase2();
			} else {
				caster.setShielded(true);
			}
		}
		
		public void phase2() {
			caster.setShielded(false);
			caster.setUsingSpell(spellIndex, false);
		}
	}

	/**
	 * Boosts the players movement speed for a limited time
	 *
	 * @param caster     : the entity to cast the spell
	 * @param spellIndex : index of spell
	 */
	public static class Overcharge extends BaseSpell {
		private Player caster;
		private int spellIndex;
		
		public Overcharge(Player caster, int spellIndex) {
			this.caster = caster;
			this.spellIndex = spellIndex;
		}

		@Override
		public void phaseSetup() {
			caster.setSpellPhases(spellIndex, 1);
			caster.setSpellLoopIterations(spellIndex, 0, 1);
			caster.setSpellLoopTimings(spellIndex, 0, 1);
		}

		@Override
		public void phase1(int phase) {
			PlayerBuff speedBuff = new PlayerBuff("speed", 5, 5000);
			caster.addPlayerBuff(speedBuff);
			caster.setUsingSpell(spellIndex, false);
		}
	}

	public static class Heal extends BaseSpell {
		private Player caster;
		private int healAmount;
		private int spellIndex;
		
		public Heal(Player caster, int spellIndex, int healAmount) {
			this.caster = caster;
			this.healAmount = healAmount;
			this.spellIndex = spellIndex;
		}

		@Override
		public void phaseSetup() {
			caster.setSpellPhases(spellIndex, 1);
			caster.setSpellLoopIterations(spellIndex, 0, 10);
			caster.setSpellLoopIterations(spellIndex, 1, 0);
			caster.setSpellLoopTimings(spellIndex, 0, 10); //currently lasts for 1 seconds, should be changed
			caster.setSpellLoopTimings(spellIndex, 1, 0);
		}
		
		@Override
		public void phase1(int phase) {
			if (phase == 1) {
				phase2();
			} else {
				caster.addHealth(healAmount);
			}
		}
		public void phase2() {
			caster.setUsingSpell(spellIndex, false);
		}
	}

	public static class Glide extends BaseSpell {
		private Player caster;
		private int glideTime;
		private int spellIndex;

		public Glide(Player caster, int glideTime, int spellIndex) {
			this.caster = caster;
			this.glideTime = glideTime;
			this.spellIndex = spellIndex;
		}

		@Override
		public void phaseSetup() {
			caster.setSpellPhases(spellIndex, 1);
			caster.setSpellLoopIterations(spellIndex, 0, 10);
			caster.setSpellLoopIterations(spellIndex, 1, 0);
			caster.setSpellLoopTimings(spellIndex, 0, this.glideTime);
			caster.setSpellLoopTimings(spellIndex, 1, 0);
		}

		@Override
		public void phase1(int phase) {
			if (phase == 1) {
				phase2();
			} else {
				caster.setGliding(true);
			}
		}

		public void phase2() {
			caster.setGliding(false);
			caster.setUsingSpell(spellIndex, false);
		}
	}


	/**
	 * Makes player appear invisible to mobs
	 *
	 * @param caster        : the entity to cast the spell
	 * @param invisibleTime : time(in seconds) the player stays invisible
	 * @param spellIndex    : index of spell
	 */
	public static class Invisibility extends BaseSpell {
		private Player caster;
		private int spellIndex;
		private int invisibleTime;

		public Invisibility(Player caster, int invisibleTime, int spellIndex) {
			this.caster = caster;
			this.invisibleTime = invisibleTime;
			this.spellIndex = spellIndex;
		}
		
		@Override
		public void phaseSetup() {
			caster.setSpellPhases(spellIndex, 1);
			caster.setSpellLoopIterations(spellIndex, 0, 10);
			caster.setSpellLoopIterations(spellIndex, 1, 0);
			caster.setSpellLoopTimings(spellIndex, 0, this.invisibleTime);
			caster.setSpellLoopTimings(spellIndex, 1, 0);
		}

		@Override
		public void phase1(int phase) {
			if (phase == 1) {
				phase2();
			} else {
				caster.setInvisible(true);
			}
		}

		public void phase2() {
			caster.setInvisible(false);
			caster.setUsingSpell(spellIndex, false);
		}
	}

	/**
	 * creates a ring of fire around the player that damages any enemy in range for an extended period of time.
	 *
	 * @param caster     : the entity to cast the spell
	 * @param spellIndex : index of spell
	 */
	public static class ATField extends BaseSpell {
		private Player caster;
		private int spellIndex;
		private int damageCheck = 0;

		public ATField(Player caster, int spellIndex) {
			this.caster = caster;
			this.spellIndex = spellIndex;
		}

		@Override
		public void phaseSetup() {
			caster.setSpellPhases(spellIndex, 1);
			caster.setSpellLoopIterations(spellIndex, 0, 500);
			caster.setSpellLoopIterations(spellIndex, 1, 0);
			caster.setSpellLoopTimings(spellIndex, 0, 10);
			caster.setSpellLoopTimings(spellIndex, 1, 0);
		}

		@Override
		public void phase1(int phase) {
			if (phase == 1) {
				phase2();
			} else {
				caster.setATField(true);
					if (damageCheck >= 10) {
						//damage mobs inside each tick
						for (int i = 0; i < 50; i++) {
							List<Entity> nearbyMobs = caster.getNearbyEntities(5);
							// Only keep BasicMovingEntities
							for (Entity damageTarget : nearbyMobs) {
								if (!(damageTarget instanceof Particle) && (damageTarget instanceof BaseNPC)) {
									((BaseNPC) damageTarget).receiveDOTDamage(1);
								}
							}
						}
						damageCheck = 0;
					} else {
						damageCheck++;
					}
			}
		}

		public void phase2() {
			caster.setATField(false);
			caster.setUsingSpell(spellIndex, false);
		}
	}

	public static class ForcePush extends BaseSpell {
		private Player caster;
		private List<Entity> nearbyEntities;
		private int spellIndex;
		private int radius;

		public ForcePush(Entity caster, int spellIndex, int radius) {
			this.caster = (Player) caster;
			this.spellIndex = spellIndex;
			this.nearbyEntities = caster.getNearbyEntities(radius);
			this.radius = radius;
		}

		@Override
		public void phaseSetup() {
			this.caster.setSpellPhases(spellIndex, 1);
			this.caster.setSpellLoopIterations(spellIndex, 0, 1);
			this.caster.setSpellLoopIterations(spellIndex, 1, 0);
			this.caster.setSpellLoopTimings(spellIndex, 0, 1000);
			this.caster.setSpellLoopTimings(spellIndex, 1, 0);
		}

		@Override
		public void phase1(int phase) {
			if (phase == 1) {
				phase2();
			} else {
				for (Entity e : nearbyEntities) {
					if (e instanceof BaseNPC) {
						if (e.getX() - caster.getX() < 0 && e.getY() - caster.getY() < 0) {
							double angle = Math.atan2(e.getY() - caster.getY(), e.getX() - caster.getX());
							float xPos = (float) (radius * Math.sin(angle)) + caster.getX();
							float yPos = (float) (radius * Math.cos(angle)) + caster.getY();
							e.setVelocity((-(e.getX() - xPos) * 50), ((yPos - e.getY()) + 10));
						}
						if (e.getX() - caster.getX() > 0 && e.getY() - caster.getY() < 0) {
							double angle = Math.atan2(e.getY() - caster.getY(), e.getX() - caster.getX());
							float xPos = (float) ((float) radius * Math.sin(angle)) + caster.getX();
							float yPos = (float) ((float) radius * Math.cos(angle)) + caster.getY();
							e.setVelocity(((e.getX() - xPos) * 50), e.getY() - yPos + 10);
						}
					}
				}
			}
		}

		public void phase2() {
			caster.setUsingSpell(spellIndex, false);
		}
	}
	
	/**
	 * Boosts the players movement speed for a limited time
	 *
	 * @param caster     : the entity to cast the spell
	 * @param spellIndex : index of spell
	 */
	public static class Invincible extends BaseSpell {
		private Player caster;
		private int spellIndex;
		
		public Invincible(Player caster, int spellIndex, int spellLevel) {
			this.caster = caster;
			this.spellIndex = spellIndex;
			this.spellLevel = spellLevel;
		}

		@Override
		public void phaseSetup() {
			if (spellLevel == 1) {				
				caster.setSpellPhases(spellIndex, 1);
				caster.setSpellLoopIterations(spellIndex, 0, 100);
				caster.setSpellLoopIterations(spellIndex, 1, 0);
				caster.setSpellLoopTimings(spellIndex, 0, 10);
				caster.setSpellLoopTimings(spellIndex, 1, 0);
			} else if (spellLevel == 2) {
					caster.setSpellPhases(spellIndex, 1);
					caster.setSpellLoopIterations(spellIndex, 0, 300);
					caster.setSpellLoopIterations(spellIndex, 1, 0);
					caster.setSpellLoopTimings(spellIndex, 0, 10);
					caster.setSpellLoopTimings(spellIndex, 1, 0);
			}
		}

		@Override
		public void phase1(int phase) {
			if (phase == 1) {
				phase2();
			} else {
				caster.setInvincible();
			}
		}

		public void phase2() {
			caster.disableInvinciblity();
			caster.setUsingSpell(spellIndex, false);
		}
	}

}
