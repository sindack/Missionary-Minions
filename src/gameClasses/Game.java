package gameClasses;
import gameElementClasses.*;

public class Game {
	private final RowCol OVERWORLD_SIZE = new RowCol(15, 15);
	private final RowCol PLAYER_INIT_STARTING_LOCATION = new RowCol(14, 7);
		
	private GridData data;
	
	private int level;
	
	private Player player;
	
	private RowCol worldLocation; // X-Axis, Y-Axis
	
	/*******************************
	 *****Functions and Methods*****
	 *******************************/
	
	public Game(RowCol initWorldLocation){
		RowCol playerStartingCoordinate = PLAYER_INIT_STARTING_LOCATION;
		player = new Player(playerStartingCoordinate);
		
		worldLocation = initWorldLocation;
		level = 0;
		
		initializeGridData();
		addPlayerToGrid();
	}
	
	private void initializeGridData(){
		data = new GridData(worldLocation, OVERWORLD_SIZE);
	}
	
	private void addPlayerToGrid(){
		data.addEntity(player.getLocation(), player.toString());
	}

	public GridData getGridData(){
		return data;
	}
	
	public int getLevel() {
		return level;
	}

	public void incrementLevel() {
		this.level += 1;
	}

	public RowCol getWorldLocation() {
		return worldLocation;
	}

	public void setWorldLocation(RowCol worldLocation) {
		this.worldLocation = worldLocation;
	}
	
}
