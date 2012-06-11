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
	FloatSample sampleBuffer;
	int samplePosition = 0;
	int bufferSize = 11025;
	VariableRateMonoReader samplePlayer;
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
		
		sampleBuffer = new FloatSample(bufferSize);
		sampleBuffer.setFrameRate(44100);
		
		
	}
	
	public void playSample(){
		
	}
	
	public void playAudio()
	{
		// Create a context for the synthesizer.
				synth = JSyn.createSynthesizer();
				synth.add( lineOut = new LineOut() );
				
				synth.start();
				

				samplePlayer = new VariableRateMonoReader();
				samplePlayer.dataQueue.queue( sampleBuffer, 0, samplePosition );
				samplePlayer.output.connect( 0, lineOut.input, 0 );
				
				synth.add( samplePlayer);
				samplePlayer.rate.set(44100);
				synth.startUnit( lineOut );
				

				lineOut.start();
				btnNewButton.setText("pause");


				// Sleep while the sound is generated in the background.
				// Sleep while the sound is generated in the background.
				try
				{
					// Wait until the sample has finished playing.
					do
					{
						synth.sleepFor( 1.0 );
					} while( samplePlayer.dataQueue.hasMore() );
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
	
	public void writeToAudioBuffer(float[] samples){
		sampleBuffer.write(samplePosition, samples, 0, 1);
		samplePosition = (samplePosition + samples.length)%bufferSize;
	}
}
