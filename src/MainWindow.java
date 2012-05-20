
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
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;


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
		frame.setBounds(100, 100, 546, 448);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		simulationFieldCanvas = new Canvas();
		simulationFieldCanvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				
				mouseClickInSimulationField(event);
				
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
		runButton.setBounds(119, 391, 117, 29);
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
		btnStep.setBounds(6, 391, 117, 29);
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
		lblNewLabel_1.setBounds(16, 239, 97, 16);
		frame.getContentPane().add(lblNewLabel_1);
		
		drawMode_luft = new JRadioButton("luft");
		drawMode_luft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				drawMode_luft.setSelected(true);
				drawMode_solid.setSelected(false);
				drawMode_nil.setSelected(false);
				drawMode_gen.setSelected(false);
				
			}
		});
		drawMode_luft.setSelected(true);
		drawMode_luft.setBounds(29, 267, 72, 23);
		frame.getContentPane().add(drawMode_luft);
		
		drawMode_solid = new JRadioButton("wand");
		drawMode_solid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				drawMode_luft.setSelected(false);
				drawMode_solid.setSelected(true);
				drawMode_nil.setSelected(false);
				drawMode_gen.setSelected(false);
				
			}
		});
		drawMode_solid.setBounds(29, 293, 72, 23);
		frame.getContentPane().add(drawMode_solid);
		
		drawMode_gen = new JRadioButton("generator");
		drawMode_gen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				drawMode_luft.setSelected(false);
				drawMode_solid.setSelected(false);
				drawMode_nil.setSelected(false);
				drawMode_gen.setSelected(true);
			}
		});
		drawMode_gen.setBounds(29, 320, 117, 23);
		frame.getContentPane().add(drawMode_gen);
		
		drawMode_nil = new JRadioButton("nil");
		drawMode_nil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawMode_luft.setSelected(false);
				drawMode_solid.setSelected(false);
				drawMode_nil.setSelected(true);
				drawMode_gen.setSelected(false);
			}
		});
		drawMode_nil.setBounds(29, 347, 117, 23);
		frame.getContentPane().add(drawMode_nil);
		
		JButton btnSaveMesh = new JButton("save mesh");
		btnSaveMesh.setBackground(Color.GREEN);
		btnSaveMesh.setBounds(311, 391, 117, 29);
		frame.getContentPane().add(btnSaveMesh);
		
		JButton btnLoadMesh = new JButton("load mesh");
		btnLoadMesh.setBackground(Color.GREEN);
		btnLoadMesh.setBounds(423, 391, 117, 29);
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
			simulationField.mouseClickInField(event.getX(), event.getY());
		}else if (drawMode_solid.isSelected()) {
			simulationField.mouseClickInFieldWithCellType(event.getX(), event.getY(), CellType.CellTypeSolidCell);
		}else if (drawMode_gen.isSelected()) {
			simulationField.mouseClickInFieldWithCellType(event.getX(), event.getY(), CellType.CellTypeGeneratorCell);
		}
		
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
