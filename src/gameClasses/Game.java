package gameClasses;
import gameElementClasses.*;

public class Game {
	private final static RowCol OVERWORLD_SIZE = new RowCol(15, 15);

	private final static RowCol PLAYER_INIT_STARTING_LOCATION = new RowCol(13, 7);
		
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
		data = new GridData(worldLocation, OVERWORLD_SIZE, 0, 0, null, player.getLocation());
	}
	
	private void addPlayerToGrid(){
		data.addEntity(player.getLocation(), player.toString());
	}
	
	public String getEntityAtLocation(RowCol location){
		return data.getSpaceData(location);
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

	public void setWorldLocation(int deltaX, int deltaY) {
		worldLocation.setRow(worldLocation.getRow() + deltaX);
		worldLocation.setCol(worldLocation.getCol() + deltaY);
		data.moveGrid(deltaX, deltaY);
		String[][] oldGrid = data.getGrid();
		
		data = null;
		data = new GridData(worldLocation, OVERWORLD_SIZE, deltaX, -deltaY, oldGrid, player.getLocation());
		System.out.println("World Location: " + worldLocation);
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public void setPlayerLocation(RowCol location){
		player.setLocation(location);
		System.out.println("Player Location: " + player.getLocation());
	}
	
	public static RowCol getOverworldSize() {
		return OVERWORLD_SIZE;
	}
	
	
	
}
