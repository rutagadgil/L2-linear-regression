package com.dm.linearRegression;

public class DataSet {
	private String trainData;
	private String TestData;
	
	public DataSet(String trainData, String testData) {
		super();
		this.trainData = trainData;
		TestData = testData;
	}
	public String getTrainData() {
		return trainData;
	}
	public void setTrainData(String trainData) {
		this.trainData = trainData;
	}
	public String getTestData() {
		return TestData;
	}
	public void setTestData(String testData) {
		TestData = testData;
	}
}
