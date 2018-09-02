package gtu.jfreechart;

import java.awt.Color;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.Dataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

public class TimeSeriesChartDef implements ChartService {
    public String getChartTitle() {
        return "Monthly Premium";
    }

    public Dataset getDataSet() throws Exception {
        TimeSeries s1 = new TimeSeries("Monthly AFYP", Month.class);
        s1.add(new Month(1, 2008), 137.8);
        s1.add(new Month(2, 2008), 140.8);
        s1.add(new Month(3, 2008), 150.3);
        s1.add(new Month(4, 2008), 153.8);
        s1.add(new Month(5, 2008), 167.6);
        s1.add(new Month(6, 2008), 158.8);
        s1.add(new Month(7, 2008), 148.3);
        s1.add(new Month(8, 2008), 153.9);
        s1.add(new Month(9, 2008), 142.7);
        s1.add(new Month(10, 2008), 130.2);
        s1.add(new Month(11, 2008), 135.8);
        s1.add(new Month(12, 2008), 139.6);
        s1.add(new Month(1, 2009), 145.9);
        s1.add(new Month(2, 2009), 155.7);
        s1.add(new Month(3, 2009), 165.3);
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        dataset.setDomainIsPointsInTime(true);
        return dataset;
    }

    public JFreeChart getJFreeChart(Dataset dataset, String chartTitle) throws Exception {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle, // title
                "Date", // x-axis label
                "Premium(K$)", // y-axis label
                (XYDataset) dataset, // data
                true, // create legend?
                true, // generate tooltips?
                false // generate URLs?
                );
        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            // renderer.setDefaultShapesVisible(true);
            // renderer.setDefaultShapesFilled(true);
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
        }
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("MM-yyyy"));
        return chart;
    }

    public float getQuality() {
        return 100;
    }

    public int getWidth() {
        return 330;
    }

    public int getHeight() {
        return 300;
    }

    public boolean hasData() {
        return true;
    }
}