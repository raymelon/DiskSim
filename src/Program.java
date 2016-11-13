import javax.swing.*;

public class Program implements Runnable {

	@Override
	public void run() {
		GUI gui = new GUI();
	}

	public static void main(String[] args) {
		Program program = new Program();
		SwingUtilities.invokeLater( program );
	}
}
