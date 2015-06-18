import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RefineryUtilities;

public class PieChart {

	public static void main(String[] args) {

		DefaultPieDataset pieDataset = new DefaultPieDataset();
		pieDataset.setValue("JavaWorld", 75);
		pieDataset.setValue("Other", 25);
		JFreeChart chart = ChartFactory.createPieChart("Sample Pie Chart", // Title
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