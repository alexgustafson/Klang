import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;


public class Cell {
	
	private double displacement;
	private double nextDisplacement;
	private double previousDisplacement = 0;

	private int idNr;
	
	private CellType cellType;
	
	ArrayList<Cell> neighbors;
	
	CellDelegate delegate;
	
	int neighborCounter;
	
	private double K = 0.04;
	private double time = 0.0;
	
	private PinkNoise pinkNoiseGenerator;
	
	
	public Cell(CellDelegate delegate)
	{
		
		this.delegate = delegate;
		
		displacement = 0;
		nextDisplacement = 0;
		
		K = 343.0 * 343.0 * delegate.getTimeH() * delegate.getTimeH() / (delegate.getSpaceH() * delegate.getSpaceH());
		
	}
	
	public void setCellType(CellType cellType)
	{
		this.cellType = cellType;
		
		if (cellType == CellType.CellTypeGeneratorCell) {
			pinkNoiseGenerator = new PinkNoise();
		}
		
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
		displacement = displacement + d;
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
		
		previousDisplacement = displacement;
		displacement = (nextDisplacement) - displacement;
		
	}
	
	public void prepareUpdate()
	{
		if (cellType == CellType.CellTypeSimulationCell) {
			
			nextDisplacement = 0;
			double neightborValues = 0;
			
			int i = 0;
			for (Cell neighbor : neighbors) {
				
				if ((i%2) == 0) {
					//nextPressure += 0.1 * neighbor.getPressure(previousY)  ;
				}else
				{
					neightborValues +=  neighbor.getDisplacement( previousDisplacement);
				}
				
				i++;
				
			}
			
			nextDisplacement = (2 * displacement) - previousDisplacement + (K*( neightborValues - (4 * displacement)));
			//System.out.println("K value: " + K);
			
		}else if(cellType == CellType.CellTypeSolidCell)
		{
			nextDisplacement = 0;
		}else if(cellType == CellType.CellTypeGeneratorCell)
		{

			nextDisplacement = pinkNoiseGenerator.nextValue() * 2;
			/*
			nextPressure = Math.sin( time) * 2;
			time = time + (480 * 2 * Math.PI * delegate.getTimeH());
			if(time > 2 * Math.PI)
			{
				time = -1 * 2 * Math.PI;
			}
			*/

		}
		
	}

	public double getDisplacement() {
		return displacement;
	}
	
	public double getDisplacement(double externalPrevious)
	{
		if(cellType == CellType.CellTypeBeyondEdgeCell)
		{
			return externalPrevious ;
		}
		
		return displacement;
	}

	public void setDisplacement(double displacement) {
		this.displacement = displacement;
	}

	public CellType getCellType() {
		return cellType;
	}

}
