package uq.deco2800.coaster.game.entities.npcs;

import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.GenericExplosion;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * Created by adam on 10/22/16.
 */
public class ExplosionBunnyNPC extends MeleeEnemyNPC {
	private int fadeTimer;
	private boolean exploded;
	public ExplosionBunnyNPC(){
		super();
		moveSpeedHor = 15;
		moveSpeedVer = 15;
		baseDamage = (int) (15 * difficultyScale);

		/* Set sprites */
		sprites.put(EntityState.STANDING, new Sprite(SpriteList.EXPLOSION_BUNNY_SITTING));
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.EXPLOSION_BUNNY_SITTING));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.EXPLOSION_BUNNY_JUMPING));
		sprites.put(EntityState.DEAD, new Sprite(SpriteList.EXPLOSION_BUNNY_EXPLOSION));
		setSprite(sprites.get(EntityState.STANDING));
	}

	@Override
	public void determineNextAction(){
		/* Target the player */
		Player target = determinePriorityTarget();
		distanceFromPlayer = getDistanceFromPlayer(target);
		determineFacingDirection(target, false);
		boolean withinAttackRange = (distanceFromPlayer <= attackRangeHor) && (yDistance <= attackRangeVer);

		if (withinAttackRange){
			GenericExplosion explosion = new GenericExplosion(bounds.posX(), this.bounds.posY(), 10);
			world.addEntity(explosion);
			target.addHealth(-baseDamage);
			this.delete();
		}

		/* Animate NPC */
		if (isOnGround()){
			availableActions.get(1).execute();
		}
	}
}
