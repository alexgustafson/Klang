import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.Serializable;


public class SimulationField implements Runnable, Serializable{

	float cSquared;
	int stepCount;

	private transient Thread runner;
	private Boolean runningSim;

	float oldMesh[];
	float mesh[];
	float newMesh[];
	float damping[];
	float pressure[];
	boolean walls[];
	boolean boarder[];
	boolean generators[];


	private transient BufferedImage dbimage;

	private int pixelBuffer[];
	private int refreshGraphicAfterSteps = 0;
	private int resolution;
	private int xcount;
	private int ycount;
	private int cellCount;
	private int pixelCount;

	private transient Canvas canvasField;
	private float xPixelCellRatio;
	private float yPixelCellRatio;

	private transient Canvas scopeCanvas;

	private float timeH;
	private float spaceH;
	private float time;

	private transient SensorRecorder sensorRecorder;
	private int sensorPosition;


	transient PinkNoise pinkNoiseGenerator;
	float frequency;
	private int speed;
	float dampingValue = 0.00006f;
	float variableDampingCoeff;
	int boarderWidth = 35;
	private boolean pinkNoise;
	
	private float audioRecordingTimeStep = 1f/44100f;
	private float audioRecordingTime = 0;
	public boolean recording = false;
	private int skipFrame;
	private int sleepTIme;
	private int sleepTime;
	private float b1;
	private float a0;
	private float a1;
	private float prevSampleValue;
	private float prevCalculatedSampleValue;

	public void createNewField(int sizex_cm, int sizey_cm, int resolution, float timeH)
	{
		this.resolution = resolution;
		this.timeH = timeH;
		
		SetLPF();

		spaceH = (float) (1.0 / ( 100.0 * resolution));

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
		pressure = new float[cellCount];

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


		if ((340*timeH*2/spaceH) > 0.7071) {
			System.out.println("time step is too large");
		}

		pinkNoiseGenerator = new PinkNoise();
		frequency = 440f;
	}
	
	public void initialize(){
		pinkNoiseGenerator = new PinkNoise();
		recording = false;
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
		mesh[cellNumber] = 0.01f;
		oldMesh[cellNumber] = 0.01f;

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


		int xCellCoordinate = (int) Math.floor(x / xPixelCellRatio);
		int yCellCoordinate = (int) Math.floor(y / yPixelCellRatio);
		sensorPosition = (yCellCoordinate * xcount) + xCellCoordinate;

	}



	public void mouseClickInFieldCreateRohr(int startX, int startY, int endX, int endY, boolean geschlossen)
	{


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


		Color c = Color.MAGENTA;

		canvasField.createBufferStrategy(2);
		canvasField.setIgnoreRepaint(true);
		BufferStrategy bs = canvasField.getBufferStrategy();
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();

		for (int i = 0; i < mesh.length; i++){


			if(boarder[i]){

				//c = new Color(  Color.HSBtoRGB((float)(pressure[i] / 4000f) +1.82f, (float)0.8, (float)0.8) );
				c = new Color(  Color.HSBtoRGB((float)(mesh[i] / 3f ) +1.82f, (float)0.8, (float)0.8) );

			}else if(walls[i]){

				c = Color.BLACK;

			}else{

				c = new Color(  Color.HSBtoRGB((float)(mesh[i] / 2.5f) +1.8f, (float)0.8, (float)0.8) );
				//c = new Color(  Color.HSBtoRGB((float)(pressure[i] / 4000f) +1.8f, (float)0.8, (float)0.8) );

			}


			pixelBuffer[i] = c.getRGB();

		}




		if (scopeCanvas != null) {

			double valueAtSensor = mesh[sensorPosition];

			Graphics gs = scopeCanvas.getGraphics();
			int y = (int) ((scopeCanvas.getHeight()/2.0) - valueAtSensor*60f);
			Color cs = Color.red;
			gs.setColor(Color.BLACK);
			gs.copyArea(0, 0, 100, 100, -1, 0);
			gs.fillRect(99, 0, 1, 99);

			gs.setColor(cs);
			gs.fillRect(98, y, 2, 2);




			c = Color.ORANGE;
			pixelBuffer[sensorPosition] = c.getRGB();

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


					if (x < 2   ) {
						west = oldMesh[n];
					}else if(x > xcount - 2){
						east = oldMesh[n];
					}else if(y < 2){
						north = oldMesh[n];
					}else if(y > ycount - 2){
						south = oldMesh[n];
					}

					//pressure[n] = ((south - north)+(east-west))/(2*timeH);
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

		audioRecordingTime = audioRecordingTime + timeH;
		
		if (audioRecordingTime > audioRecordingTimeStep && recording){
			
			audioRecordingTime = 0;
			//sensorRecorder.saveValue(pressure[sensorPosition]/ 4000f);
			sensorRecorder.saveValue(filter(mesh[sensorPosition]));
		}
		
		stepCount++;

	}

	public void runSimulation()
	{
		if (runningSim) 
		{

			runningSim = false;
		}else
		{
			
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
				refreshGraphicAfterSteps = skipFrame;

			}else{

				refreshGraphicAfterSteps = refreshGraphicAfterSteps - 1;

			}
			try {

				Thread.sleep(sleepTime);

			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
	}


	public Canvas getScopeCanvas() {
		return scopeCanvas;
	}


	public void setScopeCanvas(Canvas scopeCanvas) {
		this.scopeCanvas = scopeCanvas;
	}

	public void shutDownSimulation(){

		if (runningSim) {
			runningSim = false;
		}

		if (sensorRecorder != null) {
			sensorRecorder.close();
		}
	}

	public float generatorFunction(){
		
		if (pinkNoise) {
			return (float) (pinkNoiseGenerator.nextValue() / 2f);
		}

		float nextValue = (float) (Math.sin( time) * 2f);

		time =  (float) (time + (frequency * 2f * Math.PI * timeH));
		
		if(time > 2 * Math.PI)
		{
			time = (float) (-1 * 2 * Math.PI);
		}
		return nextValue;
	}

	public void setFrequency(float f) {

		frequency = f;
		System.out.println("frequency:"+frequency);

	}


	public void setSpeed(int value) {

		//speed = (int) (100f - value/2.0f);
		
		if (value > 100) {
			skipFrame = (value - 100) * 3;
			sleepTime = 0;
		}else{
			skipFrame = 0;
			sleepTime = 200 - (value * 2);
		}
		

	}

	public void setVariableDampingValue(int value){

		variableDampingCoeff = (float)(value/30f);

	}


	public void clearGenerators() {
		for (int i = 0; i < generators.length; i++) {
			generators[i] = false;
		}

	}


	public void clearWalls() {
		for (int i = 0; i < walls.length; i++) {
			walls[i] = false;
		}
	}


	public void setGeneratorToPinkNoise(boolean selected) {

		pinkNoise = selected;

	}
	
	public void recSensorData(){
		
		if(recording){
			
			recording = false;
			sensorRecorder.stop();
			
		}else{
			
			sensorRecorder = new SensorRecorder();
			recording = true;
			
		}
		
	}
	
	void SetLPF()
	{
		float fCut = 44100/4;
	    float w = 2.0f * 44100;
	    float Norm;

	    fCut *= 2.0F * Math.PI;
	    Norm = 1.0f / (fCut + w);
	    b1 = (w - fCut) * Norm;
	    a0 = a1 = fCut * Norm;
	}
	
	float filter(float sampleValue){
		
		//out[n] = in[n]*a0 + in[n-1]*a1 + out[n-1]*b1;
		float filteredValue = (sampleValue * a0) + (prevSampleValue * a1) + ( prevCalculatedSampleValue * b1);
		prevCalculatedSampleValue = filteredValue;
		prevSampleValue = sampleValue;
		return filteredValue;
	}

}
