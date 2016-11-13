

import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.lang.Math;

//import engine.Disk;

public class SSTF implements Comparator<Integer> {

	private LinkedList<Integer> tracks;
	private LinkedList<Integer> duplicate;
	private LinkedList<Integer> points;
	private int previous;
	private int current;
	private int thm;
	private int seekRate;
	private String thmSolution;
	private String stSolution;

	public SSTF(int previous, int current, int seekRate, LinkedList<Integer> tracks) {
		
		this.tracks = new LinkedList<Integer>( tracks );
		duplicate = new LinkedList<Integer>( this.tracks );

		this.previous = previous;
		this.current = current;
		this.seekRate = seekRate;
	}

	public LinkedList<Integer> getPoints() {
		
		points = new LinkedList<Integer>();
		int cur = current;

		while( !duplicate.isEmpty() ) {
			points.add( cur = getNearest( cur ) );
			duplicate.remove( new Integer(cur) );
		}
		return points;
	}

	private int getNearest(int current) {
		return Collections.min( duplicate, this );
	}

	public int getTHM() {		

		LinkedList<Integer> diffs = new LinkedList<Integer>();
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

	@Override
	public int compare(Integer a, Integer b) {
		return Integer.compare( Math.abs(current - a), Math.abs(current - b) );
	}
}