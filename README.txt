Recommender Systems Assignment 2 Arvind Nair README File.

Folder Extraction and its contents:
1. PA2 is zipped folder and you need a WinZip or WinRar or 7zip to extract the contents.

2. It contains JARwithftoutput folder, JAMA.jar, README.txt, rec2.jar, recassign2.txt, recassign2.java, results.txt, train.txt and test.txt.

3. The JARwithftoutput folder contains rec2ftoutput.jar, jama.jar, recassign2.txt, recassign2.java, test.txt and train.txt.

How to compile and run the code:
1. I have used Java programming to do the assignment. The program is in a JAR file called rec2.jar. 

2. I have compiled into a runnable JAR file as for Matrix operations I have used an external JAR package named JAMA. I have included JAMA.jar in the folder PA2 for reference.

3. The source code has been provided in a separate text file and java file.

4. I choose to make it a runnable JAR file as it encapsulates the JAMA library which is not a part of Java installation.

5. To run the JAR file from command prompt, open cmd and set the path in the folder PA2 to (location where Java is installed)/Java/jdk(version)/bin and type java -jar rec2.jar

6. Note that the train.txt and test.txt must be in the same folder as that of the JAR File.

7. The program starts and runs as per the instructions given in the assignment.

8. The program also asks the user for inputs of k, lambda, maxlters & epsilon and user has to enter them.

9. The program also asks for the file name to store the results of that particular value set of k, lambda, maxlters & epsilon and the filename must be in the format of 
filename.txt where filename can be the name of the user's choice. But filename must end in a .txt extension.

10. The program will generate a file in the same folder in which the JAR file was present. It can be viewed in any text editor like Notepad. 

11. I have also included a results.txt file in PA2 folder which has the results of the program as specified in the assignment in the order: k lambda maxIters epsilon MSE RMSE training_time testing_time 

Note:
1. For viewing the source code you can check the recassign2.txt or recassign2.java file. But to run that code you need to install the JAMA package in the project in Netbeans or Eclipse IDE and
hence I chose to make it a runnable JAR file for convenience.

2. The JAR file can be extracted using WinZip or WinRar or 7zip but there will be only class files.

3. If for double checking you need to run the source code, then you have to copy paste the recassign2.java file into an IDE and import the JAMA.jar which is there in the folder as an external JAR file in 
the particular package.

4. The program will then run. So for conveience I have created a runnable JAR file.

5. In the JARwithftoutput folder, I have placed another JAR file called rec2ftoutput.jar which shows the output of decreasing f(t) optimization function after every iteration for reference. That file can also be run 
by setting the path in the folder JARwithftoutput as in step 5 in how to compile and run the code and type java -jar rec2ftoutput.jar

6. For double checking the JARwithftoutput you need to follow step 3 in note and use the recassign2.java inside the JARwithftoutput folder. It just uncomments the [Line 300].

7. I have also included a results.txt file which includes the results as reported by the program in the format as given in the assignment:
   k lambda maxlters epsilon MSE RMSE training_time testing_time 
 
How my code works:
1. I have taken the train.txt file which has to be manually entered at [Line 16]. This reads the file and constructs the CSR represenation from the dataset in the file.

2. After that it constructs the CSR transpose in which as the items are unsorted it takes the row pointer transpose and stores as array of objects the items as two attributes namely value and 
number of times(its count). So if item 55 occurs 5 times and 51 occurs next and is rated 3 times the 0 index of row pointer transpose will show as rowptr[0].value=55 & rowptr[0].notimes=0, the index 1 of the 
row pointer transpose will show rowptr[1].value=51 & rowptr[0].notimes=5 and the index 2 of the row pointer transpose will show rowptr[2].value=0 & rowptr[2].notimes=8. The difference in notimes will give how 
many times the item occurs and the corresponding column index of users and ratings in value array.

3. [Lines 157-171] The inputs are taken from the user for  latent dimension k, control parameter lambda, maximum number of iterations allowed maxlters and ratio of value change epsilon. 

4. [Lines 175-190] The matrices P and Q are generated at [Lines 213-214] using JAMA package imported at [Line 4]. For that I have used a double array and passed the double array as parameter and generated a 
Matrix object using JAMA.

5. [Line 222]The timer starts for while loop which contains the iterations.

6. [Line 230]The LS_Closed function for solving P and fixing Q using Alternating Least Squares method(ALS). It is a static function which passes the parameters value(user ratings),col_ind(item ids),row_ptr(users), rowptr(items as array of objects in CSR transpose),
Q(the item Matrix in dense form), latent dimension k and control parameter lambda.

7. [Lines 462-512]
7.1 The LS_Closed function has a temporary double array ptemp which stores the factorizations for each userstarting from zero as per the user index row wise and it is a [users*k] array. 
7.2 An identity matrix B1 is generated and its elements are made zero using the functions identity and timesEquals of JAMA. 
7.3 For each user, each rated item is scanned and the ratings are multiplied with the corresponding Q matrix row. 
7.4 The particular row of that item in Q matrix is selected by checking the row transpose index of the item with its value. Eg: if item 55 is stored at rowptr[i].value=55 where i=3 then the 4rd row of Q matrix (as it starts from zero) is taken 
for multiplication with ratings and transpose. 
7.5 The ratings are multiplied with the corresponding rows in Q and added. 
7.6 The corresponding rows of Q matrix are also multiplied with its transpose and added along with lambda multiplied with the identity matrix I1 using functions times, timesEquals, identity and transpose stored in a Matrix B1 and its inverse is taken. At some places I have used temporary arrays for simple multiplication like [Lines 488-489] where then I have converted the double array 
into a Matrix for transpose and inverse. 
7.7 Then BR2 stores the ratings multiplied by the rows in BR1 and the result is stored in the corresponding row matrix factor of ptemp for each user.
7.8 Then finally the ptemp array is converted into a Matrix Ptemp and returned to P in whle loop.

8. [Lines 515-567] 
8.1 The LS_Closed1 function for solving Q and fixing P has the same funtionality as LS_Closed but the only difference is it passes different parameters namely valT, col_indT, row_ptrT, Matrix P, k, and lambda.
8.2 Also there is a boolean variable called flag which checks if rowptr.value is zero as some items from 10 to 50 are missing towards the end. 
8.3 So the corresponding rows in the Matrix Q are set to zero as LS_Closed will return zero as there are no ratings. 
8.4 Rest of the functionality is same as LS_Closed.

9. [Lines 234-312] 
9.1 In the while loop for each iteration the f(t) value of the optimization function is calculated by subtracting the corresponding ratings from the multiplication of the corresponding row of user in P and transpose of q matrix corresponding row 
using rowptr.value eg:for user 2 who has rated item 55 as 4; row 3 of P matrix is multiplied with the transpose of row 5(rowptr.value=55 is present at rowptr[i] where i=4) and the result is a scalar as [1*k]*[k*1] is [1*1] 
which is subtarcted from 4. 
9.2 This is done for all ratings and stored in rating1sf. 
9.3 The Frobenius norm for P and Q are calculated using summation of squares of all the elements in P and Q. 
9.4 For Q Matrix, there is a counter which will count the number of unique items present in rowptr as some are missing and do the squares summation only for them as the items which are not present have zero values. 
9.5 It is multiplied with lambda and added with rating1sf. 
9.6 That is the value of f(t). 
9.7 For the first iteration, the f(t) is not checked with epsilon but for next iterations it is checked with f(t-1) which is stored and replaced.

10. [Line 314] It has the end time for the while loop.

11. The while loop will stop if |f(t)-f(t-1)|/f(t-1)<epsilon or when thw while loop ends when t has iterated for the number of times specified in maxlters.

12. [Lines 320-363] They take the testing data as input and store them in CSR representation. [Line 321]File name is specified.

13. [Line 365] The timer is started for taking the predictions.

14. [Lines 366-438] 
14.1 The predictions are taken as follows: For each item in the testing dataset i and user u the corresponding rows of Q and P are taken and a dot product is performed.Eg:if in testing data item 55 has been rated by user 4 then the 
dot product of row 5 of Matrix P and row 3 of Matrix Q(checked with rowptr[i].value=55 where i=3) is performed in which the corresponding each column element is multiplied and added as a scalar and it is stored in Pred1.
14.2 Then the bias(meanR) is added back to the Pred1 and stored in Pred(Predictions) and if Pred is lesser than 1 which is the lowest rating it will be rounded to 1 and if higher than 5 then it will be rounded to 5. 
14.3 The Pred is subtracted with the actual rating and squred and added in the Mean Squared Error(MSE) for all user ratings. 
14.4 For missing items in training set but present in testing set the Q row is taken as zero and the rest is the same.

15. [Line 438]The Root Mean Squared Error is calculated as the square root of MSE. 

16. [Line 440]The timer ends for the MSE and RMSE calculations.

17. [Line 447]The results of k, lambda, maxIters, epsilon, MSE, RMSE, training_time & testing_time are output on the stdio/console.

18. [Lines 450-458] The results are output into a file(name is a choice of the user) in the format: k lambda maxIters epsilon MSE RMSE training_time testing_time

Note:
1.For my program, the f(t) starts at around 1.22*e6 goes down to 1.05*e6 and then becomes steady at around 1.18*e6.

2.The MSE is around 3.67 and the RMSE is around 1.91.

3.Some comments contain outputs which were used for debugging to check the correct functionality of each part.

4.General comments have been included to increase readability of the code.
