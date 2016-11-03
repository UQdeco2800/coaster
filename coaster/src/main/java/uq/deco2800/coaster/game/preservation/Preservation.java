package uq.deco2800.coaster.game.preservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * EXAMPLE: https://github.com/UQdeco2800/deco2800-2016-coaster/commit/e65312e84ac3a2637ad9266752239510bd8f07d3 <p>
 * Creating new parts to save; EXTENDING; Add the property you want as a public var (extended off the exportable object)
 * Make sure it gets applied when creating that Exportable object Add the property application during the load (load
 * method in Engine.java) Add test to PreservationTest (only 2 lines, don't worry) CREATING; Create the Exportable
 * object you want Make sure it gets referenced from the ExportableWorld Follow EXTENDING steps to add all properties
 * <p> REASONING; public properties are scary, when saving and loading, all the input verification is done by the
 * objects being saved and loaded. So verification is not an issue. None of these properties are ever getting 'used' a
 * new object is created every time a load occurs. So the public properties will never have conflicts. As there is also
 * no multithreading going on during the saving and loading (or the sharing of objects for that matter) there is no
 * synchronization issues. So it was decided to stick with public properties, as making getters and setters would make
 * it FAR harder for people to add their own properties to save, and for the JSON parsing, and debugging/testing. IMO
 * there is no downside to the public variables except for 'ew public', and large benefits (especially in readability).
 * <p> If there are any disagreements with this please create and issue on github.com or join our public slack chat <p>
 * RyanCarrier #ducko2800-flock4-public
 */

public class Preservation {
	private static Logger logger = LoggerFactory.getLogger(Preservation.class);


	/**
	 * Save current state of the game, helper (wrapper) for saving an ExportableWorld.
	 *
	 * @param location location to save to
	 */
	public static void save(String location) {
		save(new ExportableWorld(), location);
	}


	/**
	 * Saves the current state of the world passed through
	 *
	 * @param world    world to save
	 * @param location location to save the world state to
	 */
	public static void save(ExportableWorld world, String location) {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String jsonText;
		try {
			jsonText = ow.writeValueAsString(world);
			File f = new File(location);
			File parent = f.getParentFile();
			if (!parent.exists() && !parent.mkdirs()) {
				throw new IllegalStateException("Couldn't create dir: " + parent);
			}
			if (!f.exists()) {
				logger.debug("Save file does not exist at; " + location);
				if (!f.createNewFile()) {
					throw new IOException();
				}
			}
			logger.debug("Save file created at; " + location);
			FileWriter fw = new FileWriter(f);
			fw.write(jsonText);
			fw.close();
			logger.info("Game saved to; " + location);
		} catch (Exception e) {
			logger.error("Error while saving to; " + location, e);
		}
	}

	/**
	 * @param location location to load the ExportableWorld from
	 * @return returns the ExportableWorld generated from the save file
	 */
	public static ExportableWorld load(String location) {
		ExportableWorld world;
		File f = new File(location);
		try {
			if (!f.exists()) {
				throw new FileNotFoundException();
			}
			world = (new ObjectMapper()).readValue(f, ExportableWorld.class);
			if (world.movingEntities.size() > 0) {
				world.entities.addAll(world.movingEntities);
				world.movingEntities.clear();
			}
			if (world.itemEntities.size() > 0) {
				world.entities.addAll(world.itemEntities);
				world.itemEntities.clear();
			}
		} catch (Exception e) {
			logger.error("Loading from save; " + location, e);
			world = new ExportableWorld();
		}
		//TODO: need return default if the file has an issue lmao
		logger.debug("Loading world");
		return world;
	}

}
