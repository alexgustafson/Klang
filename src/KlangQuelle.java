import com.jsyn.unitgen.UnitOscillator;


public class KlangQuelle extends UnitOscillator {

	private double sampleCount = 0;
	
	@Override
	public void generate(int start, int limit) {
		
		double[] outputs = output.getValues();
		for( int i = start; i < limit; i++)
		{
			

				// test
				outputs[i] = Math.sin( sampleCount/200.0 * Math.sin( sampleCount*2.0)  ) ;
				sampleCount++;

			
		}

	}

}
