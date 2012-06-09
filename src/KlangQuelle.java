import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import com.jsyn.data.FloatSample;
import com.jsyn.unitgen.UnitOscillator;
import com.jsyn.util.SampleLoader;


public class KlangQuelle extends UnitOscillator {

	private double sampleCount = 0;
	private FloatSample inputFile;
	
	@Override
	public void generate(int start, int limit) {
		
		double[] outputs = output.getValues();
		for( int i = start; i < limit; i++)
		{
			
				//float sample = inputFile.readFloat();
				// test
				try {
					outputs[i] = (double)((DataInput) inputFile).readFloat();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sampleCount++;

			
		}

	}

	public void setAudioDataFile(File audioDataFile) {
		


			try {
				inputFile = SampleLoader.loadFloatSample(audioDataFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


	}

}
