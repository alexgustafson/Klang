import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
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
	
	// gibt an wie viele zellen pro cm existieren ( simulations auflšsung )
	private int resolution;
	
	private int xcount;
	private int ycount;
	
	private Cell beyondEdgeCell;
	
	private Canvas canvasField;
	private float xPixelCellRatio;
	private float yPixelCellRatio;
	
	private Canvas scopeCanvas;
	


	private float timeH;
	private float spaceH;
	
	private int generator_x_coord;
	private int generator_y_coord;
	private Boolean generator;

	
	private Boolean sensorActive = false;
	private int sensorX_Coordinate;
	private int sensorY_Coordinate;
	
	private SensorRecorder sensorRecorder;
	
	private MemoryImageSource memoryImageSource;
	private int pixelBuffer[];
	
	private int refreshGraphicAfterSteps = 0;
	private float generatorFrequency = 1;
	
	public void createNewField(int sizex_cm, int sizey_cm, int resolution, float timeH)
	{
		
		this.timeH = timeH;
		
		spaceH = (float) (sizex_cm / ( 100.0 * resolution));

		if ((340*timeH/spaceH) > 0.7071) {
			System.out.println("time step is too large");
		}
		
		this.resolution = resolution;
		this.timeH = timeH;
		
		spaceH = (float) (sizex_cm / ( 100.0 * resolution));
		
		xcount = sizex_cm * resolution;
		ycount = sizey_cm * resolution;
		
		pixelBuffer = new int[xcount * ycount];
		memoryImageSource = new MemoryImageSource(xcount, ycount, pixelBuffer, 0, xcount);
		memoryImageSource.setAnimated(true);
		
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
	
	public void mouseClickInFieldWithCellType(int x, int y, CellType type)
	{
		//System.out.println("click x:" + x + " y:" + y);
		
		int xCellCoordinate = (int) Math.floor(x / xPixelCellRatio);
		int yCellCoordinate = (int) Math.floor(y / yPixelCellRatio);
		
		int cellNumber = (yCellCoordinate * ycount) + xCellCoordinate;
		System.out.println("zelle nummer:" + cellNumber);
		
		Cell cell = field.get(cellNumber);
		cell.setCellType(type);

		updateCanvas();
	}
	
	public void mouseClickPositionSensor(int x, int y)
	{
		//System.out.println("click x:" + x + " y:" + y);
		
		sensorActive = true;
		sensorX_Coordinate = x;
		sensorY_Coordinate = y;

		updateCanvas();
	}
	

	
	public void mouseClickInFieldCreateRohr(int startX, int startY, int endX, int endY)
	{
		//System.out.println("click x:" + x + " y:" + y);
		
		endX = (int) Math.floor(endX / xPixelCellRatio);
		endY = (int) Math.floor(endY / yPixelCellRatio);
		
		startX =(int)Math.floor(startX / xPixelCellRatio);
		startY =(int)Math.floor(startY / yPixelCellRatio);

			
			for (int i = (startY * ycount) + startX; i < (startY * ycount) + endX; i++) {
				
				field.get(i).setCellType(CellType.CellTypeSolidCell);
			}
			
			for (int i = (endY * ycount) + startX; i <(endY * ycount) + endX; i++) {
				
				field.get(i).setCellType(CellType.CellTypeSolidCell);
			}
			

		updateCanvas();
	}
	
	public void mouseClickInFieldCreateNil(int startX, int startY, int endX, int endY)
	{
		//System.out.println("click x:" + x + " y:" + y);
		
		endX = (int) Math.floor(endX / xPixelCellRatio);
		endY = (int) Math.floor(endY / yPixelCellRatio);
		
		startX =(int)Math.floor(startX / xPixelCellRatio);
		startY =(int)Math.floor(startY / yPixelCellRatio);
		for (int j = startY; j < endY + 1;j++){
		for (int i = (j * ycount) + startX; i < (j * ycount) + endX; i++) {
			
			field.get(i).setCellType(CellType.CellTypeBeyondEdgeCell);
			
		}
		}
		
		updateCanvas();
	}
	
	public void updateCanvas()
	{

		//prŸffe ob es mehr pixels als zellen hat
		if(xPixelCellRatio > 0){
		
			//falls ja wird fŸr jedes zelle ein 4eck gezeichnet
			float width = xPixelCellRatio;
			float height = yPixelCellRatio;
			int i = 0;
			
			int circle_left = 0;
			int circle_top = 0;
			Color circle_color = Color.red;
			Color c;
			
			Graphics g = canvasField.getGraphics();
			
			for (Cell cell : field) {
			
				
				
				if (cell.getCellType() == CellType.CellTypeSimulationCell ) {
					
					if(cell.getDisplacement(0.0) > 0.0005){
						
						c = Color.red;
						
					}else if (cell.getDisplacement(0.0) < -0.0005 ) {
						
						c = Color.BLUE;
						
					}else{
						
						c = Color.WHITE;
						
					}
					
					
					pixelBuffer[i] = c.getRGB();
					
				}else if(cell.getCellType() == CellType.CellTypeSolidCell)
				{
					
					pixelBuffer[i] = Color.BLACK.getRGB();
					
				}else if(cell.getCellType() == CellType.CellTypeGeneratorCell)
				{
					
					//circle_color =  Color.MAGENTA ;
					//g.setColor(circle_color);

					//circle_left = (int)Math.ceil(width * (i%xcount)) - 10;
					//circle_top = (int)Math.ceil(height * (i/xcount)) - 10;
					

				}else if(cell.getCellType() == CellType.CellTypeBeyondEdgeCell)
				{
					
					//g.setColor(Color.ORANGE);
					//g.fillRect((int)Math.ceil(width * (i%xcount)), (int)Math.ceil(height * (i/xcount)), (int)Math.ceil(width), (int)Math.ceil(height));
		

				}
				i++;

			}
			
			if (scopeCanvas != null) {
				

				int xCellCoordinate = (int) Math.floor(sensorX_Coordinate / xPixelCellRatio);
				int yCellCoordinate = (int) Math.floor(sensorY_Coordinate / yPixelCellRatio);
				int cellNumber = (yCellCoordinate * ycount) + xCellCoordinate;
				double valueAtSensor = field.get(cellNumber).getDisplacement(0);
				Graphics gs = scopeCanvas.getGraphics();
				int y = (int) ((scopeCanvas.getHeight()/2.0) - valueAtSensor*60);
				Color cs = Color.red;
				gs.setColor(Color.BLACK);
				gs.copyArea(0, 0, 100, 100, -1, 0);
				gs.fillRect(99, 0, 1, 99);
				
				//System.out.println("pressure at sensor:" +y);
				gs.setColor(cs);
				gs.fillRect(98, y, 2, 2);
				
				//g.setColor(Color.ORANGE);
				//g.drawOval(sensorX_Coordinate - 5, sensorY_Coordinate - 5, 10, 10);
				
				sensorRecorder.saveValue(valueAtSensor);
			}
			
			if (refreshGraphicAfterSteps == 0) {
				//memoryImageSource "weiss" schon wo pixelBuffer ist, muss nicht bei jedem
				//zyklus nochmal Ÿbergeben werden. memoryImageSource.newPixels() reicht fŸr update
				//memoryImageSource.newPixels(pixelBuffer, ColorModel.getRGBdefault(), 0, xcount);
				memoryImageSource.newPixels();
				Image img = canvasField.createImage(memoryImageSource);
				canvasField.getGraphics().drawImage(img, 0, 0, 360, 360, null);
				refreshGraphicAfterSteps = 4;
			}else{
				refreshGraphicAfterSteps = refreshGraphicAfterSteps - 1;
			}

			
		}
	}
	
	public void step()
	{
		for (Cell cell : field) 
		{
			cell.prepareUpdate();
		}
		for (Cell cell : field) 
		{

			cell.update();
		}
		updateCanvas();
	}
	
	public void runSimulation()
	{
		if (runningSim) 
		{

			runningSim = false;
		}else
		{
			sensorRecorder = new SensorRecorder();
			runningSim = true;
			runner = new Thread(this);
			runner.start();
		}
	}

	public Boolean isRunningSim() 
	{
		return runningSim;
	}

	@Override
	public void run() 
	{
		while (runningSim) 
		{
			
			/*
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			step();
			
		}
	}


	@Override
	public float getTimeH() {
		
		return timeH;
	}


	@Override
	public float getSpaceH() {
		
		return spaceH;
	}
	
	public void dumpToFile(File aFile)
	{
		
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(aFile));
			
			output.write("" + xcount + "," + ycount + "," + resolution + "," + timeH);
			output.newLine();
			for (Cell cell : field) {
				output.write( cell.getCellType().toString() + "," + cell.getDisplacement(0.0) );
				output.newLine();
			}
			
			
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void readFromFile(File aFile)
	{
		String thisLine;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(aFile));
			
			//SimulationField simulationField = new SimulationField();
			
			String delim = "[,]";
			String[] tokens = br.readLine().split(delim);
			String x = tokens[0];
			String y = tokens[1];
			String res = tokens[2];
			String tH = tokens[3];
			
			createNewField(Integer.parseInt(x) / Integer.parseInt(res),
					Integer.parseInt(y) / Integer.parseInt(res), 
					Integer.parseInt(res), 
					Float.parseFloat(tH));
			
			int i = 0;
			Cell cell;
			
			while ((thisLine = br.readLine()) != null && i < field.size()) { // while loop begins here
      
		         cell = field.get(i);
		         
		         tokens = thisLine.split(delim);
		         
		         if (tokens.length < 1) {
					return;
				}
		         
		         if (tokens[0].equalsIgnoreCase("CellTypeSimulationCell")) {
					cell.setDisplacement(Double.parseDouble(tokens[1]));
				 }else if (tokens[0].equalsIgnoreCase("CellTypeSolidCell")) {
					cell.setCellType(CellType.CellTypeSolidCell);
				}else if (tokens[0].equalsIgnoreCase("CellTypeGeneratorCell")) {
					cell.setCellType(CellType.CellTypeGeneratorCell);
				}else if (tokens[0].equalsIgnoreCase("CellTypeBeyondEdgeCell")) {
					cell.setCellType(CellType.CellTypeBeyondEdgeCell);
				}
		         
		         i++;
		         
		       } // end while 
			
			br.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public Canvas getScopeCanvas() {
		return scopeCanvas;
	}


	public void setScopeCanvas(Canvas scopeCanvas) {
		this.scopeCanvas = scopeCanvas;
	}
	
	public void shutDownSimulation(){
		
		if (sensorRecorder != null) {
			sensorRecorder.close();
		}
		
		
	}


	@Override
	public float getGeneratorFrequency() {
		
		return (float)generatorFrequency;
		
	}


	public void setGeneratorFrequency(float generatorFrequency) {
		
		this.generatorFrequency = generatorFrequency;
	}
	
}
