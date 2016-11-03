package uq.deco2800.coaster.graphics.sprites;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import uq.deco2800.coaster.game.tiles.TileInfo;

//Our sprite cache! Stores the actual images for the sprites and registers them against an ID.
//If you need to add a new sprite to the game, you can do it here.
public class SpriteCache {

	private SpriteCache() {
	}

	private static Map<SpriteList, SpriteInfo> spritesCache = new HashMap<>();
	private static Map<TileInfo, Map<String, SpriteInfo>> tileSpritesCache = new HashMap<>();
	private static boolean hasLoaded = false;

	/**
	 * To be called once, on game startup. Loads all assets from disk into our
	 * FrameCollection cache.
	 *
	 * @throws IOException
	 *             if a sprite cannot be found, or the input dimensions are
	 *             wrong
	 */
	public static void loadAllSprites() throws IOException {
		// Mounts
		loadStandardSprite(SpriteList.INV_RHINO_STANDING, "sprites/inv_rhino_standing.png", 120, 155, 1, 1);
		loadStandardSprite(SpriteList.INV_RHINO_WALKING, "sprites/mount_walking.png", 120, 155, 5, 50);
		loadStandardSprite(SpriteList.INV_RHINO_PROMPT, "sprites/inv_rhino_prompt_.png", 120, 155, 1, 1);
		// Mount BAT:
		loadStandardSprite(SpriteList.BAT_MOUNT_FLAPPING, "sprites/mount-bat-flapping.png", 60, 46, 2, 50);
		loadStandardSprite(SpriteList.BAT_MOUNT_STATIC, "sprites/mount-bat-static.png", 60, 46, 1, 1);

		loadStandardSprite(SpriteList.FROG_STANDING, "sprites/frog.png", 32, 32, 1, 1);
		loadStandardSprite(SpriteList.FROG_WALKING, "sprites/frog_walking.png", 32, 32, 2, 100);


		loadStandardSprite(SpriteList.FLAG_DOWN_LEFT, "sprites/flagDownLeft.png", 32, 96, 1, 1);
		loadStandardSprite(SpriteList.FLAG_UP_LEFT, "sprites/flagUpRight.png", 32, 96, 1, 1);
		loadStandardSprite(SpriteList.FLAG_UP_RIGHT, "sprites/flagUpLeft.png", 32, 96, 1, 1);
		
		// Enemy Sprites
		loadStandardSprite(SpriteList.EMPTY, "sprites/empty_slot.png", 500, 500, 1, 1);
		
		loadStandardSprite(SpriteList.EMPTY_POTION, "sprites/potion.png", 413, 413, 1, 1);
		loadStandardSprite(SpriteList.POTION, "sprites/mana-potion.png", 413, 413, 1, 1);
		loadStandardSprite(SpriteList.POTION2, "sprites/heal-potion.png", 413, 413, 1, 1);

		loadStandardSprite(SpriteList.BULLET, "sprites/bullet.png", 40, 31, 1, 1);
		loadStandardSprite(SpriteList.LAZOR, "sprites/lazor-particle.png", 96, 32, 1, 1);
		loadStandardSprite(SpriteList.BOSS, "sprites/boss.png", 64, 64, 1, 1);
		loadStandardSprite(SpriteList.CHEST, "sprites/chest.png", 26, 21, 1, 1);

		// Enchant Stones
		loadStandardSprite(SpriteList.FIRE_STONE, "sprites/firestone.png", 425, 274, 1, 1);
		loadStandardSprite(SpriteList.SUN_STONE, "sprites/thunderstone.png", 425, 274, 1, 1);
		loadStandardSprite(SpriteList.GRASS_STONE, "sprites/grassstone.png", 425, 274, 1, 1);
		loadStandardSprite(SpriteList.WATER_STONE, "sprites/waterstone.png", 425, 274, 1, 1);
		loadStandardSprite(SpriteList.MOON_STONE, "sprites/moonstone.png", 425, 274, 1, 1);
		loadStandardSprite(SpriteList.MOUNTAIN_STONE, "sprites/mountainstone.png", 425, 274, 1, 1);

		// DECORATION_BIRD

		// Crafting Sprites

		loadStandardSprite(SpriteList.CRYSTAL, "sprites/crystal.png", 625, 689, 1, 1);
		loadStandardSprite(SpriteList.BONE, "sprites/bone.png", 650, 179, 1, 1);
		loadStandardSprite(SpriteList.WOOD, "sprites/wood.png", 736, 571, 1, 1);
		loadStandardSprite(SpriteList.RHINO_HORN, "sprites/rhino-horn.png", 413, 480, 1, 1);
		loadStandardSprite(SpriteList.BAT_TEETH, "sprites/bat-teeth.png", 118, 224, 1, 1);
		loadStandardSprite(SpriteList.BAT_WING, "sprites/bat-wing.png", 789, 259, 1, 1);
		loadStandardSprite(SpriteList.BLOOD_DROP, "sprites/blood-drop.png", 295, 295, 1, 1);
		loadStandardSprite(SpriteList.TEARS, "sprites/rip-the-build.png", 295, 295, 1, 1);
		loadStandardSprite(SpriteList.SLIME_DROP, "sprites/slime-drop.png", 359, 359, 1, 1);
		loadStandardSprite(SpriteList.SLIME_SOUL, "sprites/slime-soul.png", 342, 359, 1, 1);
		loadStandardSprite(SpriteList.GLASS, "sprites/glass.png", 709, 709, 1, 1);

		// DECORATION_BIRD
		loadStandardSprite(SpriteList.DECORATION_BIRD, "sprites/bird.png", 40, 35, 9, 100);

		// Enemy Sprites

		// GHOSTSHIP
		loadStandardSprite(SpriteList.GHOSTSHIP, "sprites/ghost_ship.png", 417, 330, 3, 150);
		
		// SNEAKY_RAT
		loadStandardSprite(SpriteList.SNEAKY_RAT_WALKING, "sprites/sneakyRat.png", 136, 146, 2, 50);
		loadStandardSprite(SpriteList.SNEAKY_RAT_STANDING, "sprites/sneakyRat-standing.png", 136, 146, 1, 50);

		// GHOSTSHIP
		//loadStandardSprite(SpriteList.GHOSTSHIP, "sprites/ghost_ship.png", 139, 110, 3, 50);

		// TENTACLES
		loadStandardSprite(SpriteList.TENTACLES, "sprites/tentacles.png", 126, 95, 5, 50);

		// GOLEM:
		loadStandardSprite(SpriteList.GOLEM_ATTACKING, "sprites/golem_attacking.png", 64, 64, 3, 100);
		loadStandardSprite(SpriteList.GOLEM_WALKING, "sprites/golem_walk.png", 64, 64, 4, 100);
		loadStandardSprite(SpriteList.GOLEM_STANDING, "sprites/golem_standing.png", 64, 64, 1, 100);

		//UNDERGROUNG_WORM:

		// UNDERGROUNG_WORM:
		loadStandardSprite(SpriteList.WORM_WALKING, "sprites/underground_worm.png", 86, 41, 4, 100);

		// RHINOS:
		loadStandardSprite(SpriteList.RHINO_WALKING, "sprites/rhino-walking.png", 120, 155, 5, 50);
		loadStandardSprite(SpriteList.RHINO_STANDING, "sprites/rhino-standing.png", 110, 155, 1, 1);
		loadStandardSprite(SpriteList.GREEN_RHINO_WALKING, "sprites/greenrhino-nospikes-walking.png", 120, 155, 5, 50);
		loadStandardSprite(SpriteList.GREEN_RHINO_DAZED, "sprites/greenrhino-dazed.png", 120, 155, 6, 50);
		loadStandardSprite(SpriteList.RHINO_ATTACKING, "sprites/rhino-attacking.png", 120, 155, 4, 50);

		// Medieval
		loadStandardSprite(SpriteList.MEDIEVAL_WALKING, "sprites/medieval-walking.png", 100, 138, 2, 50);
		// CARL:
		loadStandardSprite(SpriteList.CARL, "sprites/carl.png", 32, 32, 1, 1);
		// BAT:
		loadStandardSprite(SpriteList.BAT_FLAPPING, "sprites/bat-flapping.png", 60, 46, 2, 50);
		loadStandardSprite(SpriteList.BAT_ATTACKING, "sprites/bat-attacking.png", 45, 45, 2, 50);
		loadStandardSprite(SpriteList.BAT_BULLET, "sprites/bat-bullet.png", 212, 233, 1, 1);

		// Eyeball:
		loadStandardSprite(SpriteList.EYE_IDLE, "sprites/eye-idle.png", 45, 45, 1, 50);
		loadStandardSprite(SpriteList.EYE_NEAR, "sprites/eye-near.png", 45, 45, 1, 50);
		loadStandardSprite(SpriteList.EYE_SHOOTING, "sprites/eye-shooting.png", 45, 45, 2, 100);

		// Ice Spirit
		loadStandardSprite(SpriteList.ICE_SPIRIT_IDLE, "sprites/ice-spirit-idle.png", 1120, 1120, 3, 100);
		loadStandardSprite(SpriteList.ICE_SPIRIT_ATTACKING, "sprites/ice-spirit-attacking.png", 1120, 1120, 2, 500);
		loadStandardSprite(SpriteList.ICE_SPIRIT_WHIRL, "sprites/ice-spirit-whirl.png",  700, 700, 4, 100);

		// MELEE:
		loadStandardSprite(SpriteList.ZOMBIE, "sprites/zombie_walking.png", 120, 87, 4, 100);
		loadStandardSprite(SpriteList.SLIME, "sprites/slime_walking.png", 107, 109, 3, 100);
		loadStandardSprite(SpriteList.EXPLOSION_BUNNY_SITTING, "sprites/explosion-bunny_sitting.png", 1736, 1736, 1, 100);
		loadStandardSprite(SpriteList.EXPLOSION_BUNNY_JUMPING, "sprites/explosion-bunny_jumping.png", 1736, 1736, 1, 100);
		loadStandardSprite(SpriteList.EXPLOSION_BUNNY_EXPLOSION, "sprites/explosion-bunny_explosion.png", 1736, 1736, 1, 500);
		// LANDMINE
		loadStandardSprite(SpriteList.LANDMINE, "sprites/landmine.png", 71, 31, 3, 100);
		// TRAP
		loadStandardSprite(SpriteList.TRAP, "sprites/trap.png", 99, 100, 5, 50);
		// SUPERBEAR
		loadStandardSprite(SpriteList.SUPERBEAR, "sprites/superbear.png", 440, 440, 5, 100);
		// Space Slime
		loadStandardSprite(SpriteList.SPACE_SLIME, "sprites/space-slime.png", 448, 448, 3, 100);
		// Skeleton
		loadStandardSprite(SpriteList.SPEAR, "sprites/spear.png", 35, 11, 1, 1);
		loadStandardSprite(SpriteList.SKELETON, "sprites/skull_walk.png", 59, 125, 2, 100);
		loadStandardSprite(SpriteList.SKELETON_FLY, "sprites/skeleton_FLY.png", 103, 124, 3, 100);
		loadStandardSprite(SpriteList.SKELETON_ATTACK, "sprites/skeleton_ATK.png", 133, 127, 3, 100);
		// Spider
		loadStandardSprite(SpriteList.SPIDER_WALKING, "sprites/spider-walking.png", 50, 32, 3, 50);
		loadStandardSprite(SpriteList.SPIDER_WEB, "sprites/spider_web.png", 59, 44, 1, 1);
		// Sprite for cross hair
		loadStandardSprite(SpriteList.CROSSHAIR, "sprites/crosshair.png", 28, 28, 1, 1);
		// PowerUp Sprites
		loadStandardSprite(SpriteList.HEALTH, "sprites/health.png", 149, 92, 1, 1);
		loadStandardSprite(SpriteList.SHOE, "sprites/shoe.png", 168, 125, 1, 1);
		loadStandardSprite(SpriteList.MANA_UP, "sprites/manaup.png", 98, 100, 1, 1);
		loadStandardSprite(SpriteList.WEAPON_UP, "sprites/weaponup.png", 160, 70, 1, 1);
		loadStandardSprite(SpriteList.SHIELD, "sprites/shield.png", 129, 121, 1, 1);
		loadStandardSprite(SpriteList.MAP, "sprites/map.png", 160, 160, 1, 1);

		// Player Sprites
		loadStandardSprite(SpriteList.KNIGHT_STANDING, "sprites/knight-standing-headless.png", 26, 43, 2, 500);
		loadStandardSprite(SpriteList.KNIGHT_WALKING, "sprites/knight-walking-headless.png", 27, 43, 2, 75);
		loadStandardSprite(SpriteList.KNIGHT_JUMPING, "sprites/knight-jumping-headless.png", 30, 43, 1, 1);
		loadStandardSprite(SpriteList.KNIGHT_WALLSLIDE, "sprites/knight-wallslide.png", 29, 43, 1, 1);
		loadStandardSprite(SpriteList.KNIGHT_DASH, "sprites/knight-dash.png", 32, 42, 1, 1);
		loadStandardSprite(SpriteList.KNIGHT_SLIDE, "sprites/knight-slide.png", 37, 37, 1, 1);
		loadStandardSprite(SpriteList.KNIGHT_ARM, "sprites/knight-walking-arm.png", 11, 10, 1, 1);
		loadStandardSprite(SpriteList.KNIGHT_HEAD, "sprites/knight-head.png", 23, 15, 1, 1);
		loadStandardSprite(SpriteList.KNIGHT_CROUCH, "sprites/knight-crouch.png", 26, 37, 1, 1);
		loadStandardSprite(SpriteList.KNIGHT_SPRINT, "sprites/knight-sprint-headless.png", 29, 43, 4, 75);
		loadStandardSprite(SpriteList.KNIGHT_KNOCK_BACK, "sprites/knight-knockback.png", 32, 42, 1, 1);

		loadStandardSprite(SpriteList.PARTICLE3, "sprites/particle3.png", 250, 250, 1, 1);
		loadStandardSprite(SpriteList.PARTICLE_HEADSHOT, "sprites/particleHeadshot.png", 250, 250, 1, 1);
		loadStandardSprite(SpriteList.PARTICLE2, "sprites/particle2.png", 50, 50, 1, 1);
		loadStandardSprite(SpriteList.PARTICLE1, "sprites/particle1.png", 50, 50, 1, 1);
		loadStandardSprite(SpriteList.PORTAL_PARTICLE1, "sprites/portal_particle1.png", 64, 64, 1, 1);
		loadStandardSprite(SpriteList.PORTAL_PARTICLE2, "sprites/portal_particle2.png", 64, 64, 1, 1);

		// Skill Sprites
		loadStandardSprite(SpriteList.DEATH_BLOSSOM, "sprites/skills/deathBlossom128.png", 128, 128, 1, 1);
		loadStandardSprite(SpriteList.HIGH_NOON, "sprites/skills/highNoon128.png", 128, 128, 1, 1);
		loadStandardSprite(SpriteList.SPLIT_SHOT, "sprites/skills/splitShot128.png", 128, 128, 1, 1);
		loadStandardSprite(SpriteList.TIME_LOCK, "sprites/skills/timeLock128.png", 128, 128, 1, 1);
		loadStandardSprite(SpriteList.NUKE, "/sprites/skills/nuke128.png", 128, 128, 1, 1);
		loadStandardSprite(SpriteList.OMNISLASH, "/sprites/skills/omnislash128.png", 128, 128, 1, 1);
		loadStandardSprite(SpriteList.TORCH, "sprites/torch.png", 32, 32, 4, 400);

		// Hud
		loadStandardSprite(SpriteList.HUD, "sprites/HUD.png", 831, 215, 1, 1);

		// Weapon sprites
		loadStandardSprite(SpriteList.GRENADE, "sprites/grenade.png", 96, 96, 1, 1);
		loadStandardSprite(SpriteList.HANDGUN, "sprites/handgun.png", 500, 500, 1, 1);
		loadStandardSprite(SpriteList.SWORD, "sprites/sword.png", 500, 500, 1, 1);
		loadStandardSprite(SpriteList.CURRENT_WEAPON, "sprites/weapon-current.png", 400, 115, 1, 1);
		loadStandardSprite(SpriteList.EXPLOSION, "sprites/explosion.png", 200, 200, 1, 1);
		loadStandardSprite(SpriteList.ROCKET, "sprites/rocket.png", 15, 13, 1, 1);
		loadStandardSprite(SpriteList.LAZOR_BULLET, "sprites/lazor-bullet.png", 148, 18, 1, 1);
		loadStandardSprite(SpriteList.SMG_NORMAL, "sprites/SMG.png", 128, 128, 1, 1);
		loadStandardSprite(SpriteList.AK_RED, "sprites/ak47red.png", 186, 86, 1, 1);
		loadStandardSprite(SpriteList.BULLET_REDTAIL, "sprites/akbulletred.png", 26, 6, 1, 1);
		loadStandardSprite(SpriteList.BULLET_PELLET, "sprites/pellet.png", 8, 4, 1, 1);
		loadStandardSprite(SpriteList.PORTALGUN, "sprites/portalgun.png", 288, 200, 1, 1);
		loadStandardSprite(SpriteList.SHOTGUN, "sprites/bigshotgun.png", 288, 200, 1, 1);
		loadStandardSprite(SpriteList.ROCKETLAUNCHER, "sprites/rlauncher.png", 185, 128, 1, 1);
		loadStandardSprite(SpriteList.GRENADELAUNCHER, "sprites/grenade_launcher.png", 288, 200, 1, 1);
		loadStandardSprite(SpriteList.BULLET_SILVER, "sprites/bullet_silver.png", 40, 31, 1, 1);
		loadStandardSprite(SpriteList.BULLET_BLUE, "sprites/bullet_blue.png", 40, 31, 1, 1);
		loadStandardSprite(SpriteList.PORTAL_BULLET_ORANGE, "sprites/portal_bullet1.png", 32, 32, 1, 1);
		loadStandardSprite(SpriteList.PORTAL_ORANGE, "sprites/portal1.png", 32, 64, 1, 1);
		loadStandardSprite(SpriteList.PORTAL_BULLET_BLUE, "sprites/portal_bullet2.png", 32, 32, 1, 1);
		loadStandardSprite(SpriteList.PORTAL_BLUE, "sprites/portal2.png", 32, 64, 1, 1);
		loadStandardSprite(SpriteList.PORTAL_GUN, "sprites/portalgun.png", 288, 200, 1, 1);
		loadStandardSprite(SpriteList.FLAME, "sprites/flame.png", 64, 32, 1, 1);
		loadStandardSprite(SpriteList.AK_BLUE, "sprites/ak47blu.png", 186, 86, 1, 1);
		loadStandardSprite(SpriteList.AK, "sprites/ak47.png", 186, 86, 1, 1);
		loadStandardSprite(SpriteList.AK_RUSTY, "sprites/ak47rusty.png", 186, 86, 1, 1);
		loadStandardSprite(SpriteList.SMG_D, "sprites/SMGdiamond.png", 128, 128, 1, 1);
		loadStandardSprite(SpriteList.SPACEGUN, "sprites/spacegun.png", 250, 250, 1, 1);
		loadStandardSprite(SpriteList.SOILGUN, "sprites/soilgun.png", 288, 200, 1, 1);
		loadStandardSprite(SpriteList.SOILBULLET, "sprites/soilbullet.png", 40, 31, 1, 1);
		loadStandardSprite(SpriteList.SMG_RED,"sprites/SMGred.png",128, 128, 1, 1);
		loadStandardSprite(SpriteList.SMG_RUSTY,"sprites/SMGrusty.png",128, 128, 1, 1);

		//ammo
		loadStandardSprite(SpriteList.AMMO, "sprites/ammo.png",27,30,1,1 );
		loadStandardSprite(SpriteList.EX_AMMO, "sprites/eammo.png",27,30,1,1 );

		// Armour Sprites
		loadStandardSprite(SpriteList.HELMETBLK, "sprites/helmetblk.png", 64, 64, 1, 1);
		loadStandardSprite(SpriteList.HELMETBLU, "sprites/helmetblu.png", 64, 64, 1, 1);
		loadStandardSprite(SpriteList.HELMETRED, "sprites/helmetred.png", 64, 64, 1, 1);
		loadStandardSprite(SpriteList.HELMETRUST, "sprites/helmetrust.png", 64, 64, 1, 1);
		loadStandardSprite(SpriteList.CHESTBLK, "sprites/chestblk.png", 64, 64, 1, 1);
		loadStandardSprite(SpriteList.CHESTBLU, "sprites/chestblu.png", 64, 64, 1, 1);
		loadStandardSprite(SpriteList.CHESTRED, "sprites/chestred.png", 64, 64, 1, 1);
		loadStandardSprite(SpriteList.CHESTRUST, "sprites/chestrust.png", 64, 64, 1, 1);
		loadStandardSprite(SpriteList.CHESTRHINO, "sprites/chestrhino.png", 827, 760, 1, 1);
		loadStandardSprite(SpriteList.CHESTBAT, "sprites/chestbat.png", 886, 886, 1, 1);
		loadStandardSprite(SpriteList.PANTSRHINO, "sprites/pants-rhino.png", 236, 321, 1, 1);
		loadStandardSprite(SpriteList.PANTSBLK, "sprites/pantsblk.png", 64, 64, 1, 1);
		loadStandardSprite(SpriteList.PANTSBLU, "sprites/pantsblue.png", 64, 64, 1, 1);
		loadStandardSprite(SpriteList.PANTSRED, "sprites/pantsred.png", 64, 64, 1, 1);
		loadStandardSprite(SpriteList.PANTSRUST, "sprites/pantsrust.png", 64, 64, 1, 1);
		loadStandardSprite(SpriteList.BOOTSWAG, "sprites/swagshoes.png", 448, 237, 1, 1);
		loadStandardSprite(SpriteList.BOOTBLK, "sprites/bootblk.png", 64, 64, 1, 1);
		loadStandardSprite(SpriteList.BOOTBLU, "sprites/bootblu.png", 64, 64, 1, 1);
		loadStandardSprite(SpriteList.BOOTRED, "sprites/bootred.png", 64, 64, 1, 1);
		loadStandardSprite(SpriteList.BOOTRUST, "sprites/bootrust.png", 64, 64, 1, 1);

		// Coin sprite
		loadStandardSprite(SpriteList.COIN, "sprites/coin.png", 40, 40, 6, 250);
		loadStandardSprite(SpriteList.STORE_NPC, "sprites/storeMan.png", 40, 72, 1, 1);
		loadStandardSprite(SpriteList.BANK_NPC, "sprites/bankMan.png", 40, 72, 1, 1);
		loadStandardSprite(SpriteList.STORE_ACT_NPC, "sprites/storeManActive.png", 40, 72, 1, 1);
		loadStandardSprite(SpriteList.BANK_ACT_NPC, "sprites/bankManActive.png", 40, 72, 1, 1);
		loadStandardSprite(SpriteList.STORE_NPC_2, "sprites/storeNPC2.png", 40, 72, 1, 1);
		loadStandardSprite(SpriteList.STORE_ACT_NPC_2, "sprites/storeNPC2Active.png", 40, 72, 1, 1);
		
		loadStandardSprite(SpriteList.BANK, "sprites/decorations/bank.png", 160, 160, 1, 1);
		loadStandardSprite(SpriteList.BANK_SNOW, "sprites/decorations/bank-snow.png", 160, 160, 1, 1);
		loadStandardSprite(SpriteList.BANK_DESERT, "sprites/decorations/bank-desert.png", 160, 160, 1, 1);

		loadStandardSprite(SpriteList.BOULDER, "sprites/decorations/boulder.png", 32, 32, 1, 1);
		loadStandardSprite(SpriteList.TREE_PALM, "sprites/decorations/tree-palm.png", 32, 128, 1, 1);
		loadStandardSprite(SpriteList.TREE_DEAD, "sprites/decorations/tree-dead.png", 32, 128, 1, 1);
		loadStandardSprite(SpriteList.TREE_SNOW, "sprites/decorations/tree-snow.png", 32, 128, 1, 1);
		loadStandardSprite(SpriteList.TREE_SHORT_1, "sprites/decorations/tree-short-1.png", 32, 64, 1, 1);
		loadStandardSprite(SpriteList.TREE_TALL_1, "sprites/decorations/tree-tall-1.png", 32, 128, 1, 1);
		loadStandardSprite(SpriteList.FLOWER, "sprites/decorations/flower.png", 32, 32, 1, 1);
		loadStandardSprite(SpriteList.FLOWERPINK, "sprites/decorations/flowerpink.png", 32, 32, 1, 1);
		loadStandardSprite(SpriteList.FLOWERBLUE, "sprites/decorations/flowerblue.png", 32, 32, 1, 1);
		loadStandardSprite(SpriteList.GRASS, "sprites/decorations/grass.png", 32, 32, 1, 1);
		//Animal Mounts
		loadStandardSprite(SpriteList.TURTLE_WALKING, "sprites/turtlemove.png", 128, 96, 4, 250);
		loadStandardSprite(SpriteList.TURTLE_STANDING, "sprites/turtlestanding.png", 128, 96, 1, 1);
		loadStandardSprite(SpriteList.ELEPHANT_WALKING, "sprites/elephantmove.png", 128, 128, 3, 50);
		loadStandardSprite(SpriteList.ELEPHANT_STANDING, "sprites/elephantstand.png", 128, 128, 1, 1);
		loadStandardSprite(SpriteList.BIRD_FLYING, "sprites/birdfly.png", 128, 128, 3, 50);
		loadStandardSprite(SpriteList.BIRD_STATIC, "sprites/birdstop.png", 128, 96, 1, 1);
		loadStandardSprite(SpriteList.DOG_WALKING, "sprites/dogmove.png", 96, 96, 2, 50);
		loadStandardSprite(SpriteList.DOG_STANDING, "sprites/dogstand.png", 96, 96, 1, 1);
		
		// Companion Sprites

		loadStandardSprite(SpriteList.COMPANION_ATTACK_STAND, "sprites/companion-attack-stand.png", 152, 159, 1, 1);
		loadStandardSprite(SpriteList.COMPANION_ATTACK_MOVING, "sprites/companion-attack-moving.png", 126, 118, 3, 50);
		loadStandardSprite(SpriteList.COMPANION_SUPPORT_STAND, "sprites/companion-support-stand.png", 152, 159, 1, 1);
		loadStandardSprite(SpriteList.COMPANION_SUPPORT_MOVING, "sprites/companion-support-moving.png", 126, 112, 3,
				50);
		loadStandardSprite(SpriteList.COMPANION_DEFENSE_STAND, "sprites/companion-defense-stand.png", 152, 159, 1, 1);
		loadStandardSprite(SpriteList.COMPANION_DEFENSE_MOVING, "sprites/companion-defense-moving.png", 126, 126, 3,
				50);

		// Duck King
		loadStandardSprite(SpriteList.DUCK_KING_ATTACKING, "sprites/duckSwordAnimSheet.png.png", 125, 125, 6, 50);
		loadStandardSprite(SpriteList.DUCK_KING_BRACING, "sprites/duckKingBrace.png", 70, 100, 1, 1);
		loadStandardSprite(SpriteList.DUCK_KING_RUNNING, "sprites/duckRunningSheet.png", 50, 100, 4, 100);
		loadStandardSprite(SpriteList.DUCK_KING_JUMPING, "sprites/duckKingJumping.png", 70, 100, 1, 1);
		loadStandardSprite(SpriteList.DUCK_KING_STRIKING, "sprites/duckKingStriking.png", 125, 125, 1, 1);

		// Rooms
		loadStandardSprite(SpriteList.TOTEM, "sprites/totem.png", 32, 64, 1, 1);
		loadStandardSprite(SpriteList.SPIKES, "sprites/spikes.png", 32, 32, 1, 1);
		loadStandardSprite(SpriteList.PLATFORM_HORZ, "sprites/paddleSideways.png", 32, 32, 1, 1);
		loadStandardSprite(SpriteList.PLATFORM_VERT, "sprites/paddleUpDown.png", 32, 32, 1, 1);
		loadStandardSprite(SpriteList.PLATFORM_DROP, "sprites/platform-drop-through.png", 32, 32, 1, 1);
		loadStandardSprite(SpriteList.LOCKED_DOOR, "sprites/doorClosed.png", 32, 64, 1, 1);
		loadStandardSprite(SpriteList.UNLOCKED_DOOR, "sprites/doorOpen.png", 32, 64, 1, 1);
		loadStandardSprite(SpriteList.LEVER_UP, "sprites/leverUp.png", 32, 32, 1, 1);
		loadStandardSprite(SpriteList.LEVER_DOWN, "sprites/leverDown.png", 32, 32, 1, 1);
		loadStandardSprite(SpriteList.CRATE, "sprites/box.png", 32, 32, 1, 1);
		loadStandardSprite(SpriteList.RED_BUTTON, "sprites/button.png", 32, 32, 1, 1);
		loadStandardSprite(SpriteList.GATE, "sprites/gate.png", 32, 32, 1, 1);
		loadStandardSprite(SpriteList.KEY, "sprites/key.png", 32, 32, 1, 1);
		loadStandardSprite(SpriteList.DUCK_KING_KEY, "sprites/duckKey.png", 32, 32, 1, 1);
		loadStandardSprite(SpriteList.BOSS_ROOM_BACKGROUND, "sprites/duckKingRoom.png", 544, 320, 2, 500);

		if (TileInfo.hasLoaded()) {
			for (TileInfo tileInfo : TileInfo.getTileRegistry().values()) {
				loadTileSprite(tileInfo);
			}
		} else {
			throw new IllegalStateException("Tried to get tile sprites before they were loaded");
		}

		loadStandardSprite(SpriteList.PLACEHOLDER, "sprites/PLACEHOLDER.png", 15, 8, 1, 1);
		hasLoaded = true;
	}

	public static boolean hasLoaded() {
		return hasLoaded;
	}

	/**
	 * Loads a frame collection (texture/spritesheet) from disk and adds it to
	 * the master frame collection. Textures are a 2D grid of animated frames --
	 * if no animation is required, then supply the value 1 to the numFrames
	 * parameter.
	 * <p>
	 * Frames are loaded row-by-row within the spritesheet, e.g.: 1234 567 (for
	 * a total of 7 frames within a 4x2 frame spritesheet)
	 */
	private static SpriteInfo loadSpriteInfo(String path, int frameWidth, int frameHeight, int numFrames,
			int frameDuration) throws SpriteLoadException, IOException {

		// Load the image as a BufferedImage so we can use .getSubImage() after
		// to split it
		BufferedImage masterImage = SwingFXUtils.fromFXImage(new Image(path), null);

		// Check that the number of frames matches up with our params
		if (masterImage.getWidth() % frameWidth != 0) {
			throw new SpriteLoadException(
					"Image " + path + " could not be loaded; image width was not divisible by the frame width.");
		}
		if (masterImage.getHeight() % frameHeight != 0) {
			throw new SpriteLoadException(
					"Image " + path + " could not be loaded; image height was not divisible by the frame height.");
		}
		// Check if we asked for more frames than the texture has
		int totalFrameCount = (masterImage.getWidth() / frameWidth) * (masterImage.getHeight() / frameHeight);
		if (numFrames > totalFrameCount) {
			throw new SpriteLoadException("Image " + path
					+ " could not be loaded; requested frame count exceeded frames in the sprite sheet.");
		}
		int numFramesX = masterImage.getWidth() / frameWidth;
		int numFramesY = masterImage.getHeight() / frameHeight;

		Image[] frames = new Image[numFrames];

		// Split into frames
		boolean done = false;
		for (int row = 0; row < numFramesY; row++) {
			for (int column = 0; column < numFramesX; column++) {
				if (row * numFramesX + column >= numFrames) {
					done = true;
					break;
				}
				// Convert to JavaFX image
				BufferedImage frame = masterImage.getSubimage(column * frameWidth, row * frameHeight, frameWidth,
						frameHeight);
				WritableImage fxImage = SwingFXUtils.toFXImage(frame, null);
				frames[row * numFramesX + column] = fxImage;
			}

			if (done) {
				break;
			}
		}

		FrameCollection frameCollection = new FrameCollection(frames);
		return new SpriteInfo(frameWidth, frameHeight, numFrames, frameDuration, frameCollection);
	}

	private static void loadStandardSprite(SpriteList spriteID, String path, int frameWidth, int frameHeight,
			int numFrames, int frameDuration) throws SpriteLoadException, IOException {
		if (spritesCache.containsKey(spriteID)) {
			throw new SpriteLoadException("Attempted to load new standard sprite, but ID " + spriteID
					+ " already exists in the frame cache.");
		}
		spritesCache.put(spriteID, loadSpriteInfo(path, frameWidth, frameHeight, numFrames, frameDuration));
	}

	private static void loadTileSprite(TileInfo tileType) throws IOException {
		for (Map.Entry<String, String> variantSprite : tileType.getSpriteFilenames().entrySet()) {
			String variant = variantSprite.getKey();
			loadTileSprite(tileType, variant, variantSprite.getValue(), tileType.getBlockWidth(),
					tileType.getBlockHeight(), tileType.getNumSpriteFrames(), tileType.getSpriteFrameDuration());
		}
	}

	private static void loadTileSprite(TileInfo tileType, String variant, String path, int frameWidth,
			int frameHeight, int numFrames, int frameDuration) throws SpriteLoadException, IOException {
		Map<String, SpriteInfo> spriteVariants = tileSpritesCache.get(tileType);
		if (spriteVariants != null && spriteVariants.containsKey(variant)) {
			throw new SpriteLoadException("Attempted to load new tile sprite, but tile type "
					+ tileType.getDisplayName() + ": " + variant + " already exists in the frame cache.");
		}
		SpriteInfo si = loadSpriteInfo(path, frameWidth, frameHeight, numFrames, frameDuration);
		if (spriteVariants == null) {
			spriteVariants = new HashMap<>();
			tileSpritesCache.put(tileType, spriteVariants);
		}
		spriteVariants.put(variant, si);
	}

	/**
	 * Retrieves a SpriteInfo from the cache.
	 */
	public static SpriteInfo getSpriteInfo(SpriteList spriteId) throws SpriteLoadException {
		if (!spritesCache.containsKey(spriteId)) {
			throw new SpriteLoadException(
					"Attempted to retrieve standard sprite ID '" + spriteId + "', which does not exist.");
		}
		return spritesCache.get(spriteId);
	}

	/**
	 * Retrieves a SpriteInfo from the cache.
	 */
	public static SpriteInfo getSpriteInfo(TileInfo tileType, String variant) throws SpriteLoadException {
		Map<String, SpriteInfo> spriteVariants = tileSpritesCache.get(tileType);
		if (spriteVariants == null || !spriteVariants.containsKey(variant)) {
			throw new SpriteLoadException("Attempted to retrieve tile sprite '" + tileType.getDisplayName() + ": "
					+ variant + "', which does not exist.");
		}
		return spriteVariants.get(variant);
	}

	/**
	 * looks up default sprite into number, based on the SpriteInfo provided
	 *
	 * @param si
	 *            sprite info to find
	 * @return index/reference of the sprite info
	 */
	public static SpriteList lookupSprite(SpriteInfo si) {
		for (SpriteList k : spritesCache.keySet()) {
			if (si.equals(spritesCache.get(k))) {
				return k;
			}
		}
		return SpriteList.NULL;
	}

	/**
	 * Retrieves a set of all spriteIDs in the cache.
	 */
	public static Set<SpriteList> defaultSpriteKeys() {
		return spritesCache.keySet();
	}
}