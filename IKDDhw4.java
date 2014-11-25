

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class IKDDhw4 extends JFrame{
	private static String [][] tmpTableData;
	private static String [] BookField;
	private DefaultTableModel tmodel;
	private JTable book;
	public static void main(String[] args) throws IOException {
	String oneLine;
	String workingDir = System.getProperty("user.dir");
	String foderPath = workingDir;
	IKDDhw4 hw = new IKDDhw4();
	ArrayList<String> fileLists = hw.getFileList(foderPath);
	int deadEnd;
	boolean booMatrix[][] = hw.readFile(foderPath, fileLists);
	ArrayList<Integer> remOri = hw.getRemList(booMatrix);
	double noDelDoubMatrix[][] = hw.boolToDoub(booMatrix);
	while((deadEnd = hw.containDeadEnds(booMatrix)) != booMatrix.length)
	{
		booMatrix = hw.deletDeadEnds(booMatrix,deadEnd);
		remOri = hw.remDelMatrix(remOri,deadEnd);
	}
	double doubMatrix[][] = hw.boolToDoub(booMatrix);
	/*
	for(int i = 0; i < doubMatrix.length; i++){
		for(int j = 0; j < doubMatrix.length; j++)
			System.out.print(doubMatrix[i][j]+" ");
		System.out.print("\n");
	}
	*/
	double ini[] = hw.getIni(doubMatrix);
	double mark[] = hw.recaxb(doubMatrix,ini);
	double resultMark[] = hw.addDelDE(mark,remOri,fileLists.size());
	double fake[] = hw.manipulate(noDelDoubMatrix,resultMark);
	double finalArray[] = hw.merge(resultMark,fake);
	Scanner scanner = new Scanner(System.in);
	String yourInput =  scanner.nextLine();
	double search[] = hw.searchAllFile(foderPath, fileLists, yourInput);
	double unsort[] = hw.axb(finalArray,search);
	double sort[] = hw.axb(finalArray,search);
	//for(int i = 0; i < unsort.length; i++)System.out.println(unsort[i]);
	HashMap<Double, Integer> trans = hw.structCorrespond(unsort);
	hw.sort(sort);
	 //for(int i = 0; i < unsort.length; i++)System.out.println(sort[i]);
	tmpTableData = hw.structTable(trans,unsort,sort,fileLists);
	BookField = hw.setBook();
	hw.printResult(tmpTableData,BookField);
	}
	public  String[][] structTable(HashMap<Double, Integer> trans, double unsort[],double sort[], ArrayList<String> fileLists)
	{
		int j = 0;
		String table[][] = new String[unsort.length][2];
		for(int i = unsort.length - 1; i >= 0; i--)
			if(sort[i] != 0)
			{
			table[j][0] = String.valueOf(j + 1);
			table[j][1] = fileLists.get(trans.get(sort[i]));
			j++;
			}
			return table;
	}
	
	
	public  String[] setBook()
	{
		String[] a = {"Rank","Filename"};
		return a;
	}
	
	
	public  HashMap<Double, Integer> structCorrespond(double unsort[])
	{
		HashMap<Double, Integer> trans = new HashMap<Double, Integer>();
		for(int i = 0; i < unsort.length; i++)trans.put(unsort[i], i);
        return trans;		
	}
	
	
	public  double[] searchAllFile(String foderPath, ArrayList<String> fileLists, String q)
	{
		int Size = fileLists.size();
		String oneLine;
		double search[] = new double[Size];
		for(int i = 0; i < Size; i++){
			search[i] = 0;
			FileReader fr;
			try {
				fr = new FileReader(foderPath+"/"+fileLists.get(i));
				BufferedReader br = new BufferedReader(fr);
				 while ((oneLine = br.readLine()) != null) {
						 if(oneLine.contains(q)){
							search[i] = 1;
						 }
				 }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return search;
	}
	
	
	public  ArrayList<String> getFileList(String folderPath){
        //String folderPath = "C:\\";//資料夾路徑
        StringBuffer fileList = new StringBuffer();
        ArrayList<String> fileLists = new ArrayList<String>();
            try{
               java.io.File folder = new java.io.File(folderPath);
               String[] list = folder.list();           
                         for(int i = 0; i < list.length; i++){
                        	 if(list[i].contains(".txt"))fileLists.add(list[i]);
                        }
                }catch(Exception e){
                      System.out.println("'"+folderPath+"'此資料夾不存在");
                } 
               return fileLists;
        }

	
	public  boolean[][] readFile(String foderPath, ArrayList<String> fileLists)
	{
		int Size = fileLists.size();
		boolean booMatrix[][] = new boolean[Size][Size];
		for(int i = 0; i < Size; i++)for(int j = 0; j < Size; j++){
			booMatrix[i][j] = false;
		}
		String oneLine;
		ArrayList<String> httpPlus = new ArrayList<String>();
		for(int i = 0; i < Size; i++)
		{
			httpPlus.add("http://" + fileLists.get(i));
		}
		for(int i = 0; i < Size; i++){
			FileReader fr;
			try {
				fr = new FileReader(foderPath+"/"+fileLists.get(i));
				BufferedReader br = new BufferedReader(fr);
				 while ((oneLine = br.readLine()) != null) {
					 for(int j = 0; j < Size; j++)
						 if(oneLine.contains(httpPlus.get(j))){
							 booMatrix[j][i] = true;
						 }
				 }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return booMatrix;
	}
	
	
	/*delete one of deadends in the matrix*/
	public boolean[][] deletDeadEnds(boolean booMatrix[][], int deadEnd)
	{
		int Size = booMatrix.length;
		boolean booMatrix2[][] = new boolean[Size-1][Size-1];
		
		int newi = 0, newj = 0;
		
		for(int i = 0; i < Size; i++)
	{
			for(int j = 0; j < Size; j++)
			
		{
			if(deadEnd != i && deadEnd != j)
			{
				if(newj == Size-1){
					newj = 0;
					newi++;	
				}
				booMatrix2[newi][newj] = booMatrix[i][j];
				newj++;
			}
		}
	}
		
		return booMatrix2;
	}
	
	
	/*print index of matrix which is deadend; otherwise ,print length of matrix*/
	public int containDeadEnds(boolean booMatrix[][])
	{
		int Size = booMatrix.length;
		boolean result = false;
		for(int i = 0; i < Size; i++)
		{
			int temp = 0;
			for(int j = 0; j < Size; j++)if(!booMatrix[j][i])temp++;
			if(temp == Size)return i;
		}
		
		return Size;
	}
	
	
	public ArrayList<Integer> remDelMatrix(ArrayList<Integer> rem, int index)
	{
		ArrayList<Integer> rem1 = new ArrayList<Integer>();
		for(int i = 0; i < rem.size(); i++)if(i != index)rem1.add(i);
		return rem1;
	}
	
	
	public  ArrayList<Integer> getRemList(boolean booMatrix[][])
	{
		ArrayList<Integer> rem = new ArrayList<Integer>();
		for(int i = 0; i < booMatrix.length; i++)rem.add(i);
		return rem;
	}
	
	
	public double[][] boolToDoub(boolean booMatrix[][])
	{
		int Size = booMatrix.length;
		double doubMatrix[][] = new double[Size][Size];
		double temp[] = new double[Size]; 
		for(int i = 0; i < Size; i++)temp[i] = 0;
		for(int i = 0; i < Size; i++)for(int j = 0; j < Size; j++)
		{
			if(booMatrix[j][i])temp[i]++;
			doubMatrix[j][i] = 0;
		}
		for(int i = 0; i < Size; i++)for(int j = 0; j < Size; j++)
			if(booMatrix[j][i] && temp[i] != 0)doubMatrix[j][i] = 1/temp[i];

		return doubMatrix;
	}
	
	
	public double[] getIni(double a[][])
	{
		int Size = a.length;
		double b[] = new double[Size];
		for(int i = 0; i < Size; i++)b[i] = 1/(double)Size;
		return b;
	}
	
	
	public double[] axb(double a[][], double b[])
	{
		int Size = b.length;
		double c[] = new double[Size];
		for(int i = 0; i < Size; i++)
			for(int j = 0; j < Size; j++)
		{
			c[i] += a[i][j]*b[j];
			
		}
		return c;
	}
	
	public double[] axb(double a[], double b[])
	{
		int Size = b.length;
		double c[] = new double[Size];
		for(int i = 0; i < Size; i++)
		{
			c[i] = a[i]*b[i];
		}
		return c;
	}
	
	public double[] recaxb(double a[][], double b[])
	{
		boolean check = false;
		while(!check){
			check = true;
			double t[] = b;
			t = axb(a,b);
			for(int i = 0; i < b.length; i++)if(t[i] != b[i])check = false; 
			b = t;
		}
		return b;
	}
	
	
	/*c is all size, b is remain index, a is mark array, other add -1*/
	public double[] addDelDE(double[] a, ArrayList<Integer> b, int c)
	
	{
		double result[] = new double[c];
		int index = 0;
		for(int i = 0; i < c; i++)
		{
			if(!b.contains(i))result[i] = -1;
			else result[i] = a[index++];
		}
		
		return result;
	}
	
	
	public double[] manipulate(double[][] a, double[] b)
	{
		int Size = b.length;
		double result[] = new double[Size];
		for(int i = 0; i < Size; i++)result[i] = b[i];
	boolean check =  true;
	
	while(check)
	{
	for(int i = 0; i < Size; i++)result[i] = rowxcol(a[i],result);
	check = false;
	for(int i = 0; i < Size; i++){
		if(result[i] == -1)check = true;
		}
	}
	return result;
	}
	
	
	public double rowxcol(double[] row, double[] col)
	{
		double result = 0;
		for(int i = 0; i < row.length; i++)
		{
			if((col[i] == -1 && row[i] == 0) || col[i] != -1)
				result += (row[i]*col[i]);
			else return -1;
		}
		return result;
	}
	
	
	public double[] merge(double[] a, double[] b)
	{
		double result[] = new double[a.length];
		for(int i = 0; i < a.length; i++)
		{
			if(a[i] == -1)result[i] = b[i];
			else result[i] = a[i];
		}
		return result;
	}
	
	
	  public void sort(double[] number) {
	        sort(number, 0, number.length-1);
	    }
	    
	    private static void sort(double[] number, 
	                             int left, int right) {
	        if(left < right) { 
	            double s = number[left]; 
	            int i = left; 
	            int j = right + 1; 

	            while(true) { 
	                
	                while(i + 1 < number.length && number[++i] < s) ;  
	                
	                while(j -1 > -1 && number[--j] > s) ;  
	                if(i >= j) 
	                    break; 
	                swap(number, i, j); 
	            } 

	            number[left] = number[j]; 
	            number[j] = s; 

	            sort(number, left, j-1);   
	            sort(number, j+1, right); 
	        }
	    }
	    
	    private static void swap(double[] number, int i, int j) {
	        double t; 
	        t = number[i]; 
	        number[i] = number[j]; 
	        number[j] = t;
	    }
	
	    
	public void printResult(String[][] tmpTableData, String[] BookField)
	{
		  tmodel = new DefaultTableModel(tmpTableData,BookField);
		  book = new JTable(tmodel);
		  Container c = getContentPane();
		  c.setLayout(new BorderLayout());
		  book.getColumnModel().getColumn(1).setPreferredWidth(180);
		  book.getTableHeader().setReorderingAllowed(false);
		  book.setShowHorizontalLines(false);
		  c.add(new JScrollPane(book),BorderLayout.CENTER);
		  setSize(500,400);
		  setLocation((int)(Math.random() * 50) + 5,250);
		  setVisible(true);
	}
}
