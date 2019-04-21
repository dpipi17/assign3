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
	private DB dataBase;
	private Connection con;
	
	private final List<String> cols;
    private final List<List<String>> tableStruct;
    
    private static final int METRO_IND = 1;
    private static final int CONT_IND = 2;
    private static final int POP_IND = 3;

    // constructor of the model Class
    public TableModel() {
        cols = new ArrayList<String>();
        tableStruct = new ArrayList<List<String>>();
        dataBase = new DB();
        	        
        addColumns();
        addFromDbToTable("SELECT * FROM metropolises");  
    }
    
    // adds all value from tableStruct into the Real Table
    private void addFromDbToTable(String query) {
    	try {
			Statement stmt;
			con = dataBase.getCon();
			stmt = con.createStatement();
			stmt.executeQuery("USE " + DB.database);
			
			ResultSet rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				String metro = rs.getString("metropolis");
				String cont = rs.getString("continent");
				String pop = String.valueOf(rs.getLong("population"));
				
				addRow(Arrays.asList(metro, cont, pop));
				
			}
			dataBase.closeCon();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    // adds column names
    private void addColumns() {
    	addColumn("Metropolis");
        addColumn("Continent");
        addColumn("population");
    }
    
    // returns row number
    @Override
    public int getRowCount() {
        return tableStruct.size();
    }

    // returns column number
    @Override
    public int getColumnCount() {
        return cols.size();
    }

    // makes table unchangeable
    @Override
    public boolean isCellEditable(int i, int j) {
        return false;
    }

    // get value from the given coordinates
    @Override
    public Object getValueAt(int i, int j) {
        List<String> row = tableStruct.get(i);
        
        if (j < row.size()) {
            return row.get(j);
        }
        
        return null;
    }
    
    // gets column name using it index
    @Override
    public String getColumnName(int i) {
        return cols.get(i);
    }


    // sets new value
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

    // adds new column 
    public void addColumn(String colName) {
    	cols.add(colName);
        fireTableStructureChanged();
    }
    
    // adds new row in the table
    public void addRow(List<String> row) {
    	tableStruct.add(row);
        fireTableRowsInserted(tableStruct.size() - 1, tableStruct.size() - 1);

    }
    
    // deletes row from the table    
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
   
	// add method
    // adds new row in the data base and calls addRow function
    // deletes other rows, because this is only row which should be visible now
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
			dataBase.closeCon();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
	}
	
	// creates String which is sql query using parameters
	private String createQuery(String metro, String cont, String pop, boolean isAllEmpty, boolean larger, boolean exact) {
		String result = "SELECT * FROM " + DB.database;
		if(isAllEmpty) return result;
		
		result += " WHERE ";
		// determines which we want exact or partial match
		if(exact) {
			result += "metropolis = '" + metro + "' AND continent = '" + cont + "'";
		} else {
			result += "metropolis LIKE '%" + metro + "%' AND continent LIKE '%" + cont + "%'";
		}
		
		result += " AND ";
		
		if(pop.isEmpty()) pop = "0";
		// determines which we want larger than pop or "equal and smaller";
		if(larger) {
			result += "population > " + Long.valueOf(pop) + ";";
		} else {
			result += "population <= " + Long.valueOf(pop) + ";";
		}
		
		return result;
	}
	
	// search function
	// calls createQuery to get sql query
	// clears all row from the tableStruct to show only that rows which are returned by query
	// calls addFromDbToTable to show query rows on the real Table
	public void search(String metro, String cont, String pop, boolean larger, boolean exact) {
		boolean isAllEmpty = metro.isEmpty() && cont.isEmpty() && pop.isEmpty();
		
		String query = createQuery(metro, cont, pop, isAllEmpty, larger, exact);
		tableStruct.clear();
		fireTableStructureChanged();
		
		addFromDbToTable(query);
	}

	
}
