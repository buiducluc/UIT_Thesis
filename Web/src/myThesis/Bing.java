package myThesis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
	String blackList;
	ArrayList<String> Snippet;
	ArrayList<String> dataFromLink = new ArrayList<String>();
	ArrayList<String> dataSplit = new ArrayList<String>();
	ArrayList<String> dataFinal = new ArrayList<String>();
	ArrayList<String> Data = new ArrayList<String>();
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

	public void SplitData(ArrayList<String> data) {
		String temp = "";
		for (int i = 0; i < Data.size(); i++) {
//			String[] temp = Data.get(i).split(".");
//			for (int j = 0; j < temp.length; j++) {
////				System.out.println("String: " + i + ": " + temp[i] );
//				dataSplit.add(temp[i]);
//			}
			temp=temp+Data.get(i);
			
		}
		String [] finalData = temp.split("\\.");
		for (int k = 0; k < finalData.length; k++) {
			//dataFinal.add(dataSplit.get(k) + ". " + dataSplit.get(k + 1) + ". " + dataSplit.get(k + 2));
			System.out.println("Data: " + finalData[k]);
		}
		List<String> finalData1 =  Arrays.asList(finalData);
		for(int i=0;i<finalData1.size()-2;i++)
		{
			dataFinal.add(finalData1.get(i)+finalData1.get(i+1)+finalData1.get(i+2));
		}
		
	}

//	@SuppressWarnings("static-access")
//	public void PatternMatch(String sentence, String question) {
//		String tempResult = "";
//
//		String regex_where_born = "(?<=Nơi sinh|sinh ở|sinh tại|tại)((\\s[\\p{Lu}][\\p{Ll}]+)\\,?)+";
//		String regex_time_born = "(ngày \\d+ tháng \\d+ năm \\d+)|(\\d+ tháng \\d+ năm \\d+)|(\\d+\\-\\d+\\-\\d+)|(\\d+\\-\\d+)"
//				+ "|(\\d+\\/\\d+\\/\\d+)|(\\d+\\/\\d+)|(\\d+ tháng \\d+, \\d+)";
//
//		// String regex_when_die = "\\d+\\)|((-|–)\\s*\\D+\\d+\\))|mất ngày \\d+
//		// tháng \\d+ năm \\d+|(mất năm) \\d+";
//		String regex_where = "(tại|ở)?(\\s(sân| sân vận động|thành phố|nước|quốc gia))(\\s[\\p{Lu}][\\p{Ll}+]+){1,}";
//		// String regex1 = "(?<=tổ chức)(\\s(tại|ở)?(\\s[\\p{Lu}][\\p{Ll}]+))+";
//		String regex_tall = "(Chiều cao|chiều cao)(\\s\\d+(\\,|\\.)\\d+)|(Chiều cao: |chiều cao: )(\\s\\d+m\\d+)";
//		String regex_height = "(Cân nặng|cân nặng)(\\s\\d+)";
//		String regex_number = "(số áo|số|Số áo)(\\s\\d+)";
//		String regex_people = "(\\s?[\\p{Lu}][\\p{Ll}]+){1,}(.*)(?=(thủ môn xuất sắc nhất 2014|Vua bóng đá|biệt danh|Quả bóng vàng|Quả bóng Vàng|vua phá lưới|Vua phá lưới|Huấn luyện viên"
//				+ "|huấn luyện viên|chủ tịch|Chủ tịch|Chủ Tịch))";
//		String regex_time_start = "(?<=bắt đầu|Bắt đầu)(\\s)?(ngày \\d+ tháng \\d+ năm \\d+)|(\\d+ tháng \\d+ năm \\d+)|(\\d+\\-\\d+\\-\\d+)|(\\d+\\-\\d+)"
//				+ "|(\\d+\\/\\d+\\/\\d+)|(\\d+\\/\\d+)|(\\d+ tháng \\d+, \\d+)";
//		String regex_time_end = "((\\s)?(ngày \\d+ tháng \\d+ năm \\d+)|(\\d+ tháng \\d+ năm \\d+)|(\\d+\\-\\d+\\-\\d+)|(\\d+\\-\\d+)"
//				+ "|(\\d+\\/\\d+\\/\\d+)|(\\d+\\/\\d+)|(\\d+ tháng \\d+, \\d+))";
//		String regex_vitri="(Vị trí|vị trí)(\\s)([\\p{Lu}][\\p{Ll}]+\\s[\\p{Ll}]+)";
//		String regex_SVD="(Sân vận động|sân vận động|SVD)(\\s)([\\p{Lu}][\\p{Ll}]+){1,}";
//		String regex_sobanthang="(?<=Tổng cộng sự nghiệp|Tổng số)(\\s\\d+)+";
//		String regex_team="(câu lạc bộ|Câu lạc bộ|CLB)(\\s)?([\\p{Lu}][\\p{Ll}]+){1,}";
//		String regex_tiso="(tỉ số|Tỉ số)(\\:\\s|\\s)?(\\d+\\-\\d+)";
//		//String regex_bietdanh="(biệt danh|Biệt danh)(\\slà)?(\\s)?(\\"|\\'|\\‘|\“)(\\D+|\\w+)(\\"|\\'|\\’|\\”)";
//		String regex_sodo="(?<= sơ đồ|Sơ đồ chiến thuật)(\\s)?(\\d+\\-?)+";
//				
//		switch (IdentifyQuestion.ClassifyQuestion(question)) {
//		case QuestionResult.PEOPLE:
//			regex_final = regex_people;
//			break;
//		case QuestionResult.TEAM:
//			regex_final = regex_team;
//			break;
//		case QuestionResult.TIME_START:
//			regex_final = regex_time_start;
//			break;
//		case QuestionResult.TIME_END:
//			regex_final = regex_time_end;
//			break;
//		case QuestionResult.MATCH_RESULT:
//			regex_final = regex_tiso;
//			break;
//		case QuestionResult.WHERE:
//			regex_final = regex_where;
//			break;
//		case QuestionResult.COUNT:
//			regex_final = "";
//			break;
//		case QuestionResult.NATION:
//			regex_final = "";
//			break;
//		case QuestionResult.FOOT:
//			regex_final = "";
//			break;
//		case QuestionResult.WEIGHT:
//			regex_final = regex_height;
//			break;
//		case QuestionResult.HEIGHT:
//			regex_final = regex_tall;
//			break;
//		case QuestionResult.POSITION:
//			regex_final = regex_vitri;
//			break;
//
//		case QuestionResult.NUMBER:
//			regex_final = regex_number;
//			break;
//		case QuestionResult.NICKNAME:
//			regex_final = "";
//			break;
//		case QuestionResult.STADIUM:
//			regex_final = regex_SVD;
//			break;
//		case QuestionResult.TIME_BORN:
//			regex_final = regex_time_born;
//			break;
//		case QuestionResult.WHERE_BORN:
//			regex_final = regex_where_born;
//			break;
//		case QuestionResult.SUM_GOAL:
//			regex_final= regex_sobanthang;
//		case QuestionResult.SUM_MATCH:
//			regex_final= "";
//		case QuestionResult.SQUAD:
//			regex_final= regex_sodo;
//		default:
//			break;
//
//		}
//
//		Pattern pattern = Pattern.compile(regex_final);
//		Matcher matcher = pattern.matcher(sentence);
//		while (matcher.find()) {
//			tempResult = matcher.group();
//			// -----System.out.println(tempResult);
//			//tempResult = tempResult.replaceAll("[^\\u00BF-\\u1FFF\\u2C00-\\uD7FF\\w\\s]", "").replaceAll("\\s+$", "");
//			result.add(tempResult);
//		}
//	}

//	public void SearchWiki(String question) throws IOException {
//		System.out.println("----Wiki-----");
//		List<String> Name = new ArrayList<String>();
//		String url = "https://vi.wikipedia.org/wiki/";
//
//		String segmented = tokenizer.segment(question);
//		System.out.println(segmented);
//		String tagged = tagger.tagging(segmented);
//		System.out.println(tagged);
//		String query_extract = "";
//		String regex_ = "\\S+/(Np|N)"; // Tag giữ lại
//		Pattern pattern = Pattern.compile(regex_);
//		Matcher matcher = pattern.matcher(tagged);
//		while (matcher.find()) {
//			query_extract += matcher.group() + " ";
//			System.out.println(matcher.group() + " ");
//			Name.add(matcher.group());
//
//		}
//		String name = "";
//		if (Name.get(0).contains("/N")) {
//			name = Name.get(0).substring(0, Name.get(0).length() - 2);
//		}
//
//		if (Name.get(0).contains("/Np")) {
//			name = Name.get(0).substring(0, Name.get(0).length() - 3);
//		}
//
//		// String name = Name.get(0).substring(0, Name.get(0).length()-2);
//		System.out.println(name);
//		url = url + name;
//
//		System.out.println("---URL----: " + url);
//
//		Document doc = Jsoup.connect(url).get();
//		// Document doc1 = Jsoup.parse(link);
//		Elements links1 = doc.select("table");
//
//		/*
//		 * text1 = doc1.body().text(); System.out.println(text1);
//		 */
//
//		for (Element link1 : links1) {
//			// System.out.println("\ntext: " + link1.text());
//			System.out.println(link1.text());
//			Data.add(link1.text());
//
//		}
////		Elements link2 = doc.select("p");
////		for (Element link1 : link2) {
////			// System.out.println("\ntext: " + link1.text());
////			System.out.println(link1.text());
////			Data.add(link1.text());
////
////		}
//
//	}
//
//	@SuppressWarnings("static-access")
//	public void SearchDocument(String question) throws IOException, ParseException {
//		if (IdentifyQuestion.ClassifyQuestion(question) == 17 || IdentifyQuestion.ClassifyQuestion(question) == 8
//				|| IdentifyQuestion.ClassifyQuestion(question) == 9 || IdentifyQuestion.ClassifyQuestion(question) == 10
//				|| IdentifyQuestion.ClassifyQuestion(question) == 11 || IdentifyQuestion.ClassifyQuestion(question) == 12
//				|| IdentifyQuestion.ClassifyQuestion(question) == 13||IdentifyQuestion.ClassifyQuestion(question) == 16|| IdentifyQuestion.ClassifyQuestion(question) == 18) {
//
//			SearchWiki(question);
//		} else {
//			SearchGoogle(question);
//			SearchBing(question);
//		}
//	}
//
//	public void SearchGoogle(String question) throws IOException {
//		String google = "http://www.google.com/search?q=";
//		String charset = "UTF-8";
//		String userAgent = "ExampleBot 1.0 (+http://example.com/bot)";
//
//		Elements links = Jsoup.connect(google + URLEncoder.encode(question, charset)).userAgent(userAgent).get()
//				.select(".g>.r>a");
//
//		try {
//			for (Element link : links) {
//				String title = link.text();
//				String url = link.absUrl("href"); // Google returns URLs in
//													// format
//													// "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
//				url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");
//
//				if (!url.startsWith("http")) {
//					continue; // Ads/news/etc.
//				}
//				try {
//					Document doc = Jsoup.connect(url).get();
//
//					// Document doc1 = Jsoup.parse(link);
//					Elements links1 = doc.select("p");
//
//					/*
//					 * text1 = doc1.body().text(); System.out.println(text1);
//					 */
//
//					for (Element link1 : links1) {
//						// -----System.out.println(link1.text());
//						Data.add(link1.text());
//
//					}
//					System.out.println("Done!");
//				} catch (Exception e) {
//					// TODO: handle exception
//					System.out.println("KO lay dc du lieu!");
//				}
//
//			}
//			// text = doc.title();
//			// text = doc.text();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		// getTextJsoup(aResult.getString("Url") + "\r\n");
//	}

	/*
	 * public ArrayList<String> truedataFromLink(ArrayList<String> dataFromLink)
	 * { ArrayList<String> truedataFromLink = new ArrayList<String>(); for (int
	 * i = 0; i < dataFromLink.size() - 3; i = i + 3) {
	 * truedataFromLink.add(dataFromLink.get(i) + dataFromLink.get(i + 1) +
	 * dataFromLink.get(i + 2)); } return truedataFromLink; }
	 */

	public List<String> GetAnswerPeople(List<String> list) {
		List<String> result_Final= new ArrayList<String>();
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
		return result_Final;
	}

//	public void SearchBing(String question) throws IOException, ParseException {
//		final String accountKey = "WQGRwBuPuPF9VXsja/V40Fv9L/2GCOxLF0FZeAX+Ujk";
//		final String bingUrlPattern = "https://api.datamarket.azure.com/Data.ashx/Bing/Search/v1/Web?Query='"
//				+ parseQuestion(question) + "'&$format=JSON";
//
//		final String accountKeyEnc = Base64.getEncoder().encodeToString((accountKey + ":" + accountKey).getBytes());
//
//		final URL url = new URL(bingUrlPattern);
//		final URLConnection connection = url.openConnection();
//		connection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
//		try (final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//			String inputLine;
//			final StringBuilder response = new StringBuilder();
//			while ((inputLine = in.readLine()) != null) {
//				response.append(inputLine);
//			}
//			final JSONObject json = new JSONObject(response.toString());
//			final JSONObject d = json.getJSONObject("d");
//			final JSONArray results = d.getJSONArray("results");
//			Snippet = new ArrayList<String>();
//
//			for (int i = 0; i < limitLink; i++) {
//
//				final JSONObject aResult = results.getJSONObject(i);
//
//				// dataFromLink.add(aResult.getString("Description"));
//				try {
//					Document doc = Jsoup.connect(aResult.getString("Url")).get();
//					Elements links = doc.select("p");
//
//					for (Element link1 : links) {
//						Data.add(link1.text());
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					limitLink += 1;
//				}
//
//			}
//
//		} catch (JSONException e) {
//			// TODO Auto-generated catch bloc
//			e.printStackTrace();
//		}
//
//	}

	
	
	public List<String> GetSumGoal(List<String> string){
		 List<String> result= new ArrayList<String>();
		for(int i = 0;i<string.size();i++){
			String[]tempString = string.get(i).split(" ");
			result.add(tempString[tempString.length-1]);
		}
		for(int i = 0;i<result.size();i++){
		System.out.println(result.get(i));
		}
		return result;
		
	}


	public static void main(String[] arg) throws Exception {
		Scanner in = new Scanner(System.in);
		String question = in.nextLine();
		in.close();
		Bing bing = new Bing();
		System.out.println(question);
		List<String> list = new ArrayList<String>();
		//bing.SearchDocument(question);
//		for(int i = 0; i<bing.Data.size(); i++){
//			System.out.println("\n" + bing.Data.get(i));
//		}
		//bing.SplitData(bing.Data);
		List<String>dataLucene = new ArrayList<String>();
		List<String>answer = new ArrayList<String>();
		SearchDocument searchDoc = new SearchDocument();
//		dataLucene = lucene.SearchSentences(question, searchDoc.SearchDocument1(question));
		dataLucene = Lucene.SearchSentences(question, searchDoc.SearchDocument2(question));
		 System.out.println("\n------Lucene-----\n");
		 for (int i = 0; i < dataLucene.size(); i++) {
		
		 System.out.println(dataLucene.get(i));
		 answer.addAll(ReturnAnswer.PatternMatch(dataLucene.get(i), question));
		 //bing.PatternMatch(bing.demo.result_Final.get(i), question);
		
		 }
		 if (IdentifyQuestion.ClassifyQuestion(question) == 1 ||
				 IdentifyQuestion.ClassifyQuestion(question) == 2) {
		 System.err.println("Action People");
		 list = bing.GetAnswerPeople(answer);
		 }
		 else if(IdentifyQuestion.ClassifyQuestion(question) == 18){
			 list = bing.GetSumGoal(bing.result);
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
