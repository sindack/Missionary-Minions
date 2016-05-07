package gameElementClasses;

public class Rock {
	RowCol location;
	
	public Rock(RowCol location){
		this.location = location;
	}
	
	@Override
	public String toString(){
		return "Rock";
	}

	public RowCol getLocation() {
		return location;
	}

	public void setLocation(RowCol location) {
		this.location = location;
	}
}
