INSTRUCTIONS: HOW TO RUN THIS CODE

This is a Maven project. Open it in an IDE like Eclipse.
It contains following java dependencies: Jama matrix library, JFreeChart 

To run the Project:
Go to package com.dm.linearRegression
Open Runner.java
Click on Run

OUTPUT:
Output will be generated in the Output folder

1. Graphs [MSE vs Lambda] for every dataset will have the following format:
   <Training-DataSet-FileName>.jpeg
   eg. For the dataset 100(1000)_100_train.csv, output graph filename: 100(1000)_100_train.jpeg

2. Learning Curves for lambda values = [1, 46, 150] will have the following format:
   Learning-curve at lambda = <value>.jpeg
   eg. Learning Curve for lambda = 46, output filename: Learning-curve at lambda = 46.jpeg
   
3. Cross Validation:
   Best lambda values and their respective test MSEs are stored in the file CV_output.txt
   
REFERENCES:
http://www.ccs.neu.edu/home/yzhao/slides/LR_no_pause.pdf
http://math.nist.gov/javanumerics/jama/
http://www.jfree.org/jfreechart/
https://www.tutorialspoint.com/
