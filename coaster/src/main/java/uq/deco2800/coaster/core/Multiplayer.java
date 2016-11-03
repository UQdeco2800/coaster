package uq.deco2800.coaster.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.coaster.game.entities.PlayerMulti;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.graphics.notifications.Toaster;
import uq.deco2800.singularity.clients.coaster.CoasterClient;
import uq.deco2800.singularity.clients.coaster.CoasterRealTimeClient;
import uq.deco2800.singularity.common.ServerConstants;
import uq.deco2800.singularity.common.SessionType;
import uq.deco2800.singularity.common.representations.User;
import uq.deco2800.singularity.common.representations.coaster.GameState;
import uq.deco2800.singularity.common.representations.coaster.state.*;
import uq.deco2800.singularity.common.representations.realtime.RealTimeSessionConfiguration;

import javax.ws.rs.WebApplicationException;
import java.util.*;

/**
 * Created by rcarrier on 20/10/2016.
 */
public class Multiplayer {
	private static final Logger logger = LoggerFactory.getLogger(Multiplayer.class);

	private static int tickrate;
	private static long lasttick = 0;
	private static long initial = System.currentTimeMillis();

	private static int betweenTick = 0;

	private static CoasterRealTimeClient realTimeClient;
	private static CoasterClient client;
	private static GameState currentState;

	private static int refreshCount = 0;

	private static boolean host;

	private static List<List<Update>> incomingUpdates = new ArrayList<>();

	private static List<List<Update>> outgoingUpdates = new ArrayList<>();

	private static LinkedList<NewPlayer> incomingPlayers = new LinkedList<>();

	private static List<PlayerMulti> players = new ArrayList<>();

	private static Map<String, Integer> playerMap = new HashMap<>();

	public static boolean init() {
		return init(-1);
	}

	public static int getDefaultTick() {
		return 100;
	}

	public static boolean init(int tr) {
		if (tr > 0) {
			host = true;
		}

		try {
			client = new CoasterClient(ServerConstants.PRODUCTION_SERVER, ServerConstants.REST_PORT);
			//client = new CoasterClient();
			login("rcArrier", "tstaass");

		} catch (Exception e) {
			// Try to refresh 6 times
			if (refreshCount++ < 6) {
				logger.warn("exception while setting up client", e);
				init(tr);
				return false;
			} else {
				logger.error("Failure to register CoasterClient", e);
			}
		}
		RealTimeSessionConfiguration rts = null;
		List<RealTimeSessionConfiguration> gameList;
		CoasterRealTimeClient rtc = null;
		try {
			if (tr < 1) {
				gameList = client.getActiveGames(SessionType.COASTER);
				if (gameList.size() > 0) {
					logger.info("Attempting to join a room");
					rts = gameList.get(0);
				} else {
					logger.info("No rooms available");
					Toaster.toast("No rooms available");
					return false;
				}

			} else {
				logger.info("Attempting to create a room");
				Toaster.toast("Attempting to create a room");
				rts = client.requestGameSession(SessionType.COASTER);
			}
			rtc = new CoasterRealTimeClient(rts, client);
		} catch (Exception e) {
			logger.error("error init Multiplayer, real time client exception", e);
			if (refreshCount++ < 6) {
				logger.warn("exception while setting up client", e);
				init(tr);
				return false;
			}
		}

		//	Update u = new NilUpdate();
		//while (!(u instanceof GameStateUpdate) {
		//	u = realTimeClient.tickRecieve();
		//}
		//GameState state = ((GameStateUpdate) u).getState();
		//if state

		refreshCount = 0;
		return init(tr, rtc);

	}


	public static void pushNewPlayer(PlayerMulti pm) {
		if (pm.getName().equals("")) {
			throw new IllegalArgumentException("NewPlayer cannot have empty name");
		}
		NewPlayer np = null;
		if (host) {
			np = new NewPlayer(pm.getName(), tickrate);
		} else {
			np = new NewPlayer(pm.getName());
		}
		try {
			realTimeClient.sendUpdate(np);
		} catch (Exception e) {
			logger.error("Exception while sending new player", e);
			return;
		}
		players.add(pm);
		playerMap.put(np.getName(), 0);
		outgoingUpdates.add(0, new ArrayList<>());
		incomingUpdates.add(0, new ArrayList<>());
	}

	public static boolean init(int tr, CoasterRealTimeClient rtc) {
		tickrate = tr;
		realTimeClient = rtc;
		betweenTick = 1_000_000_000 / tickrate;
		return true;
	}


	public static NewPlayer getNewPlayer() {
		NewPlayer np = new NewPlayer("");
		if (incomingPlayers.size() == 0) {
			return np;
		}
		try {
			np = incomingPlayers.pop();
		} catch (Exception e) {
			logger.info("Exception popping new players", e.getCause());
			return np;
		}
		if (np != null) {
			int i = playerMap.size();
			playerMap.put(np.getName(), i);
			outgoingUpdates.add(i, new ArrayList<>());
			incomingUpdates.add(i, new ArrayList<>());
		}
		return np;
	}

	public static void setPlayer(String name, int i) {
		playerMap.put(name, i);
		outgoingUpdates.add(i, new ArrayList<>());
		incomingUpdates.add(i, new ArrayList<>());
	}

	public static boolean tick(long now) {
		tickReceive();
		if (now - lasttick >= betweenTick && currentState == GameState.START_GAME) {
			//logger.debug("TICK " + now / 1_000_000);
			//logger.info("Getting updates for " + player.getName());
			addIfOk(0, players.get(0).getPlayerUpdate());
			//addIfOk(0, players.get(0).getFireUpdate());
			//addIfOk(0, players.get(0).getDamageUpdate());
			tryPush();
			lasttick = now;
			return true;
		}
		return false;
	}

	private static void addIfOk(int i, Update u) {
		if (!(u instanceof NilUpdate)) {
			//logger.info("adding update to " + i + " format " + u.getClass().toGenericString());
			PlayerUpdate pu = (PlayerUpdate) u;
			logger.info("" + pu.getName() + " " + pu.getPosX() + pu.getPosY());
			addUpdate(i, u);
		}
	}

	public static void tryPush() {
		outgoingUpdates.forEach(l -> {
			l.forEach(realTimeClient::sendUpdate);
			l.clear();
		});
	}

	public static void tickReceive() {
		//logger.debug("tick");
		Update u = realTimeClient.tickRecieve();
		if (u instanceof NilUpdate) {
			return;
		}
		//logger.info("Recieved update: " + u.getClass().toGenericString());
		if (u instanceof GameStateUpdate) {
			currentState = ((GameStateUpdate) u).getState();
		} else if (u instanceof NewPlayer) {
			if (!host) {
				//setTickrate(((NewPlayer) u).getTick());
				setTickrate(getDefaultTick());
			}
			incomingPlayers.push((NewPlayer) u);
		} else {
			if (u instanceof PlayerUpdate) {
				incomingUpdates.get(playerMap.get(((PlayerUpdate) u).getName())).add(0, u);
			} else if (u instanceof FireUpdate) {
				incomingUpdates.get(playerMap.get(((FireUpdate) u).getName())).add(1, u);
			} else if (u instanceof DamageUpdate) {
				incomingUpdates.get(playerMap.get(((DamageUpdate) u).getName())).add(2, u);
			} else if (u instanceof DisconnectedPlayer) {
				incomingUpdates.get(playerMap.get(((DisconnectedPlayer) u).getName())).add(0, u);
			} else {
				logger.error("fsdjakngsdjkanfjasdkf");
			}
		}
		tickReceive();

	}


	public static int getTickrate() {
		return tickrate;
	}

	public static void setTickrate(int tr) {
		tickrate = tr;
		betweenTick = 1_000_000_000 / tickrate;
	}

	public static long getLasttick() {
		return lasttick;
	}

	public static List<List<Update>> getOutgoingUpdates() {
		return outgoingUpdates;
	}


	public static void addlUpdate(String name, Update p) {
		addUpdate(playerMap.get(name), p);
	}


	public static List<Update> getUpdates(String name) {
		//logger.info("getting updates for; " + name);
		List<Update> us = incomingUpdates.get(playerMap.get(name));
		//logger.info("got updates for; " + name + " got " + us.size());
		//incomingUpdates.get(playerMap.get(name)).clear();
		incomingUpdates.add(playerMap.get(name), new ArrayList<>());
		//logger.info("got updates for; " + name + " got " + us.size());
		return us;
	}


	public static void addUpdate(int i, Update p) {
		//logger.info("Setting i:" + i + " Update:" + p.getClass().toGenericString());
		outgoingUpdates.get(i).add(p);
	}

	public static void login(String username, String password) {
		try {
			// Attempt to login
			client.setupCredentials(username, password);
			User user = client.getUserInformationByUserName(username);
			World world = World.getInstance();
			world.setUser(user);
			world.setClient(client);
			//Window.goToMainMenu();
		} catch (WebApplicationException e) {
			logger.debug("Setting user and pass failure", e);
			logger.info(Integer.toString(e.getResponse().getStatus()));

			// No user found, a register new user
			switch (e.getResponse().getStatus()) {
				case 404:
					logger.debug("fail");
					break;
				case 403:
					User user = new User(username, "Unknown", null, "Unknown", password);
					try {
						user = client.createUser(user);
						login(username, password);
						return;
					} catch (WebApplicationException i) {
						logger.debug("Error creating user", i);

						// Handle Web application exception create conflict (i.e. user name already exists)
						if ((i).getResponse().getStatus() == 409) {
							logger.info("Username already exists.");
							login(username + "1", password);
						} else {
							// TODO Handle generic registration error. What this is shouldn't matter as this will be user facing.
						}
					} catch (JsonProcessingException i) {
						logger.debug("Error processing json", i);

					} catch (Exception i) {
						logger.error("Other error logging in", i);
					}
					break;
				default:
					// TODO: Handle all other login errors. Again, these can be bundled as they will have no meaning to user.
			}
		} catch (Exception e) {
			logger.error("Other error logging in", e);
		}
	}
}
