package uq.deco2800.coaster.graphics.screens.controllers;


import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import uq.deco2800.coaster.core.sound.SoundCache;
import uq.deco2800.coaster.graphics.notifications.Toaster;
import uq.deco2800.coaster.game.items.ItemRegistry;
import uq.deco2800.coaster.game.items.Armour;
import uq.deco2800.coaster.game.items.enchantments.*;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.inventory.Crafting;
import uq.deco2800.coaster.game.inventory.Inventory;
import uq.deco2800.coaster.game.world.World;


/**
 * Controller for inventory fxml. Responsible for front end inventory functionality.
 *
 * @author RohanSingh
 */
public class InventoryController implements EventHandler<ActionEvent> {
	/**
	 * Booleans determining the state of the inventory i.e item selected, crafting mode on
	 */
	private boolean weaponActive = false;
	private boolean craftingActive = false;
	private final static String EMPTY = "empty";
	private final static String EMPTY_SLOT = "emptyslot";
	private final static String HELMET = "helmet";
	private final static String QUANTITY = "Quantity: ";
	private final static String CRAFTING_ACTIVE = "Crafting Active";
	private final static String SPECIFY_ITEM = "Specify Ingredient";

	//Strings for crafting
	private String firstID="";
	private String secondID="";
	private String thirdID="";
	private String fourthID="";
	
	//Strings for enchanting
	private String armourID="";
	private String gemID="";

	/**
	 * Images for the sprites that sit in the inventory slots
	 */

	private Image swapImageFirst;
	private Image swapImageSecond;
	private Image trashImage = new Image(getClass().getClassLoader()
			.getResourceAsStream("sprites/ui/inventory/Empty Slot.png"));
	private Image playerImage = new Image(getClass().getClassLoader()
			.getResourceAsStream("sprites/knight-wallslide.png"));
	private Image craftingImage = new Image(getClass().getClassLoader()
			.getResourceAsStream("sprites/ui/inventory/Player-Crafting.png"));
	private Image enchantingImage = new Image(getClass().getClassLoader()
			.getResourceAsStream("sprites/ui/inventory/enchanting-icon.png"));
	private Image defaultWindow = new Image(getClass().getClassLoader()
			.getResourceAsStream("sprites/ui/inventory/Main Window.png"));
	private Image craftingWindow = new Image(getClass().getClassLoader()
			.getResourceAsStream("sprites/ui/inventory/Crafting Window.png"));
	private ImageView activeSpriteFirst;
	private ImageView activeSpriteSecond;

	/**
	 * Variables sourced from player stats and commerce
	 */

	private Integer moneyAmount;

	//Crafting
	private int craftingStage = 1;
	Crafting craft = new Crafting();

	/**
	 * Variables used to retrieve information from the inventory.java arrays
	 */
	private String firstItemID = "";
	private String secondItemID = "";
	private int firstItemQuantity = 0;
	private String firstArray;
	private String secondArray;
	private int firstIndex;
	private int secondIndex;
	/**
	 * The inventory that needs to be worked on
	 */
	private Inventory inv = null;
	private Player player = null;
	@FXML
	Label descTitle;
	@FXML
	Label descLine1;
	@FXML
	Label descLine2;
	@FXML
	Label descLine3;
	@FXML
	Label descLine4;
	@FXML
	Label descLine5;
	@FXML
	Label playerMoney;


	@FXML
	Button wEquip1;
	@FXML
	Button wEquip2;
	@FXML
	Button wEquip3;
	@FXML
	Button wEquip4;
	@FXML
	Button wEquip5;
	@FXML
	Button aEquip1;
	@FXML
	Button aEquip2;
	@FXML
	Button aEquip3;
	@FXML
	Button aEquip4;
	@FXML
	Button iSlot1;
	@FXML
	Button iSlot2;
	@FXML
	Button iSlot3;
	@FXML
	Button iSlot4;
	@FXML
	Button iSlot5;
	@FXML
	Button iSlot6;
	@FXML
	Button iSlot7;
	@FXML
	Button iSlot8;
	@FXML
	Button iSlot9;
	@FXML
	Button iSlot10;
	@FXML
	Button iSlot11;
	@FXML
	Button iSlot12;
	@FXML
	Button iSlot13;
	@FXML
	Button iSlot14;
	@FXML
	Button iSlot15;
	@FXML
	Button iSlot16;
	@FXML
	Button iSlot17;
	@FXML
	Button iSlot18;
	@FXML
	Button iSlot19;
	@FXML
	Button iSlot20;
	@FXML
	Button iSlot21;
	@FXML
	Button iSlot22;
	@FXML
	Button iSlot23;
	@FXML
	Button iSlot24;
	@FXML
	Button iSlot25;
	@FXML
	Button iSlot26;
	@FXML
	Button iSlot27;
	@FXML
	Button iSlot28;
	@FXML
	Button iSlot29;
	@FXML
	Button confirmCrafting;
	@FXML
	Button confirmEnchant;

	@FXML
	ImageView mainWindow;
	@FXML
	ImageView playerSprite;
	@FXML
	ImageView displayImage;
	@FXML
	ImageView iSlot1Sprite;
	@FXML
	ImageView iSlot2Sprite;
	@FXML
	ImageView iSlot3Sprite;
	@FXML
	ImageView iSlot4Sprite;
	@FXML
	ImageView iSlot5Sprite;
	@FXML
	ImageView iSlot6Sprite;
	@FXML
	ImageView iSlot7Sprite;
	@FXML
	ImageView iSlot8Sprite;
	@FXML
	ImageView iSlot9Sprite;
	@FXML
	ImageView iSlot10Sprite;
	@FXML
	ImageView iSlot11Sprite;
	@FXML
	ImageView iSlot12Sprite;
	@FXML
	ImageView iSlot13Sprite;
	@FXML
	ImageView iSlot14Sprite;
	@FXML
	ImageView iSlot15Sprite;
	@FXML
	ImageView iSlot16Sprite;
	@FXML
	ImageView iSlot17Sprite;
	@FXML
	ImageView iSlot18Sprite;
	@FXML
	ImageView iSlot19Sprite;
	@FXML
	ImageView iSlot20Sprite;
	@FXML
	ImageView iSlot21Sprite;
	@FXML
	ImageView iSlot22Sprite;
	@FXML
	ImageView iSlot23Sprite;
	@FXML
	ImageView iSlot24Sprite;
	@FXML
	ImageView iSlot25Sprite;
	@FXML
	ImageView iSlot26Sprite;
	@FXML
	ImageView iSlot27Sprite;
	@FXML
	ImageView iSlot28Sprite;
	@FXML
	ImageView iSlot29Sprite;

	@FXML
	ImageView wEquip1Sprite;
	@FXML
	ImageView wEquip2Sprite;
	@FXML
	ImageView wEquip3Sprite;
	@FXML
	ImageView wEquip4Sprite;
	@FXML
	ImageView wEquip5Sprite;
	@FXML
	ImageView aEquip1Sprite;
	@FXML
	ImageView aEquip2Sprite;
	@FXML
	ImageView aEquip3Sprite;
	@FXML
	ImageView aEquip4Sprite;


	private static final String ACTIVE_ARRAY = "Active Inventory";
	private static final String PASSIVE_ARRAY = "Passive Inventory";
	private static final String GENERAL_ARRAY = "General Inventory";

	private HashMap<Integer, ImageView> inventorySprite = new HashMap<Integer, ImageView>();
	private HashMap<Integer, Button> inventoryButton = new HashMap<Integer, Button>();

	public InventoryController() {
		FXMLControllerRegister.register(InventoryController.class, this);
	}

	@FXML
	public void initialize() {
		inventorySprite.put(1, wEquip1Sprite);
		inventorySprite.put(2, wEquip2Sprite);
		inventorySprite.put(3, wEquip3Sprite);
		inventorySprite.put(4, wEquip4Sprite);
		inventorySprite.put(5, wEquip5Sprite);
		inventorySprite.put(6, aEquip1Sprite);
		inventorySprite.put(7, aEquip2Sprite);
		inventorySprite.put(8, aEquip3Sprite);
		inventorySprite.put(9, aEquip4Sprite);
		inventorySprite.put(10, iSlot1Sprite);
		inventorySprite.put(11, iSlot2Sprite);
		inventorySprite.put(12, iSlot3Sprite);
		inventorySprite.put(13, iSlot4Sprite);
		inventorySprite.put(14, iSlot5Sprite);
		inventorySprite.put(15, iSlot6Sprite);
		inventorySprite.put(16, iSlot7Sprite);
		inventorySprite.put(17, iSlot8Sprite);
		inventorySprite.put(18, iSlot9Sprite);
		inventorySprite.put(19, iSlot10Sprite);
		inventorySprite.put(20, iSlot11Sprite);
		inventorySprite.put(21, iSlot12Sprite);
		inventorySprite.put(22, iSlot13Sprite);
		inventorySprite.put(23, iSlot14Sprite);
		inventorySprite.put(24, iSlot15Sprite);
		inventorySprite.put(25, iSlot16Sprite);
		inventorySprite.put(26, iSlot17Sprite);
		inventorySprite.put(27, iSlot18Sprite);
		inventorySprite.put(28, iSlot19Sprite);
		inventorySprite.put(29, iSlot20Sprite);
		inventorySprite.put(30, iSlot21Sprite);
		inventorySprite.put(31, iSlot22Sprite);
		inventorySprite.put(32, iSlot23Sprite);
		inventorySprite.put(33, iSlot24Sprite);
		inventorySprite.put(34, iSlot25Sprite);
		inventorySprite.put(35, iSlot26Sprite);
		inventorySprite.put(36, iSlot27Sprite);
		inventorySprite.put(37, iSlot28Sprite);
		inventorySprite.put(38, iSlot29Sprite);
		inventoryButton.put(1, wEquip1);
		inventoryButton.put(2, wEquip2);
		inventoryButton.put(3, wEquip3);
		inventoryButton.put(4, wEquip4);
		inventoryButton.put(5, wEquip5);
		inventoryButton.put(6, aEquip1);
		inventoryButton.put(7, aEquip2);
		inventoryButton.put(8, aEquip3);
		inventoryButton.put(9, aEquip4);
		inventoryButton.put(10, iSlot1);
		inventoryButton.put(11, iSlot2);
		inventoryButton.put(12, iSlot3);
		inventoryButton.put(13, iSlot4);
		inventoryButton.put(14, iSlot5);
		inventoryButton.put(15, iSlot6);
		inventoryButton.put(16, iSlot7);
		inventoryButton.put(17, iSlot8);
		inventoryButton.put(18, iSlot9);
		inventoryButton.put(19, iSlot10);
		inventoryButton.put(20, iSlot11);
		inventoryButton.put(21, iSlot12);
		inventoryButton.put(22, iSlot13);
		inventoryButton.put(23, iSlot14);
		inventoryButton.put(24, iSlot15);
		inventoryButton.put(25, iSlot16);
		inventoryButton.put(26, iSlot17);
		inventoryButton.put(27, iSlot18);
		inventoryButton.put(28, iSlot19);
		inventoryButton.put(29, iSlot20);
		inventoryButton.put(30, iSlot21);
		inventoryButton.put(31, iSlot22);
		inventoryButton.put(32, iSlot23);
		inventoryButton.put(33, iSlot24);
		inventoryButton.put(34, iSlot25);
		inventoryButton.put(35, iSlot26);
		inventoryButton.put(36, iSlot27);
		inventoryButton.put(37, iSlot28);
		inventoryButton.put(38, iSlot29);
	}


	/**
	 * Used to identify which item slot has been selected
	 */
	@Override
	public void handle(ActionEvent evt) {
		String slotName;
		for (int i = 1; i < 39; i++) {
			Integer slotNo;
			Button currentButton = inventoryButton.get(i);
			ImageView currentSprite = inventorySprite.get(i);
			if (i<=5){
				slotName = "wEquip" + i;
				slotNo = i-1;
			}
			else if (i>5 && i<10){
				slotName = "aEquip" + (i-5);
				slotNo = i-6;
			}
			else {
				slotName = "iSlot" + (i-9);
				slotNo = i-10;
			}
			if (evt.getSource().equals(currentButton)){
				moveItem(slotName, currentSprite, slotNo, true);
			}
		}
	}

	/**
	 * Used to update the sprites in the inventory slot
	 */
	public void updateSprites() {
		String slotName;
		for (int i = 1; i < 30; i++) {
			Integer slotNo;
			inventoryButton.get(i);
			ImageView currentSprite = inventorySprite.get(i);
			if (i<6){
				slotName = "wEquip" + i;
				slotNo = i-1;
			}
			else if (i<10){
				slotName = "aEquip" + (i-5);
				slotNo = i-6;
			}
			else {
				slotName = "iSlot" + (i-9);
				slotNo = i-10;
			}
			moveItem(slotName, currentSprite, slotNo, false);
			moveItem(slotName, currentSprite, slotNo, false);

		}
		generateStats();
	}


	/**
	 * Responsible for moving items between storage and equip slots in the inventory screen. Displays information about
	 * the selected item in the description box
	 *
	 * @param itemslot - String corresponding to a slot in the inventory
	 * @param spriteSlot - The Image associated with the itemSlot
	 * @param index - Integer that represents the slot number
	 * @param stack set true if you would like the items you are swapping to be stacked
	 */
	public void moveItem(String itemslot, ImageView spriteSlot, Integer index, boolean stack) {
		if (player == null || inv == null) {
			player = World.getInstance().getFirstPlayer();
			inv = World.getInstance().getFirstPlayer().getInventory();
		}

		if (craftingActive){
			craftItems(itemslot, spriteSlot, index);
			return;
		}

		String first = Character.toString(itemslot.charAt(0));
		String second = Character.toString(itemslot.charAt(1));
		if (!weaponActive) {
			activeSpriteFirst = spriteSlot;
			firstIndex = index;
			if (second.endsWith("S")) {
				firstArray = GENERAL_ARRAY;
				firstItemID = inv.getItemID(GENERAL_ARRAY, firstIndex);
				firstItemQuantity = inv.getItemQuantity(GENERAL_ARRAY, firstIndex);
			} else {
				firstArray = getArrayName(first);
				firstItemID = inv.getItemID(firstArray, firstIndex);
				firstItemQuantity = inv.getItemQuantity(firstArray, firstIndex);
			}
			updateUIFirst();
			weaponActive = true;
		} else {
			activeSpriteSecond = spriteSlot;
			secondIndex = index;
			if (second.endsWith("S")) {
				secondArray = GENERAL_ARRAY;
				secondItemID = inv.getItemID(GENERAL_ARRAY, secondIndex);
			} else {
				secondArray = getArrayName(first);
				secondItemID = inv.getItemID(secondArray, secondIndex);
			}

			if (!checkValidType()){
				weaponActive = false;
				return;
			} else if (!checkLevelRequirements()){
				weaponActive = false;
				Toaster.toast("You are not a high enough level to equip this many weapons");
				return;
			}
			if (firstItemID == secondItemID && (firstIndex != secondIndex)){
				updateUISecond(true);
			} else {
				updateUISecond(false);
			}
			inv.swapItem(firstArray, firstIndex, secondArray, secondIndex, stack);
			weaponActive = false;
		}
		player.updateWeapons();

	}

	/**
	 * This method are using to find the array name for different item type the function use case to search the
	 * firstletter of all the array, and return the string of the array name that match to the case for function found
	 *
	 * @param firstLetter string for find out
	 * @return string the name of the array
	 */
	public String getArrayName(String firstLetter) {
		switch (firstLetter) {
			case "w":
				return ACTIVE_ARRAY;
			case "a":
				return PASSIVE_ARRAY;
			default:
				return GENERAL_ARRAY;
		}
	}

	/** Checks to see that the item being moved is moving into a valid slot
	 *  Will return false if invalid (i.e moving weapon into armour slot)
	 * @return true if the item is moving to a valid slot
	 */
	public boolean checkValidType(){
		if(firstItemID != EMPTY_SLOT && (inv.getItemType(firstItemID)
				!= inv.getItemType("Gun1")) && secondArray == ACTIVE_ARRAY){
			Toaster.toast("You cannot place this item in a active slot");
			return false;
		} else if(firstItemID != EMPTY_SLOT && (inv.getItemType(secondItemID)
				!= inv.getItemType("Gun1")) && firstArray == ACTIVE_ARRAY){
			Toaster.toast("You cannot place this item in a active slot");
			return false;
		} else if (firstItemID != EMPTY_SLOT && (inv.getItemType(firstItemID)
				!= inv.getItemType(HELMET)) && secondArray == PASSIVE_ARRAY){
			Toaster.toast("You cannot place this item in a passive slot");
			return false;
		} else if (firstItemID != EMPTY_SLOT && (inv.getItemType(secondItemID)
				!= inv.getItemType(HELMET)) && firstArray == PASSIVE_ARRAY){
			Toaster.toast("You cannot place this item in a passive slot");
			return false;
			}
			return true;
		}
	

	/** Checks to see that the item can be equip or not
	 *  Will return false if not get the level require
	 * @return true if the item can be equip
	 */
	public boolean checkLevelRequirements(){
		if (firstArray == ACTIVE_ARRAY && secondArray == ACTIVE_ARRAY){
			return inv.checkActiveAmount(player.getPlayerStatsClass().getPlayerLevel(), false);
		} else if (secondArray == ACTIVE_ARRAY){
			return inv.checkActiveAmount(player.getPlayerStatsClass().getPlayerLevel(), true);
		} else{
			return true;
		}
	}

	/**
	 * Updates the Inventory UI with information about the selected item
	 */
	public void updateUIFirst() {
		if (inv == null) {
			inv = World.getInstance().getFirstPlayer().getInventory();
		}
		String description = inv.getItemInfo(firstItemID, "Description");
		String firstDesc;
		String secondDesc;
		String thirdDesc ;
		if (description.length()<16) {
			firstDesc = description;
			descLine1.setText(firstDesc);
			descLine2.setText(QUANTITY + firstItemQuantity);
			descLine3.setText("");
			descLine4.setText("");
		} else if (description.length()>16 && description.length()<32){
			firstDesc = description.substring(0, 16);
			secondDesc = description.substring(16);
			descLine1.setText(firstDesc);
			descLine2.setText(secondDesc);
			descLine3.setText(QUANTITY + firstItemQuantity);
			descLine4.setText("");
		} else if (description.length()>32){
			firstDesc = description.substring(0, 16);
			secondDesc = description.substring(16,32);
			thirdDesc = description.substring(32);
			descLine1.setText(firstDesc);
			descLine2.setText(secondDesc);
			descLine3.setText(thirdDesc);
			descLine4.setText(QUANTITY + firstItemQuantity);
		}
		swapImageFirst = inv.getSprite(firstItemID);
		descTitle.setText(inv.getItemInfo(firstItemID, "Name"));
		displayImage.setImage(swapImageFirst);
		playerSprite.setImage(trashImage);
	}


	/**
	 * Updates the inventory UI by exchanging the two selected sprites
	 *
	 * @param stack - set to true if the two items are meant to stack
	 */
	public void updateUISecond(boolean stack) {
		if (inv == null) {
			inv = World.getInstance().getFirstPlayer().getInventory();
		}
		swapImageSecond = inv.getSprite(secondItemID);
		activeSpriteSecond.setImage(swapImageFirst);
		activeSpriteFirst.setImage(swapImageSecond);
		displayImage.setImage(trashImage);
		playerSprite.setImage(playerImage);
		if (stack){
			activeSpriteFirst.setImage(trashImage);
		}
		generateStats();
	}

	/**
	 * Toggles the inventory between crafting and non-crafting mode
	 */
	public void toggleCrafting(){
		if (craftingActive){
			SoundCache.play("back");
			craftingActive = false;
			craftingStage = 1;
			generateStats();
			mainWindow.setImage(defaultWindow);
			displayImage.setImage(trashImage);
			playerSprite.setImage(playerImage);
		} else {
			SoundCache.play("click");
			craftingActive = true;
			craftTemplate();
			mainWindow.setImage(craftingWindow);
			displayImage.setImage(craftingImage);
			playerSprite.setImage(trashImage);
		}
	}

	/**
	 *  Generates template for fresh crafting screen
	 */
	public void craftTemplate(){
		descTitle.setText(CRAFTING_ACTIVE);
		descLine1.setText(SPECIFY_ITEM);
		descLine2.setText(SPECIFY_ITEM);
		descLine3.setText(SPECIFY_ITEM);
		descLine4.setText(SPECIFY_ITEM);
	}

	/**
	 *  Responsible for the crafting mechanic in the inventory, checks if the recipe is
	 *  valid after each item is selected
	 * @param itemslot - the slot selected in the inventory
	 * @param spriteSlot - the sprite associated with the item slot
	 * @param index - the slot's position as part of the invetory array
	 */

	public void craftItems(String itemslot, ImageView spriteSlot, Integer index){
		String first = Character.toString(itemslot.charAt(0));
		String second = Character.toString(itemslot.charAt(1));
		String itemID;

		if (second.endsWith("S")){
			itemID = inv.getItemID(GENERAL_ARRAY, index);
		} else {
			itemID = inv.getItemID(getArrayName(first), index);
		}

		if (craftingStage == 1){
			firstIndex = index;
			firstID = itemID;
			firstArray = getArrayName(first);
			craftingStage +=1;
			descLine1.setText(inv.getItemInfo(firstID, "Name"));
		} else if (craftingStage == 2){
			secondIndex= index;
			secondID = itemID;
			thirdID = EMPTY;
			fourthID = EMPTY;
			secondArray = getArrayName(first);
			craftingStage +=1;
			descLine2.setText(inv.getItemInfo(secondID, "Name"));
			checkValidRecipe(firstID, secondID, thirdID, fourthID);
			checkValidEnchantment(firstID, secondID);
		} else if (craftingStage == 3){
			thirdID = itemID;
			fourthID = EMPTY;
			craftingStage +=1;
			descLine3.setText(inv.getItemInfo(thirdID, "Name"));
			checkValidRecipe(firstID, secondID, thirdID, fourthID);
			checkValidEnchantment("cancel", "cancel");
		} else{
			fourthID = itemID;
			craftingStage = 1;
			descLine4.setText(inv.getItemInfo(fourthID, "Name"));
			checkValidRecipe(firstID, secondID, thirdID, fourthID);
		}
	}

	/** Checks to see if the items the player has selected satisfy a recipe in the crafting
	 * recipe list
	 *
	 * @param firstItem - ID for the item that was selected first
	 * @param secondItem - ID for the item that was selected second
	 * @param thirdItem - ID for the item that was selected third
	 * @param fourthItem - ID for the item that was selected fourth
	 */
	public void checkValidRecipe(String firstItem, String secondItem, String thirdItem, String fourthItem){
		if (craft.getCraftingProduct(firstItem, secondItem, thirdItem, fourthItem) != "invalid recipe"){
			String productID = craft.getCraftingProduct(firstItem,secondItem,thirdItem,fourthItem);
			descTitle.setText("Confirm Recipe?");
			swapImageFirst = inv.getSprite(productID);
			displayImage.setImage(swapImageFirst);
			playerSprite.setImage(trashImage);
			confirmCrafting.setVisible(true);
		} else {
			descTitle.setText(CRAFTING_ACTIVE);
			displayImage.setImage(craftingImage);
			confirmCrafting.setVisible(false);
		}
	}

	/** Checks to see if the items the player has selected satisfy an enchantment formula
	 * 
	 * @param armour - ID for the armour piece selected
	 * @param gem - ID for the enchantment stone selected
	 */
	
	public void checkValidEnchantment(String armour, String gem){
		if (inv.getItemType(armour) == inv.getItemType(HELMET) &&
				inv.getItemType(gem) == inv.getItemType("firestone")){
			armourID = armour;
			gemID = gem;
			descTitle.setText("Enchantment!");
			displayImage.setImage(enchantingImage);
			playerSprite.setImage(trashImage);
			confirmEnchant.setVisible(true);
		} else {
			descTitle.setText(CRAFTING_ACTIVE);
			displayImage.setImage(craftingImage);
			confirmEnchant.setVisible(false);
		}
	}

	/**
	 * Consumes ingredients and adds crafting product to inventory, turns crafting off
	 */
	public boolean completeCrafting(){
		// Ensures that the items are in the inventory
		if (!inv.checkItem(firstID) || !inv.checkItem(secondID)) {
			return false;
		}
		if (!inv.checkItem(thirdID) || !inv.checkItem(fourthID)) {
			return false;
		}
		inv.addItem(1, craft.getCraftingProduct(firstID, secondID, thirdID, fourthID));
		craftingStage = 1;
		SoundCache.play("PowerUp");
		confirmCrafting.setVisible(false);
		toggleCrafting();
		updateSprites();
		return true;
	}
	
	/**
	 * Consumes stone and enchants armour, turns crafting off
	 */
	
	public void completeEnchant(){
		String currentGem = gemID;
		switch (currentGem){
			case "firestone":
				((Armour) ItemRegistry.getItem(armourID)).setEnchantment(new Fire());
				break;
			case "grassstone":
				((Armour) ItemRegistry.getItem(armourID)).setEnchantment(new Grass());
				break;
			case "waterstone":
				((Armour) ItemRegistry.getItem(armourID)).setEnchantment(new Water());
				break;
			case "moonstone":
				((Armour) ItemRegistry.getItem(armourID)).setEnchantment(new Moon());
				break;
			case "sunstone":
				((Armour) ItemRegistry.getItem(armourID)).setEnchantment(new Sun());
				break;
			default:
				((Armour) ItemRegistry.getItem(armourID)).setEnchantment(new EmptyEnchant());
		}
		inv.removeItem(1, gemID);
		craftingStage = 1;
		SoundCache.getInstance().play("PowerUp");
		confirmEnchant.setVisible(false);
		toggleCrafting();
		updateSprites();
	}

	/**
	 * Removes an item from the inventory
	 */
	public void trashFunction() {

		if (inv == null) {
			inv = World.getInstance().getFirstPlayer().getInventory();
		}
		if (weaponActive) {
			inv.clearInvSlot(firstArray, firstIndex);
			activeSpriteFirst.setImage(trashImage);
			playerSprite.setImage(playerImage);
			generateStats();
			weaponActive = false;
		}
	}


	/**
	 * Sets the description box to display that player's current stats
	 */
	public void generateStats() {
		moneyAmount = player.getCommerce().getGold();
		Integer playerLevel = player.getPlayerStatsClass().getPlayerLevel();
		Integer playerMana = player.getPlayerStatsClass().getMana();
		Integer playerHealth = player.getPlayerStatsClass().getMaxHealth();
		playerMoney.setText(moneyAmount.toString());
		descTitle.setText("Character Stats");
		descLine1.setText("Level: " + playerLevel);
		descLine2.setText("Mana: " + playerMana);
		descLine3.setText("Health: " + playerHealth);
		descLine4.setText("");
	}

	/**
	 * Used so that a new player is brought into focus on new game
	 */
	public void resetPlayer() {
		inv = null;
		player = null;
	}

	/**
	 * Used so that a new inventory screen is brought into focus on new game
	 */
	public void setInventory(Player worldPlayer) {
		player = worldPlayer;
		inv = worldPlayer.getInventory();
	}
}
