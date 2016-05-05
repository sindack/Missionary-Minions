package gameClasses;

import java.util.ArrayList;

import gameElementClasses.*;

public class GridData {
	private final int NUM_ROWS;
	private final int NUM_COLS;
	
	private String[][] grid;
	private RowCol worldLocation;
	private ArrayList<RowCol> movementQueue;
	

	
	
	
	public GridData(RowCol worldGrid, RowCol gridSize){ // worldGrid, gridSize
		worldLocation = worldGrid;
		
		NUM_ROWS = gridSize.getRow();
		NUM_COLS = gridSize.getCol();
		
		grid = new String[NUM_ROWS][NUM_ROWS];
		fillGrid();
	}
	
	private void fillGrid(){
		fillGridWithEmptySlots();
	}
	
	private void fillGridWithEmptySlots(){
		for (int i = 0; i < NUM_ROWS; i++){
			for (int j = 0; j < NUM_COLS; j++){
				grid[i][j] = "Empty";
			}
		}
	}
	
	public String getSpaceData(RowCol space){
		return grid[space.getRow()][space.getCol()];
	}
	
	public void appendMovementQueue(RowCol location){
		movementQueue.add(location);
	}
	
	public void updateObjectMovement(RowCol lastLocation, RowCol newLocation, String entity){
		removeEntity(lastLocation);
		addEntity(newLocation, entity);
	}
	
	public void removeEntity(RowCol location){
		grid[location.getRow()][location.getCol()] = "Empty";
	}
	
	public void addEntity(RowCol location, String entity){
		grid[location.getRow()][location.getCol()] = entity;
	}
	
	public int getNUM_ROWS() {
		return NUM_ROWS;
	}
	
	public int getNUM_COLS() {
		return NUM_COLS;
	}
}
