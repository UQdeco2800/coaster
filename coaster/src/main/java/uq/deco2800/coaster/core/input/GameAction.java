package uq.deco2800.coaster.core.input;

//A list of possible controls/game actions.
public enum GameAction {
	MOVE_LEFT,
	MOVE_RIGHT,
	JUMP,
	BASIC_ATTACK,
	SPECIAL_ATTACK,

	//CieranKent
	DASH,
	SLIDE,
	MOVE_UP,
	MOVE_DOWN,

	//Steven
	PAUSE,

	//WilliamHayward
	SHOW_DEBUG,
	SHOW_HITBOXES,
	DELETE_TILE,
	ADD_TILE,
	SHOW_MAP,

	//ScriptSmith
	ADD_NPC,

	//Skills
	//Wilson
	SKILL_TREE,        //Skill tree menu key

	//Callum
	SKILL_KEY_W,
	SKILL_KEY_Q,
	SKILL_KEY_E,
	SKILL_KEY_R,

	//Anh Tran, CPS
	SHOW_CONTROLS,
	RE_MAP,
	QUERY_KEY,
	PRINT_TILE,
	ADD_MOUNT,
	ACTIVATE_MOUNT,
	SKIP_TUTORIAL,
	ENABLE_CHECKPOINTS, 
	
	//Bosco
	INVENTORY,

	//Hayley
	WEAPON_ONE,
	WEAPON_TWO,
	WEAPON_THREE,
	WEAPON_FOUR,
	WEAPON_FIVE,
	WEAPON_SIX,
	WEAPON_SEVEN,
	WEAPON_EIGHT,
	WEAPON_NINE,

	//Leon
	SKILL_TREE_UI,
	PASSIVE_SKILL,


	// Commerce
	TRADER_NPC,

	// Debug Console
	DEBUG_CONSOLE,

	//Sound
	MUTE,
	VOLUME_UP,
	VOLUME_DOWN,

	//Companion
	UPGRADE_COMPANION,
	CHANGE_MODE,

	//ROOMS
	ENTER_ROOM,
	
	// NPC gen
	TOGGLE_NPC_GEN
}
