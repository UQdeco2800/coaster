package uq.deco2800.coaster.game.mechanics;

/** The ordering of entries in this enum are surprisingly important
 * 
 * VOID should always be the first entry, followed by MAIN. After that, other entries are ranked in hitbox priority
 * i.e. HEAD comes after MAIN because a collision with head hitbox has priority over a collision with a MAIN hitbox
 * 
 * 
 * @author Cieran
 *
 */
public enum BodyPart {
	VOID,
	MAIN,
	BACK,
	SHIELD,
	ARM,
	HEAD,
	HEAD_ARMOUR_WEAK,
	BODY_ARMOUR_WEAK,
	LEG_ARMOUR_WEAK,
	FOOT_ARMOUR_WEAK,
	HEAD_ARMOUR_MED,
	BODY_ARMOUR_MED,
	LEG_ARMOUR_MED,
	FOOT_ARMOUR_MED,
	HEAD_ARMOUR_STRONG,
	BODY_ARMOUR_STRONG,
	LEG_ARMOUR_STRONG,
	FOOT_ARMOUR_STRONG;
	
	public float getMultiplier() {
		switch(this) {
			case VOID:
			case SHIELD:
				return 0;
			case HEAD:
				return 1.5f;
			case HEAD_ARMOUR_WEAK:
				return 1f;
			case BODY_ARMOUR_WEAK:
				return 0.8f;
			case LEG_ARMOUR_WEAK:
				return 0.9f;
			case FOOT_ARMOUR_WEAK:
				return 0.9f;
			case HEAD_ARMOUR_MED:
				return 0.8f;
			case BODY_ARMOUR_MED:
				return 0.6f;
			case LEG_ARMOUR_MED:
				return 0.5f;
			case FOOT_ARMOUR_MED:
				return 0.5f;
			case HEAD_ARMOUR_STRONG:
				return 0.6f;
			case BODY_ARMOUR_STRONG:
				return 0.4f;
			case LEG_ARMOUR_STRONG:
				return 0.3f;
			case FOOT_ARMOUR_STRONG:
				return 0.3f;
			default:
				return 1;
		}
	}
	
}
