import java.sql.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RefineryUtilities;

public class connectTest {

	public static void main(String args[]) {

		String url = "jdbc:oracle:thin:@dbl43.beuth-hochschule.de:1521:oracle";
		Connection con;
		String query = "Select loc.CITY, Count(*) From VACATIONREQUEST vaq INNER JOIN LOCATIONS loc ON CONTAINS(vaq.QUERYTEXT, LOWER(loc.CITY), 1) > 0 Group By loc.CITY Order By Count(*) DESC";
				
//				"Select loc.CITY, Count(*) " 
//				+ "From VACATIONREQUEST vaq "
//				+ "INNER JOIN LOCATIONS loc "
//				+ "ON CONTAINS(vaq.QUERYTEXT, LOWER(loc.CITY), 1) > 0 "
//				+ "Group By loc.CITY " + "Order By Count(*) DESC;"
//				;
		Statement stmt;
		
		DefaultPieDataset pieDataset = new DefaultPieDataset();

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

		} catch (java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}

		try {
			con = DriverManager.getConnection(url, "ullmann", "HIGHscreen");

			stmt = con.createStatement();
			System.out.println(query);

			ResultSet rs = stmt.executeQuery(query);
			System.out.println("sql executed");
			ResultSetMetaData rsmd = rs.getMetaData();
			
			PrintColumnTypes.printColTypes(rsmd);
			System.out.println("");

			int numberOfColumns = rsmd.getColumnCount();

			for (int i = 1; i <= numberOfColumns; i++) {
				if (i > 1)
					System.out.print(",  ");
				String columnName = rsmd.getColumnName(i);
				System.out.print(columnName);
			}
			System.out.println("");
			
			String dataname = "leer";

			while (rs.next()) {
				for (int i = 1; i <= numberOfColumns; i++) {
					if (i > 1)
						System.out.print(",  ");
					
					String columnValue = rs.getString(i);

					int a = 0;
					if (i == 1) 
						dataname = columnValue;
					else{
						a = (int) (Float.valueOf(columnValue)).floatValue(); 
						//jfreechart
					}
					pieDataset.setValue(dataname, a);
					
					System.out.print(columnValue);
				}
				System.out.println("");
			}

			stmt.close();
			con.close();
		} catch (SQLException ex) {
			System.err.print("SQLException: ");
			System.err.println(ex.getMessage());
		}

	
	JFreeChart chart = ChartFactory.createPieChart("Welche 5 Städte wurden am häufigsten im Zusammenhang mit Vacation gesucht", // Title
			pieDataset, // Dataset
			true,// legend
			true,// tooltips
			true// URL
			);

	// create and display a frame...
	ChartFrame frame = new ChartFrame("Example", chart);
	frame.pack();
	RefineryUtilities.centerFrameOnScreen(frame);
	frame.setVisible(true);
	}
}

class PrintColumnTypes {

	public static void printColTypes(ResultSetMetaData rsmd)
			throws SQLException {
		int columns = rsmd.getColumnCount();
		for (int i = 1; i <= columns; i++) {
			int jdbcType = rsmd.getColumnType(i);
			String name = rsmd.getColumnTypeName(i);
			System.out.print("Column " + i + " is JDBC type " + jdbcType);
			System.out.println(", which the DBMS calls " + name);
		}
	}

}
