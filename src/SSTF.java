import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

public class SSTF extends DischedAlgoBase implements Comparator<Integer> {

	public SSTF(int previous, int current, int seekRate, ArrayList<Integer> tracks) {
		
		this.tracks = new ArrayList<Integer>( tracks );
		duplicate = new ArrayList<Integer>( this.tracks );

		this.previous = previous;
		this.current = current;
		this.seekRate = seekRate;
	}

	@Override // from DischedAlgoBase
	public ArrayList<Integer> getPoints() {
		
		points = new ArrayList<Integer>();
		int cur = current;

		while( !duplicate.isEmpty() ) {
			points.add( cur = getNearest( cur ) );
			duplicate.remove( new Integer(cur) );
		}
		return points;
	}

	private int getNearest(int current) {
		return Collections.min( duplicate, this ); // 'this' refers to implemented Comparator
	}

	@Override // from Comparator
	public int compare(Integer a, Integer b) {
		return Integer.compare( Math.abs(current - a), Math.abs(current - b) );
	}
}