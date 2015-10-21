package gamedev.lwjgl.game.systems;

public class ResourceSystem {

	private int energy;
	
	public void addEnergy(int energy) {
		this.energy += energy;
	}
	
	public int getEnergy() {
		return energy;
	}
}
