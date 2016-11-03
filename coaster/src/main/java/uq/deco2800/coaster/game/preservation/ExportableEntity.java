package uq.deco2800.coaster.game.preservation;

import uq.deco2800.coaster.game.entities.Decoration;
import uq.deco2800.coaster.game.entities.Entity;
import uq.deco2800.coaster.graphics.sprites.SpriteList;

/**
 * Created by ryanj on 12/08/2016.
 */
public class ExportableEntity {
	public float posX;
	public float posY;
	public float height;
	public float width;
	public boolean onGround;

	public SpriteList spriteID;
	public boolean decoration;


	/**
	 * ExportableEntity default constructor is needed for importing from json
	 **/
	public ExportableEntity() {
	}

	/**
	 * Converts an Entity to an ExportableEntity
	 *
	 * @param entity the Entity to be converted
	 */
	public ExportableEntity(Entity entity) {
		posX = entity.getX();
		posY = entity.getY();
		height = entity.getHeight();
		width = entity.getWidth();

		onGround = entity.isOnGround();
		//must not be null
		spriteID = entity.getSpriteID();
		decoration = entity instanceof Decoration;
	}

}
