package gamedev.lwjgl.engine.physics;

import org.joml.Vector2f;

public class CollisionBox {
	
	private Circle inner;
	private Circle outer;
	
	public CollisionBox(float x, float y, float innerRadius, float outerRadius){
		inner = new Circle(x, y, innerRadius);
		outer = new Circle(new Vector2f(x, y), outerRadius);
	}
	
	public Circle getInner(){
		return inner;
	}
	
	public Circle getOuter(){
		return outer;
	}
	
	public void setPosition(float x, float y){
		inner.setPosition(x, y);
		outer.setPosition(x, y);
	}
	
	public void setInnerOffset(float x, float y){
		inner.setPosition(outer.getPosition().x + x, outer.getPosition().y + y);
	}
	
	public void addPosistion(float dx, float dy){
		inner.addPosition(dx, dy);
		outer.addPosition(dx, dy);
	}
	
}
