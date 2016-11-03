package uq.deco2800.coaster.game.entities.npcs.mounts;

import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * This mount is a flying bird.
 */
public class BirdMount extends Mount {
	public BirdMount() {
		super();
		bounds = new AABB(posX, posY, 4f, 3f);
		sprites.put(EntityState.DEFAULT, new Sprite(SpriteList.BIRD_STATIC));
		sprites.put(EntityState.STANDING, new Sprite(SpriteList.BIRD_FLYING));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.BIRD_FLYING));
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.BIRD_FLYING));

		this.setState(EntityState.DEFAULT);
		this.saddleX = 1;
		this.saddleY = 0;
		this.setMoveSpeed(15);
		this.jumpSpeed = -30;
		this.flyingMount = true;
	}
}
