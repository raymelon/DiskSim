public class Disk {

	private int MAX_TRACKS = 199;
	private int MIN_TRACKS = 0;

	public Disk() {
		
	}

	public Disk(int max) {
		MAX_TRACKS = max;
	}

	public Disk(int min, int max) {
		MAX_TRACKS = max;
		MIN_TRACKS = min;
	}

	public int getMin() {
		return MIN_TRACKS;
	}

	public int getMax() {
		return MAX_TRACKS;
	}
}