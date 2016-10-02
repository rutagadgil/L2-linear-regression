package com.dm.linearRegression;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileOperations {
	
	public List<double[]> readCSVData(String csv){
		//System.out.println("Opening file: " + csv);
		List<double[]> csvData = new ArrayList<double[]>();
		
		try {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(csv));
			String line = br.readLine();
			
			while((line = br.readLine()) != null){	
				String[] data = line.split(",");
				double[] dataValues = new double[data.length];

				for(int i = 0; i < data.length; i++){
					dataValues[i] = Double.parseDouble(data[i]);
				}
				csvData.add(dataValues);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return csvData;
	}
}
