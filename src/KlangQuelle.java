import com.jsyn.unitgen.UnitOscillator;


public class KlangQuelle extends UnitOscillator {

	
	
	@Override
	public void generate(int start, int limit) {
		
		double[] outputs = output.getValues();
		System.out.println("start: " + start + "  limit: " + limit + " inputs:");
		for( int i = start; i < limit; i++)
		{
			

				
				outputs[i] = Math.sin( (double)i/200.0  ) * 20;
				

			
		}

	}

}
