package assign3;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel{

	//private instance variables
	private ResultSet result;
	private int numCol;
	private int numRow;
	private DB dataBase;
	private Connection con;
	
	//
	private static final int METRO_IND = 1;
	private static final int CONT_IND = 2;
	private static final int POP_IND = 3;
	
	
	private final List<String> cols;
    private final List<List<String>> tableStruct;

    public TableModel() {
        cols = new ArrayList<String>();
        tableStruct = new ArrayList<List<String>>();
        dataBase = new DB();
        	        
        addColumns();
        addStartingTable();  
    }
    
    private void addStartingTable() {
    	try {
			Statement stmt;
			con = dataBase.getCon();
			stmt = con.createStatement();
			stmt.executeQuery("USE " + DB.database);
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM metropolises");
			
			while (rs.next()) {
				String metro = rs.getString("metropolis");
				String cont = rs.getString("continent");
				String pop = String.valueOf(rs.getLong("population"));
				
				addRow(Arrays.asList(metro, cont, pop));
				
				System.out.println(metro + "\t" + cont + " " +  pop);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void addColumns() {
    	addColumn("Metropolis");
        addColumn("Continent");
        addColumn("population");
    }
    
    @Override
    public int getRowCount() {
        return tableStruct.size();
    }

    @Override
    public int getColumnCount() {
        return cols.size();
    }

    @Override
    public String getColumnName(int i) {
        return cols.get(i);
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int i, int j) {
        return false;
    }

    @Override
    public Object getValueAt(int i, int j) {
        List<String> row = tableStruct.get(i);
        
        if (j < row.size()) {
            return row.get(j);
        }
        
        return null;
    }

    @Override
    public void setValueAt(Object o, int i, int j) {
        List<String> row = tableStruct.get(i);
        
        if (j >= row.size()) {
            while (j >= row.size()) {
                row.add(null);
            }
        }
        
        row.set(j, (String) o);
        
        fireTableCellUpdated(i, j);
    }

    public void addColumn(String colName) {
    	cols.add(colName);
        fireTableStructureChanged();
    }
    
    public int addRow(List<String> row) {
    	//System.out.println(11111);
    	tableStruct.add(row);
        fireTableRowsInserted(tableStruct.size() - 1, tableStruct.size() - 1);
        return tableStruct.size() - 1;
    }
    
    public int addRow() {
        List<String> row = new ArrayList<String>();
        return addRow(row);
    }
    
    public void deleteRow(int rowIndex) {
        if (rowIndex < 0 || rowIndex > tableStruct.size() - 1) {
            return;
        }
        
        if (tableStruct.isEmpty()) {
            return;
        }
        
        tableStruct.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
   
	
	public void add(String metro, String cont, String pop) {
		// only add nonempty metropolis and continent rows
		if(metro.isEmpty() || cont.isEmpty()) return;
		
		try {		
			con = dataBase.getCon();
			Statement stmt = con.createStatement();
			stmt.executeQuery("USE " + DB.database);
				
			PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO " + DB.database + " VALUES(?,?,?)");
			preparedStatement.setString(METRO_IND, metro);
			preparedStatement.setString(CONT_IND, cont);
			
			// if population is empty string, should set null for that cell
			if(pop.isEmpty()) {
				preparedStatement.setNull(POP_IND, Types.BIGINT);
			} else {		
				preparedStatement.setLong(POP_IND, Long.valueOf(pop));
			}
			preparedStatement.execute();
			
			while(true) {
				deleteRow(tableStruct.size() - 1);
				if(getRowCount() == 0) break;
			}
			addRow(Arrays.asList(metro, cont, pop));			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
	}
	
	public ResultSet search(String metro, String cont, String pop, boolean larger, boolean exact) {
		
		
		return null;
	}

	
}
