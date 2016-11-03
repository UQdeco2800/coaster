package uq.deco2800.coaster.game.entities;

//Possible states an entity might be in. This is really only used for character entities (the player, mobs, etc), and as
//a key for animations (sprites)
public enum EntityState {
	DEFAULT,
	KNOCK_BACK, //Implementation is different to NPC STUNNED, avoiding namespace clashes as well
	STUNNED,
	STANDING, //For when the entity is standing still
	MOVING, //Entity is moving
	JUMPING, //Entity is in midair
	WALL_SLIDING, //Entity is in the air and against a wall
	DASHING,
	SLIDING,
	AIR_DASHING,
	CROUCHING,
	DEAD, 
	SPRINTING,
	AIR_CROUCHING,
	ATTACKING,
	STRIKING,
	INVINCIBLE
}
