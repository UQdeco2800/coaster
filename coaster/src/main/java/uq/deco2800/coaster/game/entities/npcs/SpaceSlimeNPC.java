package uq.deco2800.coaster.game.entities.npcs;

import uq.deco2800.coaster.game.entities.AABB;
import uq.deco2800.coaster.game.items.ItemDrop;
import uq.deco2800.coaster.game.items.ItemRegistry;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.game.entities.EntityState;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.entities.npcs.npcactions.DefaultMove;
import uq.deco2800.coaster.graphics.sprites.Sprite;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

import java.util.Random;

/**
 * Created by adam on 10/22/16.
 */
public class SpaceSlimeNPC extends MeleeEnemyNPC{
	private boolean isReal = false;
	public SpaceSlimeNPC(){
		super();

		/* Cheap hack to get it to not jump as per enemy wiki requirements */
		availableActions.clear();
		availableActions.add(new DefaultMove(this));
		availableActions.add(new DefaultMove(this));

		/* Set sprites */
		sprites.put(EntityState.STANDING, new Sprite(SpriteList.SPACE_SLIME));
		sprites.put(EntityState.MOVING, new Sprite(SpriteList.SPACE_SLIME));
		sprites.put(EntityState.JUMPING, new Sprite(SpriteList.SPACE_SLIME));
		setSprite(sprites.get(EntityState.MOVING));

		/* Make smaller and faster */
		bounds = new AABB(posX, posY, 1f, 1f);
		bounds.setActive(true);
		commonHitboxes.clear();
		hitboxesCache.clear();
		moveSpeedHor = 3;
		baseDamage = 1;
		terminalVelocityModifier = 0.01f;
	}

	@Override
	public void onDeath(Entity cause) {
		if (!isReal){
			if (world.getNpcEntities().size() < 100){
				SpaceSlimeNPC fakeSlime1 = new SpaceSlimeNPC();
				SpaceSlimeNPC fakeSlime2 = new SpaceSlimeNPC();
				fakeSlime1.setPosition(getX()-1,getY());
				fakeSlime2.setPosition(getX()+1,getY());
				world.addEntity(fakeSlime1);
				world.addEntity(fakeSlime2);
			}
			this.delete();
		} else {
			for (Entity entity : world.getNpcEntities()){
				if (entity instanceof SpaceSlimeNPC){
					((SpaceSlimeNPC) entity).delete();
				}
			}
			super.onDeath(cause);
		}
		if (isBoss) {
			((Player) cause).addBossKill("SPACESLIME BOSS");
		}
		ItemDrop.drop(ItemRegistry.getItem("slimesoul"), this.getX(),  this.getY());
		ItemDrop.drop(ItemRegistry.getItem("slime"), this.getX(),  this.getY());
	}

	public void spawnMob(){
		Random random = new Random();
		isReal = true;
		for (int i = 0; i < 5; i++){
			SpaceSlimeNPC slime = new SpaceSlimeNPC();
			slime.setPosition(getX()+random.nextInt(15), getY()+random.nextInt(5));
			world.addEntity(slime);
		}
	}
}
