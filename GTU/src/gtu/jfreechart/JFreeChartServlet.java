package gtu.jfreechart;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

@WebServlet("/JFreeChartServlet")
public class JFreeChartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public JFreeChartServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    private ChartService chartService;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            response.setContentType("image/jpeg");
            JFreeChart chart = chartService.getJFreeChart(chartService.getDataSet(), chartService.getChartTitle());
            chart.setTitle(chartService.getChartTitle());
            ChartUtilities.writeChartAsJPEG(response.getOutputStream(), chartService.getQuality(), chart, chartService.getWidth(), chartService.getHeight());
            response.getOutputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
