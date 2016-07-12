package myThesis;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.queryparser.classic.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



import jvntagger.MaxentTagger;
import vn.hus.nlp.tokenizer.VietTokenizer;

import java.util.ArrayList;
import java.util.Arrays;

public class Bing {
	String link;
	String query;
	Utils util;
	Lucene demo = new Lucene();
	String regex_final = "";
	List<String> result_Final = new ArrayList<String>();
	
	int limitLink = 10;

	List<String> result = new ArrayList<String>();

	VietTokenizer tokenizer = new VietTokenizer();
	MaxentTagger tagger = new MaxentTagger("model\\maxent");

	// use bing to search
	public String parseQuestion(String str) {
		String result = null;
		result = str.replace(' ', '+');
		System.out.println(result);
		return result;
	}

//	public void SplitData(ArrayList<String> data) {
//		String temp = "";
//		for (int i = 0; i < Data.size(); i++) {
////			String[] temp = Data.get(i).split(".");
////			for (int j = 0; j < temp.length; j++) {
//////				System.out.println("String: " + i + ": " + temp[i] );
////				dataSplit.add(temp[i]);
////			}
//			temp=temp+Data.get(i);
//			
//		}
//		String [] finalData = temp.split("\\.");
//		for (int k = 0; k < finalData.length; k++) {
//			//dataFinal.add(dataSplit.get(k) + ". " + dataSplit.get(k + 1) + ". " + dataSplit.get(k + 2));
//			System.out.println("Data: " + finalData[k]);
//		}
//		List<String> finalData1 =  Arrays.asList(finalData);
//		for(int i=0;i<finalData1.size()-2;i++)
//		{
//			dataFinal.add(finalData1.get(i)+finalData1.get(i+1)+finalData1.get(i+2));
//		}
//		
//	}



	public void GetAnswerPeople(List<String> list) {
		result_Final= new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			String segmented = tokenizer.segment(list.get(i));
			// System.err.println(segmented);
			String tagged = tagger.tagging(segmented);
			// System.err.println(tagged);
			String regex_ = "\\S+/(Np)"; // Tag giữ lại
			Pattern pattern = Pattern.compile(regex_);
			Matcher matcher = pattern.matcher(tagged);
			while (matcher.find()) {
			 String temp = matcher.group();
			 
				temp = temp.replace("/N", "").replace("_", " ");
				temp = temp.replace("/Np", "").replace("_", " ");
				result_Final.add(temp);
				System.out.println(temp + "\n");
			}
		}
	}
	
	public void GetSumGoal(List<String> string){
		 result_Final= new ArrayList<String>();
		for(int i = 0;i<string.size();i++){
			String[]tempString = string.get(i).split(" ");
			result_Final.add(tempString[tempString.length-1]);
		}
		for(int i = 0;i<result_Final.size();i++){
		System.out.println(result_Final.get(i));
		}
		
		
	}
	
	public void GetSumMatch(List<String> string){
		 result_Final= new ArrayList<String>();
		for(int i = 0;i<string.size();i++){
			String[]tempString = string.get(i).split(" ");
			result_Final.add(tempString[tempString.length-2]);
		}
		for(int i = 0;i<result_Final.size();i++){
		System.out.println(result_Final.get(i));
		}
		
		
	}


	public static void main(String[] arg) throws Exception {
		Scanner in = new Scanner(System.in);
		String question = in.nextLine();
		in.close();
		Bing bing = new Bing();
		System.out.println(question);
		//bing.SearchDocument(question);
//		for(int i = 0; i<bing.Data.size(); i++){
//			System.out.println("\n" + bing.Data.get(i));
//		}
		//bing.SplitData(bing.Data);
		List<String>dataLucene = new ArrayList<String>();
		List<String>answer = new ArrayList<String>();
		Lucene lucene = new Lucene();
		SearchDocument searchDoc = new SearchDocument();
//		dataLucene = lucene.SearchSentences(question, searchDoc.SearchDocument1(question));
		dataLucene = searchDoc.SearchDocument2(question);
		 System.out.println("\n------Lucene-----\n");
		 for (int i = 0; i < dataLucene.size(); i++) {
		
		 System.out.println(dataLucene.get(i));
		 answer.addAll(ReturnAnswer.PatternMatch(dataLucene.get(i), question));
		 //bing.PatternMatch(bing.demo.result_Final.get(i), question);
		
		 }
		 if (IdentifyQuestion.ClassifyQuestion(question) == 1 ||
				 IdentifyQuestion.ClassifyQuestion(question) == 2) {
		 System.err.println("Action People");
		 bing.GetAnswerPeople(bing.result);
		 }
		 else if(IdentifyQuestion.ClassifyQuestion(question) == 18){
			 bing.GetSumGoal(bing.result);
			 for (int i = 0; i < 30; i++) {
				 System.err.println("Result: " + bing.result.get(i));
				 }
		 }
		 else {
		 for (int i = 0; i < answer.size(); i++) {
		 System.err.println("Result: " + answer.get(i));
		 }
		 }

	}

}
