

import javax.swing.BorderFactory;
import javax.swing.border.*;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Dimension;

import javax.swing.JPanel;
import java.util.Arrays;

public class Plotter extends JPanel {

	private int startX;
	private int startY;
	private int endX;
	private int endY;
	private int thickness;
	private int previous;
	private int current;
	private Boolean isActivated = true;

	private Integer[] points;

	private int lineIncrement;

	private Disk disk;

	public Plotter() {

		super();
		setBackground(Color.WHITE);
		setBorder( BorderFactory.createLineBorder(Color.BLACK) );
	}

	public void setStatus(Boolean isActivated) {
		this.isActivated = isActivated;
	}

	public void setMax(int max) {
		disk = new Disk(max);
	}

	public void setPoints(Integer[] points, int previous, int current) {
		this.points = Arrays.copyOf(points, points.length);
		this.previous = previous;
		this.current = current;
	}

	public void setThickness(int t) {
		thickness = t;
	}

	private void drawRuler(Graphics2D g2D) {
		g2D.setStroke( new BasicStroke(thickness) );
		g2D.drawLine( 10, 20, disk.getMax() + 20, 20 );
		g2D.drawString( "0", 10, 15 );
		g2D.drawString( String.valueOf(disk.getMax()), disk.getMax(), 15 );
	}

	private void graph(Graphics2D g2D) {

		int x = 0;
		int y = 50;

		lineIncrement = 30;
		int lineRelative = 30;
		int pointRelative = lineRelative - 5;
		int stringRelative = lineRelative + 20;
		int pointThickness = thickness + 10;

		g2D.setStroke( 	new BasicStroke( 
							thickness, 
							BasicStroke.CAP_BUTT, 
							BasicStroke.JOIN_BEVEL, 
							0, 
							new float[] { 5 }, 
							0
						)
		);

		g2D.setColor(Color.BLUE);
		g2D.drawLine( previous + lineRelative, y, current + lineRelative, y + lineIncrement);
		g2D.drawString("" + previous + "     Previous Position", previous + stringRelative, y);
		g2D.fillOval( previous + pointRelative, y - 5, pointThickness, pointThickness );
		y += lineIncrement;

		g2D.drawLine( current + lineRelative, y, points[0] + lineRelative, y + lineIncrement);
		g2D.drawString("" + current + "     Current Position", current + stringRelative, y);
		g2D.fillOval( current + pointRelative, y - 5, pointThickness, pointThickness );
		y += lineIncrement;

		g2D.setStroke( new BasicStroke(thickness) );
		g2D.setColor(Color.BLACK);

		for( ; x < points.length - 1; x++, y += lineIncrement ) {
		
			g2D.drawLine( points[x] + lineRelative, y, points[x + 1] + lineRelative, y + lineIncrement);
			g2D.fillOval( points[x] + pointRelative, y - 5, pointThickness, pointThickness );
			
			g2D.drawString("" + points[x], points[x] + stringRelative, y);
		}

		g2D.drawString("" + points[x], points[x] + stringRelative, y);
		g2D.fillOval( points[x] + pointRelative, y - 5, pointThickness, pointThickness );
	}
	
	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2D = (Graphics2D) g;

		if( points != null && isActivated == true ) {
			drawRuler(g2D);
			graph(g2D);
			return;
		}

		g2D.setFont( new Font( "Arial", Font.BOLD, 48) );
		g2D.drawString( "No graph to show.", 50, 60 );
		
	}

	@Override
	public Dimension getPreferredSize() {
		int vert = 500;
		if(points != null)
			vert = (points.length * lineIncrement) + 500;

		return new Dimension( disk.getMax() + 50, vert );
	}

}