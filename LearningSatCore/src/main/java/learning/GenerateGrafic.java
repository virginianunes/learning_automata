package learning;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class GenerateGrafic {
    private static String title;
    private static XYSeriesCollection dataSet;

    public GenerateGrafic(String title) throws IOException {
        this.title = title;
        this.dataSet = new XYSeriesCollection();
    }
    public static JFreeChart createChartPanel() throws IOException {
        String xAxisLabel = "Execution";
        String yAxisLabel = "Accurate(%)";
        XYDataset data = dataSet;

        JFreeChart chart = ChartFactory.createXYLineChart(title,
                xAxisLabel, yAxisLabel, data);

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesStroke(0,  new BasicStroke(2.0f));
        renderer.setSeriesPaint(1, Color.GREEN);
        renderer.setSeriesStroke(1,  new BasicStroke(2.0f));

        plot.setBackgroundPaint(Color.DARK_GRAY);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);
        plot.setRenderer(renderer);
        return chart;
    }

    public void setDataset(String method, List<Double> accurate, int executions) {
        boolean autoSort = false;
        XYSeries serie = new XYSeries(method, autoSort);
        for (int i = 0; i < Main.executions; i++) {
            serie.add(i, accurate.get(i));
        }
        dataSet.addSeries(serie);
    }

    public void saveInPNG(JFreeChart grafic) throws IOException {
        //saída do gráfico para PNG
        /*OutputStream arquivo = new FileOutputStream(AcessFile.grafic);
        ChartUtilities.writeChartAsPNG(arquivo, grafic, 550, 400);*/
    }

    public void showInJpanel(JFreeChart grafic) throws IOException {
        //saída em um JPanel
        JFrame frame = new JFrame("Grafic");
        frame.add(new ChartPanel(grafic));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}