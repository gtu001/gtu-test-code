package gtu.jfreechart;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

/**
 * Pie chart service implement
 *
 * @author xinhua.zhong
 */
public class PieChartServiceImpl implements ChartService {
    public String getChartTitle() {
        return "AFPY報表" + " " + chartDataHelper.thisYearMonth() + "("
                + "NT$" + ")";
    }

    public Dataset getDataSet() throws Exception {
        DefaultPieDataset dataset = new DefaultPieDataset();
        List<Map<String, String>> afypData = chartDataHelper.getData();
        int index = 0;
        BigDecimal sumAfapAmount = new BigDecimal(0);
        for (Map<String, String> map : afypData) {
            index++;
            String benefitType = map.get("BENEFIT_TYPE");
            String shortBenefitName = getShortName(benefitType);
            BigDecimal afapAmount = new BigDecimal(map.get("AFYP"));
            if (index < 6) {
                dataset.setValue(shortBenefitName, afapAmount);
            } else {
                sumAfapAmount = sumAfapAmount.add(afapAmount);
            }
        }
        if (index >= 6) {
            dataset.setValue("other", sumAfapAmount);
        }
        return dataset;
    }

    public JFreeChart getJFreeChart(Dataset dataset, String chartTitle) throws Exception {
        final JFreeChart chart = ChartFactory.createPieChart3D(chartTitle, (DefaultPieDataset) dataset, true, true, false);
        final PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setSectionPaint(0, new Color(100, 149, 237));
        plot.setSectionPaint(1, new Color(255, 215, 0));
        plot.setSectionPaint(2, new Color(0, 92, 66));
        plot.setSectionPaint(3, new Color(238, 0, 66));
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        plot.setNoDataMessage("No data to display");
        return chart;
    }

    private String getShortName(String benefitType) {
        if (benefitType == null) {
            return null;
        } else if (benefitType.equals("11")) {
            return "END";
        } else if (benefitType.equals("12")) {
            return "ANN";
        } else if (benefitType.equals("13")) {
            return "WL";
        } else if (benefitType.equals("14")) {
            return "TL";
        } else if (benefitType.equals("22")) {
            return "RB";
        } else if (benefitType.equals("23")) {
            return "CB";
        } else if (benefitType.equals("31")) {
            return "AD&D";
        } else if (benefitType.equals("15")) {
            return "Mortgage";
        } else if (benefitType.equals("16")) {
            // Long Term Disability
            return "PTD";
        } else if (benefitType.equals("17")) {
            return "LA";
        } else if (benefitType.equals("41")) {
            return "ILP";
        } else if (benefitType.equals("51")) {
            return "Smart Saver";
        } else if (benefitType.equals("99")) {
            return "Others";
        } else if (benefitType.equals("42")) {
            return "VA";
        }
        return null;
    }

    public float getQuality() {
        return 100;
    }

    public int getWidth() {
        return 360;
    }

    public int getHeight() {
        return 280;
    }
    
    private static class ChartDataHelper {
        List<Map<String, String>> data;
        int year;
        int month;
        public String thisYearMonth(){
            return "" + year + month;
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
        public List<Map<String, String>> getData() {
            return data;
        }
        public void setData(List<Map<String, String>> data) {
            this.data = data;
        }
    }

    @Resource(name="bean_ChartDataHelper")
    private ChartDataHelper chartDataHelper;

    @Override
    public boolean hasData() {
        return !chartDataHelper.getData().isEmpty();
    }
}