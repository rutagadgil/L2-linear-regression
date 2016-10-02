package com.dm.linearRegression;

import java.util.List;

import Jama.Matrix;

public class MatrixOperator {
	Matrix m;
	Matrix x;
	Matrix y;

	public MatrixOperator(List<double[]> trainData) {
		// TODO Auto-generated constructor stub
		if(trainData.size() > 0){
			int rows = trainData.size();
			int cols = trainData.get(0).length;
			this.m = new Matrix(rows, cols);

			for(int r = 0; r < rows; r++){
				for(int c = 0; c < cols; c++){
					m.set(r, c, trainData.get(r)[c]);
				}
			}
		}
		//printM();
	}

	public void printM(){
		System.out.println("Printing matrix m");
		for(int r = 0; r < m.getRowDimension(); r++){
			for(int c = 0; c < m.getColumnDimension(); c++){
				System.out.print(m.get(r, c) + " ");
			}
			System.out.println();
		}
	}

	public Matrix readXValues(){
		// append a column of all 1s at the beginning
		Matrix xtemp = m.getMatrix(0, m.getRowDimension() - 1, 0, m.getColumnDimension() - 2);
		int rows = xtemp.getRowDimension();
		int cols = xtemp.getColumnDimension() + 1;
		x = new Matrix(rows, cols);
		for(int r = 0 ; r < rows; r++){
			for(int c = 0; c < cols; c++){
				if(c == 0){
					x.set(r, c, 1);
				}
				else{
					x.set(r, c, xtemp.get(r, c - 1));
				}
			}
		}
		//printX();
		return x;
	}

	public void printX(){

		System.out.println("Printing matrix x: ");
		System.out.println("Rows: " + x.getRowDimension() + "Columns: " + x.getColumnDimension());
		for(int r = 0; r < x.getRowDimension(); r++){
			for(int c = 0; c < x.getColumnDimension(); c++){
				System.out.print(x.get(r, c) + " ");
			}
			System.out.println();
		}
	}

	public Matrix readYValues(){
		y = m.getMatrix(0, m.getRowDimension() - 1, m.getColumnDimension() - 1, m.getColumnDimension() - 1);
		//printY();
		return y;
	}

	public void printY(){
		System.out.println();
		System.out.println("Printing matrix y");
		for(int r = 0; r < y.getRowDimension(); r++){
			System.out.println(y.get(r, 0) + " ");
		}
	}
}
