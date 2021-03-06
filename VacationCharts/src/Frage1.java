import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RefineryUtilities;

public class Frage1 {

	public static void main(String args[]) {

		Properties props = new Properties();
		try {
			props.load(new FileInputStream("config.properties"));
		} catch (IOException e1) {
			System.out.println("Fehler beim einlesen der Config.properties :");
			System.out.println(e1);
		}

		String url = props.getProperty("URL");
		System.out.println(url);
		Connection con;
		String query = "Select loc.CITY, Count(*) From VACATIONREQUEST vaq INNER JOIN LOCATION loc ON CONTAINS(vaq.QUERYTEXT, LOWER(loc.CITY), 1) > 0 Group By loc.CITY Order By Count(*) DESC";

		Statement stmt;
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		try {
			Class.forName(props.getProperty("DRV"));
		}
		catch (java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}
		try {
			con = DriverManager.getConnection(url, props.getProperty("USR"),
					props.getProperty("PW"));
			stmt = con.createStatement();
			System.out.println(query);

			ResultSet rs = stmt.executeQuery(query);
			System.out.println("sql executed");
			ResultSetMetaData rsmd = rs.getMetaData();
			//bis hier

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
					else {
						a = (int) (Float.valueOf(columnValue)).floatValue();
						// jfreechart
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

		JFreeChart chart = ChartFactory
				.createPieChart(
						"Welche 5 St�dte wurden am h�ufigsten im Zusammenhang mit Vacation gesucht", // Title
						pieDataset, // Dataset
						true,// legend
						true,// tooltips
						true// URL
				);

		ChartFrame frame = new ChartFrame("Anfrage 1", chart);
		frame.pack();
		RefineryUtilities.centerFrameOnScreen(frame);
		frame.setVisible(true);
	}
}
