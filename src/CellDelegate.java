import java.util.ArrayList;


public interface CellDelegate {
	/*
	 * Dieser interface definiert die Schnnittstell vom Zelle zum SimulationField
	 * Ab und zu braucht einer Zelle information �ber seiner umfeld, seiner 
	 * nachbarn und physikalische gesetzte. Alle diese abfragen werden hier
	 * definiert. Die Zelle soll nicht zu viel zugang zum SimulationsField 
	 * haben, es kann nur �ber dieser schnittstelle abfragen.
	 */
	
	public ArrayList<Cell> getNeighbors(int idNr);

}
