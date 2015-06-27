import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.RefineryUtilities;

public class TimeSeriesExample {
public static void main(String[] args) {
// Create a time series chart
XYSeries pop = new XYSeries("XYGraph");
pop.add(0,0);
//pop.add(new Day(10, 2, 2004), 150);
//pop.add(new Day(10, 3, 2004), 250);
//pop.add(new Day(10, 4, 2004), 275);
//
//pop.add(new Day(10, 5, 2004), 325);
//pop.add(new Day(10, 6, 2004), 425);
TimeSeriesCollection dataset = new TimeSeriesCollection();
//dataset.addSeries(pop);
JFreeChart chart = ChartFactory.createTimeSeriesChart(
"Population of CSC408 Town",
"Date",
"Population",
dataset,
true,
true,
false);

ChartFrame frame = new ChartFrame("Example", chart);
frame.pack();
RefineryUtilities.centerFrameOnScreen(frame);
frame.setVisible(true);
}
}
