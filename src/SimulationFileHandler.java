import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;


public class SimulationFileHandler {

	
	public void readFromFile(File aFile)
	{
		String thisLine;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(aFile));
			

			br.close();
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}
	
	public void writeToFile(File aFile, SimulationField simField)
	{
		
		try {
			
			FileOutputStream fos = new FileOutputStream(aFile);
	        ObjectOutputStream oos = new ObjectOutputStream(fos);
	        oos.writeObject(simField);
	        oos.close();
	        
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
}
