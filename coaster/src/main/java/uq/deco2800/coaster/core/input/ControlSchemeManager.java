package uq.deco2800.coaster.core.input;

import java.util.Map;

import javafx.scene.input.KeyCode;

/**
 * This class is used to define and manage different control schemes.
 */
public class ControlSchemeManager {

	private ControlSchemeManager() {

	}

	/**
	 * Adds the key mappings for the specified control scheme to the specified
	 * KeyCode/GameAction map.
	 *
	 * @param controlScheme The control scheme to use
	 * @param keymap The KeyCode/GameAction map to store the key mappings in
	 */
	public static void loadControlScheme(ControlScheme controlScheme, Map<KeyCode, GameAction> keymap) {
		// KobiMcKerihan (kobimac)
		keymap.put(KeyCode.Z, GameAction.BASIC_ATTACK);
		keymap.put(KeyCode.X, GameAction.SPECIAL_ATTACK);
		keymap.put(KeyCode.W, GameAction.MOVE_UP);
		keymap.put(KeyCode.S, GameAction.MOVE_DOWN);

		// CieranKent
		keymap.put(KeyCode.SHIFT, GameAction.DASH);
		keymap.put(KeyCode.CONTROL, GameAction.SLIDE);

		// Steven
		keymap.put(KeyCode.ESCAPE, GameAction.PAUSE);

		// Skills keys
		// Wilson
		keymap.put(KeyCode.T, GameAction.SKILL_TREE_UI);

		// WilliamHayward
		keymap.put(KeyCode.J, GameAction.SHOW_DEBUG);
		keymap.put(KeyCode.H, GameAction.SHOW_HITBOXES);
		keymap.put(KeyCode.BACK_SPACE, GameAction.DELETE_TILE);
		keymap.put(KeyCode.ENTER, GameAction.ADD_TILE);

		// CPS
		keymap.put(KeyCode.EQUALS, GameAction.RE_MAP);
		keymap.put(KeyCode.SLASH, GameAction.ENABLE_CHECKPOINTS);

		// Callum ?
		keymap.put(KeyCode.F, GameAction.SKILL_KEY_W);
		keymap.put(KeyCode.Q, GameAction.SKILL_KEY_Q);
		keymap.put(KeyCode.E, GameAction.SKILL_KEY_E);
		keymap.put(KeyCode.R, GameAction.SKILL_KEY_R);

		// Anh Tran, Show ControlsScreen
		keymap.put(KeyCode.C, GameAction.SHOW_CONTROLS);
		// CPS
		keymap.put(KeyCode.EQUALS, GameAction.RE_MAP);
		keymap.put(KeyCode.F1, GameAction.QUERY_KEY);
		keymap.put(KeyCode.F2, GameAction.PRINT_TILE);
		keymap.put(KeyCode.F3, GameAction.ADD_MOUNT);
		keymap.put(KeyCode.G, GameAction.ACTIVATE_MOUNT);
		keymap.put(KeyCode.Y, GameAction.SKIP_TUTORIAL);

		// Bosco, Inventory
		keymap.put(KeyCode.I, GameAction.INVENTORY);

		// Commerce
		keymap.put(KeyCode.V, GameAction.TRADER_NPC);
		keymap.put(KeyCode.BACK_SLASH, GameAction.DEBUG_CONSOLE);

		// Hayley, switching weapons (subject to change with inventory
		// updates)
		keymap.put(KeyCode.DIGIT1, GameAction.WEAPON_ONE);
		keymap.put(KeyCode.DIGIT2, GameAction.WEAPON_TWO);
		keymap.put(KeyCode.DIGIT3, GameAction.WEAPON_THREE);
		keymap.put(KeyCode.DIGIT4, GameAction.WEAPON_FOUR);
		keymap.put(KeyCode.DIGIT5, GameAction.WEAPON_FIVE);
		keymap.put(KeyCode.DIGIT6, GameAction.WEAPON_SIX);
		keymap.put(KeyCode.DIGIT7, GameAction.WEAPON_SEVEN);
		keymap.put(KeyCode.DIGIT8, GameAction.WEAPON_EIGHT);
		keymap.put(KeyCode.DIGIT9, GameAction.WEAPON_NINE);

		keymap.put(KeyCode.L, GameAction.SHOW_MAP);

		keymap.put(KeyCode.U, GameAction.UPGRADE_COMPANION);

		// ScriptSmith, NPC debugging
		keymap.put(KeyCode.N, GameAction.ADD_NPC);

		// Daniel, Sound Controls
		keymap.put(KeyCode.M, GameAction.MUTE);
		keymap.put(KeyCode.COMMA, GameAction.VOLUME_DOWN);
		keymap.put(KeyCode.PERIOD, GameAction.VOLUME_UP);

		// Blake, Companion Mode
		keymap.put(KeyCode.P, GameAction.CHANGE_MODE);

		// Rooms
		keymap.put(KeyCode.K, GameAction.ENTER_ROOM);

		if (controlScheme == ControlScheme.DEFAULT) {
			// KobiMcKerihan (kobimac)
			keymap.put(KeyCode.A, GameAction.MOVE_LEFT);
			keymap.put(KeyCode.D, GameAction.MOVE_RIGHT);
			keymap.put(KeyCode.SPACE, GameAction.JUMP);

		} else if (controlScheme == ControlScheme.ALTERNATE_1) {
			// KobiMcKerihan (kobimac)
			keymap.put(KeyCode.LEFT, GameAction.MOVE_LEFT);
			keymap.put(KeyCode.RIGHT, GameAction.MOVE_RIGHT);
			keymap.put(KeyCode.UP, GameAction.JUMP);
		}
	}
}
