import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

public class FCFS extends DischedAlgoBase {

	public FCFS(int previous, int current, int seekRate, ArrayList<Integer> tracks) {
		
		this.tracks = tracks;
		this.points = tracks;

		this.previous = previous;
		this.current = current;
		this.seekRate = seekRate;
	}

	@Override // from DischedAlgoBase
	public ArrayList<Integer> getPoints() {
		return points;
	}
}