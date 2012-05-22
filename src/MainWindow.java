
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import java.awt.Canvas;
import java.awt.Color;
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

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import java.awt.event.MouseMotionAdapter;


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
	private JRadioButton drawMode_nil;
	private JTextField timeField;
	private JRadioButton drawMode_rohr;
	private JRadioButton drawMode_sensor;
	private Canvas scopeCanvas;

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
		frame.setBounds(100, 100, 546, 504);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		simulationFieldCanvas = new Canvas();
		simulationFieldCanvas.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent event) {
				mouseMovedOverCanvas(event);
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
		simulationFieldCanvas.setBounds(176, 10, 360, 360);
		frame.getContentPane().add(simulationFieldCanvas);
		
		runButton = new JButton("run");
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				runSimulation();
				
			}


		});
		runButton.setBackground(Color.GREEN);
		runButton.setBounds(6, 447, 117, 29);
		frame.getContentPane().add(runButton);
		
		JButton btnSetupSim = new JButton("setup sim");
		btnSetupSim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				setUpSimulationField();
			}
		});
		btnSetupSim.setBounds(6, 146, 117, 29);
		frame.getContentPane().add(btnSetupSim);
		
		JButton btnStep = new JButton("step");
		btnStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				stepSimulation();
			}
		});
		btnStep.setBounds(6, 417, 117, 29);
		frame.getContentPane().add(btnStep);
		
		widthField = new JTextField();
		widthField.setText("36");
		widthField.setBounds(83, 10, 87, 28);
		frame.getContentPane().add(widthField);
		widthField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("width");
		lblNewLabel.setBounds(16, 16, 61, 16);
		frame.getContentPane().add(lblNewLabel);
		
		lblHeight = new JLabel("height");
		lblHeight.setBounds(15, 52, 61, 16);
		frame.getContentPane().add(lblHeight);
		
		heightField = new JTextField();
		heightField.setText("36");
		heightField.setColumns(10);
		heightField.setBounds(83, 46, 87, 28);
		frame.getContentPane().add(heightField);
		
		resLabel = new JLabel("resolution");
		resLabel.setBounds(16, 85, 72, 16);
		frame.getContentPane().add(resLabel);
		
		resField = new JTextField();
		resField.setText("5");
		resField.setColumns(10);
		resField.setBounds(83, 79, 87, 28);
		frame.getContentPane().add(resField);
		
		lblNewLabel_1 = new JLabel("Draw Mode");
		lblNewLabel_1.setBounds(16, 208, 97, 16);
		frame.getContentPane().add(lblNewLabel_1);
		
		drawMode_luft = new JRadioButton("luft");
		drawMode_luft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				drawMode_luft.setSelected(true);
				drawMode_solid.setSelected(false);
				drawMode_nil.setSelected(false);
				drawMode_gen.setSelected(false);
				drawMode_rohr.setSelected(false);
				drawMode_sensor.setSelected(false);
			}
		});
		drawMode_luft.setSelected(true);
		drawMode_luft.setBounds(16, 230, 72, 23);
		frame.getContentPane().add(drawMode_luft);
		
		drawMode_solid = new JRadioButton("wand");
		drawMode_solid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				drawMode_luft.setSelected(false);
				drawMode_solid.setSelected(true);
				drawMode_nil.setSelected(false);
				drawMode_gen.setSelected(false);
				drawMode_rohr.setSelected(false);
				drawMode_sensor.setSelected(false);
			}
		});
		drawMode_solid.setBounds(16, 256, 72, 23);
		frame.getContentPane().add(drawMode_solid);
		
		drawMode_gen = new JRadioButton("generator");
		drawMode_gen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				drawMode_luft.setSelected(false);
				drawMode_solid.setSelected(false);
				drawMode_nil.setSelected(false);
				drawMode_gen.setSelected(true);
				drawMode_rohr.setSelected(false);
				drawMode_sensor.setSelected(false);
			}
		});
		drawMode_gen.setBounds(16, 283, 117, 23);
		frame.getContentPane().add(drawMode_gen);
		
		drawMode_nil = new JRadioButton("nil");
		drawMode_nil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawMode_luft.setSelected(false);
				drawMode_solid.setSelected(false);
				drawMode_nil.setSelected(true);
				drawMode_gen.setSelected(false);
				drawMode_rohr.setSelected(false);
				drawMode_sensor.setSelected(false);
			}
		});
		drawMode_nil.setBounds(16, 310, 117, 23);
		frame.getContentPane().add(drawMode_nil);
		
		drawMode_rohr = new JRadioButton("rohr");
		drawMode_rohr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				drawMode_luft.setSelected(false);
				drawMode_solid.setSelected(false);
				drawMode_nil.setSelected(false);
				drawMode_gen.setSelected(false);
				drawMode_rohr.setSelected(true);
				drawMode_sensor.setSelected(false);
			}
		});
		drawMode_rohr.setBounds(16, 334, 117, 23);
		frame.getContentPane().add(drawMode_rohr);
		
		drawMode_sensor = new JRadioButton("sensor");
		drawMode_sensor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				drawMode_luft.setSelected(false);
				drawMode_solid.setSelected(false);
				drawMode_nil.setSelected(false);
				drawMode_gen.setSelected(false);
				drawMode_rohr.setSelected(false);
				drawMode_sensor.setSelected(true);
				
			}
		});
		drawMode_sensor.setBounds(16, 359, 117, 23);
		frame.getContentPane().add(drawMode_sensor);
		
		JButton btnSaveMesh = new JButton("save mesh");
		btnSaveMesh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					saveMesh();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		btnSaveMesh.setBackground(Color.GREEN);
		btnSaveMesh.setBounds(176, 447, 117, 29);
		frame.getContentPane().add(btnSaveMesh);
		
		JButton btnLoadMesh = new JButton("load mesh");
		btnLoadMesh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					loadMesh();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnLoadMesh.setBackground(Color.GREEN);
		btnLoadMesh.setBounds(176, 417, 117, 29);
		frame.getContentPane().add(btnLoadMesh);
		
		JLabel lblTimeH = new JLabel("time h");
		lblTimeH.setBounds(16, 119, 72, 16);
		frame.getContentPane().add(lblTimeH);
		
		timeField = new JTextField();
		timeField.setText(".0001");
		timeField.setColumns(10);
		timeField.setBounds(83, 113, 87, 28);
		frame.getContentPane().add(timeField);
		
		JButton btnClearSim = new JButton("clear sim");
		btnClearSim.setBounds(6, 175, 117, 29);
		frame.getContentPane().add(btnClearSim);
		
		scopeCanvas = new Canvas();
		scopeCanvas.setBackground(Color.BLACK);
		scopeCanvas.setBounds(436, 376, 100, 100);
		frame.getContentPane().add(scopeCanvas);
		

		

	}
	
	protected void loadMesh() throws IOException, ClassNotFoundException {
		
		simulationField.readFromFile(new File("simulationField.csv"));
		simulationField.attatchCanvasElement(simulationFieldCanvas);
		simulationField.updateCanvas();

	}

	protected void saveMesh() throws IOException {

		simulationField.dumpToFile(new File("simulationField.csv"));
		
	}

	private void setUpSimulationField()
	{
		simulationField = new SimulationField();
		simulationField.createNewField(Integer.parseInt(widthField.getText()), 
				Integer.parseInt(heightField.getText()), 
				Integer.parseInt(resField.getText()),
				Float.parseFloat(timeField.getText()));
		simulationField.attatchCanvasElement(simulationFieldCanvas);
		simulationField.updateCanvas();
	}
	
	private void mouseClickInSimulationField(MouseEvent event)
	{
		if(drawMode_luft.isSelected()){
			
			simulationField.mouseClickInFieldWithCellType(event.getX(), event.getY(), CellType.CellTypeSimulationCell);
		}else if (drawMode_solid.isSelected()) {
			simulationField.mouseClickInFieldWithCellType(event.getX(), event.getY(), CellType.CellTypeSolidCell);
		}else if (drawMode_gen.isSelected()) {
			simulationField.mouseClickInFieldWithCellType(event.getX(), event.getY(), CellType.CellTypeGeneratorCell);
		}else if (drawMode_rohr.isSelected()){
			simulationField.mouseClickInFieldCreateRohr(event.getX(), event.getY());
		}else if (drawMode_sensor.isSelected()){
			simulationField.setScopeCanvas(scopeCanvas);
			simulationField.mouseClickPositionSensor(event.getX(), event.getY());
		}
		
	}
	
	private void mouseMovedOverCanvas(MouseEvent event) {
		
		simulationField.mouseMovedOverField(event.getX(), event.getY());
	}
	
	private void stepSimulation()
	{
		simulationField.step();
	}
	
	private void runSimulation() {
		
		simulationField.runSimulation();
		if (simulationField.isRunningSim()) {
			runButton.setText("Stop");
		}else{
			runButton.setText("Run");
		}
		
		
	}
}
