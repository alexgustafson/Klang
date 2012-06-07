import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
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
	float damping[];
	boolean walls[];
	boolean boarder[];
	boolean generators[];
	
	private BufferedImage dbimage;
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
	private float time;
	
	private int generator_x_coord;
	private int generator_y_coord;
	private Boolean generator;

	private Boolean sensorActive = false;
	private int sensorX_Coordinate;
	private int sensorY_Coordinate;
	
	private SensorRecorder sensorRecorder;
	PinkNoise pinkNoiseGenerator;
	float frequency;
	private int speed;
	float dampingValue = 0.001f;
	float variableDampingCoeff;
	int boarderWidth = 35;
	
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
		generators = new boolean[cellCount];
		damping = new float[cellCount];
		
		pixelBuffer = new int[cellCount];
		
		runningSim = false;
		
		
		
		
		for (int x = 0; x < xcount; x++)
		{
		    for (int y = 0; y < ycount; y++) 
		    {
		    	int cell = x+xcount*y;
		    	
		    	damping[cell] = dampingValue;

		    	if (x < boarderWidth || x > xcount- boarderWidth ) 
		    	{
		    		boarder[cell] = true;
		    		damping[cell] = x < boarderWidth ? (float)(dampingValue*(boarderWidth - x)):(dampingValue*(boarderWidth - (xcount -  x)));
		    	}
		    
		    	if (y < boarderWidth || y > ycount- boarderWidth)
		    	{
		    		boarder[cell] = true;
		    		damping[cell] = y < boarderWidth ? (float)(dampingValue*(boarderWidth - y)):(dampingValue*(boarderWidth - (ycount -  y)));
		    	}
		    }
	    }
		

		cSquared = (float) (343.0 * 343.0 * (timeH * timeH) / (spaceH * spaceH));
		
	
		if ((340*timeH/spaceH) > 0.7071) {
			System.out.println("time step is too large");
		}
		
		pinkNoiseGenerator = new PinkNoise();
		frequency = 440f;
	}

	
	public void attatchCanvasElement(Canvas canvas)
	{
		this.canvasField = canvas;
		xPixelCellRatio = this.canvasField.getWidth() / xcount;
		yPixelCellRatio = this.canvasField.getHeight() / ycount;
		pixelCount = canvas.getWidth() * canvas.getHeight();
		dbimage = new BufferedImage(xcount, ycount, BufferedImage.TYPE_INT_ARGB);
		pixelBuffer = ((DataBufferInt) dbimage.getRaster().getDataBuffer()).getData();
		
	}
	
	public void mouseClickInField(int x, int y)
	{
		System.out.println("click x:" + x + " y:" + y);
		
		float xfloat = (float)x;
		float yfloat = (float)y;
		float width = canvasField.getWidth();
		float height = canvasField.getHeight();
		
		int xCellCoordinate = (int) (float)(xcount * (xfloat / width));
		int yCellCoordinate = (int) (float)(ycount * (yfloat / height));
		
		int cellNumber = (yCellCoordinate * xcount) + xCellCoordinate;
		mesh[cellNumber] = 5.0f;
		oldMesh[cellNumber] = 5.0f;
		
		System.out.println("cell:" + xCellCoordinate + " y:" + yCellCoordinate);
		
		
	}
	
	public void mouseClickInFieldWithCellType(int x, int y, CellType type)
	{
		
		int xCellCoordinate = (int) Math.floor(x / xPixelCellRatio);
		int yCellCoordinate = (int) Math.floor(y / yPixelCellRatio);
		
		int cellNumber = (yCellCoordinate * xcount) + xCellCoordinate;
		System.out.println("zelle nummer:" + cellNumber);
		
		if (type == CellType.CellTypeGeneratorCell) {
			
			generators[cellNumber] = true;
			
		}else if(type == CellType.CellTypeSolidCell){
			
			walls[cellNumber] = true;
			
		}else if(type == CellType.CellTypeSimulationCell){
			
			walls[cellNumber] = false;
			
		}
		
		
	}
	
	public void mouseClickPositionSensor(int x, int y)
	{
		
		sensorActive = true;
		sensorX_Coordinate = x;
		sensorY_Coordinate = y;

	}
	

	
	public void mouseClickInFieldCreateRohr(int startX, int startY, int endX, int endY, boolean geschlossen)
	{
		
		
		for (int i = 0; i < walls.length; i++) {
			walls[i] = false;
		}
		
		
		endX = (int) Math.floor(endX / xPixelCellRatio);
		endY = (int) Math.floor(endY / yPixelCellRatio);
		
		startX =(int)Math.floor(startX / xPixelCellRatio);
		startY =(int)Math.floor(startY / yPixelCellRatio);

			
		
			for (int i = (startY * xcount) + startX; i < (startY * xcount) + endX; i++) {
				
				walls[i] = true;
			}
			
			for (int i = (endY * xcount) + startX; i <(endY * xcount) + endX; i++) {
				
				walls[i] = true;
				
			}
			
			if (geschlossen) {
				
				for (int i = (startY * xcount) + endX;  i <(endY * xcount) + endX ; i = i + xcount) {
					
					walls[i] = true;
					
				}
				
			}
			

		
	}
	
	public void mouseClickInFieldCreateNil(int startX, int startY, int endX, int endY)
	{
		//System.out.println("click x:" + x + " y:" + y);
		
		endX = (int) Math.floor(endX / xPixelCellRatio);
		endY = (int) Math.floor(endY / yPixelCellRatio);
		
		startX =(int)Math.floor(startX / xPixelCellRatio);
		startY =(int)Math.floor(startY / yPixelCellRatio);
		for (int j = startY; j < endY + 1;j++){
		for (int i = (j * xcount) + startX; i < (j * xcount) + endX; i++) {
			
			
			
		}
		}
		
		
	}
	
	public void updateCanvas()
	{


		
			int circle_left = 0;
			int circle_top = 0;
			Color circle_color = Color.red;
			Color c = Color.MAGENTA;
			
			canvasField.createBufferStrategy(2);
	        canvasField.setIgnoreRepaint(true);
			BufferStrategy bs = canvasField.getBufferStrategy();
			Graphics2D g = (Graphics2D) bs.getDrawGraphics();
			
			for (int i = 0; i < mesh.length; i++){
			
					
					if(boarder[i]){
					
						c = new Color(  Color.HSBtoRGB((float)(mesh[i]) +1.9f, (float)0.8, (float)0.8) );
						
					}else if(walls[i]){
						
						c = Color.BLACK;
						
					}else{
					
						c = new Color(  Color.HSBtoRGB((float)(mesh[i]) +1.8f, (float)0.8, (float)0.8) );
						
					}
					
					
					pixelBuffer[i] = c.getRGB();
				
			}
			
			

			
			if (scopeCanvas != null) {
				
				
				int xCellCoordinate = (int) Math.floor(sensorX_Coordinate / xPixelCellRatio);
				int yCellCoordinate = (int) Math.floor(sensorY_Coordinate / yPixelCellRatio);
				int cellNumber = (yCellCoordinate * xcount) + xCellCoordinate;
				double valueAtSensor = mesh[cellNumber];
				
				Graphics gs = scopeCanvas.getGraphics();
				int y = (int) ((scopeCanvas.getHeight()/2.0) - valueAtSensor*60f);
				Color cs = Color.red;
				gs.setColor(Color.BLACK);
				gs.copyArea(0, 0, 100, 100, -1, 0);
				gs.fillRect(99, 0, 1, 99);

				//System.out.println("pressure at sensor:" +y);
				gs.setColor(cs);
				gs.fillRect(98, y, 2, 2);
				
				
				sensorRecorder.saveValue(valueAtSensor);
				
				c = Color.ORANGE;
				pixelBuffer[cellNumber] = c.getRGB();
				
			}
			

			dbimage.setRGB(0, 0, xcount, ycount, pixelBuffer, 0, xcount);
			g.drawImage(dbimage, 0, 0, canvasField.getWidth(),canvasField.getHeight(),null);
			bs.show();
		
	}
	
	public void step()
	{
		updateMesh();
		updateCanvas();
		
	}
	
	public void updateMesh(){
		
		float north;
		float south;
		float east;
		float west;
		float center;
		
		int n;
		float k = 0.4f;
		
		
		for(int y = 1; y < ycount -1; y++){
			
			int yOffset = y * xcount;
		
			for (int x = 1; x < xcount -1; x++) 
			{
				
				n = x + yOffset;
				
				if(!walls[n]){

					center = mesh[n];
				
				
				//falls nachbarelement ein wand element ist 
				north = walls[n - xcount] ? 0 : mesh[n - xcount];
				south = walls[n + xcount] ? 0 : mesh[n + xcount];
				west = walls[n -1] ? 0 : mesh[n - 1];
				east = walls[n +1] ? 0 : mesh[n + 1];
		

				if (x == 1   ) {
					west = oldMesh[n];
		    	}else if(x == xcount - 2){
		    		east = oldMesh[n];
		    	}else if(y == 1){
		    		north = oldMesh[n];
		    	}else if(y == ycount - 2){
		    		south = oldMesh[n];
		    	}
		    

				center =  ((2.0f*center) - oldMesh[n] ) + cSquared*( north+south+east+west - (4.0f * center) )  ;
				center = center - center*damping[n]*variableDampingCoeff;
				newMesh[n] = generators[n] ? generatorFunction() : center;
				
				}
				
				oldMesh[n] = mesh[n];
				
				
			}
		}
		
		float tempMesh[] = mesh;
		mesh = newMesh;
		newMesh = tempMesh;
		
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
			updateMesh();
			if (refreshGraphicAfterSteps < 1) {
				
				updateCanvas();

				

				refreshGraphicAfterSteps = 30 - speed;
				
			}else{
				refreshGraphicAfterSteps = refreshGraphicAfterSteps - 1;
			}
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	
	public float generatorFunction(){
		
		//float nextValue = (float) (pinkNoiseGenerator.nextValue() / 2f);
		
		float nextValue = (float) (Math.sin( time) * 2);
		
		time = (float) (time + (frequency * 2 * Math.PI * timeH));
		if(time > 2 * Math.PI)
		{
			time = (float) (-1 * 2 * Math.PI);
		}
		
		return nextValue;
	}
	
	public void setFrequency(float f) {
	
		frequency = (float)f;
		
	}


	public void setSpeed(int value) {
	
		speed = (int) (50f - value/2.0f);
		
	}
	
	public void setVariableDampingValue(int value){
		
		variableDampingCoeff = (float)(value/30f);
		
	}



	
}
