package com.dm.linearRegression;

import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class XYLineChart_AWT extends ApplicationFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	XYSeries trainErrors;
	XYSeries testErrors;

	public XYLineChart_AWT(String applicationTitle, String chartTitle, String xAxis,String yAxis, XYSeries trainErrors, XYSeries testErrors)
	{
		super(applicationTitle);
		this.trainErrors = trainErrors;
		this.testErrors = testErrors;
		JFreeChart xylineChart = ChartFactory.createXYLineChart(
				chartTitle ,
				xAxis ,
				yAxis ,
				createDataset(),
				PlotOrientation.VERTICAL ,
				true , true , false);

		int width = 640; /* Width of the image */
		int height = 480; /* Height of the image */

		File XYChart = new File( "Output/" + chartTitle + ".jpeg" );

		try {
			ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private XYDataset createDataset() {
		// TODO Auto-generated method stub
		final XYSeriesCollection dataset = new XYSeriesCollection( );          
		dataset.addSeries(trainErrors);          
		dataset.addSeries(testErrors);
		return dataset;
	}
}
