import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.jsyn.unitgen.UnitOscillator;


public class KlangQuelle extends UnitOscillator {

	private double sampleCount = 0;
	private DataInputStream inputFile;
	
	@Override
	public void generate(int start, int limit) {
		
		double[] outputs = output.getValues();
		for( int i = start; i < limit; i++)
		{
			
				//float sample = inputFile.readFloat();
				// test
				try {
					outputs[i] = (double)inputFile.readFloat();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sampleCount++;

			
		}

	}

	public void setAudioDataFile(File audioDataFile) {
		

		//outputFile = new DataOutputStream( new BufferedOutputStream(new FileOutputStream(audioDataFile)));
		try {
			inputFile = new DataInputStream(new BufferedInputStream(new FileInputStream(audioDataFile)));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
