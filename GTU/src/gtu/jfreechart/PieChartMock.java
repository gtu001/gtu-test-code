package gtu.jfreechart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultPieDataset;

public class PieChartMock implements ChartService {

    public String getChartTitle() {
        return "Newbiz Transaction";
    }

    public Dataset getDataSet() throws Exception {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("New Unread", 100);
        dataset.setValue("New Claimed", 200);
        dataset.setValue("Proofing", 300);
        dataset.setValue("Underwriting", 400);
        dataset.setValue("Issued", 500);
        return dataset;
    }

    public JFreeChart getJFreeChart(Dataset dataset, String chartTitle) throws Exception {
        return ChartFactory.createPieChart(chartTitle, (DefaultPieDataset) dataset, true, false, false);
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