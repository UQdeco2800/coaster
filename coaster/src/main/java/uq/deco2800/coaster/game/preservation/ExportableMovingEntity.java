package uq.deco2800.coaster.game.preservation;

import uq.deco2800.coaster.game.entities.BasicMovingEntity;

public class ExportableMovingEntity extends ExportableEntity {

	public float moveSpeed;
	public float jumpSpeed;
	public int currentHealth;


	public ExportableMovingEntity() {

	}

	public ExportableMovingEntity(BasicMovingEntity entity) {
		super(entity);
		//move speed is set by mob
		moveSpeed = entity.getMoveSpeed();
		//Jump speed is set
		jumpSpeed = entity.getJumpSpeed();
		currentHealth = entity.getCurrentHealth();
	}

}
