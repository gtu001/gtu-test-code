package gtu.jfreechart;

import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;

public interface ChartService {
    String BEAN_DEFAULT = "chartAFYPPie";
    String BEAN_CHART_AFYP_SERVICE = "chartAFYPSeries";
    String BEAN_CHART_BAR_CHART = "chartBarChart";

    public JFreeChart getJFreeChart(Dataset dataset, String chartTitle) throws Exception;

    public Dataset getDataSet() throws Exception;

    public String getChartTitle();

    public float getQuality();

    public int getWidth();

    public int getHeight();

    public boolean hasData();
}