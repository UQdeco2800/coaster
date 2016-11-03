package uq.deco2800.coaster.game;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uq.deco2800.coaster.TestHelper;
import uq.deco2800.coaster.core.input.GameAction;
import uq.deco2800.coaster.game.entities.Player;
import uq.deco2800.coaster.game.tutorial.BankerScenario;
import uq.deco2800.coaster.game.tutorial.BasicActionScenario;
import uq.deco2800.coaster.game.tutorial.CoinDropScenario;
import uq.deco2800.coaster.game.tutorial.CompanionScenario;
import uq.deco2800.coaster.game.tutorial.*;
import uq.deco2800.coaster.game.tutorial.Scenario;
import uq.deco2800.coaster.game.world.World;
import uq.deco2800.coaster.game.world.WorldTiles;

public class PlayerInstructionTest {
	private static World world = World.getInstance();

	private static Logger logger = LoggerFactory.getLogger(PlayerInstructionTest.class);
	private static WorldTiles destructionTiles;

	@Before
	public void init() {
		TestHelper.load();
		if (destructionTiles == null) {
			logger.info("tiles are null");
			destructionTiles = TestHelper.getWaterTiles();
		}
	}

	public void initialise() {
		TestHelper.load();
		world.resetWorld();
	}

	@Test
	public void testEarlyCompletion() {
		initialise();
		Player player = new Player();
		PlayerInstruction tutorial = new PlayerInstruction(player);
		assertEquals(false, tutorial.checkCompletion());
		tutorial.resendInstruction();
		tutorial.nextCommand();
		assertEquals(false, tutorial.checkCompletion());
		assertEquals(false, tutorial.tutorialPassed());
	}
	
	@Test
	@Ignore //runs locally, but not with gradle 
	public void testScenarioBasic() {
		initialise();
		Player player = new Player();
		BasicActionScenario scenario = new BasicActionScenario(GameAction.JUMP);
		assertEquals(GameAction.ENABLE_CHECKPOINTS, scenario.getAction());
		scenario.reward();
		scenario.setUpScenario();
		assertEquals(false, scenario.checkScenarioCompleted());	
	}
	
	@Test
	public void testBankScenario() {
		initialise();
		Player player = new Player();
		Scenario scenario = new BankerScenario(player);
		scenario.setUpScenario();
		assertEquals(false, scenario.checkScenarioCompleted());	
	}
	
	@Test
	public void testCoinScenario() {
		initialise();
		Player player = new Player();
		Scenario scenario = new CoinDropScenario(player);
		scenario.setUpScenario();
		assertEquals(false, scenario.checkScenarioCompleted());	
	}

	@Test
	public void testCompanionScenario() {
		initialise();
		Player player = new Player();
		Scenario scenario = new CompanionScenario(player);
		scenario.setUpScenario();
		assertEquals(false, scenario.checkScenarioCompleted());	
	}
	
	@Test
	public void testCompanionUpgradeScenario() {
		initialise();
		Player player = new Player();
		Scenario scenario = new CompanionUpgrade(player);
		scenario.setUpScenario();
		assertEquals(false, scenario.checkScenarioCompleted());	
	}
	
	@Test
	public void testEnemyScenario() {
		initialise();
		Player player = new Player();
		Scenario scenario = new KillEnemyScenario(player);
		scenario.setUpScenario();
		assertEquals(false, scenario.checkScenarioCompleted());	
	}
	
	@Test
	public void testEnemiesScenario() {
		initialise();
		Player player = new Player();
		Scenario scenario = new KillEnemySwarm(player);
		scenario.setUpScenario();
		assertEquals(false, scenario.checkScenarioCompleted());	
	}
	
	@Test
	public void testMountScenario() {
		initialise();
		Player player = new Player();
		Scenario scenario = new MountScenario(player);
		scenario.setUpScenario();
		assertEquals(false, scenario.checkScenarioCompleted());	
	}
	
	@Test
	public void testShieldScenario() {
		initialise();
		Player player = new Player();
		Scenario scenario = new ShieldScenario(player);
		scenario.setUpScenario();
		assertEquals(false, scenario.checkScenarioCompleted());	
	}
	
	@Test
	public void testSprintScenario() {
		initialise();
		Player player = new Player();
		Scenario scenario = new SprintScenario(player);
		scenario.setUpScenario();
		assertEquals(false, scenario.checkScenarioCompleted());	
	}
	
	@Test
	public void testConclusionScenario() {
		initialise();
		Player player = new Player();
		Scenario scenario = new TutorialConclusion(player);
		scenario.setUpScenario();
		assertEquals(false, scenario.checkScenarioCompleted());	
	}
}
