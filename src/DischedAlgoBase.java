import java.util.ArrayList;
import java.util.Set;
import java.lang.Math;

public abstract class DischedAlgoBase {

	ArrayList<Integer> tracks;
	ArrayList<Integer> duplicate;
	ArrayList<Integer> points;
	int previous;
	int current;
	int thm;
	int seekRate;
	String thmSolution;
	String stSolution;

	public abstract ArrayList<Integer> getPoints();

	public int getTHM() {		

		ArrayList<Integer> diffs = new ArrayList<Integer>();
		thm = 0;

		thmSolution = "\nTHM Solution: ";

		diffs.add( current - points.get(0) );

		for( int i = 0; i < points.size() - 1; i++ )
			diffs.add( points.get(i) - points.get(i + 1) );

		for( int i = 0; i < diffs.size(); i++ )
			diffs.set(i, Integer.signum(diffs.get(i)) );

		int start = current;
		int end;
		int i = 0;

		for( ; i < diffs.size() - 1; i++ ) {

			if( diffs.get(i) != diffs.get(i + 1) ) {
				end = points.get(i);
				thm += (end > start) ? (end - start) : (start - end);
				
				thmSolution += (end > start) ? "( " + end + " - " + start + " ) + " : 
											"( " + start + " - " + end + " ) + ";

				start = end;
			}
		}
		end = points.get(i);
		thm += (end > start) ? (end - start) : (start - end);

		thmSolution += (end > start) ? "( " + end + " - " + start + " )" : 
									"( " + start + " - " + end + " )";

		return thm;
	}

	public String getTHMSolution() {
		return thmSolution;
	}

	public int getST() {
		return thm * seekRate;
	}

	public String getSTSolution() {
		stSolution = "\nST Solution: " + thm + " * " + seekRate;
		return stSolution;
	}
}