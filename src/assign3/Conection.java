package assign3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//import java.sql.*;

public class Conection {

	static String account = MyDBInfo.MYSQL_USERNAME;
	static String password = MyDBInfo.MYSQL_PASSWORD;
	static String server = MyDBInfo.MYSQL_DATABASE_SERVER;
	static String database = MyDBInfo.MYSQL_DATABASE_NAME;

	public static void main(String[] args) {

		try {
			Class.forName("com.mysql.jdbc.Driver");

			Connection con = DriverManager.getConnection("jdbc:mysql://"
					+ server, account, password);
			//Connection con = ConnectionPool.getInstance().getConnectionFromPool();

			Statement stmt = con.createStatement();
			stmt.executeQuery("USE " + database);
			stmt.executeUpdate("INSERT INTO metropolises VALUES ('Tbilisi', 'Europe', 1580000)");
			stmt.executeUpdate("DELETE FROM metropolises where metropolis='Tbilisi'");
			
			/*
			PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO metropolises VALUES(?,?,?)");
			preparedStatement.setString(1, "Tbilisi");
			preparedStatement.setString(2, "Europe");
			preparedStatement.setInt(3, 1580000);
			preparedStatement.execute();
			*/
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM metropolises");

			while (rs.next()) {
				String name = rs.getString("metropolis");
				long pop = rs.getLong("population");
				System.out.println(name + "\t" + pop);
			}

			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}