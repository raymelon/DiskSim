

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.UIManager.*;

import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;
import java.util.Arrays;


public class GUI 
	extends JFrame 
	implements 	ActionListener, 
				ChangeListener, 
				FocusListener, 
				Runnable {

	private ArrayList<Integer> points = new ArrayList<Integer>();

	private JPanel panelInput;

	private JPanel panelInputOthers;
	private JSpinner jsPrevious;
	private JSpinner jsCurrent;
	private JSpinner jsSeekRate;
	private JSpinner jsMaxTrack;
	private JSpinner jsTrackCount;
	private JPanel panelInputPoints;
	private ArrayList<JSpinner> listJsPoints;
	private ArrayList<JButton> listBtnPointRemove;
	
	private JPanel panelControls;
	private JPanel panelComputation;
	private JPanel panelGraph;

	private JButton btnAddInput;
	private JButton btnReset;
	private JButton btnEdit;
	private JButton btnDoGraph;
	private JButton btnAbout;

	private int spinnerCount = 0;

	private Plotter plotter;

	private SSTF generator;
	private String content;

	private JTextArea txtAreaSolution;

	private JScrollPane scr;
	private JScrollPane scrSolution;

	private Component highlighter;

	public GUI() {

		super("Disk Scheduling Algorithm Simulator");

		try {
        	UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
	    } 
	    catch (UnsupportedLookAndFeelException ex) {
	    	ex.printStackTrace();
	    }
	    catch (ClassNotFoundException ex) {
			ex.printStackTrace();
	    }
	    catch (InstantiationException ex) {
	    	ex.printStackTrace();
	    }
	    catch (IllegalAccessException ex) {
	    	ex.printStackTrace();
	    }

		initInput();
		initPlotter();
		initThm();

		setDefaultCloseOperation( EXIT_ON_CLOSE );

		setSize(700, 600);
		setMinimumSize( new Dimension(620, 500) );

		getRootPane().setDefaultButton(btnAddInput);

		setLocationRelativeTo(null);
		setFocusable(true);
		setVisible(true);

	}

	private void initInput() {

		panelInputPoints = new JPanel();

		JScrollPane scr = new JScrollPane(
			panelInputPoints, 
			JScrollPane.VERTICAL_SCROLLBAR_NEVER, 
			JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
		);

		listJsPoints = new ArrayList<JSpinner>();
		listBtnPointRemove = new ArrayList<JButton>();

		panelInput = new JPanel();
		panelInput.setLayout( new BorderLayout() );
		panelInput.add(scr, BorderLayout.SOUTH);
		
		jsMaxTrack = new JSpinner( new SpinnerNumberModel(300, 1, 10000, 1) );	

		panelInputOthers = new JPanel();
		panelInputOthers.setLayout( new FlowLayout( FlowLayout.CENTER ) );
		
		panelInputOthers.add( new JLabel("Previous: ") );
		panelInputOthers.add( jsPrevious = new JSpinner( new SpinnerNumberModel(0, 0, (int) jsMaxTrack.getValue(), 1) ) );

		JComponent editor = jsPrevious.getEditor();
		JFormattedTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
		tf.setColumns(4);
		tf.addFocusListener(this);

		panelInputOthers.add( new JLabel("Current: ") );
		panelInputOthers.add( jsCurrent = new JSpinner( new SpinnerNumberModel(1, 1, (int) jsMaxTrack.getValue(), 1) ) );

		editor = jsCurrent.getEditor();
		tf = ((JSpinner.DefaultEditor) editor).getTextField();
		tf.setColumns(4);
		tf.addFocusListener(this);

		panelInputOthers.add( new JLabel("Seek Rate: ") );
		panelInputOthers.add( jsSeekRate = new JSpinner( new SpinnerNumberModel(1, 1, 100, 1) ) );

		editor = jsSeekRate.getEditor();
		tf = ((JSpinner.DefaultEditor) editor).getTextField();
		tf.setColumns(4);
		tf.addFocusListener(this);

		panelInputOthers.add( new JLabel("Max Track: ") );
		panelInputOthers.add( jsMaxTrack );
		jsMaxTrack.addChangeListener(this);

		editor = jsMaxTrack.getEditor();
		tf = ((JSpinner.DefaultEditor) editor).getTextField();
		tf.setColumns(4);
		tf.addFocusListener(this);

		panelInputOthers.add( new JLabel("No. Of Tracks: ") );
		panelInputOthers.add( jsTrackCount = new JSpinner( new SpinnerNumberModel(1, 1, 1000, 1) ) );
		
		editor = jsTrackCount.getEditor();
		tf = ((JSpinner.DefaultEditor) editor).getTextField();
		tf.setColumns(4);
		tf.addFocusListener(this);
		jsTrackCount.addChangeListener(this);
		
		addJSpinner();
		
		panelInput.add(panelInputOthers, BorderLayout.NORTH);

		add(panelInput, BorderLayout.NORTH);

	}

	private void initPlotter() {

		panelGraph = new JPanel();
		panelGraph.setLayout( new BorderLayout() );
		panelGraph.setBorder( BorderFactory.createEmptyBorder(10,10,10,10) );

		plotter = new Plotter();
		plotter.setMax( (int) jsMaxTrack.getValue() );

		scr = new JScrollPane(
			plotter, 
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
		);

		panelControls = new JPanel();

		btnAddInput = new JButton("Add point");
		btnAddInput.addActionListener(this);
		panelControls.add(btnAddInput);

		btnEdit = new JButton("Edit values");
		btnEdit.addActionListener(this);
		btnEdit.setEnabled(false);
		panelControls.add(btnEdit);

		btnReset = new JButton("Reset");
		btnReset.addActionListener(this);
		panelControls.add(btnReset);

		btnDoGraph = new JButton("Graph");
		btnDoGraph.addActionListener(this);
		panelControls.add(btnDoGraph);

		btnAbout = new JButton("About");
		btnAbout.addActionListener(this);
		panelControls.add(btnAbout);

		panelGraph.add(panelControls, BorderLayout.NORTH);
		panelGraph.add(scr, BorderLayout.CENTER);

		add(panelGraph, BorderLayout.CENTER);
	}

	private void initThm() {

		panelComputation = new JPanel();
		panelComputation.setLayout( new BorderLayout() );
		panelComputation.setBorder( BorderFactory.createEmptyBorder(10,10,10,10) );
		
		txtAreaSolution = new JTextArea( content );
		txtAreaSolution.setEditable(false);
		txtAreaSolution.setFont( new Font("Arial", Font.BOLD, 14) );

		scrSolution = new JScrollPane( 
			txtAreaSolution, 
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
			JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
		);
		
		panelComputation.add( scrSolution, BorderLayout.CENTER );
		add(panelComputation, BorderLayout.SOUTH);

	}

	private void addJSpinner() {

		JSpinner jsTmp = new JSpinner( new SpinnerNumberModel( spinnerCount + 2, 0, (int) jsMaxTrack.getValue(), 1) );	

		JComponent editor = jsTmp.getEditor();
		JFormattedTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
		tf.setColumns(4);

		tf.addFocusListener( this );
		jsTmp.addChangeListener( this );

		listJsPoints.add( jsTmp );

		JPanel panelTmp = new JPanel();
		panelTmp.add( listJsPoints.get( spinnerCount ) );

		listJsPoints.get( spinnerCount ).requestFocusInWindow();

		JButton btnTmp = new JButton("x");
		btnTmp.addActionListener( this );

		listBtnPointRemove.add( btnTmp );

		panelTmp.add( listBtnPointRemove.get( spinnerCount ) );
		panelInputPoints.add( panelTmp );

		spinnerCount++;
		jsTrackCount.setValue(spinnerCount);

		revalidate();
	}

	private void plot() {

		for( JButton btnPointRemove : listBtnPointRemove )
			btnPointRemove.setEnabled(false);

		for( JSpinner jsPoints : listJsPoints )
			jsPoints.setEnabled(false);

		for( Component comp : panelInputOthers.getComponents() )
			comp.setEnabled(false);

		btnAddInput.setEnabled(false);
		btnDoGraph.setEnabled(false);

		btnEdit.setEnabled(true);

		panelInputPoints.setEnabled(false);

		plotter.setStatus(true);
		points.clear();

		for(int i = 0; i < listJsPoints.size(); i++)
			points.add( (Integer) listJsPoints.get(i).getValue() );

		content = new String();

		generator = new SSTF( (int) jsPrevious.getValue(), (int) jsCurrent.getValue(), (int) jsSeekRate.getValue(), points);
		points = generator.getPoints();

		plotter.setMax( (int) jsMaxTrack.getValue() );
		plotter.setPoints( points.toArray( new Integer[ points.size() ] ), (int) jsPrevious.getValue(), (int) jsCurrent.getValue() );

		plotter.setThickness(4);

		int thm_ = generator.getTHM();
		int st = generator.getST();

		content = generator.getTHMSolution();
		content += ( "\n\nTHM: " + thm_ + "\n" );
		content += ( generator.getSTSolution() );
		content += ( "\nST: " + String.format("%,d", st) + " ms\n" );

		txtAreaSolution.setText( content );
		points.clear();
	}

	private void reset() {

		for( JButton btnPointRemove : listBtnPointRemove )
			btnPointRemove.setEnabled(true);

		for( JSpinner jsPoints : listJsPoints )
			jsPoints.setEnabled(true);

		for( Component comp : panelInputOthers.getComponents() )
			comp.setEnabled(true);

		jsCurrent.setValue(1);
		jsPrevious.setValue(0);
		jsMaxTrack.setValue(300);
		jsSeekRate.setValue(1);
		txtAreaSolution.setText("");

		btnEdit.setEnabled(false);
		btnAddInput.setEnabled(true);
		btnDoGraph.setEnabled(true);

		plotter.setStatus(false);
		panelInputPoints.removeAll();

		listBtnPointRemove.clear();
		listJsPoints.clear();
		spinnerCount = 0;
		addJSpinner();

		points.clear();

		repaint();
		revalidate();
	}

	private void editPoints() {

		for( JButton btnPointRemove : listBtnPointRemove )
			btnPointRemove.setEnabled(true);

		for( JSpinner jsPoints : listJsPoints )
			jsPoints.setEnabled(true);

		for( Component comp : panelInputOthers.getComponents() )
			comp.setEnabled(true);

		btnAddInput.setEnabled(true);
		btnDoGraph.setEnabled(true);
		btnEdit.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Object src = e.getSource();
		int index = 0;

		if( src == btnAddInput ) {
			addJSpinner();
			repaint();
		
		} else if( src == btnDoGraph ) {
			plot();
			revalidate();
			repaint();

		} else if( src == btnReset ) {
			reset();
			revalidate();
			repaint();

		} else if( src == btnEdit ) {
			editPoints();
			revalidate();
			repaint();

		} else if( src == btnAbout ) {
			JOptionPane.showMessageDialog(
				this, 
				"<html>" +
				"Disk Scheduling Algorithm Simulator<br>" +
				"by Raymel Francisco<br><br>" +
				"<a href=\"https://github.com/raymelon/DiskSim\">github.com/raymelon/DiskSim</a>" +
				"<br><br><br>" +
				"License:<br>" +
				"MIT License<br><br>" +
				"Copyright (c) 2016 Raymel Francisco<br><br>" +
				"Permission is hereby granted, free of charge, to any person obtaining a copy of this software<br>" +
				"and associated documentation files (the \"Software\"), to deal in the Software without restriction,<br>" +
				"including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,<br>" +
				"and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,<br>" +
				"subject to the following conditions:" +
				"<br><br>" +
				"The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software." +
				"<br><br>" +
				"THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,<br>" +
				"INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR<br>" +
				"PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,<br>" +
				"DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,<br>" +
				"OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE." +

				"</html>",
				
				"About", JOptionPane.INFORMATION_MESSAGE);
			
		} else if( ( index = listBtnPointRemove.indexOf(src) ) > 0 ) {

			listJsPoints.remove( listJsPoints.get( index ) );
			listBtnPointRemove.remove( listBtnPointRemove.get( index ) );
			
			panelInputPoints.remove( panelInputPoints.getComponent(index) );

			spinnerCount--;
			jsTrackCount.setValue(spinnerCount);

			revalidate();
			repaint();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		repaint();
		revalidate();
		
		int curIndex = 1;
		JSpinner jsSrc = (JSpinner) e.getSource();

		if( (curIndex = listJsPoints.indexOf( jsSrc )) > -1 
			&& curIndex < listJsPoints.size() ) {
			
			Object currentValue = jsSrc.getValue();
			
			if( curIndex == 0 && listJsPoints.get( curIndex + 1 ).getValue() == currentValue ) {
				
				JOptionPane.showMessageDialog(this, "Consecutive values cannot be equal! Please enter another value.", "Ooops!", JOptionPane.WARNING_MESSAGE);
				listJsPoints.get(1).setValue(-1);
				return;
			}
			
			if( curIndex == listJsPoints.size() - 1 && listJsPoints.get( curIndex - 1 ).getValue() == currentValue ) {
		
				JOptionPane.showMessageDialog(this, "Consecutive values cannot be equal! Please enter another value.");
				listJsPoints.get(listJsPoints.size() - 2).setValue(-1);
				return;

			} else if( curIndex == listJsPoints.size() - 1 ) {
				return;
			}

			if( listJsPoints.get( curIndex + 1 ).getValue() == currentValue ||
				listJsPoints.get( curIndex - 1 ).getValue() == currentValue ) {

				JOptionPane.showMessageDialog(this, "Consecutive values cannot be equal! Please enter another value.");
				jsSrc.setValue(-1);
				return;
			}
		}
			
		
		if( e.getSource() == jsMaxTrack ) {
			jsPrevious.setModel( new SpinnerNumberModel( (int) jsPrevious.getValue(), 0, (int) jsMaxTrack.getValue(), 1) );
			jsCurrent.setModel( new SpinnerNumberModel( (int) jsCurrent.getValue(), 1, (int) jsMaxTrack.getValue(), 1) );

			for( JSpinner track : listJsPoints )
				track.setModel( new SpinnerNumberModel( (int) track.getValue(), 0, (int) jsMaxTrack.getValue(), 1 ) );

			return;
		}

		if( e.getSource() != jsTrackCount)
			return;

		int value = (int) jsTrackCount.getValue();

		if( value > spinnerCount ) {
			for(int i = spinnerCount; i < value; i++)
				addJSpinner();

		} else if( value < spinnerCount ) {
			
			for( int index = spinnerCount - 1; index >= value; index--) {
				listJsPoints.remove( listJsPoints.get( index ) );
				listBtnPointRemove.remove( listBtnPointRemove.get( index ) );
			
				panelInputPoints.remove( panelInputPoints.getComponent(index) );

				spinnerCount--;
				jsTrackCount.setValue(spinnerCount);

				revalidate();
				repaint();
			}
		}
	}

	@Override
	public void run() {
		JTextField highlighter = (JTextField) this.highlighter;
		highlighter.setText( highlighter.getText() );
        highlighter.selectAll();
	}

	@Override
	public void focusGained(FocusEvent e) {

		highlighter = e.getComponent();
		if ( highlighter instanceof JTextField )
            SwingUtilities.invokeLater( this );
	}

	@Override
	public void focusLost(FocusEvent e) {

	}

}