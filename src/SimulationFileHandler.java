import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class SimulationFileHandler {

	
	public SimulationField readFromFile(File aFile)
	{
		
		FileInputStream fileIn = null;
		SimulationField simField = null;
		try {
			fileIn = new FileInputStream(aFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(fileIn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			simField = (SimulationField) in.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fileIn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
		return simField;
		

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
