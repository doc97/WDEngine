package gamedev.lwjgl.game.entities;

import java.util.Map;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.physics.Circle;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.sound.SoundSystem;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.engine.utils.Timer;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.ui.Inventory;

public class Player extends Entity {

//	private AnimatedTexture animation;

	private int radius;
	private Timer dashTimer = new Timer();
	private Inventory inventory;
	private int energy;

	
	public Player(float x, float y) {
		super(x, y);
		init();
		collisionShape = new Circle(x, y, radius);
		//animation = new AnimatedTexture(AssetManager.loadAnimation("Test.anim"), 60/30);
		dynamic = true;
		inventory = new Inventory();
	}
	
	private void init() {
		Map<String, String> data = AssetManager.getData("player");
		String coretex = data.get("coretexture");
		String rad = data.get("radius");

		radius = Integer.parseInt(rad);
		addTexture(AssetManager.getTexture(coretex), -radius, -radius / 2, 2 * radius, 2 * radius, 0, 0, 0);
	}
	
	public void dash() {
		dashTimer.set(10);
		dashTimer.setActive(true);
	}
	
	public void addEnergy(int energy) {
		this.energy += energy;
		Game.INSTANCE.sounds.playSound(AssetManager.getSound("nom"));
	}
	
	@Override
	public void update() {
//		animation.update(dt);
		collisionShape.setPosition(x, y);
		dashTimer.update();
		if(dashTimer.getPercentage() == 1)
			dashTimer.setActive(false);
		if(dashTimer.isActive())
			speed.set(Math.signum(speed.x) * 5 * maxSpeed, 0);
		
		super.update();
	}
	
	@Override
	public void render(SpriteBatch batch) {
		//super.setTexture(0, animation.getCurrent());
		super.render(batch);
	}
	
	@Override
	public void addEntityPosition(float dx, float dy) {
		super.addEntityPosition(dx, dy);
		collisionShape.addPosition(dx, dy);
	}

	@Override
	public void setEntityPosition(float x, float y) {
		super.setEntityPosition(x, y);
		collisionShape.setPosition(x, y);
	}
	
	public boolean isDashing() {
		return dashTimer.isActive();
	}
	
	public Inventory getInventory(){
		return inventory;
	}
	
	public int getEnergy() {
		return energy;
	}
	
}