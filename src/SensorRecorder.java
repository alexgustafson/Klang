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
	
	public SensorRecorder(){
		
		try {
			
			Date date= new java.util.Date();
			Timestamp time = new Timestamp(date.getTime());
			File audioDataFile = new File("sensorData" + time.getTime());
			
			writer = new WaveFileWriter(audioDataFile);
			
			
			
			//outputFile = new DataOutputStream( new BufferedOutputStream(new FileOutputStream(audioDataFile)));
			isOpen = true;
			audioPlayerWindow.setVisible(true);
			audioPlayerWindow.setAudioFile(audioDataFile);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void saveValue(float mesh){
		try {
			if(isOpen){
				//outputFile.writeFloat(mesh);
				writer.write(mesh);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close(){
		try {
			if (isOpen) {
				outputFile.close();
				isOpen = false;
				audioPlayerWindow.setVisible(false);
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
