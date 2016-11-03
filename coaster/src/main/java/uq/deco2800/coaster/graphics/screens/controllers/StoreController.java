package uq.deco2800.coaster.graphics.screens.controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.items.ItemRegistry;
import uq.deco2800.coaster.game.items.ItemStore;
import uq.deco2800.coaster.game.world.World;

/**
 * The controller for the Store View.
 *
 * Created by draganmarjanovic on 16/09/2016.
 */
public class StoreController {


	private List<ItemStore> itemStoreList;

	private ObservableList<ItemStore> observableListItemStore;

	@FXML
	private TableView<ItemStore> buyTable;
	@FXML
	private TableView<ItemStore> sellTable;

	/**
	 * Initialises the Store Controller.
	 */
	public StoreController() {
		// Initialise controller.
		FXMLControllerRegister.register(StoreController.class, this);
		itemStoreList = new ArrayList<>();
		observableListItemStore = FXCollections.observableList(itemStoreList);
	}

	/**
	 * Updates the Controller to contain a reference to the nearest store.
	 */
	public void updateNearestStore() {
		Player player = World.getInstance().getFirstPlayer();
		// Get the first/only player
		if (player != null) {
			observableListItemStore.setAll(player.getCommerce().getNearestStore().getItems());
		} else {
			itemStoreList = null;
		}
	}

	/**
	 * Initialises the Table View and populates it with Item data.
	 */
	public void initialize() {
		assert buyTable != null : "fx:id=\"buyTable\" was not injected: check your FXML file 'store.fxml'.";
		assert sellTable != null : "fx:id=\"sellTable\" was not injected: check your FXML file 'store.fxml'.";
		loadBuyableItems();
		loadSellItems();
		FXMLControllerRegister.register(PauseMenuController.class, this);
	}


	/**
	 * Loads Items from the store which are buyable.
	 */
	private void loadBuyableItems() {
		buyTable.setItems(observableListItemStore);
	}

	/**
	 * Loads the users inventory Items into the Sell tab for sales.
	 */
	private void loadSellItems() {
		List<ItemStore> inventory = getInventoryItems();
		for (ItemStore itemStore : inventory) {
			sellTable.getItems().add(itemStore);
		}
	}

	// Mock Methods

	/**
	 * This is a mock method until the proper method is implemented by the inventory team.
	 */
	private List<ItemStore> getInventoryItems() {
		List<ItemStore> list = new ArrayList<>();
		list.add(new ItemStore(ItemRegistry.getItem("Gun1"), 4));
		list.add(new ItemStore(ItemRegistry.getItem("Gun2"), 3));
		list.add(new ItemStore(ItemRegistry.getItem("Gun3"), 7));
		return list;
	}
}

