package simulationGUI;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;


public class SimulationField implements CellDelegate, Runnable{
	
	private Thread runner;
	private Boolean runningSim;
	
	//arraylist mit alle zellen
	private ArrayList<Cell> field;
	
	//arraylist mit zellen die "touched" sind, das
	//heisst, zellen die beeinflusst worden sind.
	//es sollen nur zellen berechnet werden die 
	//aktiv sind
	private ArrayList<Cell> activeCells;
	
	// gibt an wie viele zellen pro cm existieren ( simulations aufl�sung )
	private int resolution;
	
	private int xcount;
	private int ycount;
	
	private Cell beyondEdgeCell;
	
	private Canvas canvasField;
	private float xPixelCellRatio;
	private float yPixelCellRatio;
	
	
	public void createNewField(int sizex_cm, int sizey_cm, int resolution)
	{
		this.resolution = resolution;
		
		xcount = sizex_cm * resolution;
		ycount = sizey_cm * resolution;
		
		beyondEdgeCell = new Cell((CellDelegate)this);
		beyondEdgeCell.setCellType(CellType.CellTypeBeyondEdgeCell);
		beyondEdgeCell.setIdNr(-1);
		field = new ArrayList<Cell>();
		
		for (int i = 0; i < (sizex_cm * sizey_cm * resolution * resolution); i++) {
			
			Cell cell = new Cell((CellDelegate)this);
			cell.setCellType(CellType.CellTypeSimulationCell);
			field.add(cell);
			cell.setIdNr(i);
			
		}
		
		for (Cell cell : field) {
			
			cell.setupNeighbors();
			
		}
		
		runningSim = false;
		
	}


	// Cell Delegate Methods
	@Override
	public ArrayList<Cell> getNeighbors(int idNr) {
		
		ArrayList<Cell> neighbors = new ArrayList<Cell>();
		
		// eine zelle hat 8 nachbarn wenn es nicht am rand des feldes sich befindet
		int distanceFromLeft = (idNr % xcount);
		int distanceFromRight = xcount - (idNr % xcount);
		int distanceFromBottom = (int) (ycount - Math.floor((float)(idNr / xcount)));
		
		//links oben
		if (idNr > xcount && distanceFromLeft > 0 ) {
			neighbors.add(field.get((idNr - 1)-xcount));
		}else
		{
			neighbors.add(beyondEdgeCell);
		}
		
		//oben
		if (idNr > xcount) {
			neighbors.add(field.get((idNr )-xcount));
		}else
		{
			neighbors.add(beyondEdgeCell);
		}
		
		//rechts oben
		if (idNr > xcount && distanceFromRight > 1 ) 
		{
			neighbors.add(field.get((idNr + 1) -xcount));
		}else
		{
			neighbors.add(beyondEdgeCell);
		}
		
		//links
		if (distanceFromRight > 1) {
			neighbors.add(field.get((idNr + 1)));
		}else
		{
			neighbors.add(beyondEdgeCell);
		}
		
		//links unten
		if (distanceFromRight > 1 && distanceFromBottom > 1) {
			neighbors.add(field.get((idNr + 1) +xcount));
		}else
		{
			neighbors.add(beyondEdgeCell);
		}
		
		//unten
		if ( distanceFromBottom > 1) {
			neighbors.add(field.get((idNr ) +xcount));
		}else
		{
			neighbors.add(beyondEdgeCell);
		}
		
		//rechts unten
		if ( distanceFromBottom > 1 && distanceFromLeft > 0 ) {
			neighbors.add(field.get((idNr -1 ) +xcount));
		}else
		{
			neighbors.add(beyondEdgeCell);
		}
		
		//rechts
		if ( distanceFromLeft > 0 ) {
			neighbors.add( field.get(idNr -1) );
		}else
		{
			neighbors.add(beyondEdgeCell);
		}
		
		return neighbors;
	}
	
	public void attatchCanvasElement(Canvas canvas)
	{
		this.canvasField = canvas;
		xPixelCellRatio = this.canvasField.getWidth() / xcount;
		yPixelCellRatio = this.canvasField.getHeight() / ycount;
	}
	
	public void mouseClickInField(int x, int y)
	{
		//System.out.println("click x:" + x + " y:" + y);
		
		int xCellCoordinate = (int) Math.floor(x / xPixelCellRatio);
		int yCellCoordinate = (int) Math.floor(y / yPixelCellRatio);
		
		int cellNumber = (yCellCoordinate * ycount) + xCellCoordinate;
		System.out.println("zelle nummer:" + cellNumber);
		
		Cell cell = field.get(cellNumber);
		cell.changePressure(1.0);
		
		updateCanvas();
	}
	
	public void updateCanvas()
	{
		
		
		//pr�ffe ob es mehr pixels als zellen hat
		if(xPixelCellRatio > 0){
		
			//falls ja wird f�r jedes zelle ein 4eck gezeichnet
			float width = xPixelCellRatio;
			float height = yPixelCellRatio;
			int i = 0;
			
			for (Cell cell : field) {
			
				Graphics g = canvasField.getGraphics();
				Color c = new Color(Math.min((int)cell.getPressure() * 20,255), Math.min((int)cell.getPressure() * 2, 255), 180);
				g.setColor(c);
				g.fillRect((int)Math.ceil(width * (i%xcount)), (int)Math.ceil(height * (i/xcount)), (int)Math.ceil(width), (int)Math.ceil(height));
				i++;
			}
		}
	}
	
	public void step()
	{
		for (Cell cell : field) {
			
			
			cell.prepareUpdate();

			
			
		}
		for (Cell cell : field) {
			
			
			cell.update();
			
		}
		
		updateCanvas();
	}
	
	public void runSimulation()
	{
		if (runningSim) {
			runningSim = false;
		}else{
			runningSim = true;
			runner = new Thread(this);
			runner.start();
		}

	}


	@Override
	public void run() {
		
		while (runningSim) {
			
			step();
			
		}
	}
	
}
