package com.dm.linearRegression;

import java.util.ArrayList;
import java.util.Collections;

import org.jfree.data.xy.XYSeries;

import Jama.Matrix;

public class L2Regression {

	public Matrix trainRegression(int lambda, Matrix x, Matrix y) {
		// TODO Auto-generated method stub
		//System.out.println("printing trainX");
		Matrix w;
		int cols = x.getColumnDimension();
		Matrix xTranspose = x.transpose();
		Matrix identityMatrix = Matrix.identity(cols, cols);
		//temp1 = X^T * X
		Matrix temp1 = xTranspose.times(x);
		//temp2 = lambda * I
		Matrix temp2 = identityMatrix.times(lambda);
		//temp3 = X^T * X + lambda * I
		Matrix temp3 = temp1.plus(temp2);
		//temp4 = (X^T * X + lambda * I) ^ -1
		Matrix temp4 = temp3.inverse();
		//temp5 = ((X^T * X + lambda * I) ^ -1) * X^T
		Matrix temp5 = temp4.times(xTranspose);
		w = temp5.times(y);
		//printXXT(w);
		return w;
	}

	public void printW(Matrix w){
		System.out.println("Printing w: ");
		for(int r = 0; r < w.getRowDimension(); r++){
			System.out.print(w.get(r, 0) + " ");
		}
	}

	public double calculateMSE(Matrix w, Matrix trainX, Matrix trainY){
		Matrix predictedY = trainX.times(w);

		Matrix temp = trainY.minus(predictedY);

		int rows = temp.getRowDimension();
		double sum = 0;

		for(int i = 0; i < rows ; i++){
			sum += temp.get(i,  0) * temp.get(i, 0);
		}

		double mse = sum/temp.getRowDimension();
		return mse;
	}

	public void printXXT(Matrix xxt){
		System.out.println("row dimension: " + xxt.getRowDimension() + " column dimension: " + xxt.getColumnDimension());
		for(int r = 0; r < xxt.getRowDimension(); r++){
			for(int c = 0; c < xxt.getColumnDimension(); c++){
				System.out.print(xxt.get(r, c) + " ");
			}
			System.out.println();
		}
	}

	public ArrayList<XYSeries> trainRegressionForLambdaRange(int min, int max, Matrix trainX, Matrix trainY, Matrix testX, Matrix testY) {
		// TODO Auto-generated method stub
		final XYSeries trainDataErrors = new XYSeries( "trainDataErrors" ); 
		final XYSeries testDataErrors = new XYSeries( "testDataErrors" );
		for(int i = min; i <= max; i++){
			//System.out.println("lambda= " + i);
			Matrix w = trainRegression(i, trainX, trainY);
			double trainMSE = calculateMSE(w, trainX, trainY);
			double testMSE = calculateMSE(w, testX, testY);
			trainDataErrors.add(i, trainMSE);
			testDataErrors.add(i, testMSE);
		}

		ArrayList<XYSeries> MSE = new ArrayList<XYSeries>();
		MSE.add(trainDataErrors);
		MSE.add(testDataErrors);
		return MSE;
	}

	public ArrayList<Double> trainRegressionForLambda(int lambda, Matrix trainX, Matrix trainY, Matrix testX, Matrix testY) {
		// TODO Auto-generated method stub
		ArrayList<Double> MSE = new ArrayList<Double>();

		Matrix w = trainRegression(lambda, trainX, trainY);
		double trainMSE = calculateMSE(w, trainX, trainY);
		double testMSE = calculateMSE(w, testX, testY);
		MSE.add(trainMSE);
		MSE.add(testMSE);

		return MSE;
	}
	
	public ArrayList<Double> crossValidation(int min, int max, Matrix trainX, Matrix trainY, Matrix testX, Matrix testY) {
		// TODO Auto-generated method stub
		int numberOfChunks = 10;
		ArrayList<Double> MSE = new ArrayList<Double>();
		
		ArrayList<Matrix> restTrainXi = generateRestFold(trainX, numberOfChunks);
		ArrayList<Matrix> trainXi = splitTrain(trainX, numberOfChunks);

		ArrayList<Matrix> restTrainYi = generateRestFold(trainY, numberOfChunks);
		ArrayList<Matrix> trainYi = splitTrain(trainY, numberOfChunks);
		
		// loop over all lambda values
		for(int lambda = min; lambda <= max; lambda++){
			double[] mses = new double[numberOfChunks];
			
			// train model for every chunk
			
			for(int i = 0; i < numberOfChunks; i++){
				Matrix trainX1 = restTrainXi.get(i);
				Matrix testX1 = trainXi.get(i);
				
				Matrix trainY1 = restTrainYi.get(i);
				Matrix testY1 = trainYi.get(i);
				
				Matrix w = trainRegression(lambda, trainX1, trainY1);
				mses[i] = calculateMSE(w, testX1, testY1);
				
				//System.out.println("MSE for i = " + i + " : " + mses[i]);
			}
			double averageMSE = 0;
			for(int i = 0; i < numberOfChunks; i++){
				averageMSE += mses[i];
			}
			averageMSE = averageMSE/numberOfChunks;
			//System.out.println("lambda: "+ lambda + " MSE: " + averageMSE);
			MSE.add(averageMSE);
		}
		/*System.out.println("Printing MSEs: ");
		for(int i = 0; i < MSE.size(); i++) {   
		    System.out.println("lambda = " + i + " MSE: " + MSE.get(i));
		}*/
		int minIndex = MSE.indexOf(Collections.min(MSE));
		
		//System.out.println("minIndex: " + minIndex);
		
		Matrix w = trainRegression(minIndex, trainX, trainY);
		double testMSE = calculateMSE(w, testX, testY);
		
		ArrayList<Double> lambdaMSE = new ArrayList<Double>();
		//HashMap<Integer, Double> lambdaMSE = new HashMap<Integer, Double>();
		lambdaMSE.add((double) minIndex);
		lambdaMSE.add(testMSE);
		
		return lambdaMSE;
	}
	
	private ArrayList<Matrix> generateRestFold(Matrix m, int numberOfChunks) {
		// TODO Auto-generated method stub
		ArrayList<Matrix> restTrainXi = new ArrayList<Matrix>();
		int rows = m.getRowDimension() ;
		int foldsize = rows/numberOfChunks;
		
		for(int i = 0; i < numberOfChunks; i++){
			//System.out.println("topLast: " + foldsize * i);
			//System.out.println("bottomFirst: " + (foldsize * (i+1) - 1));
			int topLast = foldsize * i;
			int bottomFirst = foldsize * (i+1) - 1;
			int cols = m.getColumnDimension();
			
			Matrix a1 = m.getMatrix(0, topLast - 1, 0, cols-1);
			double[][] A1 =a1.getArray();
			Matrix a2 = m.getMatrix(bottomFirst + 1, rows - 1, 0, cols-1);
			double[][] A2 = a2.getArray();

			double[][] result = new double[A1.length + A2.length][];
	        System.arraycopy(A1, 0, result, 0, A1.length);
	        System.arraycopy(A2, 0, result, A1.length, A2.length);
	        
	        Matrix trainx = new Matrix(result);
	        //trainx.print(2, 7);
	        restTrainXi.add(trainx);
		}
		
		return restTrainXi;
	}

	private ArrayList<Matrix> splitTrain(Matrix m, int numberOfChunks) {
		// TODO Auto-generated method stub
		ArrayList<Matrix> trainXi = new ArrayList<Matrix>();
		int rows = m.getRowDimension() ;
		int foldsize = rows/numberOfChunks;
		
		Matrix iMatrix;
		for(int i = 0; i < numberOfChunks; i++){
			iMatrix = m.getMatrix(foldsize * i, foldsize * (i+1) - 1, 0, m.getColumnDimension() - 1);
			trainXi.add(iMatrix);
			//System.out.println("Printing fold i = " + i);
			//printXXT(iMatrix);	
		}
		return trainXi;
	}
}
