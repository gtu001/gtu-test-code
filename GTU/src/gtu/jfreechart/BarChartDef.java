package gtu.jfreechart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;

public class BarChartDef implements ChartService {
    public String getChartTitle() {
        return "Newbiz Productivity Feb 2009";
    }

    public Dataset getDataSet() throws Exception {
        // row keys...
        String series1 = "Registration";
        String series2 = "Proofing";
        String series3 = "Underwriting";
        // column keys...
        String category1 = "Ernest";
        String category2 = "Eric";
        String category3 = "Chungang";
        String category4 = "Blade";
        String category5 = "Bruce";
        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1.0, series1, category1);
        dataset.addValue(4.0, series1, category2);
        dataset.addValue(3.0, series1, category3);
        dataset.addValue(5.0, series1, category4);
        dataset.addValue(5.0, series1, category5);
        dataset.addValue(5.0, series2, category1);
        dataset.addValue(7.0, series2, category2);
        dataset.addValue(6.0, series2, category3);
        dataset.addValue(8.0, series2, category4);
        dataset.addValue(4.0, series2, category5);
        dataset.addValue(4.0, series3, category1);
        dataset.addValue(3.0, series3, category2);
        dataset.addValue(2.0, series3, category3);
        dataset.addValue(3.0, series3, category4);
        dataset.addValue(6.0, series3, category5);
        return dataset;
    }

    public JFreeChart getJFreeChart(Dataset dataset, String chartTitle) throws Exception {
        return ChartFactory.createBarChart(chartTitle, // chart title
                "User", // domain axis label
                "Policy Amount", // range axis label
                (CategoryDataset) dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
                );
    }

    public float getQuality() {
        return 100;
    }

    public int getWidth() {
        return 660;
    }

    public int getHeight() {
        return 230;
    }

    public boolean hasData() {
        return true;
    }
}