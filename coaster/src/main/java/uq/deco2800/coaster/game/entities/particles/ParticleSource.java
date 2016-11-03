package uq.deco2800.coaster.game.entities.particles;

import uq.deco2800.coaster.game.world.World;


public class ParticleSource {
	private ParticleSource() {
	}

	public static void addParticleSource(float posX, float posY, int particleType, int particleDensity,
										 int velocity, boolean gravity, boolean deletable) {
		for (int i = 0; i < 366; i += particleDensity) {
			double angle = Math.toRadians(i);
			float xVelocity = (float) (velocity * Math.cos(angle));
			float yVelocity = (float) (velocity * Math.sin(angle));
			Particle particle = new Particle(xVelocity, yVelocity, posX, posY, particleType, 40, gravity,
					deletable);
			World.getInstance().addEntity(particle);
		}
	}

	public static void addParticleSource(float posX, float posY) {
		addParticleSource(posX, posY, 301, 20, 5, true, false);
	}
}
