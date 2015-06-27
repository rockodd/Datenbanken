import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class Frage5 {

	public static void main(String args[]) {
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
		String query = "Select to_char(reqtime, 'HH24') as Tag, Count(*) From VACATIONREQUEST Group By to_char(reqtime, 'HH24') Order By to_char(reqtime, 'HH24') ASC";

		// "Select loc.CITY, Count(*) "
		// + "From VACATIONREQUEST vaq "
		// + "INNER JOIN LOCATIONS loc "
		// + "ON CONTAINS(vaq.QUERYTEXT, LOWER(loc.CITY), 1) > 0 "
		// + "Group By loc.CITY " + "Order By Count(*) DESC;"
		// ;
		Statement stmt;

		XYSeries pop = new XYSeries("XYGraph");
		DefaultCategoryDataset dataset = new DefaultCategoryDataset( );

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
			System.out.println(query);

			ResultSet rs = stmt.executeQuery(query);
			System.out.println("sql executed");
			ResultSetMetaData rsmd = rs.getMetaData();

		//	PrintColumnTypes.printColTypes(rsmd);
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
				double a=0;
				for (int i = 1; i <= numberOfColumns; i++) {
					if (i > 1)
						System.out.print(",  ");

					String columnValue = rs.getString(i);

					
					if (i == 1)
						dataname = columnValue;
					else if(i == 2) {
						a = (Float.valueOf(columnValue)).floatValue();
						System.out.println(a);
						// jfreechart
					}
					
					

					System.out.print(columnValue);
				}
				dataset.addValue(a,"Vacationrequests",dataname);
				System.out.println("");
			}
			
			stmt.close();
			con.close();
		} catch (SQLException ex) {
			System.err.print("SQLException: ");
			System.err.println(ex.getMessage());
		}

        final JFreeChart chart = ChartFactory.createLineChart(
                "Zu welcher Uhrzeit wird der Urlaub geplant?",       // chart title
                "Uhrzeit",                    // domain axis label
                "Hits",                   // range axis label
                dataset,                   // data
                PlotOrientation.VERTICAL,  // orientation
                true,                      // include legend
                true,                      // tooltips
                false                      // urls
            );
				

		// create and display a frame...
		ChartFrame frame = new ChartFrame("Anfrage 5", chart);
		frame.pack();
		RefineryUtilities.centerFrameOnScreen(frame);
		frame.setVisible(true);
	}



	}


