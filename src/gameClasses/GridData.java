package gameClasses;

public class GridData {
	private String[][] grid;
	private RowCol worldLocation;
	
	private final int NUM_ROWS;
	private final int NUM_COLS;
	
	
	
	public GridData(RowCol worldGrid, RowCol gridSize){
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
	
	public String checkHighlightedSpace(RowCol space){
		return grid[space.getRow()][space.getCol()];
	}
	
}
