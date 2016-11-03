package uq.deco2800.coaster.game.entities.npcs;

import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * Created by adam on 10/20/16.
 */
public class GolemNPC extends MeleeEnemyNPC {
	public GolemNPC(){
		super();
		baseDamage = 25;
		moveSpeedHor = 2;
		jumpSpeed = 1;
		maxHealth = 1000;
		currentHealth = maxHealth;
		/* Set sprites */
		sprites.put(EntityState.STANDING, new Sprite(SpriteList.GOLEM_STANDING));
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.GOLEM_WALKING));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.GOLEM_WALKING));
		setSprite(sprites.get(EntityState.MOVING));
	}
}
