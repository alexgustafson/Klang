import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;


public class SensorRecorder {

	private DataOutputStream outputFile;
	public Boolean isOpen;
	
	public SensorRecorder(){
		
		try {
			
			Date date= new java.util.Date();
			Timestamp time = new Timestamp(date.getTime());
			
			outputFile = new DataOutputStream( new BufferedOutputStream(new FileOutputStream("sensorData" + time.getTime())));
			isOpen = true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void saveValue(float mesh){
		try {
			if(isOpen){
				outputFile.writeFloat(mesh);
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
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
