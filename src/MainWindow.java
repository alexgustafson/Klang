
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JSlider;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JSeparator;


public class MainWindow {

	private JFrame frame;
	private Canvas simulationFieldCanvas;
	
	private SimulationField simulationField;
	private JTextField widthField;
	private JLabel lblHeight;
	private JTextField heightField;
	private JLabel resLabel;
	private JTextField resField;
	private JLabel lblNewLabel_1;
	private JButton runButton;
	private JRadioButton drawMode_luft;
	private JRadioButton drawMode_solid;
	private JRadioButton drawMode_gen;
	private JTextField timeField;
	private JRadioButton drawMode_rohr;
	private JRadioButton drawMode_sensor;
	private Canvas scopeCanvas;
	
	private Boolean drawModeActive = false;
	private int drawStartXCoordinate;
	private int drawStartYCoordinate;
	private JRadioButton drawMode_disturb;
	private JSlider slider_1;
	
	private JSlider frequencySlider ;
	private JSlider frequencyFineTuneSlider;
	private JTextField frequencyTextField;
	private JSlider slider;
	private JLabel lblDamping;
	private JLabel rohrLengthLabel;
	private JLabel lblFreq;
	private JLabel rohrFundamentalFreq;
	private JCheckBox chckbxGeschlossen;
	private JButton button;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				simulationField.shutDownSimulation();
			}

		});
		frame.setBounds(100, 100, 787, 554);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		simulationFieldCanvas = new Canvas();
		simulationFieldCanvas.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent event) {
				mouseMovedOverCanvas(event);
			}

			@Override
			public void mouseDragged(MouseEvent event) {
				mouseDraggedOverCanvas(event);
			}


		});
		simulationFieldCanvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				
				mouseClickInSimulationField(event);
				
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				
				
				
			}
		});
		simulationFieldCanvas.setBackground(Color.DARK_GRAY);
		simulationFieldCanvas.setBounds(176, 10, 600, 360);
		frame.getContentPane().add(simulationFieldCanvas);
		
		runButton = new JButton("run");
		runButton.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				runSimulation();
				
			}


		});
		runButton.setBackground(Color.GREEN);
		runButton.setBounds(6, 497, 117, 29);
		frame.getContentPane().add(runButton);
		
		JButton btnSetupSim = new JButton("setup sim");
		btnSetupSim.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		btnSetupSim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				setUpSimulationField();
			}
		});
		btnSetupSim.setBounds(81, 161, 87, 29);
		frame.getContentPane().add(btnSetupSim);
		
		JButton btnStep = new JButton("step");
		btnStep.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		btnStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				stepSimulation();
			}
		});
		btnStep.setBounds(6, 470, 117, 29);
		frame.getContentPane().add(btnStep);
		
		widthField = new JTextField();
		widthField.setFont(new Font("Lucida Grande", Font.PLAIN, 10));

		widthField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				
				
				
			}
			@Override
			public void keyReleased(KeyEvent e) {
				
				if (widthField.getText() != "") {
					heightField.setText( Integer.toString( (Integer.parseInt(widthField.getText()) * 36 / 60) ));
				}
			}
		});
		widthField.setText("60");
		widthField.setBounds(81, 30, 87, 28);
		frame.getContentPane().add(widthField);
		widthField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("width cm");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		lblNewLabel.setBounds(14, 36, 61, 16);
		frame.getContentPane().add(lblNewLabel);
		
		lblHeight = new JLabel("height cm");
		lblHeight.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		lblHeight.setBounds(13, 72, 78, 16);
		frame.getContentPane().add(lblHeight);
		
		heightField = new JTextField();
		heightField.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		heightField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				
				if (heightField.getText() != "") {
					widthField.setText( Integer.toString( (Integer.parseInt(heightField.getText()) * 60 / 36) ));
				}
				
			}
		});
		heightField.setText("36");
		heightField.setColumns(10);
		heightField.setBounds(81, 66, 87, 28);
		frame.getContentPane().add(heightField);
		
		resLabel = new JLabel("resolution");
		resLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		resLabel.setBounds(14, 105, 72, 16);
		frame.getContentPane().add(resLabel);
		
		resField = new JTextField();
		resField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				
				// CFL Condition : ( 343 * deltaT * 2 / deltaXY ) > 0.7071
				// http://en.wikipedia.org/wiki/Courant–Friedrichs–Lewy_condition
				
				Float deltaXY = (1/ (Float.parseFloat(resField.getText())*100) );
				timeField.setText( Float.toString(  deltaXY * 0.7f / (2.0f * 343.0f))) ;
				
			}
		});
		resField.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		resField.setToolTipText("gibt an mit wie viele zellen pro cm gerechnet werden soll ( delta x und delta y )");
		resField.setText("5");
		resField.setColumns(10);
		resField.setBounds(81, 99, 87, 28);
		frame.getContentPane().add(resField);
		
		timeField = new JTextField();
		timeField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				
				// CFL Condition : ( 343 * deltaT * 2 / deltaXY ) > 0.7071
				// http://en.wikipedia.org/wiki/Courant–Friedrichs–Lewy_condition
				
				float deltaXY = 340f*2f*( Float.parseFloat(timeField.getText() ))/0.7f ;
				resField.setText( Integer.toString( (int)Math.ceil((double)1f/(deltaXY*100)) )  );
				
			}
		});
		timeField.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		timeField.setToolTipText("gibt an mit welcher zeit schritten gerechnet werden soll ( delta t )");
		timeField.setText("0.0000020408165");
		timeField.setColumns(10);
		timeField.setBounds(81, 133, 87, 28);
		frame.getContentPane().add(timeField);
		
		lblNewLabel_1 = new JLabel("Draw Mode");
		lblNewLabel_1.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		lblNewLabel_1.setBounds(14, 196, 97, 16);
		frame.getContentPane().add(lblNewLabel_1);
		
		drawMode_luft = new JRadioButton("luft");
		drawMode_luft.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		drawMode_luft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				drawMode_luft.setSelected(true);
				drawMode_solid.setSelected(false);
				drawMode_gen.setSelected(false);
				drawMode_rohr.setSelected(false);
				drawMode_sensor.setSelected(false);
				drawMode_disturb.setSelected(false);
			}
		});
		drawMode_luft.setSelected(true);
		drawMode_luft.setBounds(14, 220, 72, 23);
		frame.getContentPane().add(drawMode_luft);
		
		drawMode_solid = new JRadioButton("wand");
		drawMode_solid.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		drawMode_solid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				drawMode_luft.setSelected(false);
				drawMode_solid.setSelected(true);
				drawMode_gen.setSelected(false);
				drawMode_rohr.setSelected(false);
				drawMode_sensor.setSelected(false);
				drawMode_disturb.setSelected(false);
			}
		});
		drawMode_solid.setBounds(14, 246, 72, 23);
		frame.getContentPane().add(drawMode_solid);
		
		drawMode_gen = new JRadioButton("generator");
		drawMode_gen.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		drawMode_gen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				drawMode_luft.setSelected(false);
				drawMode_solid.setSelected(false);
				drawMode_gen.setSelected(true);
				drawMode_rohr.setSelected(false);
				drawMode_sensor.setSelected(false);
				drawMode_disturb.setSelected(false);
			}
		});
		drawMode_gen.setBounds(14, 273, 87, 23);
		frame.getContentPane().add(drawMode_gen);
		
		drawMode_rohr = new JRadioButton("rohr");
		drawMode_rohr.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		drawMode_rohr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				drawMode_luft.setSelected(false);
				drawMode_solid.setSelected(false);
				drawMode_gen.setSelected(false);
				drawMode_rohr.setSelected(true);
				drawMode_sensor.setSelected(false);
				drawMode_disturb.setSelected(false);
			}
		});
		drawMode_rohr.setBounds(14, 321, 117, 23);
		frame.getContentPane().add(drawMode_rohr);
		
		drawMode_sensor = new JRadioButton("sensor");
		drawMode_sensor.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		drawMode_sensor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				drawMode_luft.setSelected(false);
				drawMode_solid.setSelected(false);
				drawMode_gen.setSelected(false);
				drawMode_rohr.setSelected(false);
				drawMode_sensor.setSelected(true);
				drawMode_disturb.setSelected(false);
			}
		});
		drawMode_sensor.setBounds(14, 408, 78, 23);
		frame.getContentPane().add(drawMode_sensor);
		
		drawMode_disturb = new JRadioButton("disturb");
		drawMode_disturb.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		drawMode_disturb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				drawMode_luft.setSelected(false);
				drawMode_solid.setSelected(false);
				drawMode_gen.setSelected(false);
				drawMode_rohr.setSelected(false);
				drawMode_sensor.setSelected(false);
				drawMode_disturb.setSelected(true);
				
			}
		});
		drawMode_disturb.setBounds(14, 431, 117, 23);
		frame.getContentPane().add(drawMode_disturb);
		
		JButton btnSaveMesh = new JButton("save mesh");
		btnSaveMesh.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		btnSaveMesh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

					saveMesh();

			}


		});
		btnSaveMesh.setBackground(Color.GREEN);
		btnSaveMesh.setBounds(664, 497, 117, 29);
		frame.getContentPane().add(btnSaveMesh);
		
		JButton btnLoadMesh = new JButton("load mesh");
		btnLoadMesh.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		btnLoadMesh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
	
					loadMesh();

				
			}
		});
		btnLoadMesh.setBackground(Color.GREEN);
		btnLoadMesh.setBounds(548, 497, 117, 29);
		frame.getContentPane().add(btnLoadMesh);
		
		JLabel lblTimeH = new JLabel("time h");
		lblTimeH.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		lblTimeH.setBounds(14, 139, 72, 16);
		frame.getContentPane().add(lblTimeH);
		
		scopeCanvas = new Canvas();
		scopeCanvas.setBackground(Color.BLACK);
		scopeCanvas.setBounds(676, 376, 100, 100);
		frame.getContentPane().add(scopeCanvas);
		
		frequencySlider = new JSlider();
		frequencySlider.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				if (simulationField != null) {
					setSimulationGeneratorFrequency();
				}
				
			}
		});
		
		
		frequencySlider.setMinimum(1);
		frequencySlider.setMaximum(9000);
		frequencySlider.setValue(400);
		frequencySlider.setBounds(176, 389, 246, 29);
		frame.getContentPane().add(frequencySlider);
		
		slider_1 = new JSlider();
		slider_1.addMouseMotionListener(new MouseMotionAdapter() {
			

			@Override
			public void mouseDragged(MouseEvent e) {
				
				if (simulationField != null) {
					simulationField.setSpeed(slider_1.getValue());
				}
				
			}
		});
		slider_1.setValue(100);
		slider_1.setMinimum(1);
		slider_1.setMaximum(200);
		slider_1.setBounds(419, 389, 246, 29);
		frame.getContentPane().add(slider_1);
		
		JLabel lblNewLabel_2 = new JLabel("Generator Frequency Hz");
		lblNewLabel_2.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		lblNewLabel_2.setBounds(186, 376, 150, 16);
		frame.getContentPane().add(lblNewLabel_2);
		
		frequencyFineTuneSlider = new JSlider();
		frequencyFineTuneSlider.setValue(100);
		frequencyFineTuneSlider.setMaximum(200);
		frequencyFineTuneSlider.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				
				if (simulationField != null) {
					
					
					setSimulationGeneratorFrequency();
				}
				
			}
		});
		frequencyFineTuneSlider.setBounds(175, 430, 246, 29);
		frame.getContentPane().add(frequencyFineTuneSlider);
		
		JLabel lblSimulationSpeed = new JLabel("Simulation Speed");
		lblSimulationSpeed.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		lblSimulationSpeed.setBounds(431, 376, 170, 16);
		frame.getContentPane().add(lblSimulationSpeed);
		
		frequencyTextField = new JTextField();
		frequencyTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				
				if (simulationField != null) {
					simulationField.setFrequency(Float.parseFloat(frequencyTextField.getText()));
				}
				
				
			}
		});
		frequencyTextField.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		frequencyTextField.setBounds(323, 458, 84, 28);
		frame.getContentPane().add(frequencyTextField);
		frequencyTextField.setColumns(10);
		
		JLabel lblFine = new JLabel("Fine");
		lblFine.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		lblFine.setBounds(186, 421, 150, 16);
		frame.getContentPane().add(lblFine);
		
		slider = new JSlider();
		slider.setMaximum(50);
		slider.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				
				simulationField.setVariableDampingValue(slider.getValue()*20);
			}
		});
		slider.setMinimum(0);
		slider.setValue(0);
		slider.setBounds(419, 434, 246, 29);
		frame.getContentPane().add(slider);
		
		lblDamping = new JLabel("Damping");
		lblDamping.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		lblDamping.setBounds(429, 421, 150, 16);
		frame.getContentPane().add(lblDamping);
		
		chckbxGeschlossen = new JCheckBox("Geschlossen");
		chckbxGeschlossen.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		chckbxGeschlossen.setBounds(34, 343, 97, 23);
		frame.getContentPane().add(chckbxGeschlossen);
		
		JLabel lblLngeCm = new JLabel("l\u00E4nge cm");
		lblLngeCm.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		lblLngeCm.setBounds(44, 371, 61, 16);
		frame.getContentPane().add(lblLngeCm);
		
		rohrLengthLabel = new JLabel("0");
		rohrLengthLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		rohrLengthLabel.setHorizontalAlignment(SwingConstants.LEFT);
		rohrLengthLabel.setBounds(105, 370, 61, 16);
		frame.getContentPane().add(rohrLengthLabel);
		
		lblFreq = new JLabel("freq Hz");
		lblFreq.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		lblFreq.setBounds(44, 393, 61, 16);
		frame.getContentPane().add(lblFreq);
		
		rohrFundamentalFreq = new JLabel("0");
		rohrFundamentalFreq.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		rohrFundamentalFreq.setHorizontalAlignment(SwingConstants.LEFT);
		rohrFundamentalFreq.setBounds(105, 391, 61, 16);
		frame.getContentPane().add(rohrFundamentalFreq);
		
		JButton btnClear = new JButton("clear");
		btnClear.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				clearGenerators();
				
			}
		});
		btnClear.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		btnClear.setBounds(96, 271, 72, 29);
		frame.getContentPane().add(btnClear);
		
		button = new JButton("clear");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				
				clearWalls();
				
			}


		});
		button.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		button.setBounds(96, 244, 72, 29);
		frame.getContentPane().add(button);
		
		final JCheckBox chckbxPinkNoise = new JCheckBox("pink noise");
		chckbxPinkNoise.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				
				simulationField.setGeneratorToPinkNoise(chckbxPinkNoise.isSelected());
				
			}
		});
		chckbxPinkNoise.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		chckbxPinkNoise.setBounds(14, 297, 87, 23);
		frame.getContentPane().add(chckbxPinkNoise);
		
		final JButton btnRec = new JButton("rec");
		btnRec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if (simulationField != null) {
					if (simulationField.recording){
						btnRec.setText("rec");
						btnRec.setForeground(Color.black);
					}else{
						btnRec.setText("stop");
						btnRec.setForeground(Color.red);
					}
					simulationField.recSensorData();
				}
				
			}
		});
		btnRec.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		btnRec.setBounds(96, 406, 72, 29);
		frame.getContentPane().add(btnRec);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(13, 187, 154, 12);
		frame.getContentPane().add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(16, 457, 154, 12);
		frame.getContentPane().add(separator_1);
		
		JLabel lblSimSetup = new JLabel("Sim Setup");
		lblSimSetup.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		lblSimSetup.setBounds(12, 10, 97, 16);
		frame.getContentPane().add(lblSimSetup);

	}
	

	private void setUpSimulationField()
	{
		if (simulationField != null) {
			simulationField.shutDownSimulation();
		}
		
		simulationField = new SimulationField();
		simulationField.createNewField(Integer.parseInt(widthField.getText()), 
				Integer.parseInt(heightField.getText()), 
				Integer.parseInt(resField.getText()),
				Float.parseFloat(timeField.getText()));
		simulationField.attatchCanvasElement(simulationFieldCanvas);
		simulationField.updateCanvas();
		setSimulationGeneratorFrequency();
		
	}
	
	private void mouseClickInSimulationField(MouseEvent event)
	{
		if(simulationField == null){
			return;
		}
		
		if(drawMode_luft.isSelected()){
			
			simulationField.mouseClickInFieldWithCellType(event.getX(), event.getY(), CellType.CellTypeSimulationCell);
		}else if (drawMode_solid.isSelected()) {
			simulationField.mouseClickInFieldWithCellType(event.getX(), event.getY(), CellType.CellTypeSolidCell);
		}else if (drawMode_gen.isSelected()) {
			simulationField.mouseClickInFieldWithCellType(event.getX(), event.getY(), CellType.CellTypeGeneratorCell);
		}else if (drawMode_rohr.isSelected()){
			
			if (drawModeActive) {
				
				drawModeActive = false;
				simulationField.mouseClickInFieldCreateRohr(drawStartXCoordinate,drawStartYCoordinate, event.getX(), event.getY(), chckbxGeschlossen.isSelected() );
				
				float length =  (event.getX() - drawStartXCoordinate)*Float.parseFloat(widthField.getText())/simulationFieldCanvas.getWidth();
				
				rohrLengthLabel.setText(Float.toString( length   ));
				
				
				float frequency = chckbxGeschlossen.isSelected() ?  343.0f /  (4*length/100 )  :  343.0f /  (2*length/100 )  ;
				rohrFundamentalFreq.setText(Float.toString( frequency ));
				
			}else{
				
				drawModeActive = true;
				drawStartXCoordinate = event.getX();
				drawStartYCoordinate = event.getY();
				
			}
			
		}else if (drawMode_sensor.isSelected()){
			
			simulationField.setScopeCanvas(scopeCanvas);
			simulationField.mouseClickPositionSensor(event.getX(), event.getY());
			
		}else if (drawMode_disturb.isSelected()){
			
			simulationField.mouseClickInField(event.getX(),	event.getY());

		}
		
	}
	
	private void mouseMovedOverCanvas(MouseEvent event) {
		
		if(simulationField == null){
			return;
		}
		
		if (drawModeActive) {
			
			Graphics g = simulationFieldCanvas.getGraphics();
			Color c = Color.gray;
			
			g.setColor(c);
			g.drawRect(drawStartXCoordinate, drawStartYCoordinate, event.getX() - drawStartXCoordinate, event.getY() - drawStartYCoordinate);
			
			float length =  (event.getX() - drawStartXCoordinate)*Float.parseFloat(widthField.getText())/simulationFieldCanvas.getWidth();
			
			rohrLengthLabel.setText(Float.toString( length   ));
			
			
			float frequency = chckbxGeschlossen.isSelected() ?  343.0f /  (4*length/100 )  :  343.0f /  (2*length/100 )  ;
			rohrFundamentalFreq.setText(Float.toString( frequency ));
			
		}
		
	}
	
	private void mouseDraggedOverCanvas(MouseEvent event) {
		if(simulationField == null){
			return;
		}
		
		if (drawModeActive) {
			
			
		}
		
		if (drawMode_solid.isSelected()) {
			simulationField.mouseClickInFieldWithCellType(event.getX(), event.getY(), CellType.CellTypeSolidCell);
		}else if(drawMode_luft.isSelected()){
			simulationField.mouseClickInFieldWithCellType(event.getX(), event.getY(), CellType.CellTypeSimulationCell);
		}
		
	}
	
	
	private void stepSimulation()
	{
		if(simulationField == null){
			return;
		}
		
		simulationField.step();
	}
	
	private void runSimulation() {
		
		if(simulationField == null){
			return;
		}
		
		simulationField.runSimulation();
		if (simulationField.isRunningSim()) {
			runButton.setText("Stop");
		}else{
			runButton.setText("Run");
		}
	
	}
	
	private void setSimulationGeneratorFrequency(){
		simulationField.setFrequency(frequencySlider.getValue() - ( 100 - frequencyFineTuneSlider.getValue())/10f);
		frequencyTextField.setText(Float.toString(frequencySlider.getValue() - ( 100 - frequencyFineTuneSlider.getValue())/10f));
	}
	
	private void clearGenerators(){
		simulationField.clearGenerators();
	}
	
	private void clearWalls() {
		simulationField.clearWalls();
		
	}
	
	private void saveMesh() {
		
		JFileChooser fileChooser = new JFileChooser();
		int status = fileChooser.showSaveDialog(frame);
		if (status == JFileChooser.APPROVE_OPTION) {
			
			File selectedFile = fileChooser.getSelectedFile();
			SimulationFileHandler simFileHandler = new SimulationFileHandler();
			simFileHandler.writeToFile(selectedFile, simulationField);
		}

	}
	
	private void loadMesh() {
		
		JFileChooser fileChooser = new JFileChooser();
		int status = fileChooser.showOpenDialog(frame);
		if (status == JFileChooser.APPROVE_OPTION) {
			
			File selectedFile = fileChooser.getSelectedFile();
			SimulationFileHandler simFileHandler = new SimulationFileHandler();
			simulationField = simFileHandler.readFromFile(selectedFile);
			simulationField.initialize();
			simulationField.attatchCanvasElement(simulationFieldCanvas);
			simulationField.setScopeCanvas(scopeCanvas);
			simulationField.updateCanvas();
			
		}

	}
}
