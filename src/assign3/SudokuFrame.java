package assign3;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;



 public class SudokuFrame extends JFrame {
	 
	 // private instance variables
	 JTextArea puzzleArea;
	 JTextArea resultsArea;
	 JButton check;
	 JCheckBox autoCheck;
	 Sudoku sudo;
	 
	 
	// calls sudoku solve method and changes result text;
	private void solveSudoku() {
		try {
			String newText = "";
			sudo = new Sudoku(puzzleArea.getText());
			int solutions = sudo.solve();
			
			newText += sudo.getSolutionText() + "\n";
			newText += "solutions: " + solutions + "\n";
			newText += "elapsed: " + sudo.getElapsed() + "\n";
			
			resultsArea.setText(newText);
		} catch (Exception e) {

		}
	}
	
	
	public SudokuFrame() {
		super("Sudoku Solver");
		
		setLayout(new BorderLayout(4, 4));
		
		puzzleArea = new JTextArea(15, 20);
		puzzleArea.setBorder(new TitledBorder("Puzzle"));
		add(puzzleArea, BorderLayout.CENTER);
		
		resultsArea = new JTextArea(15, 20);
		resultsArea.setBorder(new TitledBorder("Solution"));
		add(resultsArea, BorderLayout.EAST);
		
		
		JPanel checkers = new JPanel();
		checkers.setLayout(new BoxLayout(checkers, BoxLayout.X_AXIS));
		check = new JButton("Check");
		autoCheck = new JCheckBox("Auto Check", true);
		checkers.add(check);
		checkers.add(autoCheck);	
		add(checkers, BorderLayout.SOUTH);
		
		addListeners();
		
		// Could do this:
		setLocationByPlatform(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	// listens events. after every events tries to solve sudoku
	private void addListeners() {
		// listens if is check button is clicked
		check.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				solveSudoku();
			}
		});
		
		
		// listens if puzzle text area changed and autoCheck is selected
		puzzleArea.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				if(autoCheck.isSelected()) {
					solveSudoku();
				}
				
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				if(autoCheck.isSelected()) {
					solveSudoku();
				}
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				if(autoCheck.isSelected()) {
					solveSudoku();
				}
			}
		});
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
