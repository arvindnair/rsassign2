//package rec2;
import java.io.*;
import java.util.*;
import Jama.Matrix;

class row_ptrT{
	int notimes=0;
	int value=0;
}

public class recassign2 {//start of class containing main method
	static int users=0;
	static int items=0;
	static int ratings=0;
	public static void main(String[] args)throws Exception {//start of main method
			Scanner sc=new Scanner(new File("train.txt"));//Specify the train dataset file here 
			String countn=sc.nextLine();
			String cntemp[]=countn.split(" ");
		    int[] cntemp1=new int[cntemp.length];
			int iconst=0,row=1;
	        
			for(int i=0;i<cntemp.length;i++){
	        	cntemp1[i]=Integer.parseInt(cntemp[i]);
	        }
	        /*Constructing the CSR representation from training dataset*/
			int[] val=new int[cntemp1[2]];
			int[] row_ptr=new int[cntemp1[0]+1];
			int[] col_ind=new int[cntemp1[2]];
			users=cntemp1[0];
			items=cntemp1[1];
			ratings=cntemp1[2];
			double minR=1;
			double maxR=5;
			row_ptr[0]=0;
		
		while(sc.hasNextLine()){
			String currentline=sc.nextLine();
			String sttemp[]=currentline.split(" ");
			int[] inttemp=new int[sttemp.length];
	        
			 for(int i=0;i<sttemp.length;i++){
	        	inttemp[i]=Integer.parseInt(sttemp[i]);
	         }
	        
			int[] a=new int[inttemp.length];
	        a=inttemp.clone();
	        
	        for(int i=0;i<a.length;i++){
	        	if((i%2)==0){
	        	     col_ind[iconst]=a[i];
	        	     }
	        	else{
	        		val[iconst]=a[i];
	        		iconst++;
	        	}
	        
	        }
	        
	        row_ptr[row]=iconst;
	        row++;
	        
		}
		
	/*	for(int i=0;i<val.length;i++){
		System.out.print(val[i]+" ");
		}
		System.out.println();*/
		
		double sum=0,meanR=0;
		for(int i=0;i<val.length;i++){
			sum=sum+val[i];
			}
		meanR=sum/(val.length);
	//	System.out.println("meanR: "+meanR);
		
		double[] valb=new double[val.length];
		for(int i=0;i<valb.length;i++){//removing the biases here
			valb[i]=val[i];
			valb[i]=valb[i]-meanR;
			}
		
	/*	System.out.println();
		for(int i=0;i<valb.length;i++){
			System.out.print(valb[i]+" ");
			}*/
		
	/*	System.out.println();
		for(int i=0;i<col_ind.length;i++){
			System.out.print(col_ind[i]+" ");
			}
		System.out.println();
		
		for(int i=0;i<row_ptr.length;i++){
			System.out.print(row_ptr[i]+" ");
			}*/
		
		/*Constructing the CSR transpose from the CSR representation training dataset*/
		double[] valT=new double[valb.length];
		int[] col_indT=new int[col_ind.length];
		boolean check=false;
		row_ptrT[] rowptr=new row_ptrT[cntemp1[1]+1];
		for(int i=0;i<rowptr.length;i++){
			rowptr[i]=new row_ptrT();
		}
		rowptr[0].notimes=0;
		int c1=0;
		int ce=0;
		for(int k=0;k<col_ind.length;k++){
			for(int y=0;y<rowptr.length;y++){
				if(rowptr[y].value==col_ind[k]){
					check=true;
					break;
			    }
			}
			if(check!=true){
				for(int i=0;i<col_ind.length;i++){
					
					if(col_ind[k]==col_ind[i]){
						valT[ce]=valb[i];
						int j=0;
						while(j<row_ptr.length-1){
					     for(int r1=row_ptr[j];r1<row_ptr[j+1];r1++)
							{
					    	 if(i==r1){
					    		 col_indT[ce]=j;
					    		 ce++;
					    	  }
							}j++;
					    }
					}
					}
					rowptr[c1].value=col_ind[k];
					rowptr[c1+1].notimes=ce;//as per assumption rowptr[0].notimes=0
				    c1++;
				}check=false;
			}
		System.out.println();
		/*for(int i=0;i<rowptr.length;i++)
		{
		System.out.print("Row_PtrTV:"+rowptr[i].value+" ");
		System.out.print("Row_PtrTN:"+rowptr[i].notimes+" ");
		}*/

		/*System.out.println();
		for(int i=0;i<valT.length;i++)
		{
			System.out.print("ValT:"+valT[i]+" ");
		}
		System.out.println();
		for(int i=0;i<col_indT.length;i++)
		{
			System.out.print("Col_IndT:"+col_indT[i]+" ");
		}
		System.out.println();*/
		
		/*Taking input of k,lambda,maxlters and epsilon from user*/
		int k,maxlters;
		double lambda,epsilon,kd;
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the latent dimension k: ");
		k=Integer.parseInt(br.readLine());
		kd=k;
		
		System.out.println("Enter the control parameter lambda: ");
		lambda=Double.parseDouble(br.readLine());
		
		System.out.println("Enter the maximum number of iterations allowed maxlters: ");
		maxlters=Integer.parseInt(br.readLine());
		
		System.out.println("Enter the ratio of value change epsilon: ");
		epsilon=Double.parseDouble(br.readLine());
		
	//	System.out.println("k: "+k+" lambda: "+lambda+" maxlters: "+maxlters+" epsilon: "+epsilon);
		
		double[][] p=new double[cntemp1[0]][k];
		double[][] q=new double[cntemp1[1]][k];
		
		for(int i=0;i<cntemp1[0];i++)
		{
			for(int j=0;j<k;j++){
			p[i][j]=1/kd;	
			}
		}
		
		for(int i=0;i<cntemp1[1];i++)
		{
			for(int j=0;j<k;j++){
			q[i][j]=1/kd;	
			}
		}
		
	/*	System.out.println();
		System.out.println("p array: ");
		for(int i=0;i<cntemp1[0];i++)
		{
			for(int j=0;j<k;j++){
				System.out.print(p[i][j]+" ");	
			}
			System.out.println();
		}*/
		
	/*	System.out.println();
		System.out.println("q array: ");
		for(int i=0;i<cntemp1[1];i++)
		{
			for(int j=0;j<k;j++){
				System.out.print(q[i][j]+" ");	
			}
			System.out.println();
		}*/
		
		/*Constructing P and Q matrix using JAMA library*/
		Matrix P=new Matrix(p);
		Matrix Q=new Matrix(q);
		
		//P.print(1, 1);
		//Q.print(1, 1);
		//double d=P.get(1, 1);
		//System.out.println("dgot: "+d);
		
	
		long startTime=System.currentTimeMillis();//starting the timer for LS_Closed while loop iterations
		int t=0;
		double ft1=0;
		
		/*Starting the iterations*/
		while(t<maxlters){
			t++;
			
			P=LS_closed(val,col_ind,row_ptr,rowptr,Q,k,lambda);//Fixing Q solving for P
			Q=LS_closed1(valT,col_indT,rowptr,P,k,lambda);//Fixing P solving for Q
			
			/*Starting calculations for checking if optimized*/
			double rating1=0,rating1s=0,rating1sf=0,pqm=0;
			double[][] putemp=new double[1][k];
			double[][] qitemp=new double[1][k];
			for(int i=0;i<row_ptr.length-1;i++){
				int j=0;
				for(j=row_ptr[i];j<row_ptr[i+1];j++){
					rating1=val[j];
					int a1=col_ind[j];
					int a2=0;
					for(int k1=0;k1<rowptr.length-1;k1++){
						if(a1==rowptr[k1].value){
							a2=k1;
						}
					}
					for(int k1=0;k1<k;k1++){
						putemp[0][k1]=P.get(i,k1);
					}
					Matrix Putemp=new Matrix(putemp);
					for(int k1=0;k1<k;k1++){
						qitemp[0][k1]=Q.get(a2,k1);
					}
					Matrix Qitemp=new Matrix(qitemp);
					Matrix Qitempt=Qitemp.transpose();
					Matrix PQm=Putemp.times(Qitempt);
					pqm=PQm.get(0,0);
					rating1s=rating1-pqm;
					rating1sf=rating1sf+(rating1s*rating1s);
				}
			}
			
			/*Frobenius norm for Matrix P */
			double e1=0,e2=0,e3=0;
			for(int i=0;i<users;i++){
				for(int j=0;j<k;j++){
					e1=P.get(i,j);
					e2=e2+e1;
					e3=e3+(e2*e2);
					e2=0;
					}
			}
			
			/*Counting the number of unique items rated in training dataset*/
			int counter=0;
			for(int i=0;i<rowptr.length;i++){
				if(rowptr[i].value==0){
					break;
				}
				counter++;
			}
			
			/*Frobenius norm for Matrix Q*/
			double f1=0,f2=0,f3=0;
			for(int i=0;i<counter;i++){
				for(int j=0;j<k;j++){
					f1=Q.get(i,j);
					f2=f2+f1;
					f3=f3+(f2*f2);
					f2=0;
					}
			}
			
			/*Checking if lesser than epsilon to break*/
			double Y=lambda*(e3+f3);
			double ft=rating1sf+Y;
			
			/*Open the comments to check if ft(the optimization function is decreasing)*/
			System.out.println("ft: "+ft);
			double rft=100;
			double rft1=100;
			double rft2=100;
			if(t>1){
				 rft1=ft-ft1;
				 rft2=Math.abs(rft1);//taking absolute value to remove negative sign
				 rft=rft2/ft1;
			}
			if(rft<epsilon){
				break;
			}
			ft1=ft;
		}
		long endTime=System.currentTimeMillis();//end timer for iterations of LS_Closed
		float sec=0;
		sec=(endTime-startTime)/1.0f;
		String training_timestr=String.format("%.2f", sec);
		float training_time=Float.parseFloat(training_timestr);
		
		/*Constructing CSR representation from Testing Data*/
		Scanner sct=new Scanner(new File("test.txt"));
		String countnt=sct.nextLine();
		String cntempt[]=countnt.split(" ");
	    int[] cntemp1t=new int[cntempt.length];
		int iconstt=0,rowt=1;
        
		for(int i=0;i<cntempt.length;i++){
        	cntemp1t[i]=Integer.parseInt(cntempt[i]);
        }
        
		int[] valt=new int[cntemp1t[2]];
		int[] row_ptrt=new int[cntemp1t[0]+1];
		int[] col_indt=new int[cntemp1t[2]];
		
		row_ptrt[0]=0;
	
	while(sct.hasNextLine()){
		String currentlinet=sct.nextLine();
		String sttempt[]=currentlinet.split(" ");
		int[] inttempt=new int[sttempt.length];
        
		 for(int i=0;i<sttempt.length;i++){
        	inttempt[i]=Integer.parseInt(sttempt[i]);
         }
        
		int[] at=new int[inttempt.length];
        at=inttempt.clone();
        
        for(int i=0;i<at.length;i++){
        	if((i%2)==0){
        	     col_indt[iconstt]=at[i];
        	     }
        	else{
        		valt[iconstt]=at[i];
        		iconstt++;
        	}
        
        }
        
        row_ptrt[rowt]=iconstt;
        rowt++;
        
	}
	
	long startTime1=System.currentTimeMillis();//Starting timer for MSE and RMSE calculations
		double MSE=0;
		double RMSE=0;
		double Pred=0;
	for(int i=0;i<row_ptrt.length-1;i++){//users loop
		double[] pdot=new double[k];
		for(int k1=0;k1<k;k1++){
			pdot[k1]=P.get(i,k1);
		}
		int j=0;
		for(j=row_ptrt[i];j<row_ptrt[i+1];j++){//items loop
			int a1=col_indt[j];
			int a2=-1;
			double a3=val[j];
			double[] qdot=new double[k];
			
			for(int k1=0;k1<rowptr.length-1;k1++){
				if(a1==rowptr[k1].value){
					a2=k1;
					break;
				}
			}
			
			if(a2!=-1){
			for(int k1=0;k1<k;k1++){
				qdot[k1]=Q.get(a2,k1);
			}
			double dotprod=0;
			for(int k1=0;k1<k;k1++){
				dotprod=dotprod+(pdot[k1]*qdot[k1]);
			}
				double Pred1=0;
				Pred1=dotprod+meanR;
				Pred=Pred+Pred1;
				if(Pred<minR){
					Pred=minR;
				}
				else if(Pred>maxR){
					Pred=maxR;
				}
				
			
			double a4=a3-Pred;
			MSE=MSE+(a4*a4);
		    }
			
			/*If item is not present in training dataset*/
			else{
				for(int k1=0;k1<k;k1++){
					qdot[k1]=0;
				}
				double dotprod=0;
				for(int k1=0;k1<k;k1++){
					dotprod=dotprod+(pdot[k1]*qdot[k1]);	
					}
					double Pred1=0;
					Pred1=dotprod+meanR;
					Pred=Pred+Pred1;
					if(Pred<minR){
						Pred=minR;
					}
					else if(Pred>maxR){
						Pred=maxR;
					}
					
				
				double a4=a3-Pred;
				MSE=MSE+(a4*a4);
			}
		}
	}
	
	MSE=MSE/cntemp1t[2];//MSE calculated
	RMSE=Math.sqrt(MSE);//RMSE calculated
	
	long endTime1=System.currentTimeMillis();//end timer for MSE and RMSE calculations
	float sec1=0;
	sec1=(endTime1-startTime1)/1.0f;
	String testing_timestr=String.format("%.2f", sec1);
	float testing_time=Float.parseFloat(testing_timestr);
	
	/*Output to stdio as mentioned in assignment*/
	System.out.println("k="+k+" "+"lambda="+lambda+" "+"maxlters="+maxlters+" "+"epsilon="+epsilon+" "+"MSE="+MSE+" "+"RMSE="+RMSE+" ");
	
	/*Write to output file the results*/
	 System.out.println("Enter the filename to output results to: ");
	 String filename=br.readLine();
	 File outFile = new File (filename);
	    FileWriter fWriter = new FileWriter (outFile);
	    PrintWriter pWriter = new PrintWriter (fWriter);
	    pWriter.println(k+" "+lambda+" "+maxlters+" "+epsilon+" "+MSE+" "+RMSE+" "+training_time+" "+"ms"+" "+testing_time+" "+"ms"+" ");
	    pWriter.close();
	    sc.close();
	    sct.close();
	}//end of main
	
	/*Begin LS_Closed function for solving Q Matrix*/
	public static Matrix LS_closed(int[] val,int[] col_ind,int[] row_ptr,row_ptrT[] rowptr,Matrix Q,int k,double lambda){
		
		double[][] ptemp=new double[users][k];
		for(int i=0;i<row_ptr.length-1;i++){
			
			int j=0;
			double[][] br1=new double[1][k];
			for(int k1=0;k1<k;k1++){
				br1[0][k1]=0;
			}
			Matrix B1=Matrix.identity(k,k);
			B1=B1.timesEquals(0);
			for(j=row_ptr[i];j<row_ptr[i+1];j++){
				int a1=col_ind[j]; 
				int a2=0; 
				double a3=val[j];
				for(int k1=0;k1<rowptr.length-1;k1++){
					if(a1==rowptr[k1].value){
						a2=k1;
						break;
					}
				}
				double[] ar1=new double[k];
				double[][] ar2=new double[1][k];
			
				for(int k1=0;k1<k;k1++){
					ar1[k1]=a3*Q.get(a2,k1);
					br1[0][k1]=br1[0][k1]+ar1[k1];
				}
			    for(int k1=0;k1<k;k1++){
			    	ar2[0][k1]=Q.get(a2,k1);
			    }
			    Matrix AR2=new Matrix(ar2);
			    Matrix AR2t=AR2.transpose();
			    Matrix AR2m=AR2t.times(AR2);
			    B1=B1.plusEquals(AR2m);
			}
			Matrix I1=Matrix.identity(k,k);
			I1=I1.timesEquals(lambda);
			B1=B1.plusEquals(I1);
			B1=B1.inverse();
			Matrix BR1=new Matrix(br1);
			BR1=BR1.times(B1);
			for(int k1=0;k1<k;k1++){
				ptemp[i][k1]=BR1.get(0,k1);
			}
		}
		Matrix Ptemp=new Matrix(ptemp);
		return Ptemp;
		
	}//End LS_Closed for Matrix Q
	
	/*Begin LS_Closed function for solving P Matrix*/
	public static Matrix LS_closed1(double[] valT,int[] col_indT,row_ptrT[] rowptr,Matrix P,int k,double lambda){
		double[][] qtemp=new double[items][k];
		for(int i=0;i<rowptr.length-1;i++){//items loop
			int j=0;
			boolean flag=false;
			if(rowptr[i].value==0){
				flag=true;
			}
			if(flag==false){
			double[][] br1=new double[1][k];
			for(int k1=0;k1<k;k1++){
				br1[0][k1]=0;
			}
			Matrix B1=Matrix.identity(k,k);
			B1=B1.timesEquals(0);
			for(j=rowptr[i].notimes;j<rowptr[i+1].notimes&&rowptr[i+1].notimes!=0;j++){//users loop
				int a1=col_indT[j];
				double a3=valT[j];
				double[] ar1=new double[k];
				double[][] ar2=new double[1][k];
			
				for(int k1=0;k1<k;k1++){
					ar1[k1]=a3*P.get(a1,k1);
					br1[0][k1]=br1[0][k1]+ar1[k1];
				}
			    for(int k1=0;k1<k;k1++){
			    	ar2[0][k1]=P.get(a1,k1);
			    }
			    Matrix AR2=new Matrix(ar2);
			    Matrix AR2t=AR2.transpose();
			    Matrix AR2m=AR2t.times(AR2);
			    B1=B1.plusEquals(AR2m);
			}
			Matrix I1=Matrix.identity(k,k);
			I1=I1.timesEquals(lambda);
			B1=B1.plusEquals(I1);
			B1=B1.inverse();
			Matrix BR1=new Matrix(br1);
			BR1=BR1.times(B1);
			for(int k1=0;k1<k;k1++){
				qtemp[i][k1]=BR1.get(0,k1);
			}
		 }
			else{
				for(int k1=0;k1<k;k1++){
					qtemp[i][k1]=0;
				}
			}
		}
		Matrix Qtemp=new Matrix(qtemp);
		return Qtemp;
		
	}//end LS_Closed for Matrix P
	
}//end of class
