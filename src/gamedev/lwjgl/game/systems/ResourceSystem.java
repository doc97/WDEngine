package gamedev.lwjgl.game.systems;

public class ResourceSystem {

	private int energy;
	private int maxEnergy;
	
	public void init() {
		energy = 1;
		maxEnergy = 5;
	}
	
	public void addEnergy(int energy) {
		this.energy += energy;
		if (this.energy > maxEnergy)
			this.energy = maxEnergy;
		else if (this.energy < 0)
			this.energy = 0;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	public int getMaxEnergy() {
		return maxEnergy;
	}
}
