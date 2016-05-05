package gameElementClasses;

public class Player {
	private final int MAX_HEALTH = 100;
	private final int MIN_HEALTH = 0;
	
	private RowCol location;
	private int health;
	
	public Player(RowCol initLoc){
		setLocation(initLoc);
		health = MAX_HEALTH;
	}
	
	
	
	@Override
	public String toString(){
		return "Player";
	}



	public RowCol getLocation() {
		return location;
	}



	public void setLocation(RowCol location) {
		this.location = location;
	}



	public int getHealth() {
		return health;
	}



	public void increaseHealth(int amount) {
		health = health + amount > MAX_HEALTH ? MAX_HEALTH : health + amount;
	}
	
	
	public void decreaseHealth(int amount){
		health = health - amount < MIN_HEALTH ? MIN_HEALTH : health - amount;
	}
}
