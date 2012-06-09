import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.data.FloatSample;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.UnitOscillator;
import com.jsyn.unitgen.VariableRateDataReader;
import com.jsyn.unitgen.VariableRateMonoReader;
import com.jsyn.util.SampleLoader;

import java.awt.Font;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class AudioPlayerWindow extends JFrame {

	private JPanel contentPane;
	float audioBuffer[];
	Synthesizer synth;
	UnitOscillator osc;
	FloatSample sample;
	VariableRateDataReader samplePlayer;
	LineOut lineOut;
	JButton btnNewButton;
	private File audioDataFile;

	/**
	 * Create the frame.
	 */
	public AudioPlayerWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 460, 110);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnNewButton = new JButton("Play");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				
				playAudio();
				
			}
		});
		btnNewButton.setBackground(Color.LIGHT_GRAY);
		btnNewButton.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		btnNewButton.setBounds(6, 6, 75, 74);
		contentPane.add(btnNewButton);
		
		Canvas canvas = new Canvas();
		canvas.setBackground(Color.DARK_GRAY);
		canvas.setBounds(87, 6, 364, 74);
		contentPane.add(canvas);
		
		
	}
	
	public void playSample(){
		
	}
	
	public void playAudio()
	{
		// Create a context for the synthesizer.
				synth = JSyn.createSynthesizer();
				
				// Start synthesizer using default stereo output at 44100 Hz.
				synth.start();

				// Add a tone generator.
				try {
					//outputFile = new DataOutputStream( new BufferedOutputStream(new FileOutputStream(audioDataFile)));
					sample = SampleLoader.loadFloatSample(new DataInputStream(new BufferedInputStream(new FileInputStream(audioDataFile))));
					
				} catch (IOException e1) {
					
					e1.printStackTrace();
				} catch (UnsupportedAudioFileException e1) {
					
					e1.printStackTrace();
				}
				samplePlayer = new VariableRateMonoReader();
				samplePlayer.dataQueue.queueLoop( sample, 0, sample.getNumFrames() );
				
				synth.add( samplePlayer);
				// Add a stereo audio output unit.
				synth.add( lineOut = new LineOut() );

				// Connect the oscillator to both channels of the output.
				osc.output.connect( 0, lineOut.input, 0 );
				osc.output.connect( 0, lineOut.input, 1 );
				
				

				// Set the frequency and amplitude for the sine wave.
				osc.frequency.set( 345.0 );
				osc.amplitude.set( 0.6 );

				// We only need to start the LineOut. It will pull data from the
				// oscillator.
				lineOut.start();
				btnNewButton.setText("pause");


				// Sleep while the sound is generated in the background.
				try
				{
					double time = synth.getCurrentTime();
					// Sleep for a few seconds.
					synth.sleepUntil( time + 4.0 );
				} catch( InterruptedException e )
				{
					e.printStackTrace();
				}


				// Stop everything.
				synth.stop();
				btnNewButton.setText("play");
	}

	public void setAudioFile(File audioDataFile) {
	
		this.audioDataFile = audioDataFile;
		
	}
}
