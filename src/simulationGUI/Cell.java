package simulationGUI;
import java.util.ArrayList;


public class Cell {
	
	private double pressure;
	private double[] acceleration;
	private int idNr;
	
	private double nextPressure;
	
	private CellType cellType;
	
	ArrayList<Cell> neighbors;
	
	CellDelegate delegate;
	
	int neighborCounter;
	
	
	public Cell(CellDelegate delegate)
	{
		
		this.delegate = delegate;
		
		pressure = 0;
		acceleration = new double[2];
		acceleration[0] = 0;
		acceleration[1] = 0;
		nextPressure = 0;
	}
	
	public void setCellType(CellType cellType)
	{
		this.cellType = cellType;
	}
	
	public void setIdNr(int idNr) 
	{
		this.idNr = idNr;
	}
	
	public void setupNeighbors()
	{
		this.neighbors = delegate.getNeighbors(idNr);
	}
	
	public void changePressure(double d)
	{
		pressure = pressure + d;
	}
	
	public double getPressure()
	{
		return pressure;
	}
	
	public Cell getFirstNeighbor()
	{
		neighborCounter = 1;
		return neighbors.get(0);
		
	}
	
	public Cell getNextNeighbor()
	{
		if(neighborCounter < neighbors.size()){
			
			neighborCounter++;
			return neighbors.get(neighborCounter);
			
		}else
		{
			return null;
		}
		
	}
	
	public void update(){
		
		pressure = (pressure/8.0)+(nextPressure/8.0);
	}
	
	public void prepareUpdate()
	{
		nextPressure = 0;
		
		int i = 0;
		for (Cell neighbor : neighbors) {
			
			if ((i%2) == 0) {
				nextPressure += neighbor.getPressure() / 2 * 10;
			}else
			{
				nextPressure += neighbor.getPressure() / 10;
			}
			
			i++;
			
			if (nextPressure > 5000){
				nextPressure = 5000;
			}
		}
	}
	
	

}
