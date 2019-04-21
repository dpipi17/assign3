package assign3;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

public class TableView extends JFrame {
	
	private static final int TEXT_SIZE = 10;
	
	// north panel variables
	JPanel fieldsPanel;
	JTextField metropolisField;
	JTextField continentField;
	JTextField populationField;
	
	// east panel variables
	JPanel eastPanel;
	JButton add;
	JButton search;
	JComboBox popBox;
	JComboBox matchType;
	
	// model instance variable
	TableModel model;
	
	public TableView() {
		super("Metropolis Viewer");
        
        model = new TableModel();
        
        // create table panel
        JPanel tablePanel = new JPanel();
        GridLayout tablePanelGrid = new GridLayout(1, 2);
        tablePanelGrid.setHgap(5);
        tablePanel.setLayout(tablePanelGrid);
        
        //create table
        JTable table = new JTable(model);
        JScrollPane srcTableScroll = new JScrollPane(table, 
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tablePanel.add(srcTableScroll);
        
        
        // create north textFields area;
        createNorthArea();
        
        // create east buttons and comboBox area;
        createEastArea();
        
        // add all panels 
        add(fieldsPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);
        
        // add listeners
        addListeners();
        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true); 
	}
	
	// adds listeners to make changes on window
	// listens to search and add buttons
	private void addListeners() {
		search.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String metropolis = metropolisField.getText();
				String continent = continentField.getText();
				String population = populationField.getText();
				boolean larger = popBox.getSelectedItem().equals("Poplation Larger Than");
				boolean exact = matchType.getSelectedItem().equals("Exact Match");
				
				model.search(metropolis, continent, population, larger, exact);
			}
		});
		
		add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String metropolis = metropolisField.getText();
				String continent = continentField.getText();
				String population = populationField.getText();
				
				
				model.add(metropolis, continent, population);
			}
		});
	}


	// adds text fields on the top of the window
	private void createNorthArea() {
		fieldsPanel = new JPanel();
        
        fieldsPanel.add(new JLabel("Metropolis:"));
        metropolisField = new JTextField(TEXT_SIZE);
        fieldsPanel.add(metropolisField);  
        
        fieldsPanel.add(new JLabel("Continent:"));
        continentField = new JTextField(TEXT_SIZE);
        fieldsPanel.add(continentField);
        
        fieldsPanel.add(new JLabel("Population:"));
        populationField = new JTextField(TEXT_SIZE);        
        fieldsPanel.add(populationField);
	}
	
	// adds JButtons and JCombo Boxes on the right side of the window
	private void createEastArea() {
        eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
       
        // create button panel
        JPanel buttonPanel = new JPanel();
        GridLayout grid = new GridLayout(4, 1);
        grid.setVgap(10);
        buttonPanel.setLayout(grid);
        
        add = new JButton("Add");
        search = new JButton("Search");
        
        buttonPanel.add(add);
        buttonPanel.add(search);
           
        // create search options panel
        JPanel searchOptions = new JPanel();
        //searchOptions.setLayout(new BoxLayout(searchOptions, BoxLayout.Y_AXIS));
        searchOptions.setLayout(grid);
        searchOptions.setBorder(new TitledBorder("Search Options"));
        
        popBox = new JComboBox();
        popBox.addItem("Poplation Larger Than");
        popBox.addItem("Smaller Than or equal to");
        searchOptions.add(popBox);
        
        matchType = new JComboBox();
        matchType.addItem("Exact Match");
        matchType.addItem("Partial Match");
        searchOptions.add(matchType);
       
        eastPanel.add(buttonPanel);
        eastPanel.add(searchOptions); 
	}

	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {
		
		}
		
		TableView frame = new TableView();
	}

}
