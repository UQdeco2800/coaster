package uq.deco2800.coaster.graphics.screens.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by ryanj on 18/09/2016. <p> FXMLControllerRegister keeps track of the fxml controllers. Meaning we don't have
 * to make a place for each of them in engine.
 */
public class FXMLControllerRegister {
	private static final Logger logger = LoggerFactory.getLogger(FXMLControllerRegister.class);
	private static final Map<Class, Object> REGISTER = new HashMap<>();

	/**
	 * registers a controller for the class
	 *
	 * @param c          class to get
	 * @param controller instance of the class
	 */
	public static void register(Class c, Object controller) {
		REGISTER.put(c, controller);
		logger.info(c + " registered.");
	}

	/**
	 * get the instance of the class
	 *
	 * @param c class to get from
	 * @return instance of that class
	 */
	public static Object get(Class c) {
		return REGISTER.get(c);
	}

	/**
	 * get all classes registereed
	 *
	 * @return Set of the classes that are registered
	 */
	public static Set<Class> getReferences() {
		return REGISTER.keySet();
	}

	/**
	 * wipe the registry
	 */
	public static void clear() {
		REGISTER.clear();
	}

}
