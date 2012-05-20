
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
		frame.setBounds(100, 100, 546, 403);
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
		runButton.setBounds(6, 341, 117, 29);
		frame.getContentPane().add(runButton);
		
		JButton btnSetupSim = new JButton("setup sim");
		btnSetupSim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				setUpSimulationField();
			}
		});
		btnSetupSim.setBounds(6, 113, 117, 29);
		frame.getContentPane().add(btnSetupSim);
		
		JButton btnStep = new JButton("step");
		btnStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				stepSimulation();
			}
		});
		btnStep.setBounds(6, 315, 117, 29);
		frame.getContentPane().add(btnStep);
		
		widthField = new JTextField();
		widthField.setText("10");
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
		heightField.setText("10");
		heightField.setColumns(10);
		heightField.setBounds(83, 46, 87, 28);
		frame.getContentPane().add(heightField);
		
		resLabel = new JLabel("resolution");
		resLabel.setBounds(16, 85, 72, 16);
		frame.getContentPane().add(resLabel);
		
		resField = new JTextField();
		resField.setText("1");
		resField.setColumns(10);
		resField.setBounds(83, 79, 87, 28);
		frame.getContentPane().add(resField);
		
		lblNewLabel_1 = new JLabel("Draw Mode");
		lblNewLabel_1.setBounds(16, 154, 97, 16);
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
		drawMode_luft.setBounds(29, 182, 72, 23);
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
		drawMode_solid.setBounds(29, 208, 72, 23);
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
		drawMode_gen.setBounds(29, 235, 117, 23);
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
		drawMode_nil.setBounds(29, 262, 117, 23);
		frame.getContentPane().add(drawMode_nil);
	}
	
	private void setUpSimulationField()
	{
		simulationField = new SimulationField();
		simulationField.createNewField(Integer.parseInt(widthField.getText()), Integer.parseInt(heightField.getText()), Integer.parseInt(resField.getText()));
		simulationField.attatchCanvasElement(simulationFieldCanvas);
		simulationField.updateCanvas();
	}
	
	private void mouseClickInSimulationField(MouseEvent event)
	{
		simulationField.mouseClickInField(event.getX(), event.getY());
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
