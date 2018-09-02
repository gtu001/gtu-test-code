package gtu.jfreechart;

import java.awt.Color;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.annotation.Resource;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.Dataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

/**
 * TimeSeries chart service implement
 *
 * @author xinhua.zhong
 */
public class TimeSeriesChartServiceImpl implements ChartService {
    public String getChartTitle() {
        return "每月" + " " + "AFPY報表";
    }

    public Dataset getDataSet() throws Exception {
        TimeSeries s1 = new TimeSeries("每月" + " " + "AFPY報表", Month.class);
        int thisYear = chartDataHelper.getYear();
        int thisMonth = chartDataHelper.getMonth();
        Map<String, String> map = chartDataHelper.getData();
        String yearMonth = "";
        for (int j = thisMonth; j <= 12; j++) {
            if (j >= 10) {
                yearMonth = (thisYear - 1) + "" + j;
            } else {
                yearMonth = (thisYear - 1) + "0" + j;
            }
            BigDecimal bd = new BigDecimal(0);
            if (map.containsKey(yearMonth)) {
                bd = new BigDecimal((String) map.get(yearMonth)).divide(new BigDecimal(1000));
            }
            final double value = bd.doubleValue();
            s1.add(new Month(j, thisYear - 1), value);
        }
        for (int i = 1; i < thisMonth; i++) {
            if (i >= 10) {
                yearMonth = (thisYear - 1) + "" + i;
            } else {
                yearMonth = (thisYear - 1) + "0" + i;
            }
            BigDecimal bd = new BigDecimal(0);
            if (map.containsKey(yearMonth)) {
                bd = new BigDecimal((String) map.get(yearMonth)).divide(new BigDecimal(1000));
            }
            final double value = bd.doubleValue(); // 0.0;//
            s1.add(new Month(i, thisYear), value);
        }
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        dataset.setDomainIsPointsInTime(true);
        return dataset;
    }

    public JFreeChart getJFreeChart(Dataset dataset, String chartTitle) throws Exception {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle, // title
                "", // x-axis label
                "AFPY報表" + "(K" + "NT$" + ")", // y-axis
                (XYDataset) dataset, // data
                true, // create legend?
                true, // generate tooltips?
                false // generate URLs?
                );
        final XYPlot plot = chart.getXYPlot();
        // chart.set
        final ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setAutoRangeMinimumSize(1.0);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        // CategoryAxis domainAxis = plot.getDomainAxis();
        // axis.set setCategoryLabelPositions(CategoryLabelPositions
        // .createUpRotationLabelPositions(Math.PI / 6.0));
        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            // renderer.setDefaultShapesVisible(true);
            // renderer.setDefaultShapesFilled(true);
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
            renderer.setSeriesPaint(0, Color.BLUE);
        }
        final DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM"));
        // axis.set
        //
        return chart;
    }

    public float getQuality() {
        return 100;
    }

    public int getWidth() {
        return 330;
    }

    public int getHeight() {
        return 280;
    }
    
    private static class ChartDataHelper {
        Map<String, String> data;
        int year;
        int month;

        public Map<String, String> getData() {
            return data;
        }
        public void setData(Map<String, String> data) {
            this.data = data;
        }
        public int getYear() {
            return year;
        }
        public void setYear(int year) {
            this.year = year;
        }
        public int getMonth() {
            return month;
        }
        public void setMonth(int month) {
            this.month = month;
        }
    }
    
    @Resource(name="bean_ChartDataHelper")
    private ChartDataHelper chartDataHelper;

    @Override
    public boolean hasData() {
        return !chartDataHelper.getData().isEmpty();
    }
}