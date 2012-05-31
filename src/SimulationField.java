import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
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

public class SimulationField implements Runnable{
	
	float cSquared;
	int stepCount;
	
	private Thread runner;
	private Boolean runningSim;
	
	float oldMesh[];
	float mesh[];
	float newMesh[];
	boolean walls[];
	boolean boarder[];
	
	private MemoryImageSource memoryImageSource;
	private int pixelBuffer[];
	private int refreshGraphicAfterSteps = 0;
	private int resolution;
	
	private int xcount;
	private int ycount;
	private int cellCount;
	private int pixelCount;
	
	
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
	
	public void createNewField(int sizex_cm, int sizey_cm, int resolution, float timeH)
	{
		this.resolution = resolution;
		this.timeH = timeH;
		
		spaceH = (float) (sizex_cm / ( 100.0 * resolution));
		
		xcount = sizex_cm * resolution;
		ycount = sizey_cm * resolution;
		
		cellCount = xcount * ycount;
		
		mesh = new float[cellCount];
		newMesh = new float[cellCount];
		walls = new boolean[cellCount];
		boarder = new boolean[cellCount];
		oldMesh = new float[cellCount];
		
		pixelBuffer = new int[cellCount];
		memoryImageSource = new MemoryImageSource(xcount, ycount, pixelBuffer, 0, xcount);
		memoryImageSource.setAnimated(true);
		
		runningSim = false;
		
		for (int x = 0; x < xcount; x++)
		{
		    for (int y = 0; y < ycount; y++) 
		    {
		    	int cell = x+xcount*y;

		    	if (x == 0 || x == xcount-1) 
		    	{
		    		boarder[cell] = true;
		    	}
		    
		    	if (y == 0 || y == ycount-1)
		    	{
		    		boarder[cell] = true;
		    	}
		    }
	    }
		

		cSquared = (float) (343.0 * 343.0 * (timeH * timeH) / (spaceH * spaceH));
	
	}

	
	public void attatchCanvasElement(Canvas canvas)
	{
		this.canvasField = canvas;
		xPixelCellRatio = this.canvasField.getWidth() / xcount;
		yPixelCellRatio = this.canvasField.getHeight() / ycount;
		pixelCount = canvas.getWidth() * canvas.getHeight();
		
	}
	
	public void mouseClickInField(int x, int y)
	{
		//System.out.println("click x:" + x + " y:" + y);
		
		int xCellCoordinate = (int) Math.floor(x / xPixelCellRatio);
		int yCellCoordinate = (int) Math.floor(y / yPixelCellRatio);
		
		int cellNumber = (yCellCoordinate * ycount) + xCellCoordinate;
		System.out.println("zelle nummer:" + cellNumber);
		
		updateCanvas();
	}
	
	public void mouseClickInFieldWithCellType(int x, int y, CellType type)
	{
		//System.out.println("click x:" + x + " y:" + y);
		
		int xCellCoordinate = (int) Math.floor(x / xPixelCellRatio);
		int yCellCoordinate = (int) Math.floor(y / yPixelCellRatio);
		
		int cellNumber = (yCellCoordinate * ycount) + xCellCoordinate;
		System.out.println("zelle nummer:" + cellNumber);
		
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
				
				walls[i] = true;
			}
			
			for (int i = (endY * ycount) + startX; i <(endY * ycount) + endX; i++) {
				
				walls[i] = true;
				
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
			
			
			
		}
		}
		
		updateCanvas();
	}
	
	public void updateCanvas()
	{


		
			int circle_left = 0;
			int circle_top = 0;
			Color circle_color = Color.red;
			Color c = Color.MAGENTA;
			

			
			for (int i = 0; i < mesh.length; i++){
			
					
					if(boarder[i]){
					
						c = Color.GRAY;
						
					}else if(walls[i]){
						
						c = Color.BLACK;
						
					}else{
					
						c = new Color(  Color.HSBtoRGB((float)mesh[i] * 20.0f, (float)0.8, (float)0.8) );
						
					}
					
					
					pixelBuffer[i] = c.getRGB();
				
			}
			
			
			
			
			/*
			for (Cell cell : field) {
			
				if (cell.getCellType() == CellType.CellTypeSimulationCell ) {
					
					c = new Color(  Color.HSBtoRGB((float)(cell.getDisplacement(0.0) + 1.8), (float)0.8, (float)0.8) );
					g.setColor(c);
					g.fillRect((int)Math.ceil(width * (i%xcount)), (int)Math.ceil(height * (i/xcount)), (int)Math.ceil(width), (int)Math.ceil(height));
					
					
				}else if(cell.getCellType() == CellType.CellTypeSolidCell)
				{
					
					g.setColor(Color.BLACK);
					g.fillRect((int)Math.ceil(width * (i%xcount)), (int)Math.ceil(height * (i/xcount)), (int)Math.ceil(width), (int)Math.ceil(height));
					
				}else if(cell.getCellType() == CellType.CellTypeGeneratorCell)
				{
					
					circle_color =  Color.MAGENTA ;
					g.setColor(circle_color);

					circle_left = (int)Math.ceil(width * (i%xcount)) - 10;
					circle_top = (int)Math.ceil(height * (i/xcount)) - 10;
					

				}else if(cell.getCellType() == CellType.CellTypeBeyondEdgeCell)
				{
					
					g.setColor(Color.ORANGE);
					g.fillRect((int)Math.ceil(width * (i%xcount)), (int)Math.ceil(height * (i/xcount)), (int)Math.ceil(width), (int)Math.ceil(height));
		

				}
				i++;

			}
			*/
			
			if (scopeCanvas != null) {
				
				/*
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
				
				g.setColor(Color.ORANGE);
				g.drawOval(sensorX_Coordinate - 5, sensorY_Coordinate - 5, 10, 10);
				
				sensorRecorder.saveValue(valueAtSensor);
				*/
			}
			
			memoryImageSource.newPixels();
			memoryImageSource.setAnimated(true);
			memoryImageSource.setFullBufferUpdates(true);
			
			canvasField.getGraphics().drawImage(canvasField.createImage(memoryImageSource), 0, 0, 360, 360, null);
			

			
		
	}
	
	public void step()
	{
		
		float north;
		float south;
		float east;
		float west;
		float center;
		
		int n;
		
		//this is just test source 
		mesh[11100] = (float) ( 1.2f * Math.sin(stepCount / 100.0));
		
		for(int y = 1; y < ycount -1; y++){
			
			int yOffset = y * xcount;
		
			for (int x = 1; x < xcount -1; x++) 
			{
			
				n = x + yOffset;
				
				north = walls[n - xcount] ? 0 : mesh[n - xcount];
				south = walls[n + xcount] ? 0 : mesh[n + xcount];
				west = walls[n -1] ? 0 : mesh[n - 1];
				east = walls[n +1] ? 0 : mesh[n + 1];
				
				center = walls[n] ? 0 : mesh[n];
				
				north = boarder[n - xcount] ? oldMesh[n] : north;
				south = boarder[n + xcount] ? oldMesh[n] : south;
				west = boarder[n -1] ? oldMesh[n] : west;
				east = boarder[n +1] ? oldMesh[n] : east;
				
				
				newMesh[n] = cSquared*(north+south+east+west - (4 * center))  + 2*center - oldMesh[n];
				oldMesh[n] = mesh[n];
				
			}
		}
		
		oldMesh = mesh;
		mesh = newMesh;
		stepCount++;
		
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
			step();
			if (refreshGraphicAfterSteps < 1) {
				
				updateCanvas();

				

				refreshGraphicAfterSteps = 20;
				
			}else{
				refreshGraphicAfterSteps = refreshGraphicAfterSteps - 1;
			}
		}
	}





	
	public void dumpToFile(File aFile)
	{
		
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(aFile));
			
			output.write("" + xcount + "," + ycount + "," + resolution + "," + timeH);
			output.newLine();
			/*
			for (Cell cell : field) {
				output.write( cell.getCellType().toString() + "," + cell.getDisplacement(0.0) );
				output.newLine();
			}
			*/
			
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
			/*
			while ((thisLine = br.readLine()) != null && i < field.size()) { 
      
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
			*/
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
	
}
