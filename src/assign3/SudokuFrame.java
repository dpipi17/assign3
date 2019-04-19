package assign3;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


 public class SudokuFrame extends JFrame {
	
	public SudokuFrame() {
		super("Sudoku Solver");
		
		JComponent content = (JComponent) getContentPane();
		content.setLayout(new BorderLayout(4, 4));
		
		
		JComponent areas = new JPanel();
		areas.setLayout(new BoxLayout(areas, BoxLayout.X_AXIS));
		content.add(areas);
		
		JTextArea puzzleArea = new JTextArea(15, 20);
		puzzleArea.setBorder(new TitledBorder("Puzzle"));
		areas.add(puzzleArea, BorderLayout.CENTER);
		
		JTextArea resultsArea = new JTextArea(15, 20);
		resultsArea.setBorder(new TitledBorder("Solution"));
		areas.add(resultsArea, BorderLayout.EAST);
		
		
		JPanel checkers = new JPanel();
		checkers.setLayout(new BoxLayout(checkers, BoxLayout.X_AXIS));
		JButton check = new JButton("Check");
		JCheckBox autoCheck = new JCheckBox("Auto Check", true);
		checkers.add(check);
		checkers.add(autoCheck);	
		content.add(checkers, BorderLayout.SOUTH);
		
		// Could do this:
		setLocationByPlatform(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	
	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		SudokuFrame frame = new SudokuFrame();
	}

}
