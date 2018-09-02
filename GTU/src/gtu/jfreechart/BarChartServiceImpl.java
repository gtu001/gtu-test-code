package gtu.jfreechart;

import java.awt.Color;
import java.awt.GradientPaint;
import java.util.Map;

import javax.annotation.Resource;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;

/**
 * Bar chart service implement
 *
 * @author xinhua.zhong
 */
public class BarChartServiceImpl implements ChartService {
    public String getChartTitle() {
        return "生產力" + " " + chartDataHelper.thisYearMonth();
    }

    public Dataset getDataSet() throws Exception {
        // create the dataset...
        String[] taskName = ChartDataHelper.TASK_NAME;
        String[] coordinate = { "COD_T_WORKLIST_TASK_6", "COD_T_WORKLIST_TASK_10", "COD_T_WORKLIST_TASK_8", "COD_T_WORKLIST_TASK_81", "COD_T_WORKLIST_TASK_101", "COD_T_WORKLIST_TASK_121",
                "COD_T_WORKLIST_TASK_141" };
        String[] statItemType = ChartDataHelper.STAT_ITEM_TYPE;
        String[] statItem = { "MSG_205449", "MSG_109167", "MSG_901118936" };
        Integer amount;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Map<String, Integer>> map = chartDataHelper.getData();
        for (int i = 0; i < taskName.length; i++) {
            for (int j = 0; j < statItemType.length; j++) {
                amount = map.get(taskName[i]).get(statItemType[j]);
                dataset.addValue(amount, "文字1-" + j, "文字2-" + j);
            }
        }
        return dataset;
    }

    public JFreeChart getJFreeChart(Dataset dataset, String chartTitle) throws Exception {
        JFreeChart chart = ChartFactory.createBarChart(chartTitle, // chart
                // title
                "", // domain axis label
                "任務數量", // range
                        // axis
                        // label
                (CategoryDataset) dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
                );
        // set the background color for the chart...
        // chart.setBackgroundPaint(Color.white);
        // get a reference to the plot for further customisation...
        CategoryPlot plot = chart.getCategoryPlot();
        // plot.setBackgroundPaint(Color.lightGray);
        // plot.setDomainGridlinePaint(Color.white);
        // plot.setDomainGridlinesVisible(true);
        // plot.setRangeGridlinePaint(Color.white);
        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        // set up gradient paints for series...
        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue, 0.0f, 0.0f, new Color(0, 0, 64));
        GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green, 0.0f, 0.0f, new Color(0, 64, 0));
        GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red, 0.0f, 0.0f, new Color(64, 0, 0));
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);
        // CategoryAxis domainAxis = plot.getDomainAxis();
        // domainAxis.setCategoryLabelPositions(CategoryLabelPositions
        // .createUpRotationLabelPositions(Math.PI / 6.0));
        return chart;
    }

    public float getQuality() {
        return 100;
    }

    public int getWidth() {
        return 700;
    }

    public int getHeight() {
        return 200;
    }

    private static class ChartDataHelper {
        public static final String[] TASK_NAME = new String[0];
        public static final String[] STAT_ITEM_TYPE = new String[0];
        Map<String, Map<String, Integer>> data;
        int year;
        int month;

        public String thisYearMonth() {
            return "" + year + month;
        }

        public Map<String, Map<String, Integer>> getData() {
            return data;
        }

        public void setData(Map<String, Map<String, Integer>> data) {
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

    @Resource(name = "bean_ChartDataHelper")
    private ChartDataHelper chartDataHelper;

    @Override
    public boolean hasData() {
        return !chartDataHelper.getData().isEmpty();
    }
}