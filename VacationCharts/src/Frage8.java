import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.RefineryUtilities;

public class Frage8 {

	public static void main(String args[]) {
				
		List<String> querys = new ArrayList<String>();
		// zugriff auf config.property
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
		
		querys.add("Select Count(*) as Bus from Vacationrequest Where CONTAINS(Vacationrequest.QUERYTEXT, 'bus', 1) > 0");
		querys.add("Select Count(*) as Train from Vacationrequest Where CONTAINS(Vacationrequest.QUERYTEXT, 'train', 1) > 0");
		querys.add("Select Count(*) as Plane from Vacationrequest Where CONTAINS(Vacationrequest.QUERYTEXT, 'plane', 1) > 0");
		querys.add("Select Count(*) as Car from Vacationrequest Where CONTAINS(Vacationrequest.QUERYTEXT, 'car', 1) > 0");
		// "Select loc.CITY, Count(*) "
		// + "From VACATIONREQUEST vaq "
		// + "INNER JOIN LOCATIONS loc "
		// + "ON CONTAINS(vaq.QUERYTEXT, LOWER(loc.CITY), 1) > 0 "
		// + "Group By loc.CITY " + "Order By Count(*) DESC;"
		// ;
		Statement stmt;

		DefaultPieDataset dataset = new DefaultPieDataset();
		XYSeries pop = new XYSeries("XYGraph");
		//DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		try {
			Class.forName(props.getProperty("DRV"));

		} catch (java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}

		try {
			con = DriverManager.getConnection(url, props.getProperty("USR"),
					props.getProperty("PW"));

			stmt = con.createStatement();
			
			// for-Schleife f�r mehrere Selects
			for (String query : querys) {

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
					int a = 0;
					for (int i = 1; i <= numberOfColumns; i++) {
						if (i > 1)
							System.out.print(",  ");
						String columnName = rsmd.getColumnName(i);
						String columnValue = rs.getString(i);

//						if (i == 1)
//							dataname = columnValue;
//						else {
						a = (int) (Float.valueOf(columnValue)).floatValue();
//							// jfreechart
//						}

						System.out.print(columnValue);
						dataset.setValue(columnName,a );
					}
					System.out.println("");
					//dataset.addValue(a, query, dataname);
					
				}

			}
			stmt.close();
			System.out.println("statement closed");
			con.close();
		} catch (SQLException ex) {
			System.err.print("SQLException: ");
			System.err.println(ex.getMessage());
		}

		JFreeChart chart = ChartFactory
				.createPieChart(
						"Welche Transportmittel wurden am h�ufigsten im Zusammenhang mit Vacation gesucht", // Title
						dataset, // Dataset
						true,// legend
						true,// tooltips
						true// URL
				);
		
		/*
		JFreeChart chart = ChartFactory.createBarChart("In welchem Monat wurden die meisten Anfragen gestellt?",
		"Monate", "Hits", dataset, PlotOrientation.VERTICAL,
		true, true, true);
	*/	
		
		/*
		final JFreeChart chart = ChartFactory.createLineChart(
				"Zu welcher Uhrzeit wird der Urlaub geplant?", // chart title
				"Uhrzeit", // domain axis label
				"Hits", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls
				);
*/
		// create and display a frame...
		ChartFrame frame = new ChartFrame("Anfrage 8", chart);
		frame.pack();
		RefineryUtilities.centerFrameOnScreen(frame);
		frame.setVisible(true);
	}
}
