import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;

import com.jsyn.util.WaveFileWriter;

public class SensorRecorder {
	
	private AudioPlayerWindow audioPlayerWindow = new AudioPlayerWindow();
	private DataOutputStream outputFile;
	public Boolean isOpen;
	WaveFileWriter writer;
	private float sampleBuffer[] = new float[1024];
	private int samplePosition = 0;
	
	public SensorRecorder(){
		
		try {
			
			Date date= new java.util.Date();
			Timestamp time = new Timestamp(date.getTime());
			File audioDataFile = new File("sensorData" + time.getTime() + ".wav");
			
			
			writer = new WaveFileWriter(audioDataFile);
			writer.setFrameRate(44100);
			writer.setSamplesPerFrame(1);
			writer.setBitsPerSample(16);
			
			//outputFile = new DataOutputStream( new BufferedOutputStream(new FileOutputStream(audioDataFile)));
			isOpen = true;
			//audioPlayerWindow.setVisible(true);
			//audioPlayerWindow.setAudioFile(audioDataFile);
			//audioPlayerWindow.setTitle(audioDataFile.getName());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void saveValue(float meshValue){
		try {
			if(isOpen){
				//outputFile.writeFloat(mesh);
				writer.write((double)meshValue);
				//audioPlayerWindow.writeToAudioBuffer(meshValue);
				
				//sampleBuffer[samplePosition] = meshValue;
				//samplePosition = samplePosition + 1;
				//if(samplePosition == sampleBuffer.length){
					//samplePosition = 0;
					//audioPlayerWindow.writeToAudioBuffer(sampleBuffer);
				//}
				
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close(){
		try {
			if (isOpen) {
				
				writer.close();
				isOpen = false;
				//audioPlayerWindow.setVisible(false);
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stop(){
		close();
	}
	
}
