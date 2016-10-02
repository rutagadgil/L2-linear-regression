package com.dm.linearRegression;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jfree.data.xy.XYSeries;

import Jama.Matrix;

public class Runner {

	public static void main(String[] args){

		ArrayList<DataSet> dataSets = new ArrayList<DataSet>();

		DataSet ds1 = new DataSet("HW1_data/50(1000)_100_train.csv", "HW1_data/test-1000-100.csv");
		DataSet ds2 = new DataSet("HW1_data/100(1000)_100_train.csv", "HW1_data/test-1000-100.csv");
		DataSet ds3 = new DataSet("HW1_data/150(1000)_100_train.csv", "HW1_data/test-1000-100.csv");
		DataSet ds4 = new DataSet("HW1_data/train-100-10.csv", "HW1_data/test-100-10.csv");
		DataSet ds5 = new DataSet("HW1_data/train-100-100.csv", "HW1_data/test-100-100.csv");
		DataSet ds6 = new DataSet("HW1_data/train-1000-100.csv", "HW1_data/test-1000-100.csv");
		DataSet ds7 = new DataSet("HW1_data/train-wine.csv", "HW1_data/test-wine.csv");

		dataSets.add(ds1);
		dataSets.add(ds2);
		dataSets.add(ds3);
		dataSets.add(ds4);
		dataSets.add(ds5);
		dataSets.add(ds6);
		dataSets.add(ds7);

		// Part 1: L2 regularized linear regression 
		// [0 <= lambda <= 150]
		part1(dataSets);

		// Part 2: Learning curve
		// lambda = [1, 46, 150]
		part2();
		
		// Part 3: Cross Validation
		part3(dataSets);
	}

	private static void part1(ArrayList<DataSet> dataSets){
		System.out.println("PART1:");
		System.out.println();
		for(DataSet ds : dataSets){
			String trainDataCSV = ds.getTrainData();
			String testDataCSV = ds.getTestData();

			// Reading CSV data files for training dataset and corresponding test dataset
			FileOperations fo = new FileOperations();
			List<double[]> trainDataValues = fo.readCSVData(trainDataCSV);
			List<double[]> testDataValues = fo.readCSVData(testDataCSV);

			// Generate X and Y matrices for training and test datasets
			MatrixOperator trainData = new MatrixOperator(trainDataValues);
			Matrix trainX = trainData.readXValues();
			Matrix trainY = trainData.readYValues();

			MatrixOperator testData = new MatrixOperator(testDataValues);
			Matrix testX = testData.readXValues();
			Matrix testY = testData.readYValues();

			// L2 Regression and MSE calculation
			L2Regression trainLR = new L2Regression();
			ArrayList<XYSeries> MSE = trainLR.trainRegressionForLambdaRange(0, 150, trainX, trainY, testX, testY);

			XYSeries trainErrors = MSE.get(0);
			XYSeries testErrors = MSE.get(1);

			String[] chartTitles = trainDataCSV.split("/");
			String[] titles = chartTitles[1].split("\\.");

			// Generate graph MSE vs Lambda
			System.out.println("Generating graph MSE vs Lambda for dataset: " + trainDataCSV);
			new XYLineChart_AWT("MSE vs lambda", titles[0], "lambda", "MSE", trainErrors, testErrors);
		}

	}
	
	private static void part2(){
		System.out.println();
		System.out.println("PART2:");
		System.out.println();
		
		int N = 50;
		String trainDataCSV = "HW1_data/train-1000-100.csv";
		String testDataCSV = "HW1_data/test-1000-100.csv";
		FileOperations fo = new FileOperations();
		List<double[]> trainDataValues = fo.readCSVData(trainDataCSV);
		List<double[]> testDataValues = fo.readCSVData(testDataCSV);

		MatrixOperator testData = new MatrixOperator(testDataValues);
		Matrix testX = testData.readXValues();
		Matrix testY = testData.readYValues();

		int[] lambdaValues = {1, 46, 150};
		
		for(int itr = 0; itr < lambdaValues.length; itr++){
			XYSeries trainDataErrors = new XYSeries("trainDataErrors");
			XYSeries testDataErrors = new XYSeries("testDataErrors");

			N = 50;
			int lambda = lambdaValues[itr];
			while(N < 1001){
				double trainSum = 0;
				double testSum = 0;
				for(int i = 0; i < 10; i++){
					List<double[]> sampleTrainData = generateSampleX(trainDataValues, N);
					MatrixOperator trainData = new MatrixOperator(sampleTrainData);
					Matrix trainX = trainData.readXValues();
					Matrix trainY = trainData.readYValues();

					L2Regression trainLR = new L2Regression();
					ArrayList<Double> mse = trainLR.trainRegressionForLambda(lambda, trainX, trainY, testX, testY);
					trainSum += mse.get(0);
					testSum += mse.get(1);
				}
				double trainAverage = trainSum/10;
				double testAverage = testSum/10;
				
				trainDataErrors.add(N, trainAverage);
				testDataErrors.add(N, testAverage);
				N += 50;
			}
			// Generate graph MSE vs Lambda
			System.out.println("Generating learning curve for dataset: " + trainDataCSV + " at lambda = " + lambda);
			new XYLineChart_AWT("MSE vs N", "Learning-curve at lambda = " + lambda, "N", "MSE", trainDataErrors, testDataErrors);
		}	

	}
	
	private static void part3(ArrayList<DataSet> dataSets){
		System.out.println();
		System.out.println("PART3:");
		FileWriter fw;
		BufferedWriter bw;
		File file;
		file = new File("Output/CV_output.txt");
		try{
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.newLine();
			bw.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		for(DataSet ds : dataSets){
			String trainDataCSV = ds.getTrainData();
			String testDataCSV = ds.getTestData();
			System.out.println();
			System.out.print("Dataset: " + trainDataCSV);
			
			// Reading CSV data files for training dataset and corresponding test dataset
			FileOperations fo = new FileOperations();
			List<double[]> trainDataValues = fo.readCSVData(trainDataCSV);
			List<double[]> testDataValues = fo.readCSVData(testDataCSV);

			// Generate X and Y matrices for training and test datasets
			MatrixOperator trainData = new MatrixOperator(trainDataValues);
			Matrix trainX = trainData.readXValues();
			Matrix trainY = trainData.readYValues();

			MatrixOperator testData = new MatrixOperator(testDataValues);
			Matrix testX = testData.readXValues();
			Matrix testY = testData.readYValues();

			// L2 Regression and MSE calculation
			L2Regression trainLR = new L2Regression();
			ArrayList<Double> MSE = new ArrayList<Double>();
			MSE = trainLR.crossValidation(0, 150, trainX, trainY, testX, testY);
			Double Lambda = MSE.get(0);
			int lambda = Lambda.intValue();
			double testMSE = MSE.get(1);
			
			System.out.print(" Lambda = " + lambda + " TestMSE: " + testMSE);
			
			try{
				fw = new FileWriter(file, true);
				bw = new BufferedWriter(fw);
				bw.write("Dataset: " + trainDataCSV + " Lambda = " + lambda + " TestMSE: " + testMSE);
				bw.newLine();
				bw.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	
	}
	
	private static List<double[]> generateSampleX(List<double[]> trainDataValues, int m) {
		// TODO Auto-generated method stub
		List<double[]> sampleTrainData = new ArrayList<double[]>(m);
		Random rnd = new Random();
		int n = trainDataValues.size();
		for(int i=n-m;i<n;i++){
			int pos = rnd.nextInt(i+1);
			double[] item = trainDataValues.get(pos);
			if (sampleTrainData.contains(item))
				sampleTrainData.add(trainDataValues.get(i));
			else
				sampleTrainData.add(item);
		}
		return sampleTrainData;
	}
}
