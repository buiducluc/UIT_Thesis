package myThesis;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {

	public ArrayList<String> readFile(String path) throws FileNotFoundException 
	{
		ArrayList<String> data = new ArrayList<String>();
		try
		 {
		 //Tạo một con trỏ để connect đến pathFile
		 FileInputStream fileInPutStream = new FileInputStream(path);
		 //Đọc file với Unicode
		 Reader reader = new java.io.InputStreamReader(fileInPutStream, "UTF-8");
		 //Đọc từng byte
		 BufferedReader buffReader = new BufferedReader(reader);
		 //Tạo 1 stringBuilder để lấy từng dòng (line) của file
		 StringBuilder stringBuilder = new StringBuilder();
		 String line = null;
		 while ((line = buffReader.readLine()) != null)
		 {
		 //stringBuilder cho phép cộng dồn "append"
		 stringBuilder.append(line + "\n");
		 data.add(line);
		 System.out.println(line);
		 }
		 //Đóng kết nối với file. Không có cũng ko sao
		 reader.close();
		
		 }
		 catch(IOException io)
		 {
			 System.out.println("@@@");
		 }
		 
        return data;
	}
	
	public static int Euro(int a)
	 {
	  int year = 0;
	  year=a+(4-a%4);
	  
	  return year;
	 }
	 
	 public static int WC(int a)
	 {
	  //int year = 0;
	  if(a%4==1)
	  {
	  return a+1;
	  }
	  else if(a%4==2)
	  {
	   return a+4;
	  }
	  else if(a%4==3)
	  {
	   return a+3;
	  }
	  
	  else if(a%4==0)
	  {
	   return a+2;
	  }
	  return 0;
	 }
	 
	 public static int getYear()
	 {
		 int year;
		 Date today=new Date(System.currentTimeMillis());

		  
		  SimpleDateFormat timeFormat= new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");

		  String s=timeFormat.format(today.getTime()); 
		  String []years=s.split("/");
		  System.out.println(years[years.length-1]);
		  year = Integer.parseInt(years[years.length-1]);
		 return year;
	 }
	
	public static void main(String args[]) throws FileNotFoundException 
	{
		/*Utils util = new Utils();
		util.readFile("C:/Users/KUN AGUERO/Desktop/Euro2008.txt");*/
		String s ="World Cup sắp tới";
		String s1 = s.replace("lần tới", "2018");
		 s1 = s.replace("sắp tới", "2018");
		System.out.println(s1);
		//Utils.WC(Utils.getYear());
		System.out.println(Utils.WC(Utils.getYear()));
	}
}

