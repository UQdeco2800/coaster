package uq.deco2800.coaster.game.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class Crafting {
	// Frequently used ingredients to keep sonar happy
	private static final String EMPTY = "empty";
	private static final String POTION = "potion";

	//Maps a list of items(a crafting recipe) to a product
	private HashMap<ArrayList<String>, String> craftingProduct = new HashMap<>();
	
	/**
	 * Initialises all availble crafting recipes, to add a new crafting recipe, simply add a 
	 * list of items (recipe) and a crafting product to the hashtable 
	 * 
	 */
	
	public Crafting(){

		// Please add you recipes to the wiki:
		// https://github.com/UQdeco2800/deco2800-2016-coaster/wiki/Crafting
		
		//Potions
		craftingProduct.put(new ArrayList<>(Arrays.asList("glass", "crystal", EMPTY, EMPTY)), POTION);
		craftingProduct.put(new ArrayList<>(Arrays.asList(POTION, "tears", EMPTY, EMPTY)), POTION + "1");
		craftingProduct.put(new ArrayList<>(Arrays.asList(POTION, "blooddrop", EMPTY, EMPTY)), POTION + "2");
		
		//Armour
		craftingProduct.put(new ArrayList<>(Arrays.asList("slimesoul", "slime", EMPTY, EMPTY)), "swagshoes");
		craftingProduct.put(new ArrayList<>(Arrays.asList("horn", "wood", EMPTY, EMPTY)), "rhinopants");
		craftingProduct.put(new ArrayList<>(Arrays.asList("bone","bone","horn","horn")), "rhinochestplate");
		craftingProduct.put(new ArrayList<>(Arrays.asList("rhinochestplate", "batwing", "batwing", EMPTY)), "batchestplate");
		
	}
	
	/**
	 * Given the ID of up to four items, checks to see if the items satisfy a crafting recipe
	 * @param firstID
	 * @param secondID
	 * @param thirdID
	 * @param fourthID
	 */
	public String getCraftingProduct(String firstID, String secondID, String thirdID, String fourthID){
		ArrayList<String> recipeList = new ArrayList<>();
		recipeList.add(firstID);
		recipeList.add(secondID);
		recipeList.add(thirdID);
		recipeList.add(fourthID);
		if (craftingProduct.get(recipeList) != null){
			return craftingProduct.get(recipeList);
		} else {
			return "invalid recipe";
		}
	}
	
}


