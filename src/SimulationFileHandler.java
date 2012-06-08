import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class SimulationFileHandler {

	
	public void readFromFile(File aFile)
	{
		String thisLine;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(aFile));
			
			//SimulationField simulationField = new SimulationField();
			
			String delim = "[,]";
			String[] tokens = br.readLine().split(delim);
			String x = tokens[0];
			String y = tokens[1];
			String res = tokens[2];
			String tH = tokens[3];
			
			createNewField(Integer.parseInt(x) / Integer.parseInt(res),
					Integer.parseInt(y) / Integer.parseInt(res), 
					Integer.parseInt(res), 
					Float.parseFloat(tH));
			
			int i = 0;
			Cell cell;
			/*
			while ((thisLine = br.readLine()) != null && i < field.size()) { 
      
		         cell = field.get(i);
		         
		         tokens = thisLine.split(delim);
		         
		         if (tokens.length < 1) {
					return;
				}
		         
		         if (tokens[0].equalsIgnoreCase("CellTypeSimulationCell")) {
					cell.setDisplacement(Double.parseDouble(tokens[1]));
				 }else if (tokens[0].equalsIgnoreCase("CellTypeSolidCell")) {
					cell.setCellType(CellType.CellTypeSolidCell);
				}else if (tokens[0].equalsIgnoreCase("CellTypeGeneratorCell")) {
					cell.setCellType(CellType.CellTypeGeneratorCell);
				}else if (tokens[0].equalsIgnoreCase("CellTypeBeyondEdgeCell")) {
					cell.setCellType(CellType.CellTypeBeyondEdgeCell);
				}
		         
		         i++;
		         
		       } // end while 
			*/
			br.close();
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}
	
	public void dumpToFile(File aFile)
	{
		
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(aFile));
			
			//output.write("" + xcount + "," + ycount + "," + resolution + "," + timeH);
			output.newLine();
			/*
			for (Cell cell : field) {
				output.write( cell.getCellType().toString() + "," + cell.getDisplacement(0.0) );
				output.newLine();
			}
			*/
			
			output.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
}
